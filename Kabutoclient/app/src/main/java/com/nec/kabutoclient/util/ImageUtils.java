package com.nec.kabutoclient.util;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.nec.kabutoclient.KabutoApplication;
import com.nec.kabutoclient.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static final int CHOOSE_PHOTO_TAG = 1;
    public static final int PHOTO_RESULT = 2;
    private static final int CROP_PHOTO_OUTPUT = 180;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String MEDIA_CONTENT_PATH = "content://media/external/images/media/";

    public static void getPicture(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(FileUtils.FILE_CACHE_DIR, "temp_image.jpg")));
        fragment.startActivityForResult(intent, CHOOSE_PHOTO_TAG);
    }


    public static View creatLoadingHeaderView(Context context) {
        LinearLayout loadingHeaderView = new LinearLayout(context);
        loadingHeaderView
                .setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        context.getResources().getDimensionPixelSize(R.dimen.loading_header_height)));
        loadingHeaderView.removeAllViews();
        loadingHeaderView.setGravity(Gravity.CENTER);
        loadingHeaderView.setOrientation(LinearLayout.VERTICAL);
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        ImageUtils.showResGif(R.drawable.loading_header, simpleDraweeView);
        simpleDraweeView.setLayoutParams(new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(R.dimen.loading_header_gif_width),
                context.getResources().getDimensionPixelSize(R.dimen.loading_header_gif_width)));
        loadingHeaderView.addView(simpleDraweeView);
        return loadingHeaderView;
    }

    public static DraweeController showResGif(int resourceId, SimpleDraweeView simpleDraweeView) {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (anim != null) {
                    anim.start();
                }
            }
        };

        Uri uri = Uri.parse("res://com.qiliuwu.kratos/" + resourceId);
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(controllerListener)
                .build();
        simpleDraweeView.setController(draweeController);
        return draweeController;
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        if (orientationDegree == 0) {
            return bm;
        }
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable)// drawable 转换成bitmap
    {
        int width = drawable.getIntrinsicWidth(); // 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565; // 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
        Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // 把drawable内容画到画布中
        return bitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width == -1 || height == -1) {
            return drawable;
        }
        Bitmap oldbmp = drawableToBitmap(drawable);// drawable转换成bitmap
        Matrix matrix = new Matrix(); // 创建操作图片用的Matrix对象
        float scaleWidth = ((float) w / width); // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true); // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        return new BitmapDrawable(newbmp); // 把bitmap转换成drawable并返回
    }

    public static SaveThumbImageData getChatSelectPhotoZoomImage(String filePath,
                                                                 String localFileKey, int zoomW, int zoomH) {
        Bitmap newbmp = getSampleBmp(filePath, zoomW, zoomH);
        return new SaveThumbImageData(localFileKey, newbmp);
    }

    private static Bitmap getZoomImage(String imagePath, float zoomW, float zoomH) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, opts);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix(); // 创建操作图片用的Matrix对象
        float scaleWidth = (zoomW / width); // 计算缩放比例
        float scaleHeight = (zoomH / height);
        matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap getSampleBmp(String path, int viewWidth, int viewHeight) {
        Bitmap bmp;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opt);

        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        if (picWidth == 0 || picHeight == 0) {
            return null;
        }
        if (viewHeight != 0 && viewWidth != 0) {
            opt.inSampleSize = findBestSampleSize(picWidth, picHeight, viewWidth, viewHeight);

        }
        opt.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, opt);
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

    public static Bitmap scaleBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix(); // 创建操作图片用的Matrix对象
        float scaleWidth = ((float) w / width); // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true); // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        return newbmp; // 把bitmap转换成drawable并返回
    }

    public static Bitmap dstInBitmapFromRes(Bitmap originBitmap, int dstBitmapRes) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Bitmap shape =
                BitmapFactory
                        .decodeResource(KabutoApplication.getAppContext().getResources(), dstBitmapRes)
                        .copy(Bitmap.Config.ARGB_8888, true);
        Bitmap newBitmap = Bitmap.createBitmap(shape.getWidth(),
                shape.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(originBitmap,
                new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight()),
                new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight()), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(shape, new Rect(0, 0, shape.getWidth(), shape.getHeight()), new Rect(0, 0,
                shape.getWidth(), shape.getHeight()), paint);
        return newBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    public static ImageOptions getZoomImageOptionsFromScreen(int width, int height) {
        float widthProportion = width / SystemUtils.getScreenWidthPx();
        float heightProportion = height / SystemUtils.getScreenHeightPx();
        if (widthProportion > 1) {
            if (heightProportion > widthProportion) {
                return new ImageOptions((int) (width / heightProportion),
                        SystemUtils.getScreenHeightPx());
            } else {
                return new ImageOptions(SystemUtils.getScreenWidthPx(),
                        (int) (height / widthProportion));
            }
        } else if (heightProportion > 1) {
            return new ImageOptions((int) (width / heightProportion),
                    SystemUtils.getScreenHeightPx());
        }
        return null;
    }

    public static ImageOptions getLocalImageOptions(String imagePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        return new ImageOptions(width, height);
    }

    public static class ImageOptions {
        private int width;
        private int height;
        private int proportion = 1;
        private boolean is9patch;

        public ImageOptions(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void setProportion(int proportion) {
            this.proportion = proportion;
        }

        public int getProportion() {
            return proportion;
        }

        public boolean is9patch() {
            return is9patch;
        }

        public void setIs9patch(boolean is9patch) {
            this.is9patch = is9patch;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    public static class SaveThumbImageData {
        private String localFileKey;
        private Bitmap bitmap;

        public SaveThumbImageData(String localFileKey, Bitmap bitmap) {
            this.localFileKey = localFileKey;
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getLocalFileKey() {
            return localFileKey;
        }
    }

    public static Bitmap getReqSizeBitmapFromResources(Resources res, int resId, int reqWidth,
                                                       int reqHeight) {
        Bitmap result = null;
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, ops);
        int outWidth = ops.outWidth;
        int outHeight = ops.outHeight;
        if (outWidth > reqWidth || outHeight > reqHeight) {
            ops.inJustDecodeBounds = false;
            int sampleSize = 1;
            int halfWidth = outWidth / 2;
            int halfHeight = outHeight / 2;
            while (halfWidth / sampleSize > reqWidth && halfHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
            ops.inSampleSize = sampleSize;
            result = BitmapFactory.decodeResource(res, resId, ops);

        } else {
            result = BitmapFactory.decodeResource(res, resId);
        }
        return result;
    }

    public static Bitmap createRoundCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap createRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst_left + 0, dst_top + 0, dst_right - 0, dst_bottom - 0);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static Bitmap createRoundStrokedBitmap(Bitmap bitmap) {

        Bitmap roundBitmap = createRoundBitmap(bitmap);

        int width = roundBitmap.getWidth() + 20;
        int height = roundBitmap.getHeight() + 20;

        Bitmap buffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(buffer);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // RadialGradient gradient = new RadialGradient(width / 2, height / 2, width / 2,
        // new int[]{0xff5d5d5d,0xff5d5d5d,0x00ffffff},new float[]{0.f,0.8f,1.0f},
        // Shader.TileMode.CLAMP);
        // paint.setShader(gradient);

        paint.setColor(0xffffffff);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawBitmap(roundBitmap, 10, 10, paint);
        canvas.drawCircle(width / 2, height / 2, width / 2 - 5, paint);

        return buffer;
    }

    public static Bitmap createVipRoundStrokedBitmap(Bitmap bitmap) {

        Bitmap roundBitmap = createRoundBitmap(bitmap);

        int width = roundBitmap.getWidth() + 20;
        int height = roundBitmap.getHeight() + 20;

        Bitmap buffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(buffer);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setColor(0xfffdd415);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawBitmap(roundBitmap, 10, 10, paint);
        canvas.drawCircle(width / 2, height / 2, width / 2 - 5, paint);

        return buffer;
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        return bmp;
    }

    /**
     * 剪裁图片的指定区域，获得剪裁后的图片。
     * <p>
     * Author:Ice
     * <p>
     * Time:16/7/29 - 11:57
     * <p>
     * Version:1.0
     *
     * @param bmp   需要剪裁的图片
     * @param x     剪裁区域左上角x坐标
     * @param y     剪裁区域左上角y坐标
     * @param width 剪裁区域宽度
     * @param hight 剪裁区域高度
     */
    public static Bitmap CropBitmap(@NonNull Bitmap bmp, float x, float y, float width, float hight) {
        Bitmap cropedBmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
        cropedBmp = Bitmap.createBitmap(bmp, (int) x, (int) y, (int) width, (int) hight);
        return cropedBmp;
    }

    /**
     * 改变Bitmap的颜色。
     * <p>
     * Author:Ice
     * <p>
     * Time:16/8/7
     * <p>
     * Version:1.0
     *
     * @param bmp   需要改变的Bitmap。
     * @param color 需要改变的颜色。
     */
    public static Bitmap ChangeBmpColor(Bitmap bmp, int color) {
        Bitmap resBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resBmp);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return resBmp;
    }

    public static String savePNGBitmapToFile(Bitmap bitmap, String saveDirectory,
                                             String filename, int compress) {
        FileOutputStream fOut = null;
        try {
            if (null != bitmap) {
                if (FileUtils.createFolderIfNotExist(saveDirectory)) {
                    File f =
                            new File(saveDirectory.endsWith(File.separator) ? saveDirectory
                                    + filename : saveDirectory + File.separator + filename);
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();
                    fOut = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.PNG, compress, fOut);
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

    public static String saveJpgBitmapToFile(Bitmap bitmap, String saveDirectory,
                                             String filename, int compress) {
        FileOutputStream fOut = null;
        try {
            if (null != bitmap) {
                if (FileUtils.createFolderIfNotExistMedia(saveDirectory)) {
                    File f =
                            new File(saveDirectory.endsWith(File.separator) ? saveDirectory
                                    + filename : saveDirectory + File.separator + filename);
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();
                    fOut = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compress, fOut);
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

    public static void CopyImageFileToJPGFile(String imageFilePath) {
        File file = new File(imageFilePath);
        CopyImageFileToJPGFile(file);
    }

    public static void CopyImageFileToJPGFile(File imageFile) {
        ThreadPool.execute(() -> {
            String desPath =
                    FileUtils.getPhotoDir() + File.separator + System.currentTimeMillis() + ".jpg";
            FileUtils.copyFile(imageFile.getAbsolutePath(), desPath);
            new Handler(Looper.getMainLooper()).post(() -> {
                Uri localUri = Uri.fromFile(new File(desPath));
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                KabutoApplication.getAppContext().sendBroadcast(localIntent);
                Toast.showShortToast(R.string.photo_save_success);
            });
        });

    }

}
