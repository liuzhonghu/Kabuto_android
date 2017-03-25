package com.nec.kabutoclient.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Log;

import com.nec.kabutoclient.KabutoApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * File related util functions.
 */
public class FileUtils {
    public static String FILE_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/qiliuwu_kratos/";
    public static String FILE_CACHE_DIR = FILE_DIR + "cache";
    public static String IMAGE_THUMB_CACHE_DIR = FILE_DIR + "cache/thumbs";
    public static String IMAGE_RESIZE_CACHE_DIR = FILE_DIR + "cache/resize";
    public static String CHAT_AUDIO_CACHE_DIR = FILE_DIR + "cache/chat/audio";
    public static String APK_CACHE_DIR = FILE_DIR + "apks/";
    public static String SHARE_GIF_DIR = FILE_DIR + "gif";
    public static String AUDIO_DIR = FILE_DIR + "audio/";
    public static String LIVE_STORY_DIR = FILE_DIR + "liveStory/";
    public static String MESSAGE_AUDIO_DIR = FILE_DIR + "messageaudio";
    public static String SCREENSHOTS_DIR = FILE_DIR + "kratossavescreen";
    public static String AUDIO_DIRS = FILE_DIR + "audio";
    public static String VIDEO_DIR = FILE_DIR + "video";
    public static String FIELD_VIDEO_DIR = FILE_DIR + "fieldvideo";
    public static String SMALL_IMAGE_CACHE_NAME = "small";
    public static String NORMAL_IMAGE_CACHE_NAME = "normal";
    public static String SAVE_PHOTO_DIR = FILE_DIR + "photo";
    public static String EMOJI_DIR = FILE_DIR + "emoji";
    public static String ATTITUDE_ICON = FILE_DIR + "attitudeicon";
    public static String AVATAR_NAME = "avatar.png";
    public static String SHARE_INVITE_IMAGE = FILE_CACHE_DIR + "/share/shareInvite.png";

    private FileUtils() {
    }

