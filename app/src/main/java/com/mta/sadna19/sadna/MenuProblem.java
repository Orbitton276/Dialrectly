package com.mta.sadna19.sadna;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuProblem implements Parcelable {

    public MenuProblem(){}

    public String getmTittle() {
        return mTittle;
    }

    public void setmTittle(String mTittle) {
        this.mTittle = mTittle;
    }

    public String getmServiceName() {
        return mServiceName;
    }

    public void setmServiceName(String mServiceName) {
        this.mServiceName = mServiceName;
    }

    public String getmLastCallDialPath() {
        return mLastCallDialPath;
    }

    public void setmLastCallDialPath(String mLastCallDialPath) {
        this.mLastCallDialPath = mLastCallDialPath;
    }

    public String getmLastCallPath() {
        return mLastCallPath;
    }

    public void setmLastCallPath(String mLastCallPath) {
        this.mLastCallPath = mLastCallPath;
    }

    public String getmUserFreeText() {
        return mUserFreeText;
    }

    public void setmUserFreeText(String mUserFreeText) {
        this.mUserFreeText = mUserFreeText;
    }

    private String mTittle;
    private String mServiceName;
    private String mLastCallDialPath;
    private String mLastCallPath;
    private String mUserFreeText;
    // 11,,2,,3
    //עברי/מכירות/נציגת

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuProblem> CREATOR = new Creator<MenuProblem>() {
        @Override
        public MenuProblem createFromParcel(Parcel in) {
            return new MenuProblem(in);
        }

        @Override
        public MenuProblem[] newArray(int size) {
            return new MenuProblem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLastCallDialPath);
        dest.writeString(mLastCallPath);
        dest.writeString(mServiceName);
        dest.writeString(mTittle);
        dest.writeString(mUserFreeText);
    }

    protected MenuProblem(Parcel in){
        mLastCallDialPath = in.readString();
        mLastCallPath = in.readString();
        mServiceName = in.readString();
        mTittle = in.readString();
        mUserFreeText = in.readString();
    }
}
