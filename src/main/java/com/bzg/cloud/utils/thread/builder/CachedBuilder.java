/**
 * Title: CachedBuilder.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2017年4月20日 下午3:35:50
 * Version: 1.0
 */
package com.bzg.cloud.utils.thread.builder;



import com.bzg.cloud.utils.thread.ThreadPoolType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <pre>
 * ClassName: CachedBuilder
 * </pre>
 */
public class CachedBuilder extends ThreadPoolBuilder<ExecutorService> {
	@Override
	protected ExecutorService create() {
		return Executors.newCachedThreadPool();
	}

	@Override
	protected ThreadPoolType getType() {
		return ThreadPoolType.CACHED;
	}
}
