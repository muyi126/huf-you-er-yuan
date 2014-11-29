/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @TagEntity.java  2014年8月24日 上午9:10:38 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.huf.entity;

import java.io.Serializable;

import android.R.integer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="TagEntity")
public class TagEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@DatabaseField(uniqueIndex=true,id=true)
	private String id;
	@DatabaseField(useGetSet=true,columnName="A_score")
	private int A_score;
	@DatabaseField(useGetSet=true,columnName="B_score")
	private int B_score;
	@DatabaseField(useGetSet=true,columnName="C_score")
	private int C_score;
	@DatabaseField(useGetSet=true,columnName="D_score")
	private int D_score;
	@DatabaseField(useGetSet=true,columnName="E_score")
	private int E_score;
	@DatabaseField(useGetSet=true,columnName="Total_score")
	private int Total_score;
	@DatabaseField(useGetSet=true,columnName="Expend_score")
	private int Expend_score;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getA_score() {
		try {
			return Integer.valueOf(A_score);
		} catch (Exception e) {
			return 0;
		}
	}
	public void setA_score(String a_score) {
		try {
			A_score = Integer.valueOf(a_score);
			setTotal();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public int getB_score() {
		try {
			return Integer.valueOf(B_score);
		} catch (Exception e) {
			return 0;
		}
	}
	public void setB_score(String b_score) {
		try {
			B_score = Integer.valueOf(b_score);
			setTotal();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public int getC_score() {
		try {
			return Integer.valueOf(C_score);
		} catch (Exception e) {
			return 0;
		}
	}
	public void setC_score(String c_score) {
		try {
			C_score = Integer.valueOf(c_score);
			setTotal();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public int getD_score() {
		try {
			return Integer.valueOf(D_score);
		} catch (Exception e) {
			return 0;
		}
	}
	public void setD_score(String d_score) {
		try {
			D_score = Integer.valueOf(d_score);
			setTotal();
		} catch (Exception e) {
		}
	}
	public int getE_score() {
		try {
			return Integer.valueOf(E_score);
		} catch (Exception e) {
			return 0;
		}
	}
	public void setE_score(String e_score) {
		try {
			E_score = Integer.valueOf(e_score);
			setTotal();
		} catch (Exception e) {
		}
	}
	public int getTotal_score() {
		try {
			return Integer.valueOf(Total_score);
		} catch (Exception e) {
			return 0;
		}
	}
	public void setTotal_score(String total_score) {
		try {
			Total_score = Integer.valueOf(total_score);
		} catch (Exception e) {
		}
	}
	public int getExpend_score() {
		try {
			return Integer.valueOf(Expend_score);
		} catch (Exception e) {
			return 0;
		}
		
	}
	public void setExpend_score(String expend_score) {
		try {
			Expend_score = Integer.valueOf(expend_score);
		} catch (Exception e) {
		}
	}
	public void setA_score(int a_score) {
		A_score = a_score;
		setTotal();
	}
	public void setB_score(int b_score) {
		B_score = b_score;
		setTotal();
	}
	public void setC_score(int c_score) {
		C_score = c_score;
		setTotal();
	}
	public void setD_score(int d_score) {
		D_score = d_score;
		setTotal();
	}
	public void setE_score(int e_score) {
		E_score = e_score;
		setTotal();
	}
	public void setTotal_score(int total_score) {
		Total_score = total_score;
	}
	public void setExpend_score(int expend_score) {
		Expend_score = expend_score;
	}
	
	private void setTotal(){
		setTotal_score(A_score+B_score+C_score+D_score+E_score);
//		System.out.println("A:"+A_score+" B:"+B_score+" C:"+C_score+" D:"+D_score+" E:"+E_score+" To:"+Total_score+" EX:"+Expend_score);
	}

	
}
