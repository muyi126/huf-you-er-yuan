/* 
 * Copyright 2013 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @BaseAdapter.java  2013-12-10 8:16:07 - Carson
 * @author YanXu
 * @email:981385016@qq.com
 * @version 1.0
 */

package com.givon.huf;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.givon.huf.util.BitmapHelp;
import com.lidroid.xutils.BitmapUtils;
/**
 * 
* @ClassName: BaseAdapter
* @Description: TODO(BaseAdapter基类)
* @author Yanxu
* @date 2013-12-26 上午10:20:06
*
* @param <T>
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	public ArrayList<T> mList;
	public Context mContext;
	private int mTotal;
	private BitmapUtils bitmapUtils;

	public int getTotal() {
		return mTotal;
	}



	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.bitmapUtils = bitmapUtils;
	}


	public void setTotal(int mTotal) {
		this.mTotal = mTotal;
	}

	public BaseAdapter(Context ctx) {
		this.mContext = ctx;
		this.mList = new ArrayList<T>();
		bitmapUtils = BitmapHelp.getBitmapUtils(ctx);
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	public void clearAdapter() {
		if (this.mList != null) {
			this.mList.clear();
		}
	}
/**
 * 
* @Title: resetAdapter
* @Description: TODO(清除list原有数据加载新数据)
* @param @param list    设定文件
* @return void    返回类型
* @throws
 */
	public void resetAdapter(ArrayList<T> list) {
		mTotal = list.size();
		mList.clear();

		mList.addAll(list);
		this.notifyDataSetChanged();
	}
/**
 * 
* @Title: updateAdapter
* @Description: TODO(添加新的list，更新adapter)
* @param @param list
* @param @param total    设定文件
* @return void    返回类型
* @throws
 */
	public void updateAdapter(ArrayList<T> list, int total) {
		mTotal = total;
		if (list != null && list.size() > 0) {
			mList.addAll(list);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
/**
 * 
* @Title: getItemView
* @Description: TODO(抽象方法Adapter的getView)
* @param @param position
* @param @param convertView
* @param @param parent
* @param @return    设定文件
* @return View    返回类型
* @throws
 */
	public abstract View getItemView(int position, View convertView, ViewGroup parent);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItemView(position, convertView, parent);
	}

}
