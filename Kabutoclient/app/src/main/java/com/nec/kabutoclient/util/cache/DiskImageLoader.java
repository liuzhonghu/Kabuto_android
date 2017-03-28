package com.nec.kabutoclient.util.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.nec.kabutoclient.KabutoApplication;
import com.nec.kabutoclient.di.modules.ApplicationModule;
import com.nec.kabutoclient.util.FileUtils;
import com.nec.kabutoclient.util.ThreadPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Future;


public class DiskImageLoader {
    private DiskLruCache diskLruCache;
    private static DiskImageLoader diskImageLoader;
    private LruCache<String, Bitmap> memoryCache;

    public synchronized static DiskImageLoader getInstance() {
        if (diskImageLoader == null) {
            diskImageLoader = new DiskImageLoader();
        }
        return diskImageLoader;
    }

    private DiskImageLoader() {
        initLruCache();
        intiDiskLruCache(KabutoApplication.getAppContext());
    }

    private void initLruCache() {
        memoryCache = new LruCache<String, Bitmap>(ApplicationModule.assignedMemoryCacheSize()) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int size = value.getRowBytes() * value.getHeight();
                return size;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
    }

    private void intiDiskLruCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir("game_bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 30 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一张本地图片（返回值在子线程）
     *
     * @param localFilePath 本地图片路径
     * @return bitmap
     */
    public Future<Bitmap> getLocalBitmap(String localFilePath) {
        return ThreadPool.submit(() -> checkLocalImage(localFilePath));
    }


    @Nullable
    public Bitmap checkLocalImage(String localFilePath) {
        Bitmap bitmap = getBitmapFromMemCache(localFilePath);
        if (bitmap == null) {
            bitmap = getImageFromDisk(localFilePath);
            if (bitmap == null) {
                saveToDisk(localFilePath);
                bitmap = getImageFromDisk(localFilePath);
            }
            if (bitmap != null) {
                addBitmapToMemoryCache(localFilePath, bitmap);
            }
        }
        return bitmap;
    }

    @Nullable
    public Bitmap checkLocalHasImage(String localFilePath) {
        Bitmap bitmap = getBitmapFromMemCache(localFilePath);
        if (bitmap == null) {
            bitmap = getImageFromDisk(localFilePath);
            if (bitmap == null) {
                bitmap = getImageFromDisk(localFilePath);
            }
            if (bitmap != null) {
                addBitmapToMemoryCache(localFilePath, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 缓存一张本地图片（返回值在子线程）
     *
     * @param localFilePath 本地图片路径
     * @return bitmap
     */
    public Future<Bitmap> saveLocalImageToLru(String localFilePath) {
        return getLocalBitmap(localFilePath);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    private DiskLruCache getDiskLruCache() {
        return diskLruCache;
    }

    private File getDiskCacheDir(String uniqueName) {
        return new File(FileUtils.getFileCacheDir() + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    public String saveToDisk(String liveImagePath) {
        if (diskLruCache != null) {
            String key = hashKeyForDisk(liveImagePath);
            try {
                DiskLruCache.Editor editor = diskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (streamTo(liveImagePath, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return liveImagePath;
    }

    public Bitmap saveToDisk(String filePath, Bitmap bitmap) {
        String key = hashKeyForDisk(filePath);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (streamTo(bitmap, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public boolean streamTo(Bitmap bitmap, OutputStream outputStream) {
        try {
            if (null != bitmap) {
                // if (FileUtils.createFolderIfNotExist(filePath)) {
                // File file=new File(filePath);
                // if (!file.exists()){
                // file.createNewFile();
                // }
                //
                // }
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }


    private Bitmap getImageFromDisk(String filePath) {
        try {
            String key = hashKeyForDisk(filePath);
            DiskLruCache.Snapshot snapShot = diskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean streamTo(String filePath, OutputStream outputStream) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }
            fis.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
