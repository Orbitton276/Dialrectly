package com.mta.sadna19.sadna.MenuRegisters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.mta.sadna19.sadna.R;
import com.mta.sadna19.sadna.Resources;

import java.util.ArrayList;
import java.util.Collection;

public class Option implements Parcelable {
    //m_Keys - number of option the user need to choose
    private String m_Keys = null;
    //m_Name - The option name the user need to choose
    private String m_Name = null;
    //m_PostKeys - Usually after the user input ID/Phone he need to put #
    private String m_PostKeys = null;


    //m_SubMenu - next Menu
    private ArrayList<Option> m_SubMenu = new ArrayList<>();

    public Option() {
    }

    public Option(String i_Key, String i_Name) {
        this.m_Keys = i_Key;
        this.m_Name = i_Name;
        this.m_PostKeys = ",";
        m_SubMenu = new ArrayList<>();
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    public String getKeys() {
        return m_Keys;
    }

    public String getName() {
        return m_Name;
    }

    public String getPostKeys() {
        return m_PostKeys;
    }

    protected void setKeys(String m_Keys) {
        this.m_Keys = m_Keys;
    }

    public void setName(String m_Name) {
        this.m_Name = m_Name;
    }

    public void setPostKeys(String i_PostKeys) {
        this.m_PostKeys = i_PostKeys;
    }

    public String pressKeys() {
        return m_Keys + m_PostKeys;
    }

    public Collection<Option> getSubMenu() {
        return m_SubMenu;
    }

    public String getType() {
        return "Option";
    }

    @Override
    public String toString() {
        return "Option{" +
                " type ="+getType()+
                ", m_Name='" + m_Name + '\'' +
                ", m_SubMenu=" + m_SubMenu +
                +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_Keys);
        dest.writeString(m_Name);
        dest.writeString(m_PostKeys);
        dest.writeList(m_SubMenu);
    }

    protected Option(Parcel in){
        m_Keys = in.readString();
        m_Name = in.readString();
        m_PostKeys = in.readString();
        m_SubMenu = in.readArrayList(Option.class.getClassLoader());
    }
}
