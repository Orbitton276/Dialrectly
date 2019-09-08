package com.mta.sadna19.sadna;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String getM_ID() {
        return m_ID;
    }

    public void setM_ID(String m_ID) {
        this.m_ID = m_ID;
    }

    public String getM_phone() {
        return m_phone;
    }

    public void setM_phone(String m_phone) {
        this.m_phone = m_phone;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    private String m_email;
    private String m_ID;
    private String m_phone;
    private String m_name;

    public void setM_email(String m_email) {
        this.m_email = m_email;
    }

    public String getM_email() {
        return m_email;
    }

    public User(String email) {
        this.m_email = email;
    }
    public User(){}

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_email);
        dest.writeString(m_ID);
        dest.writeString(m_phone);
        dest.writeString(m_name);
    }

    protected User(Parcel in){
        m_email = in.readString();
        m_ID = in.readString();
        m_phone = in.readString();
        m_name = in.readString();
    }
}
