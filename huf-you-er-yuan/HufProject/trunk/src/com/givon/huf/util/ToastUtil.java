/* 
 * Copyright 2013 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ToastUtil.java  2013-12-11 ����5:11:52 - Carson
 * @author YanXu
 * @email:981385016@qq.com
 * @version 1.0
 */

package com.givon.huf.util;

import android.widget.Toast;

import com.givon.huf.BaseApplication;
import com.givon.huf.R;

public class ToastUtil {

	public static void showMessage(int msg) {
		Toast toast = Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundResource(R.drawable.base_tip_bg);
		toast.getView().setPadding(45, 25, 45, 25);
		toast.show();
	}

	public static void showMessage(CharSequence text) {
		Toast toast = Toast.makeText(BaseApplication.getAppContext(), text, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundResource(R.drawable.base_tip_bg);
		toast.getView().setPadding(45, 25, 45, 25);
		toast.show();
	}

}