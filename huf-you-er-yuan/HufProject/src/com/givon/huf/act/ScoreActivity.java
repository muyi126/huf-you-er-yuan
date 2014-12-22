/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ScoreActivity.java  2014年8月21日 下午3:19:16 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.huf.act;

import java.sql.SQLException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.givon.huf.BaseActivity;
import com.givon.huf.R;
import com.givon.huf.db.DbHelper;
import com.givon.huf.entity.TagEntity;
import com.givon.huf.util.StringUtil;
import com.givon.huf.util.ToastUtil;
import com.j256.ormlite.dao.Dao;
import com.wpx.bean.ResultInfo;
import com.wpx.myutil.PlaySoundUtil;
import com.wpx.service.IUHFService;
import com.wpx.service.impl.UHFServiceImpl;

public class ScoreActivity extends BaseActivity {

	public final static int TAG_A = 0;
	public final static int TAG_B = 1;
	public final static int TAG_C = 2;
	public final static int TAG_D = 3;
	public final static int TAG_E = 4;
	public final static int TAG_Total = 5;
	public final static int TAG_Ex = 6;
	private String Password = "00000000";
	public IUHFService uhfService = new UHFServiceImpl();
	private String mTitle;
	private int Tag;
	// private TextView et_IdEditText;
	// private EditText et_ScoreEditText;
	private TextView tv_Title;
	private ImageView bt_50;
	private ImageView bt_100;
	private ImageView iv_stopScore;
	private threadScan tScan;
	private threadWrite tWrite;
	private Button bt_queren;
	private Button bt_cexiao;
	private Button bt_back;
	private TagEntity mEntity;
	private TagEntity mNewEntity;
	private ThisListener listener;
	private DbHelper dbHelper;
	private int SelectData;
	private boolean isEdit = true;
	// private TextView z_Score;
	private TextView tv_id;
	private TextView tv_zf_score;
	private String idString;
	private int tatol_size = 7;
	private LinearLayout ly_top;
	private boolean isScanIng = false;
	private boolean isWriting = false;
	private boolean isStop = false;

