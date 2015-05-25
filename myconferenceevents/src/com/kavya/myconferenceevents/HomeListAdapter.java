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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kavya.myconferenceevents.data.ConferenceDetailsData;
import com.kavya.myconferenceevents.utils.Utils;

/**
 * List adapter to display the conference details in tabular form
 */
public class HomeListAdapter extends BaseAdapter{
	
	private Activity mContext;
	private LayoutInflater mInflater;
	private List<ConferenceDetailsData> mDataList = new ArrayList<ConferenceDetailsData>(); // Preparing the empty for the safer initial calls
	
	/** default constructor */
	public HomeListAdapter(Activity context){
		mContext = context;
		mInflater = mContext.getLayoutInflater();
	}

	/** Setting the data to adapter and update notify UI with the data changes */
	public void setData(List<ConferenceDetailsData> datalist){
		this.mDataList = datalist;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		ConferenceDetailsData data = mDataList.get(position);
		/** Efficient way to handling the list items. by using the recycling */
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.home_listitem, null);
			viewHolder = new ViewHolder();
			viewHolder.mDuration = (TextView)convertView.findViewById(R.id.item_duration);
			viewHolder.mName = (TextView)convertView.findViewById(R.id.item_name);
			viewHolder.mParentLayout = (LinearLayout)convertView.findViewById(R.id.item);
			viewHolder.mPresenter = (TextView)convertView.findViewById(R.id.item_presenter);
			viewHolder.mRoom = (TextView)convertView.findViewById(R.id.item_room);
			viewHolder.mSessionid = (TextView)convertView.findViewById(R.id.item_id);
			Utils.setRegularFont(viewHolder.mName, mContext);
			Utils.setRegularFont(viewHolder.mDuration, mContext);
			Utils.setRegularFont(viewHolder.mPresenter, mContext);
			Utils.setRegularFont(viewHolder.mRoom, mContext);
			Utils.setRegularFont(viewHolder.mSessionid, mContext);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		/** Feel good changes which bring the differentiation between two items */
		if(position %2 != 0){
			viewHolder.mParentLayout.setBackgroundResource(R.drawable.tableitem_whitebg);
		}else{
			viewHolder.mParentLayout.setBackgroundResource(R.drawable.tableitem_graybg);
		}
		
		/** updating the ui components with actual values */
		viewHolder.mName.setText(Html.fromHtml(data.getName()));
		viewHolder.mDuration.setText(data.getDuration()+" min");
		viewHolder.mPresenter.setText(data.getPresenter());
		viewHolder.mRoom.setText(data.getRoom());
		viewHolder.mSessionid.setText(data.getSessionId()+"");
		
		return convertView;
	}
	
	/** class to hold the ui objects */
	private class ViewHolder{
		TextView mSessionid;
		TextView mName;
		TextView mPresenter;
		TextView mDuration;
		TextView mRoom;
		LinearLayout mParentLayout;
	}

}
