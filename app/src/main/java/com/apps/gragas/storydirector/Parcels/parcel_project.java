package com.apps.gragas.storydirector.Parcels;

import android.os.Parcel;
import android.os.Parcelable;

public class parcel_project implements Parcelable {

    private String projectName;
    private String mainInfo;


    private int position;

    public parcel_project(String Name, int position) {
        this.projectName = Name;
        this.position = position;

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public int getPosition() {
        return position;
    }

    public void setMainInfo(String info) {
        this.mainInfo = info;
    }

    public static final Creator<parcel_project> CREATOR = new Creator<parcel_project>() {
        @Override
        public parcel_project createFromParcel(Parcel source) {
            String projectName = source.readString();
            int position = source.readInt();
            return new parcel_project(projectName, position);
        }

        @Override
        public parcel_project[] newArray(int size) {
            return new parcel_project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(projectName);
        dest.writeInt(position);
    }
}



