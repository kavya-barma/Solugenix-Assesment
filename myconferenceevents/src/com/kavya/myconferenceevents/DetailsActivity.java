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

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kavya.myconferenceevents.data.ConferenceDetailsData;
import com.kavya.myconferenceevents.managers.DatabaseManager;
import com.kavya.myconferenceevents.managers.NetworkManager;
import com.kavya.myconferenceevents.utils.Utils;


public class DetailsActivity extends Activity {
	
	private ConferenceDetailsData mData  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        prepareData();
    }
    
    @Override
    protected void onResume() {
    	 init();
    	super.onResume();
    }
    
    /** Reading the ConferenceDetailsData object form the activity bundle */
    private void prepareData(){
    	try {
			mData = getIntent().getExtras().getParcelable("ConferenceDetailsData");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /** Preparing the ui components and registration the click events */
    private void init(){
    	if(mData == null){
    		return;
    	}
    	findViewById(R.id.action).setOnClickListener(mBookmarkListener);
    	Utils.setBoldFont(findViewById(R.id.action), this);
    	
    	prepareTitle();
    	prepareProfile();
    	prepareDescription();
    }
    
    /** Preparing the header component */
    private void prepareTitle(){
    	try {
    		// updating the title
			Utils.setRegularFont(findViewById(R.id.title), this);
			((TextView)findViewById(R.id.title)).setText(mData.getName());
			
			// toggling the label of action button
			if(mData.isSubscribed() == 0){
				((Button)findViewById(R.id.action)).setText(getResources().getString(R.string.label_Bookmark));
			}else{
				((Button)findViewById(R.id.action)).setText(getResources().getString(R.string.label_Unbookmark));
			}
			
			//back button click listener
			findViewById(R.id.back).setOnClickListener(mBackClickListener);
			findViewById(R.id.applogo).setOnClickListener(mBackClickListener);
			
		} catch (Exception e) {
		}
    }
    
    /** Preparing the presenter profile ui component */
    private void prepareProfile(){
    	
    	LinearLayout profileLayout = (LinearLayout)findViewById(R.id.profilelayout);
		// removing the view if it is already there, 
    	//if activity come from a background there is chance of adding same content multiple items
    	profileLayout.removeAllViews();
    
    	View profile = getLayoutInflater().inflate(R.layout.layout_presenter, null);
    	profileLayout.addView(profile);
    	try {
			JSONObject object = new JSONObject(mData.getMetaData());

			// updating the presenter name
			String displayname = Utils.getStringSubValue("MainPresenter", "DisplayName", object);
			((TextView)profile.findViewById(R.id.profilename)).setText(displayname);
			Utils.setRegularFont(profile.findViewById(R.id.profilename), this);
			
			// updating the presenter email
			String email = Utils.getStringSubValue("MainPresenter", "Email", object);
			((TextView)profile.findViewById(R.id.email)).setText(email);
			Utils.setRegularFont(profile.findViewById(R.id.email), this);
			
			// updating the presenter userid
			String userid = Utils.getStringSubValue("MainPresenter", "UserId", object);
			((TextView)profile.findViewById(R.id.userid)).setText(userid);
			Utils.setRegularFont(profile.findViewById(R.id.userid), this);
			
			// updating the presenter last login date
			String lastlogin = Utils.getStringSubValue("MainPresenter", "LastLogin", object);
			((TextView)profile.findViewById(R.id.lastlogin)).setText(Utils.getDate(lastlogin));
			Utils.setRegularFont(profile.findViewById(R.id.lastlogin), this);
			
			// updating the presenter biography
			String Biography = Utils.getStringSubValue("MainPresenter", "Biography", object);
			Biography = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><body>"+Biography+"</body>";
			WebView webview = (WebView) profile.findViewById(R.id.about);
			WebSettings webSettings = webview.getSettings();
			webSettings.setDefaultFixedFontSize(12);
			webview.loadData(Biography, "text/html", "UTF-8");
			
			
			String ImageUrl = Utils.getStringSubValue("MainPresenter", "ImageUrl", object);
			String ImageWithBlankUrl = Utils.getStringSubValue("MainPresenter", "ImageWithBlankUrl", object);
			
			if(ImageUrl.length() > 0){ // updating the presenter profile pic 
				NetworkManager manager = new NetworkManager();
				manager.getImageData(ImageUrl, (ImageView)profile.findViewById(R.id.profilepic));
			}else if(ImageWithBlankUrl.length() > 0){ // updating the presenter profile image with default pic
				NetworkManager manager = new NetworkManager();
				manager.getImageData(ImageUrl, (ImageView)profile.findViewById(R.id.profilepic));
			}
			
			// preparing the twitter link
			String TwitterHandle = Utils.getStringSubValue("MainPresenter", "TwitterHandle", object);
			TwitterHandle = TwitterHandle.replace("@", "");
			TwitterHandle = "https://twitter.com/search?q="+TwitterHandle;
			profile.findViewById(R.id.twitter).setTag(TwitterHandle);
			profile.findViewById(R.id.twitter).setOnClickListener(mTwitterClick);
		} catch (Exception e) {
		}
    }
    
    /** Preparing the session details ui component */
    private void prepareDescription(){

    	LinearLayout descLayout = (LinearLayout)findViewById(R.id.desclayout);
    	// removing the view if it is already there, 
    	//if activity come from a background there is chance of adding same content multiple items
    	descLayout.removeAllViews();
    	
    	View desc = getLayoutInflater().inflate(R.layout.layout_description, null);
    	descLayout.addView(desc);	
    	
    	try {
			JSONObject object = new JSONObject(mData.getMetaData());

			// updating the conference title
			String ConferenceTitle = Utils.getStringSubValue("Conference", "ConferenceTitle", object);
			((TextView)desc.findViewById(R.id.title)).setText(ConferenceTitle);
			Utils.setRegularFont(desc.findViewById(R.id.title), this);

			//updating the conference description
			String Abstract = Utils.getStringValue("Abstract", object);
			Abstract = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><body>"+Abstract+"</body>";
			WebView webview = (WebView) desc.findViewById(R.id.desc);
			WebSettings webSettings = webview.getSettings();
			webSettings.setDefaultFixedFontSize(12);
			webview.loadData(Abstract, "text/html", "UTF-8");
			
			// preparing the twitter link
			String TwitterHandle = Utils.getStringSubValue("Conference", "HashTag", object);
			TwitterHandle = TwitterHandle.replace("@", "");
			TwitterHandle = TwitterHandle.replace("#", "");
			TwitterHandle = "https://twitter.com/search?q="+TwitterHandle;
			desc.findViewById(R.id.twitter).setTag(TwitterHandle);
			desc.findViewById(R.id.twitter).setOnClickListener(mTwitterClick);
			
		} catch (Exception e) {
		}
    }
    
    /** toggle click listener for bookmark/unbookmark */
    private OnClickListener mBookmarkListener = new OnClickListener() {
		
  		@Override
  		public void onClick(View v) {
  			
  			try {
  				DatabaseManager manager = new DatabaseManager(DetailsActivity.this);
  				if(mData.isSubscribed() == 0){ // add as a bookmark
  					((Button)v).setText(getResources().getString(R.string.label_Unbookmark));
  					mData.setSubscribed(1);
  					manager.subscribe(mData);
  					Toast.makeText(DetailsActivity.this, "Added to bookmarks.", Toast.LENGTH_SHORT).show();
  				}else{ // remove from bookmarks
  					((Button)v).setText(getResources().getString(R.string.label_Bookmark));
  					mData.setSubscribed(0);
  					manager.removeSubscription(mData.getSessionId());
  					Toast.makeText(DetailsActivity.this, "Removed from bookmarks.", Toast.LENGTH_SHORT).show();
  				}
  			} catch (Exception e) {
  			}
  		}
  	};
  	
  	/** Twitter click, which will launch the twitter application or default browser */
    private OnClickListener mTwitterClick = new OnClickListener() {
		
  		@Override
  		public void onClick(View v) {
  			try {
  				String url = (String) v.getTag();
  				Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
  				startActivity(viewIntent);
  			} catch (Exception e) {
  			}
  		}
  	};
  	
  	/** back button listener, which close the activity */
  	private OnClickListener mBackClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
  
}
