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
	private Button bt_A;
	private Button bt_B;
	private Button bt_C;
	private Button bt_D;
	private Button bt_E;
	private Button bt_Q;
	private ThisListener listener;
//	private TagEntity mTagEntity;
	private DbHelper dbHelper;
//	private String mId;
	private String Password = "00000000";
	public IUHFService uhfService = new UHFServiceImpl();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_menu);
		dbHelper = new DbHelper(this);
		Intent intent = getIntent();
//		if (intent.hasExtra("id")) {
//			mId = intent.getStringExtra("id");
//		}
//		new threadWrite().execute(mTagEntity.getA_score(), mTagEntity.getB_score(),
//				mTagEntity.getC_score(), mTagEntity.getD_score(), mTagEntity.getE_score(),
//				mTagEntity.getTotal_score(), mTagEntity.getExpend_score());
		initView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		if (!StringUtil.isEmpty(mId)) {
//			try {
//				Dao<TagEntity, Integer> dao = dbHelper.getTagDao();
//				List<TagEntity> list = dao.queryForEq("id", mId);
//				if (null != list && list.size() > 0) {
//					mTagEntity = list.get(0);
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	private void initView() {
		listener = new ThisListener();
		bt_A = (Button) findViewById(R.id.b_mA);
		bt_B = (Button) findViewById(R.id.b_mB);
		bt_C = (Button) findViewById(R.id.b_mC);
		bt_D = (Button) findViewById(R.id.b_mD);
		bt_E = (Button) findViewById(R.id.b_mE);
		bt_Q = (Button) findViewById(R.id.b_mF);
		bt_A.setOnClickListener(listener);
		bt_B.setOnClickListener(listener);
		bt_C.setOnClickListener(listener);
		bt_D.setOnClickListener(listener);
		bt_E.setOnClickListener(listener);
		bt_Q.setOnClickListener(listener);

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
			case R.id.b_mA:
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_A);
				intent.putExtra("Title", "A 安全演示");
//				intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.b_mB:
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_B);
				intent.putExtra("Title", "B 小车快跑");
//				intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.b_mC:
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_C);
				intent.putExtra("Title", "C 夺岛奇兵");
//				intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.b_mD:
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_D);
				intent.putExtra("Title", "D 律动校园");
//				intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.b_mE:
				intent = new Intent(MenuActivity.this, ScoreActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_E);
				intent.putExtra("Title", "E 模拟城市");
//				intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;
			case R.id.b_mF:
				intent = new Intent(MenuActivity.this, ExchangeActivity.class);
				intent.putExtra("Tag", ScoreActivity.TAG_Ex);
				intent.putExtra("Title", "积分兑换");
//				intent.putExtra("data", mTagEntity);
				startActivity(intent);
				break;

			default:
				break;
			}

		}
	}
//
//	class threadWrite extends AsyncTask<Integer, String, Integer> {
//		ResultInfo resultInfo = null;
//
//		@Override
//		protected Integer doInBackground(Integer... params) {
//			String z = "0000";
//			for (int i = 0; i < params.length; i++) {
//				z = StringUtil.int2String4(params[i]);
//				resultInfo = uhfService.writeUser(i, Password, z);
//			}
//			return 0;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			if (resultInfo != null) {
//				if ("".equals(resultInfo.getErrInfo())) {
//					if (resultInfo.getResult() == 0) {
//						// showDialogMessage("Write user data success.");
//						Dao<TagEntity, Integer> dao;
//						try {
//							dao = dbHelper.getTagDao();
//							dao.createOrUpdate(mTagEntity);
//							finish();
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					} else {
//						showDialogMessage("Write user data faild : " + resultInfo.getResult());
//					}
//				} else {
//					showDialogMessage(resultInfo.getErrInfo());
//				}
//			}
//		}
//	}

}
