/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ExchangeActivity.java  2014年8月21日 下午3:39:03 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.huf.act;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.givon.huf.BaseActivity;
import com.givon.huf.R;
import com.givon.huf.act.ScoreActivity.threadScan;
import com.givon.huf.db.DbHelper;
import com.givon.huf.entity.TagEntity;
import com.givon.huf.util.StringUtil;
import com.givon.huf.util.ToastUtil;
import com.j256.ormlite.dao.Dao;
import com.wpx.bean.ResultInfo;
import com.wpx.myutil.PlaySoundUtil;
import com.wpx.service.IUHFService;
import com.wpx.service.impl.UHFServiceImpl;

public class ExchangeActivity extends BaseActivity {
	private EditText et_IdEditText;
	private EditText et_ScoreEditText;
	private Button bt_queren;
	private Button bt_cexiao;
	private Button bt_duihuan;
	private Button bt_back;
	private ThisListener listener;
	private String SelectData;
	private String mTitle;
	private int Tag;
	private TagEntity mEntity;
	private DbHelper dbHelper;
	public IUHFService uhfService = new UHFServiceImpl();
	private String Password = "00000000";
	private int mShengyu;
	private TextView tv_Z_Score;
	private String idString;
	private int tatol_size = 7;
	private threadScan tScan;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_exchange);
		dbHelper = new DbHelper(this);
		Intent intent = getIntent();
		if (intent.hasExtra("Tag")) {
			Tag = intent.getIntExtra("Tag", -1);
		}
		if (intent.hasExtra("Title")) {
			mTitle = intent.getStringExtra("Title");
		}
		if (intent.hasExtra("data")) {
			// mEntity = (TagEntity) intent.getSerializableExtra("data");
		}
		initView();
	}

	private void initView() {
		listener = new ThisListener();
		et_IdEditText = (EditText) findViewById(R.id.id);
		et_ScoreEditText = (EditText) findViewById(R.id.score);
		et_ScoreEditText.setText("0");
		bt_queren = (Button) findViewById(R.id.queren);
		bt_cexiao = (Button) findViewById(R.id.cexiao);
		tv_Z_Score = (TextView) findViewById(R.id.z_score);
		bt_duihuan = (Button) findViewById(R.id.duihuan);
		bt_back = (Button) findViewById(R.id.back);
		initViewData();

	}

	private void initViewData() {
		if (null != mEntity) {
			et_IdEditText.setText(mEntity.getId());
			mShengyu = mEntity.getTotal_score() - mEntity.getExpend_score();
			// et_ScoreEditText.setText(mShengyu+"");
			tv_Z_Score.setText("剩余积分：" + mShengyu);
			et_ScoreEditText.addTextChangedListener(mTextWatcher);
			bt_queren.setOnClickListener(listener);
			bt_cexiao.setOnClickListener(listener);
		}else {
			et_IdEditText.setText("");
			et_ScoreEditText.setText(""+0);
			tv_Z_Score.setText("");
		}
		bt_back.setOnClickListener(listener);
		bt_duihuan.setOnClickListener(listener);

	}

	private void scanAction() {
		tScan = new threadScan();
		tScan.execute();
		bt_duihuan.setEnabled(false);
	}

	private class ThisListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.queren:
				SelectData = et_ScoreEditText.getText().toString();
				if (!StringUtil.isEmpty(SelectData)) {
					try {
						int duihuan = Integer.valueOf(SelectData);
						if (duihuan > 0) {
							if ((mEntity.getTotal_score() - mEntity.getExpend_score() - duihuan) >= 0) {
//								duihuan = mEntity.getExpend_score() + duihuan;
								new threadWrite().execute(duihuan);
							} else {
								showDialogMessage("积分不足不能兑换");
							}
						} else {
							showDialogMessage("输入积分错误");
						}
					} catch (Exception e) {
						showDialogMessage("输入积分错误");
					}
				} else {
					showDialogMessage("兑换积分不能为空");
				}
				break;
			case R.id.cexiao:
				et_ScoreEditText.setText("0");
				break;
			case R.id.back:
				finish();
				break;
			case R.id.duihuan:
				if (mEntity == null) {
					scanAction();
				} else {
					et_ScoreEditText.setText("500");
				}
				break;

			default:
				break;
			}

		}

	}

	class threadWrite extends AsyncTask<Integer, String, Integer> {
		ResultInfo resultInfo = null;

		@Override
		protected Integer doInBackground(Integer... params) {
			String block_Data = "0000";
			block_Data = StringUtil.int2String4(params[0]);
			resultInfo = uhfService.writeUser(Tag, Password, block_Data);
			return 0;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (resultInfo != null) {
				if ("".equals(resultInfo.getErrInfo())) {
					if (resultInfo.getResult() == 0) {
						// showDialogMessage("Write user data success.");
						mEntity.setExpend_score(Integer.valueOf(SelectData));
						Dao<TagEntity, Integer> dao;
						try {
							dao = dbHelper.getTagDao();
							dao.createOrUpdate(mEntity);
							ToastUtil.showMessage("兑换成功");
							mEntity =null;
						} catch (SQLException e) {
							e.printStackTrace();
						}
						initViewData();
					} else {
						ToastUtil.showMessage("Write user data faild : " + resultInfo.getResult());
					}
				} else {
					ToastUtil.showMessage(resultInfo.getErrInfo());
				}
			}
		}
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			temp = s;
			try {
				if (mShengyu - Integer.valueOf(s.toString()) >= 0) {
					tv_Z_Score.setText("剩余积分：" + (mShengyu - Integer.valueOf((String) s)));
				} else {
					tv_Z_Score.setText("剩余积分：" + mShengyu);
					ToastUtil.showMessage("积分超出兑换范围啦");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// mTextView.setText(s);//将输入的内容实时显示

		}

		@Override
		public void afterTextChanged(Editable s) {
			editStart = et_ScoreEditText.getSelectionStart();
			editEnd = et_ScoreEditText.getSelectionEnd();
			if (temp.length() > 4) {
				Toast.makeText(ExchangeActivity.this, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				et_ScoreEditText.setText(s);
				et_ScoreEditText.setSelection(tempSelection);
			}
		}
	};

	class threadScan extends AsyncTask<Integer, String, Integer> {
		ResultInfo resultInfo = null;

		// @Override
		protected Integer doInBackground(Integer... params) {
			resultInfo = uhfService.getEPCList(uhfService.getQValue());
			return 0;
		}

		// @Override
		protected void onPreExecute() {
//			ToastUtil.showMessage("Scaning.....");
		}

		// @Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (resultInfo != null) {
				if (resultInfo.getResult() == 0) {
					resultInfo.getValues();
					if (resultInfo.getValues().size() > 0
							&& !StringUtil.isEmpty(resultInfo.getValues().get(0))) {
						PlaySoundUtil.play();
						idString = resultInfo.getValues().get(0);
						new threadRead().execute();
					} else {
						bt_duihuan.setEnabled(true);
					}
					// ToastUtil.showMessage("Tag is found " + resultInfo.getValues().size());
				} else {
					bt_duihuan.setEnabled(true);
					ToastUtil.showMessage("Search tag is failed : " + resultInfo.getResult());

				}
			} else {
				bt_duihuan.setEnabled(true);
			}
		}
	}

	private class threadRead extends AsyncTask<Integer, Integer, ArrayList<ResultInfo>> {
		ResultInfo resultInfo = null;

		@Override
		protected ArrayList<ResultInfo> doInBackground(Integer... params) {
			byte read_len = 1;
			ArrayList<ResultInfo> list = new ArrayList<>();
			ResultInfo resultInfo = null;
			for (int i = 0; i < tatol_size; i++) {
				resultInfo = uhfService.readUser(i, Password, read_len);
				publishProgress((int) ((i / (float) tatol_size) * 100));
				list.add(resultInfo);
			}
			return list;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// ToastUtil.showMessage("Read user data ....");
		}

		@Override
		protected void onPostExecute(ArrayList<ResultInfo> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				boolean isSuccess = true;
				if (null == mEntity) {
					mEntity = new TagEntity();
				}
				for (int i = 0; i < result.size(); i++) {
					ResultInfo resultInfo = result.get(i);
					if ("".equals(resultInfo.getErrInfo())) {
						if (resultInfo.getResult() == 0) {

							mEntity = setTagEntity(i, resultInfo.getResultValue(), mEntity);
						} else {
							isSuccess = false;

						}
					} else {

						isSuccess = false;
						ToastUtil.showMessage(resultInfo.getErrInfo());
					}
				}
				if (isSuccess) {
					if (null != mEntity) {
						try {
							Dao<TagEntity, Integer> tagdao = dbHelper.getTagDao();
							tagdao.createOrUpdate(mEntity);
							et_ScoreEditText.setText("500");
							initViewData();
							// Intent intent = new Intent(MainActivity.this, MenuActivity.class);
							// intent.putExtra("id", mEntity.getId());
							// startActivity(intent);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					ToastUtil.showMessage("读取成功");
				} else {
					ToastUtil.showMessage("读取失败");
				}
			}
			bt_duihuan.setEnabled(true);
		}

	}

	private TagEntity setTagEntity(int i, String value, TagEntity tag) {
		// System.out.println("value:"+value);
		tag.setId(idString);
		switch (i) {
		case 0:
			tag.setA_score(value);
			break;
		case 1:
			tag.setB_score(value);

			break;
		case 2:
			tag.setC_score(value);

			break;
		case 3:
			tag.setD_score(value);

			break;
		case 4:
			tag.setE_score(value);

			break;
		case 5:
			tag.setTotal_score(value);
			break;
		case 6:
			tag.setExpend_score(value);
			break;

		default:
			break;
		}
		return tag;
	}

}
