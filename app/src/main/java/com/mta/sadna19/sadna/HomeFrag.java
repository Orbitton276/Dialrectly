package com.mta.sadna19.sadna;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Spinner;

import com.mta.sadna19.sadna.Adapter.CategoryAdapter;
import com.mta.sadna19.sadna.Adapter.MenuAdapter;
import com.mta.sadna19.sadna.Adapter.SpinnerListener;
import com.mta.sadna19.sadna.MenuRegisters.Option;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFrag extends Fragment  {

    List<ServiceItem> mMenuList;
    MenuAdapter nMenuAdapter;

    private static final String OPTION_SELECTED = "OPTION_SELECTED";
    public static final String TAG = "HomeFrag";
    private Spinner categoriesSpinner;
    RecyclerView mRecyclerView;
    ServerHandler mServerHandler;
    User mUser;
    Context mContext;
    Map <String,ArrayList<ServiceItem>> dataMap;

    private ArrayList<SpinnerItem> categoryList;
    private CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView >>");
        View fragView = inflater.inflate(R.layout.home_frag,null);
        mContext = getActivity().getApplicationContext();

        categoriesSpinner = fragView.findViewById(R.id.categorySpinner);
        categoryList = new ArrayList<>();

        categoryList.add(new SpinnerItem(" בחר קטגוריה",0));


        categoryAdapter = new CategoryAdapter(getActivity(),categoryList, new SpinnerListener(){

            @Override
            public void onSpinnerItemClicked(SpinnerItem spinnerItem, int position) {
                Log.e(TAG,"was pressed position=" + spinnerItem.getCategoryName());
                if (position!=0){
                    categoriesSpinner.setSelection(position);
                    hideSpinnerDropDown();
                    initData(spinnerItem.getCategoryName());
                }

            }
        });

        categoriesSpinner.setAdapter(categoryAdapter);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        return fragView;
    }

    public  void hideSpinnerDropDown() {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(categoriesSpinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerServices);
        dataMap = new HashMap<>();
        //categoryList = new ArrayList<>();
        //categoriesSpinner = view.findViewById(R.id.categorySpinner);
        //categoryAdapter = new CategoryAdapter(getActivity(),categoryList);
        //categoriesSpinner.setAdapter(categoryAdapter);
        //categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        Log.e(TAG,"category is : "+i_categoryToDisplay);
        Log.e(TAG,"data is : "+dataMap.get(i_categoryToDisplay).toString());

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
                Log.e(TAG,"s = "+s);
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
        mServerHandler.fetchServices();
    }


    private void initList()
    {
        Log.e(TAG,"dataMap: "+dataMap.toString());

        if (dataMap!=null)
        {
            for (Map.Entry<String, ArrayList<ServiceItem>> entry : dataMap.entrySet()) {
                Log.e(TAG,"cat is: "+entry.getKey());
                categoryList.add(new SpinnerItem(entry.getKey(),R.drawable.ic_dot));
            }
        }
    }
}
