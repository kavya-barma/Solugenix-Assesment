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

package com.kavya.myconferenceevents;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;

import com.kavya.myconferenceevents.data.ConferenceDetailsData;
import com.kavya.myconferenceevents.managers.DatabaseManager;
import com.kavya.myconferenceevents.managers.NetworkManager;
import com.kavya.myconferenceevents.managers.NetworkManagerListener;
import com.kavya.myconferenceevents.utils.Utils;

public class HomeActivity extends Activity {

	private HomeListAdapter mAdapter; 
	private boolean mIsSubscribedView = false;
	private List<ConferenceDetailsData> mResultList = new ArrayList<ConferenceDetailsData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
	}
	
	@Override
	protected void onResume() {
		init();
		super.onResume();
	}
	
	/** Preparing the ui components and registration the click events */
	private void init(){
		mAdapter = new HomeListAdapter(this);
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(mItemSelectlistener);
		
		findViewById(R.id.action).setOnClickListener(mActionListener);
		
		Utils.setRegularFont(findViewById(R.id.title), this);
		Utils.setRegularFont(findViewById(R.id.action), this);
		Utils.setBoldFont(findViewById(R.id.tableheader_id), this);
		Utils.setBoldFont(findViewById(R.id.tableheader_name), this);
		Utils.setBoldFont(findViewById(R.id.tableheader_presenter), this);
		Utils.setBoldFont(findViewById(R.id.tableheader_room), this);
		Utils.setBoldFont(findViewById(R.id.tableheader_duration), this);
		
		if(mIsSubscribedView){
			launchSubscribedView();
		}else{
			launchLatestSessionData();	
		}
	}
	
	/** show the progress bar layout */
	private void showProgressBar() {
		findViewById(R.id.progressbarlayout).setVisibility(View.VISIBLE);
	}

	/** hide the progress bar layout */
	private void hideProgressBar() {
		findViewById(R.id.progressbarlayout).setVisibility(View.INVISIBLE);
	}

	/** show the no result found watermark */
	private void showNoResults() {
		findViewById(R.id.nodata).setVisibility(View.VISIBLE);
	}
	
	/** hide the no result found watermark */
	private void hideNoResults() {
		findViewById(R.id.nodata).setVisibility(View.INVISIBLE);
	}

	/** event listener for the listview item click, launch the details page for the selected row */
	private OnItemClickListener mItemSelectlistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long arg3) {
			try {
				// launching the details page for the selected item 
				ConferenceDetailsData data = (ConferenceDetailsData) adapter.getItemAtPosition(position);
				Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
				 // Passing data as a ConferenceDetailsData object to DetailsActivity
                intent.putExtra("ConferenceDetailsData",data);
                startActivity(intent);
			} catch (Exception e) {
			}
		}
	};
	
	/** action listener which will toggle the table view between latest conference list and my saved session list */
	private OnClickListener mActionListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// ignoring the click event when progress bar is shown
			if(findViewById(R.id.progressbarlayout).getVisibility() == View.VISIBLE){
				return;
			}
			if(((Button)v).getText().equals(getResources().getString(R.string.label_SavedSessions))){
				launchSubscribedView();
			}else{
				launchLatestSessionData();
			}
		}
	};
	
	/** preparing the my saved subscribed list from local cache*/
	private void launchSubscribedView(){
		mIsSubscribedView = true;
		hideNoResults();
		mAdapter.setData(new ArrayList<ConferenceDetailsData>());
		((Button)findViewById(R.id.action)).setText(R.string.label_LatestSessions);
		((TextView)findViewById(R.id.title)).setText(R.string.label_SavedSessions);
		DatabaseManager manager = new DatabaseManager(this);
		List<ConferenceDetailsData> datalist = manager.getSubscribedItems();
		mAdapter.setData(datalist);
		if(datalist.size() == 0){
			showNoResults();
		}
	}
	
	/** preparing the latest session list by fetching from online*/
	private void launchLatestSessionData(){
		mIsSubscribedView = false;
		hideNoResults();
		((Button)findViewById(R.id.action)).setText(R.string.label_SavedSessions);
		((TextView)findViewById(R.id.title)).setText(R.string.label_LatestSessions);
		if(mResultList.size() != 0){
			mAdapter.setData(mResultList);
			return;
		}
		showProgressBar();
		NetworkManager manager = new NetworkManager();
		manager.getLatestConferences(new NetworkManagerListener() {
			
			@Override
			public void onRequestComplete(List<ConferenceDetailsData> resultlist) {
				hideProgressBar();
				if(resultlist.size() == 0){
					showNoResults();
				}
				mResultList = resultlist;
				mAdapter.setData(resultlist);
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		if(mIsSubscribedView){ // Simulating the back event by refreshing the list with latest session details
			launchLatestSessionData();
		}else{
			super.onBackPressed();
		}
	}

}
