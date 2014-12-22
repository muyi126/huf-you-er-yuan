package com.givon.huf.act;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.givon.huf.BaseActivity;
import com.givon.huf.BaseApplication;
import com.givon.huf.R;
import com.givon.huf.db.DbHelper;
import com.givon.huf.entity.TagEntity;
import com.givon.huf.util.StringUtil;
import com.givon.huf.util.TimeToUtil;
import com.j256.ormlite.dao.Dao;
import com.wpx.bean.ResultInfo;
import com.wpx.myutil.PlaySoundUtil;
import com.wpx.service.IUHFService;
import com.wpx.service.impl.UHFServiceImpl;
import com.wpx.util.GlobalUtil;

public class MainActivity extends BaseActivity {
	private ImageView bt_Kaishi;
	private ImageView bt_Lishi;
	private TextView textView_Status;
	private TextView textView_Version;
	private ProgressBar progressBar;
	private String Password = "00000000";
	private String LockCmd = "2222222222";
	private String AccessPassword = "22222222";
	private String KillPassword = "00000000";
	private TagEntity mTagEntity = new TagEntity();
	private String idString;
	private threadScan tScan;
	private int tatol_size = 7;
	private DbHelper dbHelper;
	public IUHFService uhfService = new UHFServiceImpl();
	private static int channel_Type = GlobalUtil.gChannelType;
	private static int channel_ID = GlobalUtil.gFrequencyIndex;
	private long time_1 = 1411099133979L;
	private long time_2 = time_1 + (5 * 24 * 60 * 60 * 1000);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if(TimeToUtil.getSystemTime()<time_1){
		// finish();
		// }else {
		// if(TimeToUtil.getSystemTime()>time_2){
		// finish();
		// }
		// }
		// System.out.println("Time:"+TimeToUtil.getSystemTime()+"Time@:"+TimeToUtil.getSystemTimeFormatWeekA(TimeToUtil.getSystemTime()));
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		uhfService.setDeviceType(0);
		dbHelper = new DbHelper(this);
		bt_Kaishi = (ImageView) findViewById(R.id.kaishi);
		bt_Lishi = (ImageView) findViewById(R.id.jilu);
		bt_Kaishi.setOnClickListener(null);
		bt_Lishi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showActivity(HistoryActivity.class, false);
			}
		});
		textView_Status = (TextView) findViewById(R.id.tv_Status);
		textView_Version = (TextView) findViewById(R.id.tv_Version);
		textView_Status.setText("Close Port.");
		textView_Version.setText("");
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		new threadConnect().execute();

	}

	private class StartListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// tScan = new threadScan();
			// tScan.execute();
			// bt_Kaishi.setEnabled(false);
			Intent intent = new Intent(MainActivity.this, MenuActivity.class);
			// intent.putExtra("id", mTagEntity.getId());
			startActivity(intent);
		}

	}

	class threadConnect extends AsyncTask<Integer, String, Integer> {
		ResultInfo resultInfo = null;
		String version;

		// @Override
		protected Integer doInBackground(Integer... params) {
			if (resultInfo != null) {
				if (resultInfo.getResult() == 0) {
					version = uhfService.getVersion();
					// uhfService.Channel_Calibration(4+1); //china
					if (StringUtil.isEmpty(version)) {
						version = "";
						return -1;
					} else {
						return 0;
					}
				}
			}
			return 0;
		}

		// @Override
		protected void onPreExecute() {
			// if("Connect".equals(btn_connect.getText()) || "Open".equals(btn_connect.getText()))
			// {
			int type = GlobalUtil.dev_type;
			uhfService.disConnected();
			if (type == 1)
				resultInfo = uhfService.connected(1);
			else {
				resultInfo = uhfService.connected(0);
			}
		}

		// @Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub

			if (result == 0) {
				if (resultInfo.getResult() == 0) {
					textView_Status.setText("Open port/Get Version.");
					textView_Version.setText(version);
					// btn_connect.setText("Close");
					bt_Kaishi.setOnClickListener(new StartListener());
				} else {
					bt_Kaishi.setOnClickListener(null);
					textView_Status.setText("Open port is failed : " + resultInfo.getResult());
					// clear list
					// showEPCList(new ArrayList<String>());
				}
			} else {
				textView_Status.setText("Close Port.");
				textView_Version.setText("");
				bt_Kaishi.setOnClickListener(new StartListener());
				// showEPCList(new ArrayList<String>());
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
			textView_Status.setText("Scaning.....");
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
						bt_Kaishi.setEnabled(true);
					}
					textView_Status.setText("Tag is found " + resultInfo.getValues().size());
				} else {
					bt_Kaishi.setEnabled(true);
					textView_Status.setText("Search tag is failed : " + resultInfo.getResult());
				}
			} else {
				bt_Kaishi.setEnabled(true);
			}
		}
	}

	// class threadReadReserve extends AsyncTask<Integer, Integer, ArrayList<ResultInfo>> {
	//
	// @Override
	// protected ArrayList<ResultInfo> doInBackground(Integer... params) {
	// ArrayList<ResultInfo> list = new ArrayList<>();
	// ResultInfo resultInfo = null;
	// for (int i = 0; i < tatol_size; i++) {
	// resultInfo = uhfService.readReserve(i, Password);
	// publishProgress((int) ((i / (float) tatol_size) * 100));
	// list.add(resultInfo);
	// }
	// return list;
	// }
	//
	// @Override
	// protected void onProgressUpdate(Integer... values) {
	// // TODO Auto-generated method stub
	// super.onProgressUpdate(values);
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// textView_Status.setText("Read reserve data ....");
	// }
	//
	// @Override
	// protected void onPostExecute(ArrayList<ResultInfo> result) {
	// super.onPostExecute(result);
	// if (result != null && result.size() > 0) {
	// boolean isSuccess = true;
	// for (int i = 0; i < result.size(); i++) {
	// ResultInfo resultInfo = result.get(i);
	// if ("".equals(resultInfo.getErrInfo())) {
	// if (resultInfo.getResult() == 0) {
	// textView_Status.setText("Read reserve data success.");
	// mTagEntity = setTagEntity(i, resultInfo.getResultValue());
	// } else {
	// isSuccess = false;
	// textView_Status.setText("Read reserve data faild "
	// + resultInfo.getResult());
	// }
	// } else {
	// isSuccess = false;
	// textView_Status.setText(resultInfo.getErrInfo());
	// }
	// }
	// if (isSuccess) {
	// if (null != mTagEntity) {
	// try {
	// Dao<TagEntity, Integer> tagdao = dbHelper.getTagDao();
	// tagdao.createOrUpdate(mTagEntity);
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Intent intent = new Intent(MainActivity.this, MenuActivity.class);
	// intent.putExtra("data", mTagEntity);
	// startActivity(intent);
	// }
	// } else {
	//
	// }
	// bt_Kaishi.setEnabled(true);
	// }
	// }
	// }

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
			textView_Status.setText("Read user data ....");
		}

		@Override
		protected void onPostExecute(ArrayList<ResultInfo> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				boolean isSuccess = true;
				for (int i = 0; i < result.size(); i++) {
					ResultInfo resultInfo = result.get(i);
					if ("".equals(resultInfo.getErrInfo())) {
						if (resultInfo.getResult() == 0) {
							textView_Status.setText("Read reserve data success.");
							mTagEntity = setTagEntity(i, resultInfo.getResultValue(), mTagEntity);
						} else {
							isSuccess = false;
							textView_Status.setText("Read reserve data faild "
									+ resultInfo.getResult());
						}
					} else {
						isSuccess = false;
						textView_Status.setText(resultInfo.getErrInfo());
					}
				}
				if (isSuccess) {
					if (null != mTagEntity) {
						try {
							Dao<TagEntity, Integer> tagdao = dbHelper.getTagDao();
							tagdao.createOrUpdate(mTagEntity);
							Intent intent = new Intent(MainActivity.this, MenuActivity.class);
							intent.putExtra("id", mTagEntity.getId());
							startActivity(intent);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} else {

				}
				bt_Kaishi.setEnabled(true);
			}
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		uhfService.disConnected();
	}
}
