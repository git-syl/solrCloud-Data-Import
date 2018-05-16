/**
 * Title: ThreadPoolHelp.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2017年4月20日 下午4:23:07
 * Version: 1.0
 */
package com.bzg.cloud.utils.thread;



import com.bzg.cloud.utils.thread.builder.*;

import java.util.concurrent.*;


/**
 * <pre>
 * ClassName: ThreadPoolHelp
 * </pre>
 */
public class ThreadPoolHelp {
	
	public static class Builder {
		/** 线程名称 */
		private String mName = null;
		/** 线程类型 */
		private ThreadPoolType mType = null;
		/** 固定线程池  */
		private int mSize = 1;
		/** 核心线程池大小 */
		private int mCorePoolSize = 1;
		/** 最大线程池大小 */
		private int mMaximumPoolSize = Integer.MAX_VALUE;
		/** 线程任务空闲保留时间 */
		private long mKeepAliveTime = 60;
		/** 线程任务空闲保留时间单位 */
		private TimeUnit mUnit = TimeUnit.SECONDS;
		/** 任务等待策略 */
		private BlockingQueue<Runnable> mWorkQueue = new SynchronousQueue<Runnable>();
		
		private ThreadPoolBuilder<ExecutorService> mThreadPoolBuilder = null;
		
		public Builder(ThreadPoolType type) {
			mType = type;
		}
		
		public Builder(ThreadPoolType type, int size) {
			mType = type;
			mSize = size;
		}
		
		public Builder(ThreadPoolType type, 
				int corePoolSize,
                int maximumPoolSize,
                long keepAliveTime,
                TimeUnit unit,
                BlockingQueue<Runnable> workQueue) {
			mType = type;
			mCorePoolSize = corePoolSize;
			mMaximumPoolSize = maximumPoolSize;
			mKeepAliveTime = keepAliveTime;
			mUnit = unit;
			mWorkQueue = workQueue;
		}
		
		public static Builder cached() {
			return new Builder(ThreadPoolType.CACHED);
		}
		
		public static Builder fixed(int size) {
			return new Builder(ThreadPoolType.FIXED, size);
		}
		
		public static Builder single() {
			return new Builder(ThreadPoolType.SINGLE);
		}
		
		public static Builder schedule(int size) {
			return new Builder(ThreadPoolType.SCHEDULED, size);
		}
		
		public static Builder custom(int corePoolSize,
                int maximumPoolSize,
                long keepAliveTime,
                TimeUnit unit,
                BlockingQueue<Runnable> workQueue) {
			return new Builder(ThreadPoolType.CUSTOM, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}
		
		public Builder name(String name) {
			mName = name;
			return this;
		}
		
		public ExecutorService builder() {
			createThreadPoolBuilder();
			return mThreadPoolBuilder.builder();
		}
		
		public ScheduledExecutorService scheduleBuilder() {
			createThreadPoolBuilder();
			if (mThreadPoolBuilder.builder() instanceof ScheduledExecutorService) {
				return (ScheduledExecutorService)mThreadPoolBuilder.builder();
			}
			return null;
		}
		
		private void createThreadPoolBuilder() {
			if (mType == ThreadPoolType.CACHED) {
				mThreadPoolBuilder = new CachedBuilder().poolName(mName);
			} else if (mType == ThreadPoolType.FIXED) {
				mThreadPoolBuilder = new FixedBuilder().setSize(mSize).poolName(mName);
			} else if (mType == ThreadPoolType.SCHEDULED) {
				mThreadPoolBuilder = new ScheduledBuilder().poolName(mName);
			} else if (mType == ThreadPoolType.SINGLE) {
				mThreadPoolBuilder = new SingleBuilder().poolName(mName);
			} else if (mType == ThreadPoolType.CUSTOM) {
				mThreadPoolBuilder = new CustomBuilder().corePoolSize(mCorePoolSize).maximumPoolSize(mMaximumPoolSize).keepAliveTime(mKeepAliveTime).unit(mUnit).workQueue(mWorkQueue).poolName(mName);
			}
		}
	}
}
