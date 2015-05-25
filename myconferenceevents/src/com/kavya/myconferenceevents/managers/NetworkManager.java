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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.kavya.myconferenceevents.data.ConferenceDetailsData;
import com.kavya.myconferenceevents.utils.Utils;

/**
 * Manager to handle the network related requests
 */
public class NetworkManager {

	private NetworkManagerListener mListener; // Registered listener to send the callback

	/** Updates the imageview for download the image for the given url. */
	public void getImageData(final String imageUrl,final ImageView imageview){
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bm = null;
				try {
					HttpClient sClient = new DefaultHttpClient();
					HttpGet getRequest = null;
					getRequest = new HttpGet(imageUrl);
					HttpResponse response = sClient.execute(getRequest);
					if (response != null) {
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							InputStream reader = entity.getContent();
							bm = BitmapFactory.decodeStream(reader); // convert the inputstrem to bitmap
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				final Bitmap finalbm = bm;
				if(finalbm != null){
					// context switching to main ui thread
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						
						@Override
						public void run() {
							try {
								if(imageview != null){
									// update the imageview with the downloaded bitmap
									imageview.setImageBitmap(finalbm);
								}	
							} catch (Exception e) {
							}
						}
					});
				}
				
			}
		});
		t.start();
	}
	
	/** Download the latest conference details and updated the ui with the registered listener */
	public void getLatestConferences(NetworkManagerListener listener) {
		mListener = listener;
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				String serverResponse = null;
				try {
					HttpClient sClient = new DefaultHttpClient();
					HttpGet getRequest = null;
					getRequest = new HttpGet(
							"http://myconferenceevents.com/Services/Session.svc/GetSessionsByConferenceId?conferenceId=9");
					HttpResponse response = sClient.execute(getRequest);
					if (response != null) {
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							InputStream reader = entity.getContent();
							serverResponse = Utils.getStringFromInputStream(reader);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				processData(serverResponse);
			}
		});
		t.start();
	}

	/** Parse the json response and update the ui */
	private void processData(String response) {
		final List<ConferenceDetailsData> resultlist = new ArrayList<ConferenceDetailsData>();
		try {
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				ConferenceDetailsData data = new ConferenceDetailsData();
				data.setMetaData(object.toString());
				data.setSessionId(object.getInt("SessionId"));
				data.setName(Utils.getStringValue("Name", object));
				data.setPresenter(Utils.getStringSubValue("MainPresenter",
						"DisplayName", object));
				data.setRoom(Utils.getStringSubValue("Room", "Name", object));
				data.setDuration(Utils.getDuration(
						Utils.getStringSubValue("Time", "StartDate", object),
						Utils.getStringSubValue("Time", "EndDate", object)));
				resultlist.add(data);
			}
		} catch (Exception e) {
		}
		// context switching to main ui thread
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if (mListener != null) {
					mListener.onRequestComplete(resultlist);
				}
			}
		});
	}
}
