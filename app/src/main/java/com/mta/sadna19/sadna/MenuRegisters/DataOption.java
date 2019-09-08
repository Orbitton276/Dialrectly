package com.mta.sadna19.sadna.MenuRegisters;

import android.os.Parcel;
import android.os.Parcelable;

public class DataOption extends Option implements Parcelable {
    //Type of what the user input like Phone,ID
    private String m_DataType;

    //i_Name - Text with the instruction for the user like "Enter ID here"
    //i_EndTab - Usually after the user input ID/Phone he need to put #
    //i_Type - Type of what the user input like Phone,ID
    public DataOption(String i_Name, String i_Type, String i_EndTab) {
        super("1" , i_Name);
        this.m_DataType = i_Type;
        this.setPostKeys(i_EndTab + this.getPostKeys());
    }

    public DataOption(){}

    protected void setDataType(String m_Type) {
        this.m_DataType = m_Type;
    }

    public String getDataType() {

        return m_DataType;
    }

    public void addData(String i_Data){
        this.setKeys(i_Data);
    }

    public String getType(){
        return "DataOption";
    }

    @Override
    public String toString() {
        return "DataOption{" +

                "m_DataType='" + m_DataType + '\'' +
                '}';
    }

    //-----------------------------------------------------
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(m_DataType);
    }

    protected DataOption(Parcel in){
        super(in);
        m_DataType = in.readString();
    }

    public static final Creator<DataOption> CREATOR = new Creator<DataOption>() {
        @Override
        public DataOption createFromParcel(Parcel in) {
            return new DataOption(in);
        }

        @Override
        public DataOption[] newArray(int size) {
            return new DataOption[size];
        }
    };
}
