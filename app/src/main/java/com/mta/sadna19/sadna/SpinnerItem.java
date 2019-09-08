package com.mta.sadna19.sadna;

public class SpinnerItem {

    private String mCategoryName;
    private int mFlagImage;


    public SpinnerItem(String i_categoryName, int i_flagImage){
        mCategoryName = i_categoryName;
        mFlagImage = i_flagImage;
    }

    public String getCategoryName(){return mCategoryName;}
    public int getFlagImage(){return mFlagImage;}

}
