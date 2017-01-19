package com.qccr.paycenter.common.utils.concurrent;

import com.qccr.paycenter.common.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author:denghuajun
 * desc:延迟回查线程池，以及核心业务线程池，延迟回查负责向三方回查数据，
 * 数据最后交由核心业务线程池完成相应更新工作,扩大任务吞吐量
 */
public class ExecutorUtil {
	private ExecutorUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorUtil.class);

	/**
	 * 核心任务池
	 */
	private static ThreadPoolExecutor executor =new ThreadPoolExecutor(16, 32,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(10000));

	/**
	 * 通常物理机为16核心，目前没有监控工具所以将延时任务设置为12
	 */
	private static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(12);


	/**
	 * 关闭订单任务池
	 */
	private static ThreadPoolExecutor closeExecutor =new ThreadPoolExecutor(16, 32,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(10000));


	public static <V> Future<V>  submit(Callable<V> task){
		if(task==null){
			return null;
		}
		Future<V> future = executor.submit(task);
		return  future;
	}
	
	public static <V> Future<V> schedule(Callable<V> callable, long delay, TimeUnit unit){
		if(callable==null){
			return null;
		}
		Future<V>  future = scheduledExecutor.schedule(callable, delay, unit);
		return future;
	}

	public static void  healthydetection(){
		LogUtil.info(LOGGER, "executor active count: "+executor.getActiveCount()+" ,job queue size: "+executor.getQueue().size());
		LogUtil.info(LOGGER, "scheduledExecutor active count: "+scheduledExecutor.getActiveCount()+" ,job queue size: "+scheduledExecutor.getQueue().size());
	}

	public static <V> Future<V>  close(Callable<V> task){
		if(task==null){
			return null;
		}
		Future<V> future = closeExecutor.submit(task);
		return  future;
	}

}
