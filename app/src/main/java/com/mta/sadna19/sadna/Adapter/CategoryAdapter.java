package com.mta.sadna19.sadna.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mta.sadna19.sadna.R;
import com.mta.sadna19.sadna.SpinnerItem;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<SpinnerItem> {
    public CategoryAdapter(Context context, ArrayList<SpinnerItem> categoryList, SpinnerListener spinnerListener)
    {
        super(context,R.layout.category_spinner_item,categoryList);
        this.spinnerListener = spinnerListener;
    }

    SpinnerListener spinnerListener;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_spinner_item,parent,false
            );
        }
        ImageView imageView = convertView.findViewById(R.id.image_category);
        TextView textView = convertView.findViewById(R.id.text1);

        final SpinnerItem curItem = getItem(position);
        if (curItem!=null)
        {
            imageView.setImageResource(curItem.getFlagImage());
            textView.setText(curItem.getCategoryName());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("convertView","was pressed inside "+ curItem.getCategoryName());
                spinnerListener.onSpinnerItemClicked(curItem,position);
                // todo build call back that pass SpinnerItem and position
                // todo dismiss spinner window
                // todo remove setOnItemSelectedListener

                // todo use the callback to handle the spinner click

            }
        });
        return convertView;
    }


}
