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

package com.kavya.myconferenceevents.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for conference details
 */
public class ConferenceDetailsData implements Parcelable {

	private int SessionId;
	private String Name;
	private String Presenter;
	private String Room;
	private int Duration;
	private String MetaData;
	private int isSubscribed = 0;

	public int isSubscribed() {
		return isSubscribed;
	}

	public void setSubscribed(int isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	public int getSessionId() {
		return SessionId;
	}

	public void setSessionId(int sessionId) {
		SessionId = sessionId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPresenter() {
		return Presenter;
	}

	public void setPresenter(String presenter) {
		Presenter = presenter;
	}

	public String getRoom() {
		return Room;
	}

	public void setRoom(String room) {
		Room = room;
	}

	public int getDuration() {
		return Duration;
	}

	public void setDuration(int duration) {
		Duration = duration;
	}

	public String getMetaData() {
		return MetaData;
	}

	public void setMetaData(String metaData) {
		MetaData = metaData;
	}

	/** mandatory function to make the regular data structure to Parcelable. */

	public static final Parcelable.Creator<ConferenceDetailsData> CREATOR = new Parcelable.Creator<ConferenceDetailsData>() {

		@Override
		public ConferenceDetailsData createFromParcel(Parcel source) {
			return new ConferenceDetailsData(source);
		}

		@Override
		public ConferenceDetailsData[] newArray(int size) {
			return new ConferenceDetailsData[size];
		}
	};

	/** default constructor. */
	public ConferenceDetailsData() {
	}

	/** Constructor will be called by the framework */
	public ConferenceDetailsData(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/** Serializing the contents */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(SessionId);
		dest.writeString(Name);
		dest.writeString(Room);
		dest.writeInt(Duration);
		dest.writeString(Presenter);
		dest.writeString(MetaData);
		dest.writeInt(isSubscribed);
	}

	/** deserializing the contents */
	public void readFromParcel(Parcel source) {
		SessionId = source.readInt();
		Name = source.readString();
		Room = source.readString();
		Duration = source.readInt();
		Presenter = source.readString();
		MetaData = source.readString();
		isSubscribed = source.readInt();
	}

}
