package com.mta.sadna19.sadna;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mta.sadna19.sadna.MenuRegisters.Option;

import java.util.ArrayList;

public class ServiceItem implements Parcelable {



    private String m_avatar;
    private String m_genre;
    private String m_name;

    public String getM_avatar() {
        return m_avatar;
    }

    public void setM_avatar(String m_avatar) {
        this.m_avatar = m_avatar;
    }

    public String getM_genre() {
        return m_genre;
    }

    public void setM_genre(String m_genre) {
        this.m_genre = m_genre;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public ServiceItem() {
    }


    protected ServiceItem(Parcel in){
        m_genre = in.readString();
        m_avatar = in.readString();
        m_name = in.readString();
    }

    @Override
    public String toString() {
        return "ServiceItem{" +
                "m_avatar='" + m_avatar + '\'' +
                ", m_genre='" + m_genre + '\'' +
                ", m_name='" + m_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_genre);
        dest.writeString(m_avatar);
        dest.writeString(m_name);
    }

    public static final Creator<ServiceItem> CREATOR = new Creator<ServiceItem>() {
        @Override
        public ServiceItem createFromParcel(Parcel in) {
            return new ServiceItem(in);
        }

        @Override
        public ServiceItem[] newArray(int size) {
            return new ServiceItem[size];
        }
    };

}
