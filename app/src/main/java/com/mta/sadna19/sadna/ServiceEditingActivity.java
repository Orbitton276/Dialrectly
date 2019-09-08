package com.mta.sadna19.sadna;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mta.sadna19.sadna.MenuRegisters.Option;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

public class ServiceEditingActivity extends AppCompatActivity {
    static final String TAG = "$SEditingActivity$";
    ViewGroup mContainerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() >>");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_editing);
        init();
        buildTree();
        Log.e(TAG, "onCreate() <<");
    }

    private void init() {
        Log.e(TAG, "init() >>");
        mContainerView = findViewById(R.id.mainLayout);
        Log.e(TAG, "init() <<");
    }

    private void buildTree() {
        Log.e(TAG, "buildTree() >>");
        MyHolder mh = new MyHolder(this);
        TreeNode root = TreeNode.root();
        TreeNode parent = new TreeNode(new Option("1","a")).setViewHolder(mh);
        TreeNode child0 = new TreeNode(new Option("1","vvv")).setViewHolder(mh);
        TreeNode child1 = new TreeNode(new Option("2","d")).setViewHolder(mh);
        parent.addChildren(child0, child1);
        root.addChild(parent);

        AndroidTreeView tView = new AndroidTreeView(this, root);
        //tView.setDefaultViewHolder(MyHolder.class);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyle);
        mContainerView.addView(tView.getView());
        Log.e(TAG, "buildTree() >>");
    }

    public class MyHolder extends TreeNode.BaseNodeViewHolder<Option> {
        public MyHolder(Context context) {
            super(context);
            Log.e(TAG, "MyHolder() >>");
            Log.e(TAG, "MyHolder() <<");
        }

        @Override
        public View createNodeView(TreeNode node, Option value) {
            Log.e(TAG, "createNodeView() >>");
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.option_list_item, null, false);
            Button tvValue = (Button) view.findViewById(R.id.optionButton);
            tvValue.setText(value.getName());
            Log.e(TAG, "createNodeView() << " + value.getName());
            return view;
        }
    }
}
