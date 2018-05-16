/**
 * Title: FixedBuilder.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2017年4月20日 下午3:36:36
 * Version: 1.0
 */
package com.bzg.cloud.utils.thread.builder;



import com.bzg.cloud.utils.thread.ThreadPoolType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/** 
 * <pre>
 * ClassName: FixedBuilder
 * </pre>
 */
public class FixedBuilder extends ThreadPoolBuilder<ExecutorService> {
	/** 固定线程池  */
	private int mSize = 1;
	
	@Override
	protected ExecutorService create() {
		return Executors.newFixedThreadPool(mSize);
	}

	@Override
	protected ThreadPoolType getType() {
		return ThreadPoolType.FIXED;
	}
	
	public FixedBuilder setSize(int size) {
		mSize = size;
		return this;
	}
}