    public static void deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return;
        }
        file.delete();
    }

    public static boolean exists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.deleteOnExit();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.deleteOnExit();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.deleteOnExit();
            }
        }
    }

    public static String getAttitudeIconPath() {
        File file = new File(ATTITUDE_ICON);
        if (!file.exists()) {
            file.mkdirs();
        }
        addNoMediaProperty(ATTITUDE_ICON);
        return ATTITUDE_ICON;
    }

    public static boolean mkdir(String path) {
        File dir = new File(path);
        return dir.mkdirs();
    }

    public static boolean copyFile(File srcFile, File dstFile) {
        if (srcFile.exists() && srcFile.isFile()) {
            if (dstFile.isDirectory()) {
                return false;
            }
            if (dstFile.exists()) {
                dstFile.delete();
            }
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            try {
                byte[] buffer = new byte[2048];
                input = new BufferedInputStream(new FileInputStream(srcFile));
                output = new BufferedOutputStream(new FileOutputStream(dstFile));
                while (true) {
                    int count = input.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    output.write(buffer, 0, count);
                }
                output.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public static boolean copyAssetFile(String fileName, String dstFilePath) {
        if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(dstFilePath)) {
            File dstFile = new File(dstFilePath);
            if (dstFile.isDirectory()) {
                return false;
            }
            if (dstFile.exists()) {
                dstFile.delete();
            }
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            try {
                byte[] buffer = new byte[2048];
                input =
                        new BufferedInputStream(KabutoApplication.getAppContext().getAssets().open(fileName));
                output = new BufferedOutputStream(new FileOutputStream(dstFile));
                while (true) {
                    int count = input.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    output.write(buffer, 0, count);
                }
                output.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public static String copyAssetsFileToData(Context context, String assetFileName) {
        File dstFile = new File(context.getCacheDir(), assetFileName);
        if (!new File(dstFile.getParent()).exists()) {
            new File(dstFile.getParent()).mkdirs();
        }
        if (dstFile.isFile()) {
            return dstFile.getAbsolutePath();
        } else {
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            try {
                byte[] buffer = new byte[2048];
                input = new BufferedInputStream(context.getAssets().open(assetFileName));
                output = new BufferedOutputStream(new FileOutputStream(dstFile));
                while (true) {
                    int count = input.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    output.write(buffer, 0, count);
                }
                output.flush();
                return dstFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean copyFile(String srcPath, String dstPath) {
        return copyFile(new File(srcPath), new File(dstPath));
    }

    public static void deletePath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        String[] tmpList = file.list();
        if (tmpList == null) {
            return;
        }
        for (String fileName : tmpList) {
            if (fileName == null) {
                continue;
            }
            String tmpPath;
            if (path.endsWith(File.separator)) {
                tmpPath = path + fileName;
            } else {
                tmpPath = path + File.separator + fileName;
            }
            File tmpFile = new File(tmpPath);
            if (tmpFile.isFile()) {
                tmpFile.delete();
            }
            if (tmpFile.isDirectory()) {
                deletePath(tmpPath);
            }
        }
        file.delete();
    }

    public static void deleteCatch(String path, List<String> paths) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        String[] tmpList = file.list();
        if (tmpList == null) {
            return;
        }
        for (String fileName : tmpList) {
            if (fileName == null) {
                continue;
            }
            String tmpPath;
            if (path.endsWith(File.separator)) {
                tmpPath = path + fileName;
            } else {
                tmpPath = path + File.separator + fileName;
            }
            if (!paths.isEmpty() && !isInList(tmpPath, paths)) {
                File tmpFile = new File(tmpPath);
                if (tmpFile.isFile()) {
                    tmpFile.delete();
                }
                if (tmpFile.isDirectory()) {
                    deletePath(tmpPath);
                }
            }
        }
    }


    public static boolean isInList(String str, List<String> strs) {
        int i = 0;
        for (String string : strs) {
            if (str.equals(string))
                i++;
        }
        return i > 0;
    }

    public static Future<String> saveBitmapToFileOnIo(Bitmap bitmap, String saveDirectory,
                                                      String filename, int compress) {
        return ThreadPool.submit(() -> saveBitmapToFile(bitmap, saveDirectory, filename, compress));
    }

    public static Future<Void> clearCacheOnIo() {
        return ThreadPool.submit((Callable<Void>) () -> {
            deletePath(IMAGE_THUMB_CACHE_DIR);
            deletePath(IMAGE_RESIZE_CACHE_DIR);
            deletePath(APK_CACHE_DIR);
            deletePath(AUDIO_DIR);
            deletePath(VIDEO_DIR);
            deletePath(CHAT_AUDIO_CACHE_DIR);
            deletePath(SMALL_IMAGE_CACHE_NAME);
            deletePath(NORMAL_IMAGE_CACHE_NAME);
            return null;
        });
    }

    public static Future<String> saveBitmapToFileOnIo(Bitmap bitmap, String saveDirectory,
                                                      String filename, int compress, Bitmap.CompressFormat compressFormat) {
        return ThreadPool.submit(() -> saveBitmapToFile(bitmap, saveDirectory, filename, compress,
                compressFormat));
    }

    public static String saveBitmapToFileWithoutNoMide(Bitmap bitmap, String saveDirectory,
                                                       String filename, int compress) {
        return saveBitmapToFileWithoutNoMedia(bitmap, saveDirectory, filename, compress,
                Bitmap.CompressFormat.JPEG);
    }

    public static void renameFile(String path) {
        File file = new File(path);
        File newFile = new File(FileUtils.SCREENSHOTS_DIR + "/" + System.currentTimeMillis() + ".jpg");
        file.renameTo(newFile);
    }

    public static String saveBitmapToFile(Bitmap bitmap, String saveDirectory,
                                          String filename, int compress) {
        return saveBitmapToFile(bitmap, saveDirectory, filename, compress, Bitmap.CompressFormat.JPEG);
    }

    public static String saveBitmapToFileWithoutNoMedia(Bitmap bitmap, String saveDirectory,
                                                        String filename, int compress, Bitmap.CompressFormat compressFormat) {
        FileOutputStream fOut = null;
        try {
            if (null != bitmap) {
                if (createFolderIfNotExistMedia(saveDirectory)) {
                    File f =
                            new File(saveDirectory.endsWith(File.separator) ? saveDirectory
                                    + filename : saveDirectory + File.separator + filename);
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();
                    fOut = new FileOutputStream(f);
                    bitmap.compress(compressFormat, compress, fOut);
                    fOut.flush();
                    return saveDirectory.endsWith(File.separator) ? saveDirectory
                            + filename : saveDirectory + File.separator + filename;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String saveBitmapToFile(Bitmap bitmap, String saveDirectory,
                                          String filename, int compress, Bitmap.CompressFormat compressFormat) {
        FileOutputStream fOut = null;
        try {
            if (null != bitmap) {
                if (createFolderIfNotExist(saveDirectory)) {
                    File f =
                            new File(saveDirectory.endsWith(File.separator) ? saveDirectory
                                    + filename : saveDirectory + File.separator + filename);
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();
                    fOut = new FileOutputStream(f);
                    bitmap.compress(compressFormat, compress, fOut);
                    fOut.flush();
                    return saveDirectory.endsWith(File.separator) ? saveDirectory
                            + filename : saveDirectory + File.separator + filename;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static ThumbImageData saveChatPhotoSelectBitmapToThumb(
            ImageUtils.SaveThumbImageData saveThumbImageData,
            String inFilePath, int compress) {
        FileOutputStream fOut = null;
        try {
            if (saveThumbImageData.getBitmap() != null) {
                if (createFolderIfNotExist(IMAGE_THUMB_CACHE_DIR)) {
                    String fileName = new File(inFilePath).getName();
                    File f =
                            new File(IMAGE_THUMB_CACHE_DIR.endsWith(File.separator) ? IMAGE_THUMB_CACHE_DIR
                                    + fileName : IMAGE_THUMB_CACHE_DIR + File.separator + fileName);
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();
                    fOut = new FileOutputStream(f);
                    saveThumbImageData.getBitmap().compress(Bitmap.CompressFormat.JPEG, compress, fOut);
                    fOut.flush();
                    if (saveThumbImageData.getBitmap() != null
                            && !saveThumbImageData.getBitmap().isRecycled()) {
                        saveThumbImageData.getBitmap().recycle();
                    }
                    return new ThumbImageData(saveThumbImageData.getLocalFileKey(),
                            IMAGE_THUMB_CACHE_DIR.endsWith(File.separator)
                                    ? IMAGE_THUMB_CACHE_DIR
                                    + fileName
                                    : IMAGE_THUMB_CACHE_DIR + File.separator + fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static class ThumbImageData {
        private String inImageLocalFileKey;
        private String thumbFilePath;

        public ThumbImageData(String inImageLocalFileKey, String thumbFilePath) {
            this.inImageLocalFileKey = inImageLocalFileKey;
            this.thumbFilePath = thumbFilePath;
        }

        public String getThumbFilePath() {
            return thumbFilePath;
        }

        public String getInImageLocalFileKey() {
            return inImageLocalFileKey;
        }
    }

    public static boolean createFolderIfNotExist(String dirPath) {
        File file = new File(dirPath);
        boolean mkdirs = true;
        if (!file.exists()) {
            mkdirs = file.mkdirs();
        }
        addNoMediaProperty(dirPath);
        return mkdirs;
    }

    public static boolean createFolderIfNotExistMedia(String dirPath) {
        File file = new File(dirPath);
        boolean mkdirs = true;
        if (!file.exists()) {
            mkdirs = file.mkdirs();
        }
        removeNoMediaProperty(dirPath);
        return mkdirs;
    }

    private static void addNoMediaProperty(String path) {
        File nomediaFile = new File(path + "/.nomedia");
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void removeNoMediaProperty(String path) {
        File nomediaFile = new File(path + "/.nomedia");
        if (nomediaFile.exists()) {
            nomediaFile.delete();
        }
    }

    public static String getFileCacheDir() {
        createFolderIfNotExist(FILE_CACHE_DIR);
        return FILE_CACHE_DIR;
    }

    public static String getAudioCacheDir() {
        createFolderIfNotExist(AUDIO_DIR);
        return AUDIO_DIR;
    }

    public static String getGifCacheDir() {
        createFolderIfNotExist(SHARE_GIF_DIR);
        return SHARE_GIF_DIR;
    }

    public static String getFileRecorderDirPath() {
        File file = new File(FILE_CACHE_DIR);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        return FILE_CACHE_DIR;
    }

    public static String getScreenShotsDirPath() {
        File file = new File(SCREENSHOTS_DIR);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        return SCREENSHOTS_DIR;
    }

    public static String getRecordTempDirPath() {
        String fileCacheDir = getFileCacheDir();
        String recordTempDirPath = fileCacheDir + File.separator + System.currentTimeMillis();
        File file = new File(recordTempDirPath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        return recordTempDirPath;
    }

    public static String copyImage2SD(Context context, int picResId, String fileName) {

        File dir = new File(FILE_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = FILE_CACHE_DIR + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getResources().openRawResource(picResId);
            fos = new FileOutputStream(filePath);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    public static InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    public static String getVideoDirPath() {
        File tempFile = new File(VIDEO_DIR);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        return VIDEO_DIR;
    }

    public static String getPhotoDir() {
        File tempFile = new File(SAVE_PHOTO_DIR);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        return SAVE_PHOTO_DIR;
    }

    public static Bitmap getSampleBmp(String path, int viewWidth, int viewHeight) {
        Bitmap bmp = null;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int result = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            int rotate = 0;
            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
            }
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opt);

            int picWidth = opt.outWidth;
            int picHeight = opt.outHeight;

            if (picWidth == 0 || picHeight == 0) {
                return null;
            }
            if (viewHeight != 0 && viewWidth != 0) {
                if (rotate == 90 || rotate == 270) {
                    opt.inSampleSize = findBestSampleSize(picWidth, picHeight, viewHeight, viewWidth);
                } else {
                    opt.inSampleSize = findBestSampleSize(picWidth, picHeight, viewWidth, viewHeight);
                }
            }
            opt.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(path, opt);

            if (rotate == 90 || rotate == 270) {
                Matrix matrix = new Matrix();
                matrix.setRotate(rotate);
                Bitmap rotateBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                        bmp.getHeight(), matrix, true);
                if (rotateBmp != null) {
                    bmp.recycle();
                    bmp = rotateBmp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    public static void printText2SD(String text) {
        String path =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/blink_gaia/diagnosis/"
                        + "text.txt";
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        PrintStream oldPrintStream = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        System.out.println(text);
        System.setOut(oldPrintStream);
        // 用filewriter把文件追加写入到文件中,true代表追加写入,false代表覆盖写入
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append(bos.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(FileUtils.FILE_CACHE_DIR);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        File mediaFile = new File(FileUtils.FILE_CACHE_DIR + System.currentTimeMillis() + ".jpg");
        return mediaFile;
    }

    public static String contentUriToFilePath(String contentUri, Context context) {
        Cursor cursor = null;
        String filePath = null;
        try {
            Uri uri = Uri.parse(contentUri);
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(context, uri, projection, null, null, null);
            cursor = cursorLoader.loadInBackground();
            int cursorColumnIndexOrThrow =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(cursorColumnIndexOrThrow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return filePath;
    }

    public static String getLocalFileKey(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return filePath + file.lastModified() + file.length();
        }
        return "";
    }

    public static boolean isDirExist(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean isDirEmpty(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        String[] files = file.list();
        if (files.length > 0) {
            System.out.println("目录 " + file.getPath() +
                    " 不为空！");
            return false;
        }
        return true;
    }

    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     */
    public static void copyFolderFromAssets(Context context, String rootDirFullPath,
                                            String targetDirFullPath) {
        try {
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹
            for (String string : listFiles) {// 看起子目录是文件还是文件夹，这里只好用.做区分了
                if (isFileByName(string)) {// 文件
                    copyFileFromAssets(context, rootDirFullPath + (rootDirFullPath.endsWith("/") ? "" : "/")
                            + string, targetDirFullPath + (targetDirFullPath.endsWith("/") ? "" : "/")
                            + string);
                } else {// 文件夹
                    String childRootDirFullPath =
                            rootDirFullPath + (rootDirFullPath.endsWith("/") ? "" : "/") + string;
                    String childTargetDirFullPath =
                            targetDirFullPath + (targetDirFullPath.endsWith("/") ? "" : "/") + string;
                    long size = FileUtils.getSizeFromAssets(context, childRootDirFullPath);
                    if (new File(childTargetDirFullPath).exists()) {
                        long now = FileUtils.getFileSize(childTargetDirFullPath);
                        Log.d("asset", "size != now:" + (size != now));
                        if (size != now) {
                            FileUtils.delete(new File(childTargetDirFullPath));
                            boolean a = new File(childTargetDirFullPath).mkdirs();
                            boolean b = new File(childTargetDirFullPath + "/.nomedia").mkdirs();
                            FileUtils.copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                        }
                    } else {
                        boolean a = new File(childTargetDirFullPath).mkdirs();
                        boolean b = new File(childTargetDirFullPath + "/.nomedia").mkdirs();
                        FileUtils.copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                    }
                    // new File(childTargetDirFullPath).mkdirs();
                    // new File(childTargetDirFullPath + "/.nomedia").mkdir();
                    // copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFileFromAssets(Context context, String assetsFilePath,
                                          String targetFileFullPath) {
        if (!new File(targetFileFullPath).exists()) {
            InputStream assestsFileImputStream = null;
            try {
                assestsFileImputStream = context.getAssets().open(assetsFilePath);
                copyFile(assestsFileImputStream, targetFileFullPath);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (assestsFileImputStream != null) {
                        assestsFileImputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean copyFile(InputStream ins, String destFileFullPath) {
        FileOutputStream fos = null;
        try {
            File file = new File(destFileFullPath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = ins.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long getSizeFromAssets(Context context, String rootDirFullPath) {
        long size = 0;
        try {
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹
            for (String string : listFiles) {// 看起子目录是文件还是文件夹，这里只好用.做区分了
                if (isFileByName(string)) {// 文件
                    int temp = context.getAssets().open(rootDirFullPath + "/" + string).available();
                    size += temp;
                } else {// 文件夹
                    String childRootDirFullPath = rootDirFullPath + "/" + string;
                    size += getSizeFromAssets(context, childRootDirFullPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static boolean isFileByName(String string) {
        if (string.indexOf('.') > 0) {
            return true;
        }
        return false;
    }

    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File[] flist = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static long getFileSize(String path) {
        File f = new File(path);
        try {
            return getFileSize(f);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static String getApkCacheDir() {
        return APK_CACHE_DIR;
    }

}
