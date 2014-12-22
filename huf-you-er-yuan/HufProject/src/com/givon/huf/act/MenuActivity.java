/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @MenuActivity.java  2014年8月17日 下午7:03:05 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.huf.act;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.givon.huf.BaseActivity;
import com.givon.huf.R;
import com.givon.huf.db.DbHelper;
import com.givon.huf.entity.TagEntity;
import com.givon.huf.util.StringUtil;
import com.j256.ormlite.dao.Dao;
import com.wpx.bean.ResultInfo;
import com.wpx.service.IUHFService;
import com.wpx.service.impl.UHFServiceImpl;

public class MenuActivity extends BaseActivity {
	private Button bt_Back;
	// private Button bt_A;
	// private Button bt_B;
	// private Button bt_C;
	// private Button bt_D;
	// private Button bt_E;
	// private Button bt_Q;
	private TextView mTv_xiaoChe;
	private TextView mTv_duoDao;
	private TextView mTv_jiuZuiTiYan;
	private TextView mTv_moniJiaShi;
	private TextView mTv_miniChengShi;
	private ImageView iv_jifenduihuan;
	private ImageView iv_shujuqingkong;
	private ThisListener listener;
	// private TagEntity mTagEntity;
	private DbHelper dbHelper;
	// private String mId;
	private String Password = "00000000";
	public IUHFService uhfService = new UHFServiceImpl();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_menu);
		dbHelper = new DbHelper(this);
		Intent intent = getIntent();
		// if (intent.hasExtra("id")) {
		// mId = intent.getStringExtra("id");
		// }
		// new threadWrite().execute(mTagEntity.getA_score(), mTagEntity.getB_score(),
		// mTagEntity.getC_score(), mTagEntity.getD_score(), mTagEntity.getE_score(),
		// mTagEntity.getTotal_score(), mTagEntity.getExpend_score());
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		listener = new ThisListener();
		iv_jifenduihuan = (ImageView) findViewById(R.id.iv_jifenduihuan);
		iv_shujuqingkong = (ImageView) findViewById(R.id.iv_shujuqingkong);
		mTv_duoDao = (TextView) findViewById(R.id.tv_duodaoqibin);
		mTv_jiuZuiTiYan = (TextView) findViewById(R.id.tv_zuijiutiyan);
		mTv_miniChengShi = (TextView) findViewById(R.id.tv_minichengshi);
		mTv_moniJiaShi = (TextView) findViewById(R.id.tv_monijiaoshi);
		mTv_xiaoChe = (TextView) findViewById(R.id.tv_xiaoche);
		iv_jifenduihuan.setOnClickListener(listener);
		iv_shujuqingkong.setOnClickListener(listener);
		mTv_duoDao.setOnClickListener(listener);
		mTv_jiuZuiTiYan.setOnClickListener(listener);
		mTv_miniChengShi.setOnClickListener(listener);
		mTv_moniJiaShi.setOnClickListener(listener);
		mTv_xiaoChe.setOnClickListener(listener);

		bt_Back = (Button) findViewById(R.id.back);
		bt_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private class ThisListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			 case R.id.tv_zuijiutiyan:
				 System.out.println("醉酒体验");
			 intent = new Intent(MenuActivity.this, ScoreActivity.class);
			 intent.putExtra("Tag", ScoreActivity.TAG_A);
			 intent.putExtra("Title", "醉酒体验");
			 // intent.putExtra("data", mTagEntity);
			 startActivity(intent);
			 break;
			case R.id.tv_xiaoche:
				System.out.println("小车快跑");
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_B);
				intent.putExtra("Title", "小车快跑");
				// intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.tv_duodaoqibin:
				System.out.println("夺岛奇兵");
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_C);
				intent.putExtra("Title", "夺岛奇兵");
				// intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.tv_monijiaoshi:
				System.out.println("模拟驾驶");
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_D);
				intent.putExtra("Title", "模拟驾驶");
				// intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.tv_minichengshi:
				System.out.println("MINI城市");
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_E);
				intent.putExtra("Title", "MINI城市");
				// intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.iv_jifenduihuan:
				intent = new Intent(MenuActivity.this, ExchangeActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_Ex);
				intent.putExtra("Title", "积分兑换");
				// intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.iv_shujuqingkong:
				intent = new Intent(MenuActivity.this, ShuJuQKActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}

		}
	}
	//
	// class threadWrite extends AsyncTask<Integer, String, Integer> {
	// ResultInfo resultInfo = null;
	//
	// @Override
	// protected Integer doInBackground(Integer... params) {
	// String z = "0000";
	// for (int i = 0; i < params.length; i++) {
	// z = StringUtil.int2String4(params[i]);
	// resultInfo = uhfService.writeUser(i, Password, z);
	// }
	// return 0;
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// }
	//
	// @Override
	// protected void onPostExecute(Integer result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// if (resultInfo != null) {
	// if ("".equals(resultInfo.getErrInfo())) {
	// if (resultInfo.getResult() == 0) {
	// // showDialogMessage("Write user data success.");
	// Dao<TagEntity, Integer> dao;
	// try {
	// dao = dbHelper.getTagDao();
	// dao.createOrUpdate(mTagEntity);
	// finish();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// } else {
	// showDialogMessage("Write user data faild : " + resultInfo.getResult());
	// }
	// } else {
	// showDialogMessage(resultInfo.getErrInfo());
	// }
	// }
	// }
	// }

}
