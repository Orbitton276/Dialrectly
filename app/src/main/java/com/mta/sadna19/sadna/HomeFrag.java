package com.mta.sadna19.sadna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import com.mta.sadna19.sadna.Adapter.CategoryRecyclerAdapter;
import com.mta.sadna19.sadna.Adapter.MenuAdapter;
import com.mta.sadna19.sadna.MenuRegisters.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFrag extends Fragment  {

    List<ServiceItem> mMenuList;
    MenuAdapter nMenuAdapter;
    private ProgressDialog mLoadingScreen;
    private static final String OPTION_SELECTED = "OPTION_SELECTED";
    RecyclerView mRecyclerView;
    RecyclerView mCategoriesRecycler;
    ServerHandler mServerHandler;
    User mUser;
    Context mContext;
    Map <String,ArrayList<ServiceItem>> dataMap;
    private ArrayList<String> mCategoriesList;
    private CategoryRecyclerAdapter mCategoriesAdapter;

    private ArrayList<SpinnerItem> categoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mLoadingScreen = new ProgressDialog(getActivity(),R.style.full_screen_dialog){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.fill_dialog);
                getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
            }
        };

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.home_frag,null);
        mContext = getActivity().getApplicationContext();

        mCategoriesRecycler = fragView.findViewById(R.id.recyclerCategories);


        mCategoriesList = new ArrayList<>();
        categoryList = new ArrayList<>();

        categoryList.add(new SpinnerItem(" בחר קטגוריה",0));

        return fragView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerServices);
        dataMap = new HashMap<>();



        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mServerHandler = new ServerHandler();
        mMenuList = new ArrayList<>();
        nMenuAdapter = new MenuAdapter(mContext, mMenuList);
        nMenuAdapter.setOnMenuClickListener(new MenuAdapter.OnMenuClickListener() {
            @Override
            public void OnMenuClick(ServiceItem iMenu) {
                mServerHandler.fetchMenu(iMenu.getM_name());
            }
        });
        mRecyclerView.setAdapter(nMenuAdapter);
        mServerHandler.SetOnServicesFetchedListener(new ServerHandler.OnServicesFetchedListener() {
            @Override
            public void OnServicesFetched(Map<String,ArrayList<ServiceItem>> i_servicesData) {

                mRecyclerView.setVisibility(View.VISIBLE);
                updateDataMap(i_servicesData);
                initData("הכל");
                initList();
                initCategoriesRecycler();
                mLoadingScreen.dismiss();
            }
        });
        mServerHandler.SetOnOptionFetchedListener(new ServerHandler.OnOptionFetchedListener() {
            @Override
            public void OnMenuFetch(Option i_opt, ServiceItem i_service) {
                Intent intent = new Intent(mContext, OptionsListActivity.class);

                intent.putExtra(OPTION_SELECTED, i_opt);
                intent.putExtra("service", i_service);
                startActivity(intent);
            }
        });
        getData();
    }

    private void initData(String i_categoryToDisplay) {
        mMenuList.clear();

        for (ServiceItem si : dataMap.get(i_categoryToDisplay)) {
            mMenuList.add(si);
        }

        nMenuAdapter.setmMenuListFull(new ArrayList<>(mMenuList));
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    private void updateDataMap(Map<String,ArrayList<ServiceItem>> i_servicesData) {
        dataMap = i_servicesData;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.services_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                nMenuAdapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    public void initHomeFrag(final Context i_context, User i_user) {


        //mContext = i_context;
        mUser = i_user;

    }

    private void getData() {
        mLoadingScreen.show();
        mServerHandler.fetchServices();
    }


    private void initList()
    {

        if (dataMap!=null)
        {
            for (Map.Entry<String, ArrayList<ServiceItem>> entry : dataMap.entrySet()) {
                categoryList.add(new SpinnerItem(entry.getKey(),R.drawable.ic_dot));
            }
        }


    }

    private void initCategoriesRecycler()
    {
        if (dataMap!=null)
        {
            for (Map.Entry<String, ArrayList<ServiceItem>> entry : dataMap.entrySet()) {
                mCategoriesList.add(entry.getKey());
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mCategoriesRecycler.setLayoutManager(linearLayoutManager);
        mCategoriesAdapter = new CategoryRecyclerAdapter(getActivity(),mCategoriesList);
        mCategoriesAdapter.SetOnCategoryClickedListener(new CategoryRecyclerAdapter.OnCategoryItemClickedListener() {
            @Override
            public void onCategoryClicked(String i_category) {

                initData(i_category);
            }
        });
        mCategoriesRecycler.setAdapter(mCategoriesAdapter);
    }


}
