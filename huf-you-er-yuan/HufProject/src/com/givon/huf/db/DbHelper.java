package com.givon.huf.db;
/* 
 * Copyright 2014 Guzhu.Ltd  All rights reserved.
 * SiChuan Guzhu.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @DbHelper.java  2014-2-27 ����10:18:34 - Carson
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.givon.huf.entity.TagEntity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DbHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "ormlite.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<TagEntity, Integer> tagDao = null;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, TagEntity.class);
		} catch (SQLException e) {
			Log.e(DbHelper.class.getName(), "Unable to create datbases", e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource,
			int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, TagEntity.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DbHelper.class.getName(), "Unable to upgrade database from version " + oldVer
					+ " to new " + newVer, e);
		}
	}

	public Dao<TagEntity, Integer> getTagDao() throws SQLException {
		if (tagDao == null) {
			tagDao = getDao(TagEntity.class);
		}
		return tagDao;
	}
}