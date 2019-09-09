package com.mta.sadna19.sadna.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mta.sadna19.sadna.R;
import com.mta.sadna19.sadna.SpinnerItem;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {


    private ArrayList<String> mCategories = new ArrayList<>( );
    private Context mContext;
    private OnCategoryItemClickedListener mOnCategoryItemClickedListener;

    public interface OnCategoryItemClickedListener{
        public void onCategoryClicked(String i_category);
    }

    public void SetOnCategoryClickedListener(OnCategoryItemClickedListener iOnCategoryItemClickedListener){
        mOnCategoryItemClickedListener = iOnCategoryItemClickedListener;
    }


    public CategoryRecyclerAdapter(Context mContext,ArrayList<String> mCategories) {
        this.mCategories = mCategories;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_recyclerview_item,viewGroup,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.catTitle.setText(mCategories.get(i));
        viewHolder.catTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //category was clicked
                if (mOnCategoryItemClickedListener!=null)
                {
                    mOnCategoryItemClickedListener.onCategoryClicked(mCategories.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{




        Button catTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catTitle = itemView.findViewById(R.id.tvCategoryItemTitle);
            this.catTitle = catTitle;
        }

    }
}
