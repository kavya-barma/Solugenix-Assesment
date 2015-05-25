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

package com.kavya.myconferenceevents.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Utils {

	/** Returns a not null value or matching value for the given key. */
	public static String getStringValue(String key, JSONObject jsonObj) {
		String value = "";
		try {
			value = jsonObj.getString(key);
		} catch (Exception e) {
		}
		return value;
	}

	/** Returns a not null value or matching value for the given key in the object matching with the parentkey. */
	public static String getStringSubValue(String parentKey, String key,
			JSONObject jsonObj) {
		String value = "";
		try {
			JSONObject subObject = jsonObj.getJSONObject(parentKey);
			value = subObject.getString(key);
		} catch (Exception e) {
		}
		return value;
	}
	
	/** Returns a not null value or date string formated in 27 January 15. */
	public static String getDate(String str) {
		String value = "";
		try {
			// trimming the long value in the string formatted in /(Date)12312432432-700
			str = str.replace("/Date(", ""); 
			int index = str.indexOf("-");
			if(index != -1){
				str = str.substring(0, index);
			}
			long longDate = Long.parseLong(str);
			
			//formatting the long to string 
	        Date date=new Date(longDate);
	        SimpleDateFormat df2 = new SimpleDateFormat("dd MMMM yy");
	        value = df2.format(date);
		} catch (Exception e) {
		}
		return value;
	}

	/** Returns time in minutes between to given dates. */
	public static int getDuration(String startTime, String endTime) {
		int result = 0;
		try {
			startTime = startTime.replace("/Date(", "");
			int index = startTime.indexOf("-");
			if(index != -1){
				startTime = startTime.substring(0, index);
			}
			
			endTime = endTime.replace("/Date(", "");
			index = endTime.indexOf("-");
			if(index != -1){
				endTime = endTime.substring(0, index);
			}
			
			long startTimeLong = Long.parseLong(startTime);
			long endTimeLong = Long.parseLong(endTime);
			
			result = (int) ((endTimeLong - startTimeLong)/(1000*60));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	
	/** Converts InputStream to String. */
	public static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	/** Set the regular font for the given view, view can be TextView/Button . */
	public static void setRegularFont(View view,Context cxt){
		try {
			Typeface tf = Typeface.createFromAsset(cxt.getAssets(),"gothic_3.ttf");
			if(view instanceof TextView){
				((TextView)view).setTypeface(tf);	
			}else if(view instanceof Button){
				((Button)view).setTypeface(tf);
			}
		} catch (Exception e) {
		}
	}
	
	/** Set the bold font for the given view, view can be TextView/Button . */
	public static void setBoldFont(View view,Context cxt){
		try {
			Typeface tf = Typeface.createFromAsset(cxt.getAssets(),"gothicb_3.ttf");
			if(view instanceof TextView){
				((TextView)view).setTypeface(tf);	
			}else if(view instanceof Button){
				((Button)view).setTypeface(tf);
			}
		} catch (Exception e) {
		}
	}
}
