package com.nec.kabutoclient.di.modules;

import android.app.Application;
import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.nec.kabutoclient.data.sp.AppConfig;
import com.nec.kabutoclient.navigation.Navigator;
import com.nec.kabutoclient.util.FileUtils;
import com.nec.kabutoclient.util.FontsUtils;
import com.nec.kabutoclient.util.KabutoUncaughtExceptionHandler;
import com.nec.kabutoclient.util.SystemUtils;
import com.nec.kabutoclient.util.cache.DiskImageLoader;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ApplicationModule {
  public static final int MAX_SMALL_DISK_VERY_LOW_CACHE_SIZE = 5 * ByteConstants.MB;
  public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;
  public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;
  public static final int MAX_DISK_CACHE_VERY_LOW_SIZE = 10 * ByteConstants.MB;
  public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;
  public static final int MAX_DISK_CACHE_SIZE = 150 * ByteConstants.MB;
  public static final int MAX_MEMORY_SIZE = getMemoryCacheSize(20);
  private Application application;
  private DiskImageLoader diskImageLoader;
  private static int memoryCacheCount;

  public ApplicationModule(Application application) {
    this.application = application;
    if (SystemUtils.isMainProcess(application)) {
      Thread.setDefaultUncaughtExceptionHandler(new KabutoUncaughtExceptionHandler());
      Fresco.initialize(application, configureCaches());
      FontsUtils.init();
      AppConfig.init(application);
      diskImageLoader = DiskImageLoader.getInstance();
    }
  }

  @Provides
  @Singleton
  public Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  public DiskImageLoader provideGameImageLoader() {
    return this.diskImageLoader;
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return this.application;
  }

  @Provides
  @Singleton
  Navigator provideNavigator() {
    return new Navigator();
  }

  private ImagePipelineConfig configureCaches() {
    // 内存配置
    MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
        getMemoryCacheSize(15), // 内存缓存中总图片的最大大小,以字节为单位。
        Integer.MAX_VALUE, // 内存缓存中图片的最大数量。
        getMemoryCacheSize(5), // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
        Integer.MAX_VALUE, // 内存缓存中准备清除的总图片的最大数量。
        Integer.MAX_VALUE); // 内存缓存中单个图片的最大大小。

    // 修改内存图片缓存数量，空间策略（这个方式有点恶心）
    Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = () -> bitmapCacheParams;
    // 小图片的磁盘配置
    DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(application)
        .setBaseDirectoryPath(new File(FileUtils.getFileCacheDir()))// 缓存图片基路径
        .setBaseDirectoryName(FileUtils.SMALL_IMAGE_CACHE_NAME)// 文件夹名
        // .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
        // .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
        // .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
        .setMaxCacheSize(MAX_SMALL_DISK_CACHE_SIZE)// 默认缓存的最大大小。
        .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)// 缓存的最大大小,使用设备时低磁盘空间。
        .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERY_LOW_CACHE_SIZE)// 缓存的最大大小,当设备极低磁盘空间
        // .setVersion(version)
        .build();
    // 默认图片的磁盘配置
    DiskCacheConfig diskCacheConfig =
        DiskCacheConfig
            .newBuilder(application)
            .setBaseDirectoryPath(new File(FileUtils.getFileCacheDir()))
            // 缓存图片基路径
            .setBaseDirectoryName(FileUtils.NORMAL_IMAGE_CACHE_NAME)
            // .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
            // .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
            .setMaxCacheSize(MAX_DISK_CACHE_SIZE)// 默认缓存的最大大小。
            .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)// 缓存的最大大小,使用设备时低磁盘空间。
            .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERY_LOW_SIZE)// 缓存的最大大小,当设备极低磁盘空间
            // .setVersion(version)
            .build();
    // 缓存图片配置
    ImagePipelineConfig.Builder configBuilder =
        OkHttpImagePipelineConfigFactory
            .newBuilder(
                application,
                new OkHttpClient.Builder().readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS)
                    .build())
            // .setAnimatedImageFactory(AnimatedImageFactory animatedImageFactory)//图片加载动画
            .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)// 内存缓存配置（一级缓存，已解码的图片）
            .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
            // .setDownsampleEnabled(true)
            // .setCacheKeyFactory(cacheKeyFactory)//缓存Key工厂
            // .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)//内存缓存和未解码的内存缓存的配置（二级缓存）
            // .setExecutorSupplier(new DefaultExecutorSupplier())//线程池配置
            // .setImageCacheStatsTracker(imageCacheStatsTracker)//统计缓存的命中率
            // .setImageDecoder(new ImageDecoder()) //图片解码器配置
            // .setIsPrefetchEnabledSupplier(Supplier<Boolean、>
            // isPrefetchEnabledSupplier)//图片预览（缩略图，预加载图等）预加载到文件缓存
            .setMainDiskCacheConfig(diskCacheConfig)// 磁盘缓存配置（总，三级缓存）
            // //内存用量的缩减,有时我们可能会想缩小内存用量。比如应用中有其他数据需要占用内存，不得不把图片缓存清除或者减小 或者我们想检查看看手机是否已经内存不够了。
            // .setNetworkFetchProducer(networkFetchProducer)//自定的网络层配置：如OkHttp，Volley
            // .setPoolFactory(poolFactory)//线程池工厂配置
            // .setRequestListeners(requestListeners)//图片请求监听
            // .setResizeAndRotateEnabledForNetwork(boolean
            // resizeAndRotateEnabledForNetwork)//调整和旋转是否支持网络图片
            .setDownsampleEnabled(true)
            .setSmallImageDiskCacheConfig(diskSmallCacheConfig);// 磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
    return configBuilder.build();
  }

  public static int assignedMemoryCacheSize() {
    memoryCacheCount++;
    return MAX_MEMORY_SIZE / memoryCacheCount;
  }

  private static int getMemoryCacheSize(int availableMemoryPercent) {
    if (availableMemoryPercent > 0 && availableMemoryPercent < 100) {
      long availableMemory = Runtime.getRuntime().maxMemory();
      return (int) ((float) availableMemory * ((float) availableMemoryPercent / 100.0F));
    } else {
      throw new IllegalArgumentException("availableMemoryPercent must be in range (0 < % < 100)");
    }
  }
}
