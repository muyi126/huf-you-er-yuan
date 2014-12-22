/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @HistoryActivity.java  2014年8月21日 下午3:42:48 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.huf.act;

import java.sql.SQLException;
import java.util.List;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.givon.huf.BaseListActivity;
import com.givon.huf.R;
import com.givon.huf.db.DbHelper;
import com.givon.huf.entity.TagEntity;
import com.givon.huf.util.ToastUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

public class HistoryActivity extends BaseListActivity<TagEntity> {
	private DbHelper dbHelper;
	private List<TagEntity> list;
	private int page_size = 7;
	private ImageView bt_PriButton;
	private ImageView bt_NextButton;
	private int total_size;
	private TextView tv_Total_pople;
	private ListView mListView;
	private Button mClean;

	protected void onCreate(android.os.Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_history);
		mListView = (ListView) findViewById(R.id.id_pull_listview);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		// mListView.startOnRefresh();
		mListView.setSelector(android.R.color.transparent);
		tv_Total_pople = (TextView) findViewById(R.id.total_pople);
		mListView.setAdapter(mAdapter);
		dbHelper = new DbHelper(this);
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
		bt_PriButton = (ImageView) findViewById(R.id.pri);
		bt_NextButton = (ImageView) findViewById(R.id.next);
		bt_NextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((mPageIndex) * page_size >= total_size) {
					ToastUtil.showMessage("最后一页");
				} else {
					mPageIndex++;
					loadData(mPageIndex, page_size);
				}
			}
		});
		bt_PriButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPageIndex <= 1) {
					ToastUtil.showMessage("第一页");
				} else {
					mPageIndex--;
					loadData(mPageIndex, page_size);
				}
			}
		});
		mClean = (Button) findViewById(R.id.bt_clean);
		mClean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialogMessage("是否清除所有数据", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Dao<TagEntity, Integer> dao;
						try {
							dao = dbHelper.getTagDao();
							DeleteBuilder<TagEntity, Integer> deleteBuilder = dao.deleteBuilder();
							deleteBuilder.where().isNotNull("id");
							deleteBuilder.delete();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ToastUtil.showMessage("清除数据成功");
						dialog.cancel();
						mPageIndex = 1;
						list = null;
						loadData(mPageIndex, page_size);
					}

				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}

				});

			}
		});
		loadData(mPageIndex, page_size);
	}

	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = getLayoutInflater().inflate(R.layout.adapter_list, null);
			ViewHolder viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		TagEntity entity = mAdapter.getItem(position);
		if (null != entity) {
			viewHolder.tv_IdTextView.setText(entity.getId());
			viewHolder.tv_SrcTextView.setText(entity.getTotal_score() + "");
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView tv_IdTextView;
		private TextView tv_SrcTextView;

		ViewHolder(View view) {
			this.tv_IdTextView = (TextView) view.findViewById(R.id.id);
			this.tv_SrcTextView = (TextView) view.findViewById(R.id.score);
		}

		public TextView getTv_IdTextView() {
			return tv_IdTextView;
		}

		public void setTv_IdTextView(TextView tv_IdTextView) {
			this.tv_IdTextView = tv_IdTextView;
		}

		public TextView getTv_SrcTextView() {
			return tv_SrcTextView;
		}

		public void setTv_SrcTextView(TextView tv_SrcTextView) {
			this.tv_SrcTextView = tv_SrcTextView;
		}

	}

	private void loadData(int mPageIndex, int page_size) {
		Dao<TagEntity, Integer> dao = null;
		try {
			dao = dbHelper.getTagDao();
			if (null != dao) {
				if (null == list) {
					list = dao.queryForAll();
					total_size = list.size();
					tv_Total_pople.setText("合计：" + total_size + "人");
				}
				if (total_size > 0) {
					if (total_size < page_size) {
						List<TagEntity> tagEntities = list.subList(0, total_size);
						mAdapter.putData(tagEntities);
//						System.out.println("cru:" + 0 + " n_cru:" + total_size);
					} else {
						int cru = Math.min((mPageIndex - 1) * page_size, total_size - page_size);
						int n_cru = Math.min(page_size * mPageIndex - 1, total_size - 1);
						if (total_size - ((mPageIndex - 1) * page_size) < page_size) {
							cru = Math.min((mPageIndex - 1) * page_size, total_size);
						}
						List<TagEntity> tagEntities = list.subList(cru, n_cru);
						putData(tagEntities);
					}

				} else {
					mListData.clear();
					mAdapter.notifyDataSetChanged();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void putData(List<TagEntity> data) {
		if (mPageIndex == 1) {
			mListData.clear();
		}
		if (null != data) {
			mListData.addAll(data);
			// if (data.size() < Constant.PAGE_SIZE) {
			// // mListView.setPullLoadEnable(false);
			// } else {
			// // mListView.setPullLoadEnable(true);
			// }
		}
		mAdapter.notifyDataSetChanged();
	}

}
