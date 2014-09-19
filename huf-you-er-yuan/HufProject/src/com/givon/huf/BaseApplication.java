/* 
 * Copyright 2013 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @SCSDApplication.java  2013-12-10 下午8:07:13 - Carson
 * @author YanXu
 * @email:981385016@qq.com
 * @version 1.0
 */

package com.givon.huf;

import android.app.Application;
import android.content.Context;

import com.givon.huf.util.CrashHandler;
import com.wpx.service.IUHFService;
import com.wpx.service.impl.UHFServiceImpl;

/**
 * 
* @ClassName: SCSDApplication
* @Description: TODO(初始化百度推送)
* @author Yanxu
* @date 2013-12-26 上午10:15:56
*
 */
public class BaseApplication extends Application {

	private static BaseApplication mInstance;
	public boolean m_bKeyRight = true;
	public static int mWidth;
	public static int mHeight;
	public IUHFService uhfService = new UHFServiceImpl();
	public static Context getAppContext() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mWidth = getResources().getDisplayMetrics().widthPixels;
		mHeight = getResources().getDisplayMetrics().heightPixels;
		uhfService.setDeviceType(0);
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		// 注册crashHandler
//		crashHandler.init(getApplicationContext());
		
	}


	public static BaseApplication getInstance() {
		return mInstance;
	}

}
