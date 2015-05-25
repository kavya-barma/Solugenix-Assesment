/*
 * Copyright (C) 2013 The Android Open Source Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.kavya.myconferenceevents.managers;

import java.util.ArrayList;
import java.util.List;

import com.kavya.myconferenceevents.data.ConferenceDetailsData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Manager to handle the database related requests
 */
public class DatabaseManager {
	
	public String TAG = "DatabaseManager";
	
	/** Database constants. */
	public static final String DATABASE_NAME = "myconference.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME = "CONFERENCE";
	
	public static final String COLUMN_NAME = "NAME";
	public static final String COLUMN_PRESENTER = "PRESENTER";
	public static final String COLUMN_ROOM = "ROOM";
	public static final String COLUMN_DURATION = "DURATION";
	public static final String COLUMN_SESSIONID = "ID";
	public static final String COLUMN_SUBSCRIBED = "SUBSCRIBED";	
	public static final String COLUMN_METADATA = "METADATA";	
	
	/** Create table sql query. */
	public final static String CREATETABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "
			+ COLUMN_NAME +" TEXT ,"+COLUMN_PRESENTER+" TEXT ,"+COLUMN_ROOM+" TEXT ,"
			+ COLUMN_METADATA +" TEXT ,"+ COLUMN_SUBSCRIBED +" INT ,"+ COLUMN_DURATION +" INT ,"+COLUMN_SESSIONID+" INT )";
	
	
	private SQLiteDatabase db;
	private Context mContext;

	/** default constructor. */
	public DatabaseManager(Context context) {
		this.mContext = context;
		createDb();
	}

	/** Create table. */
	private void createDb() {
		try {
			db = mContext.openOrCreateDatabase(DATABASE_NAME,DATABASE_VERSION, null);
			db.execSQL(CREATETABLE); //Create table only when it is not created
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** prepare the db session of begin transcation. */
	private void beginTranscation() {
		try {
			db.beginTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** prepare the db session of end transcation. */
	private void endTranscation() {
		try {
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	/** Returns the list of all records. */
	public List<ConferenceDetailsData> getSubscribedItems(){
		List<ConferenceDetailsData> list = new  ArrayList<ConferenceDetailsData>();
		beginTranscation();
		try {
			Cursor c = db.rawQuery("Select * from "+TABLE_NAME, null);
			
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ConferenceDetailsData data = new  ConferenceDetailsData();
						data.setDuration(c.getInt(c.getColumnIndex(COLUMN_DURATION)));
						data.setMetaData(c.getString(c.getColumnIndex(COLUMN_METADATA)));
						data.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
						data.setPresenter(c.getString(c.getColumnIndex(COLUMN_PRESENTER)));
						data.setRoom(c.getString(c.getColumnIndex(COLUMN_ROOM)));
						data.setSessionId(c.getInt(c.getColumnIndex(COLUMN_SESSIONID)));
						data.setSubscribed(c.getInt(c.getColumnIndex(COLUMN_SUBSCRIBED)));
						list.add(data);
					} while (c.moveToNext());
				}
				c.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		endTranscation();
		return list;
	}
	
	/** Save the record. */
	public void subscribe(ConferenceDetailsData data){
		beginTranscation();
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_DURATION, data.getDuration());
			cv.put(COLUMN_NAME, data.getName());
			cv.put(COLUMN_PRESENTER, data.getPresenter());
			cv.put(COLUMN_ROOM, data.getRoom());
			cv.put(COLUMN_SESSIONID, data.getSessionId());
			cv.put(COLUMN_METADATA, data.getMetaData());
			cv.put(COLUMN_SUBSCRIBED, data.isSubscribed());
			
			long ret = db.update(TABLE_NAME, cv, COLUMN_SESSIONID + " = ?", new String[] { ""
					+ data.getSessionId() });
			if (ret == -1 || ret == 0) {
				ret = db.insert(TABLE_NAME, null, cv);
				Log.e(TAG, "inserted = " + ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		endTranscation();
	}
	
	/** remove the record matching with the given sessionId. */
	public void removeSubscription(int sessionId){
		beginTranscation();
		try {
			String query = "Delete from "+TABLE_NAME+" where "+COLUMN_SESSIONID+" = "+"'"+sessionId+"'";
			db.execSQL(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		endTranscation();
	}
}