	protected void onCreate(android.os.Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_score);
		Intent intent = getIntent();
		if (intent.hasExtra("Tag")) {
			Tag = intent.getIntExtra("Tag", -1);
		}
		if (intent.hasExtra("Title")) {
			mTitle = intent.getStringExtra("Title");
			System.out.println("Title:" + mTitle);
		}
		initView();
	};

	@SuppressLint("NewApi")
	private void initView() {

		listener = new ThisListener();
		// et_IdEditText = (TextView) findViewById(R.id.id);
		// et_ScoreEditText = (EditText) findViewById(R.id.score);
		// tv_Title = (TextView) findViewById(R.id.title);
		// if (!StringUtil.isEmpty(mTitle)) {
		// tv_Title.setText(mTitle);
		// }
		tv_id = (TextView) findViewById(R.id.tv_id);
		tv_zf_score = (TextView) findViewById(R.id.tv_zf_score);
		bt_100 = (ImageView) findViewById(R.id.iv_100);
		bt_50 = (ImageView) findViewById(R.id.iv_50);
		iv_stopScore = (ImageView) findViewById(R.id.iv_stopScore);
		iv_stopScore.setVisibility(View.GONE);
		iv_stopScore.setOnClickListener(listener);
		// bt_queren = (Button) findViewById(R.id.queren);
		// bt_cexiao = (Button) findViewById(R.id.cexiao);
		bt_back = (Button) findViewById(R.id.back);
		bt_100.setOnClickListener(listener);
		bt_50.setOnClickListener(listener);
		// bt_queren.setOnClickListener(listener);
		// bt_cexiao.setOnClickListener(listener);
		bt_back.setOnClickListener(listener);
		dbHelper = new DbHelper(this);
		// z_Score = (TextView) findViewById(R.id.z_score);

		ly_top = (LinearLayout) findViewById(R.id.ly_top);
		switch (Tag) {
		case TAG_A:
			ly_top.setBackgroundResource(R.drawable.cy_t_bg);
			break;
		case TAG_B:
			ly_top.setBackgroundResource(R.drawable.jj_t_bg);
			break;
		case TAG_C:
			ly_top.setBackgroundResource(R.drawable.jj_t_bg);
			break;
		case TAG_D:
			ly_top.setBackgroundResource(R.drawable.cy_t_bg);
			break;
		case TAG_E:
			ly_top.setBackgroundResource(R.drawable.cy_t_bg);
			break;

		default:
			break;
		}

		initData();

	}

	private void initData() {
		if (null != mEntity) {
			tv_id.setText(mEntity.getId());
		} else {
			tv_id.setText("");
			tv_zf_score.setText(0 + "");
			// SelectData = 0;
		}
		if (null != mEntity) {
			switch (Tag) {
			case TAG_A:
				tv_zf_score.setText(mEntity.getA_score() + "");
				// SelectData = mEntity.getA_score();
				break;
			case TAG_B:
				tv_zf_score.setText(mEntity.getB_score() + "");
				// SelectData = mEntity.getB_score();
				break;
			case TAG_C:
				tv_zf_score.setText(mEntity.getC_score() + "");
				// SelectData = mEntity.getC_score();

				break;
			case TAG_D:
				tv_zf_score.setText(mEntity.getD_score() + "");
				// SelectData = mEntity.getD_score();

				break;
			case TAG_E:
				tv_zf_score.setText(mEntity.getE_score() + "");
				// SelectData = mEntity.getE_score();

				break;

			default:
				break;
			}
			tv_zf_score.setText("总分:" + mEntity.getTotal_score());
			// tv_zf_score.setText("" + SelectData);
		}

		if (SelectData == 0) {
			isEdit = true;
		} else {
			isEdit = false;
		}
	}

	private class ThisListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mNewEntity = new TagEntity();
			if (null != mEntity) {
				mNewEntity.setA_score(mEntity.getA_score());
				mNewEntity.setB_score(mEntity.getB_score());
				mNewEntity.setC_score(mEntity.getC_score());
				mNewEntity.setD_score(mEntity.getD_score());
				mNewEntity.setE_score(mEntity.getE_score());
				mNewEntity.setExpend_score(mEntity.getExpend_score());
				mNewEntity.setTotal_score(mEntity.getTotal_score());
			}
			switch (v.getId()) {
			case R.id.iv_100:
				bt_100.setVisibility(View.VISIBLE);
				bt_50.setVisibility(View.GONE);
				SelectData = 100;
				iv_stopScore.setVisibility(View.VISIBLE);
				scanAction();
				break;
			case R.id.iv_50:
				bt_50.setVisibility(View.VISIBLE);
				bt_100.setVisibility(View.GONE);
				SelectData = 50;
				iv_stopScore.setVisibility(View.VISIBLE);
				scanAction();
				break;
			case R.id.iv_stopScore:
				bt_50.setVisibility(View.VISIBLE);
				bt_100.setVisibility(View.VISIBLE);
				SelectData = 0;
				// 停止搜索
				isStop = true;
				iv_stopScore.setVisibility(View.GONE);
				break;
			case R.id.back:
				// 停止搜索
				isStop = true;
				finish();
				break;

			default:
				break;
			}
			// switch (v.getId()) {
			// case R.id.bt_100:
			// if (null == mEntity) {
			// scanAction();
			// } else {
			// SelectData = 100;
			// et_ScoreEditText.setText("100");
			// setEntity(Tag, mNewEntity, 100);
			// }
			// break;
			// case R.id.bt_50:
			// if (null == mEntity) {
			// scanAction();
			// } else {
			// SelectData = 50;
			// et_ScoreEditText.setText("50");
			// setEntity(Tag, mNewEntity, 50);
			// }
			// break;
			// case R.id.queren:
			// if (isEdit) {
			// showWaitingDialog();
			// if (SelectData == 0) {
			// ToastUtil.showMessage("打分为零");
			// return;
			// }
			// setEntity(Tag, mNewEntity, SelectData);
			// new threadWrite().execute(SelectData, mNewEntity.getTotal_score());
			// } else {
			// ToastUtil.showMessage("请勿重复打分");
			// }
			// break;
			// case R.id.cexiao:
			// if (!isEdit) {
			// setEntity(Tag, mNewEntity, 0);
			// if (mNewEntity.getTotal_score() - mNewEntity.getExpend_score() < 0) {
			// ToastUtil.showMessage("积分已经兑换不能撤销");
			// } else {
			// showWaitingDialog();
			// new threadCexiao().execute(0, mNewEntity.getTotal_score());
			// }
			// } else {
			// ToastUtil.showMessage("没有打分");
			// }
			// break;
			// case R.id.back:
			// finish();
			// break;
			//
			// default:
			// break;
			// }

		}

	}

	private TagEntity setEntity(int type, TagEntity entity, int value) {
		switch (type) {
		case TAG_A:
			entity.setA_score(value);
			break;
		case TAG_B:
			entity.setB_score(value);

			break;
		case TAG_C:
			entity.setC_score(value);

			break;
		case TAG_D:
			entity.setD_score(value);

			break;
		case TAG_E:
			entity.setE_score(value);

			break;

		default:
			break;
		}
		return entity;
	}

	private void scanAction() {
		if (!isScanIng && !isStop) {
			tScan = new threadScan();
			tScan.execute();
		}
		// bt_50.setEnabled(false);
		// bt_100.setEnabled(false);
	}

	private void writeAction() {
		mNewEntity = new TagEntity();
		if (null != mEntity) {
			mNewEntity.setA_score(mEntity.getA_score());
			mNewEntity.setB_score(mEntity.getB_score());
			mNewEntity.setC_score(mEntity.getC_score());
			mNewEntity.setD_score(mEntity.getD_score());
			mNewEntity.setE_score(mEntity.getE_score());
			mNewEntity.setExpend_score(mEntity.getExpend_score());
			mNewEntity.setTotal_score(mEntity.getTotal_score());
		}
		if (!isWriting && !isStop) {
			if (SelectData == 0) {
				ToastUtil.showMessage("打分为零");
				return;
			}
			setEntity(Tag, mNewEntity, SelectData);
			tWrite = new threadWrite();
			tWrite.execute(SelectData, mNewEntity.getTotal_score());
		}
		// bt_50.setEnabled(false);
		// bt_100.setEnabled(false);
	}

	class threadWrite extends AsyncTask<Integer, String, Integer> {
		ResultInfo resultInfo = null;
		private int selet = 0;

		@Override
		protected Integer doInBackground(Integer... params) {
			String block_Data = "0000";
			String z = "0000";
			selet = params[0];
			block_Data = StringUtil.int2String4(params[0]);
			z = StringUtil.int2String4(params[1]);
			resultInfo = uhfService.writeUser(Tag, Password, block_Data);
			resultInfo = uhfService.writeUser(5, Password, z);
			return 0;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showWaitingDialog();
			isWriting = true;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dismissWaitingDialog();
			if (resultInfo != null) {
				if ("".equals(resultInfo.getErrInfo())) {
					if (resultInfo.getResult() == 0) {
						// showDialogMessage("Write user data success.");
						Dao<TagEntity, Integer> dao;
						try {
							setEntity(Tag, mEntity, selet);
							dao = dbHelper.getTagDao();
							dao.createOrUpdate(mEntity);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						mEntity = null;
						PlaySoundUtil.play();
						initData();
						showDialogMessage("数据设置成功", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								scanAction();
							}
						});
					} else {
						ToastUtil.showMessage("Write user data faild : " + resultInfo.getResult());
					}
				} else {
					ToastUtil.showMessage(resultInfo.getErrInfo());
				}
			}
			isWriting = false;
		}
	}

	class threadCexiao extends AsyncTask<Integer, String, Integer> {
		ResultInfo resultInfo = null;

		@Override
		protected Integer doInBackground(Integer... params) {
			String block_Data = "0000";
			String z = "0000";
			block_Data = StringUtil.int2String4(params[0]);
			z = StringUtil.int2String4(params[1]);
			resultInfo = uhfService.writeUser(Tag, Password, block_Data);
			System.out.println("zzzz:" + z);
			resultInfo = uhfService.writeUser(5, Password, z);
			return 0;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dismissWaitingDialog();
			if (resultInfo != null) {
				if ("".equals(resultInfo.getErrInfo())) {
					if (resultInfo.getResult() == 0) {
						// showDialogMessage("Write user data success.");
						Dao<TagEntity, Integer> dao;
						try {
							setEntity(Tag, mEntity, 0);
							dao = dbHelper.getTagDao();
							dao.createOrUpdate(mEntity);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						initData();
						showDialogMessage("数据撤销成功");
					} else {
						ToastUtil.showMessage("Write user data faild : " + resultInfo.getResult());
					}
				} else {
					ToastUtil.showMessage(resultInfo.getErrInfo());
				}
			}
		}
	}

	class threadScan extends AsyncTask<Integer, String, Integer> {
		ResultInfo resultInfo = null;

		// @Override
		protected Integer doInBackground(Integer... params) {
			resultInfo = uhfService.getEPCList(uhfService.getQValue());
			return 0;
		}

		// @Override
		protected void onPreExecute() {
			isScanIng = true;
			// ToastUtil.showMessage("Scaning.....");
		}

		// @Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (isStop) {
				return;
			}
			if (resultInfo != null) {
				if (resultInfo.getResult() == 0) {
					resultInfo.getValues();
					if (resultInfo.getValues().size() > 0
							&& !StringUtil.isEmpty(resultInfo.getValues().get(0))) {
						PlaySoundUtil.play();
						idString = resultInfo.getValues().get(0);
						new threadRead().execute();
					} else {
						isScanIng = false;
						bt_50.setEnabled(true);
						bt_100.setEnabled(true);
					}
					// ToastUtil.showMessage("Tag is found " + resultInfo.getValues().size());
				} else {
					isScanIng = false;
					bt_50.setEnabled(true);
					bt_100.setEnabled(true);
					ToastUtil.showMessage("Search tag is failed : " + resultInfo.getResult());

				}
			} else {
				isScanIng = false;
				bt_50.setEnabled(true);
				bt_100.setEnabled(true);
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
							System.out.println("mEntity:" + i + " " + resultInfo.getResultValue());
						} else {
							isSuccess = false;

						}
					} else {

						isSuccess = false;
					}
				}
				if (isSuccess) {
					if (null != mEntity) {
						try {
							// mEntity = setEntity(Tag, mEntity, SelectData);
							Dao<TagEntity, Integer> tagdao = dbHelper.getTagDao();
							tagdao.createOrUpdate(mEntity);
							initData();
							ToastUtil.showMessage("读取成功");
							// Intent intent = new Intent(MainActivity.this, MenuActivity.class);
							// intent.putExtra("id", mEntity.getId());
							// startActivity(intent);
							writeAction();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					isScanIng = false;
				} else {
					isScanIng = false;
					ToastUtil.showMessage("读取失败");
				}
			}
			bt_50.setEnabled(true);
			bt_100.setEnabled(true);
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

	@Override
	public void onBackPressed() {
		if (!isStop) {
			bt_50.setVisibility(View.VISIBLE);
			bt_100.setVisibility(View.VISIBLE);
			SelectData = 0;
			// 停止搜索
			isStop = true;
			iv_stopScore.setVisibility(View.GONE);
		}else {
			super.onBackPressed();
		}
	}

}
