package com.mta.sadna19.sadna;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.mta.sadna19.sadna.MenuRegisters.DataOption;
import com.mta.sadna19.sadna.MenuRegisters.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler {

    private OnServicesFetchedListener mOnServicesFetchedListener;
    private OnOptionFetchedListener mOnOptionFetcherListener;
    private OnUserFetchedListener mOnUserFetcherListener;
    private OnAttributeFetchedListener mOnAttributeFetcherListener;
    private onLastCallFetchedListener mOnLastCallFetcherListener;
    private onFavoritesServicesFetchedListener mOnFavoritesServicesFetchedListener;
    private onPrivacyPolicyFetchedListener mOnPrivacyPolicyFetchedListener;

    public User getmUser() {
        return mUser;
    }

    private User mUser;
    private FirebaseUser fbUsr;
    interface onPrivacyPolicyFetchedListener {
         void OnPrivacyPolicyFetched(boolean i_privacyPolicy);
    }
    interface onFavoritesServicesFetchedListener {
        public void OnFavoritesServicesFetched(Map<String,ServiceItem> i_favoritesServicesArray);
    }

    interface onLastCallFetchedListener {
        public void OnLastCallFetched(String i_userLastCall,String i_userLastCallDial);
    }

    interface OnAttributeFetchedListener {
        public void OnAtttibuteFetch(String i_attributeValue);
    }


    interface OnUserFetchedListener {
        public void OnUserFetch(User i_user);
    }

    interface OnOptionFetchedListener {
        public void OnMenuFetch(Option i_opt, ServiceItem i_service);
    }

    interface OnServicesFetchedListener {
        public void OnServicesFetched(Map<String, ArrayList<ServiceItem>> i_servicesData);
    }

    public void SetOnPrivacyPolicyFetchedListener(onPrivacyPolicyFetchedListener iOnPrivacyPolicyFetchedListener) {
        mOnPrivacyPolicyFetchedListener = iOnPrivacyPolicyFetchedListener;
    }

    public void removePrivacyListener(){
        mOnPrivacyPolicyFetchedListener = null;
    }


    public void SetOnFavoritesServicesFetchedListener(onFavoritesServicesFetchedListener iOnFavoritesServicesFetchedListener) {
        mOnFavoritesServicesFetchedListener = iOnFavoritesServicesFetchedListener;
    }

    public void SetOnLastCallFetchedListener(onLastCallFetchedListener ionLastCallFetchedListener) {
        mOnLastCallFetcherListener = ionLastCallFetchedListener;
    }

    public void SetOnAttributeFetchedListener(OnAttributeFetchedListener iOnAttributeFetchedListener) {
        mOnAttributeFetcherListener = iOnAttributeFetchedListener;
    }

    public void SetOnUserFetchedListener(OnUserFetchedListener iOnUserFetchedListener) {
        mOnUserFetcherListener = iOnUserFetchedListener;
    }

    public void SetOnOptionFetchedListener(OnOptionFetchedListener iOnOptionFetchedListener) {
        mOnOptionFetcherListener = iOnOptionFetchedListener;
    }

    public void SetOnServicesFetchedListener(OnServicesFetchedListener iOnServicesFetchedListener) {
        mOnServicesFetchedListener = iOnServicesFetchedListener;
    }

    public ServerHandler() {
        mUser = new User();

    }

    private Option extractMenuTree(DataSnapshot i_dataSnapshot, Option i_menuTree) {
        if (i_dataSnapshot.hasChild("subMenu")) {
            for (DataSnapshot snap : i_dataSnapshot.child("subMenu").getChildren()) {
                Option option = null;
                String currType = snap.child("type").getValue(String.class);
                switch (currType) {

                    case "DataOption": {

                        option = snap.getValue(DataOption.class);

                        break;
                    }
                    case "Option": {
                        option = snap.getValue(Option.class);
                        break;
                    }

                }

                i_menuTree.getSubMenu().add(extractMenuTree(snap, option));
            }
        }
        return i_menuTree;

    }



    public void writeNewService(Option i_menu, ServiceItem service) {
        DatabaseReference writeMenuRef;

        //write option
        writeMenuRef = FirebaseDatabase.getInstance().getReference("Menus/" + service.getM_name());
        writeMenuRef.child("Menu").setValue(i_menu);
        //write avatar
        writeMenuRef.child("Avatar").setValue(service.getM_avatar());
        //write genre
        writeMenuRef.child("Genre").setValue(service.getM_genre());

    }

    public void fetchMenu(String i_menuName) {

        DatabaseReference serviceRef;
        serviceRef = FirebaseDatabase.getInstance().getReference("Menus/" + i_menuName);
        serviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Option menuTree = new Option();

                ServiceItem serviceItem = new ServiceItem();
                serviceItem.setM_name(dataSnapshot.getKey());
                serviceItem.setM_avatar(dataSnapshot.child("Avatar").getValue(String.class));
                serviceItem.setM_genre(dataSnapshot.child("Genre").getValue(String.class));

                //belongs to newer version of extract
                menuTree = dataSnapshot.child("Menu").getValue(Option.class);
                //end of newer version

                menuTree = extractMenuTree(dataSnapshot.child("Menu"), menuTree);

                if (mOnOptionFetcherListener != null)
                    mOnOptionFetcherListener.OnMenuFetch(menuTree, serviceItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void fetchServices() {

        DatabaseReference allMenuRef = FirebaseDatabase.getInstance().getReference("Menus");
        allMenuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get all menus data
                Map<String, ArrayList<ServiceItem>> categorizedDataMap = new HashMap<>();
                ArrayList<ServiceItem> servicesData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    ServiceItem serviceItem = new ServiceItem();
                    serviceItem.setM_name(snapshot.getKey());
                    serviceItem.setM_avatar(snapshot.child("Avatar").getValue(String.class));
                    serviceItem.setM_genre(snapshot.child("Genre").getValue(String.class));
                    servicesData.add(serviceItem);
                    if (!categorizedDataMap.containsKey(serviceItem.getM_genre())) {
                        categorizedDataMap.put(serviceItem.getM_genre(), new ArrayList<ServiceItem>());

                    }
                    categorizedDataMap.get(serviceItem.getM_genre()).add(serviceItem);
                    //call adapterOfRecyclerView(servicesData);
                }
                categorizedDataMap.put("הכל", servicesData);
                if (mOnServicesFetchedListener != null)
                    mOnServicesFetchedListener.OnServicesFetched(categorizedDataMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeUser(final User user) {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid()+"/UserObject");
        if (user!=null)
            userRef.setValue(user);
        //userRef.child("PrivacyPolicy").setValue("true");


    }

    public void fetchUser(String i_userID) {
        /*if (IsUserLogedIn()){
            return;
        }*/
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(i_userID);
        userRef.child("UserObject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                if (user!=null)
                {

                    if (mOnUserFetcherListener != null)
                        mOnUserFetcherListener.OnUserFetch(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userRef = FirebaseDatabase.getInstance().getReference("Users/"+i_userID+"/UserObject");
        userRef.child("m_PrivacyPolicy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean res = (Boolean) dataSnapshot.getValue();

                mUser.SetPrivacyPolicy(res);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void fetchUserAttribute(final String i_userAttribute) {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid());
        DatabaseReference userDataRef = userRef.child("User Data");
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mOnAttributeFetcherListener != null && dataSnapshot.child(i_userAttribute).getValue() != null){
                    String userAttribute = dataSnapshot.child(i_userAttribute).getValue().toString();
                    mOnAttributeFetcherListener.OnAtttibuteFetch(userAttribute);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeUserAttribute(String i_userAttribute, String i_attributeValue) {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid());
        userRef.child("User Data").child(i_userAttribute).setValue(i_attributeValue);
    }

    public void writeUserLastCall(String i_pathName,String i_pathCall) {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid()+"/LastCall");
        userRef.child("lastCallPathName").setValue(i_pathName);
        userRef.child("lastCallPathCall").setValue(i_pathCall);
    }

    public void fetchUserLastCall() {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid()+"/LastCall");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String lastCallDial = dataSnapshot.child("lastCallPathCall").getValue(String.class);
                String lastCallString = dataSnapshot.child("lastCallPathName").getValue(String.class);
                if (lastCallString!=null)
                    if (mOnLastCallFetcherListener != null)
                        mOnLastCallFetcherListener.OnLastCallFetched(lastCallString,lastCallDial);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeUserFavoriteServices(Map<String,ServiceItem> i_userFavoritesServices) {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid());
        userRef.child("Favorites").setValue(i_userFavoritesServices);
    }

    public void fetchUserFavoritesServices() {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid());
        DatabaseReference userDataRef = userRef.child("Favorites");
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, ServiceItem> servicesDataMap = new HashMap<>();

                int i=1;
                String index;
                while(true)
                {
                    index = String.valueOf(i);
                    if (dataSnapshot.hasChild(index))
                    {
                        ServiceItem serviceItem = new ServiceItem();
                        serviceItem = dataSnapshot.child(index).getValue(ServiceItem.class);
                        servicesDataMap.put(index,serviceItem);
                        i++;
                    }
                    else{
                        break;
                    }

                }

                if (mOnFavoritesServicesFetchedListener != null)
                    mOnFavoritesServicesFetchedListener.OnFavoritesServicesFetched(servicesDataMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onPrivacyPolicySwitchedOff(){
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid());
        userRef.child("User Data").setValue(null);
        userRef.child("UserObject").child("m_PrivacyPolicy").setValue(false);

    }
    public void onPrivacyPolicySwitchedOn(){
        fbUsr= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid()+"/UserObject");
        userRef.child("m_PrivacyPolicy").setValue(true);
    }

    public void fetchUserPrivacyPolicy()
    {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUsr.getUid()+"/UserObject");
        userRef.child("m_PrivacyPolicy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String privacyPolicy = dataSnapshot.getValue(String.class);


                if (mOnPrivacyPolicyFetchedListener!=null)
                {
                    if (privacyPolicy!=null)
                        mOnPrivacyPolicyFetchedListener.OnPrivacyPolicyFetched(privacyPolicy.equals("true"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeAProblem(MenuProblem i_menuProblem){
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Reviews/"+ i_menuProblem.getmServiceName()+"/"+fbUsr.getUid());
        userRef.setValue(i_menuProblem);

    }
    public void fetchProblems()
    {

    }

    public boolean IsUserLogedIn(){
        return (FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    public void addPointsToUser(int i_points)
    {
        fbUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/"+fbUsr.getUid()+"/UserObject");
        userRef.child("m_points").setValue(i_points);

    }



}

