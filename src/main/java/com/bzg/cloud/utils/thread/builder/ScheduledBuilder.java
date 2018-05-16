/**
 * Title: ScheduledBuilder.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2017年4月20日 下午3:36:55
 * Version: 1.0
 */
package com.bzg.cloud.utils.thread.builder;



import com.bzg.cloud.utils.thread.ThreadPoolType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/** 
 * <pre>
 * ClassName: ScheduledBuilder
 * </pre>
 */
public class ScheduledBuilder extends ThreadPoolBuilder<ExecutorService> {
	/** 固定线程池大小 */
	private int mSize = 1;
	
    protected ScheduledExecutorService mExecutorService = null;
    
	@Override
	protected ScheduledExecutorService create() {
		return Executors.newScheduledThreadPool(mSize);
	}
	
	@Override
	protected ThreadPoolType getType() {
		return ThreadPoolType.SCHEDULED;
	}
	
	@Override
	public ScheduledExecutorService builder() {
		if (mThreadPoolMap.get(getType() + "_" + mPoolName) != null) {
			mExecutorService = (ScheduledExecutorService)mThreadPoolMap.get(getType() + "_" + mPoolName);
		} else {
			mExecutorService = create();
			mThreadPoolMap.put(getType() + "_" + mPoolName, mExecutorService);
		}
		return mExecutorService;
	}
	
	public ScheduledExecutorService getExecutorService() {
		return mExecutorService;
	}
	
	public ScheduledBuilder size(int size) {
		this.mSize = size;
		return this;
	}
}
