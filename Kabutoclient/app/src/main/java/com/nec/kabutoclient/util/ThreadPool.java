package com.nec.kabutoclient.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Thread pool.
 */
public class ThreadPool {

  private static final int CORE_THREAD_NUM = 6;
  public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(CORE_THREAD_NUM);

  private ThreadPool() {}

  /**
   * Executes task with normal priority.
   *
   * @param runnable runnable task
   */
  public static void execute(Runnable runnable) {
    EXECUTOR.execute(runnable);
  }

  /**
   * Submits a callable task.
   *
   * @param callable callable task
   * @param <T> result type
   * @return future
   */
  public static <T> Future<T> submit(Callable<T> callable) {
    return EXECUTOR.submit(callable);
  }
}
