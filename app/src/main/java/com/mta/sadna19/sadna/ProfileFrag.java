package com.mta.sadna19.sadna;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.mta.sadna19.sadna.MenuListActivity.PReqCode;
import static com.mta.sadna19.sadna.MenuListActivity.REQUESTCODE;

public class ProfileFrag extends Fragment {


    EditText name;
    //EditText id;
    //EditText phone;
    EditText email;
    Button editBtn;
    User user;
    ImageView userProfilePic, rotateButton;
    FirebaseUser fbUser;
    Uri pickedImgUri;
    SwitchCompat switchButton;
    ServerHandler serverHandler;
    Boolean bool;
    TextView tvProgress,tvProgressLevel;
    ProgressBar progressBar;
    private static final String TAG = "onProfileFrag";
    private onProfileImageUpdate mOnProfileImageUpdate;

    public interface onProfileImageUpdate {
        void onProfileImageUpdate();
    }

    public void SetOnImageUpdateClickListener(onProfileImageUpdate onProfileImageUpdate) {
        mOnProfileImageUpdate = onProfileImageUpdate;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.profile_frag, null);
        rotateButton = fragView.findViewById(R.id.btnRotate);
        switchButton = fragView.findViewById(R.id.btnSwitchPrivacy);
        name = fragView.findViewById(R.id.frName);
        progressBar = fragView.findViewById(R.id.profileProgressBar);
        tvProgress = fragView.findViewById(R.id.tvProgress);
        tvProgressLevel = fragView.findViewById(R.id.tvProgressLevel);
        email = fragView.findViewById(R.id.frEmail);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchButton.isChecked()) {
                    openNotificationDialog(getResources().getString(R.string.privacyPolicySwitchedOn));
                } else {
                    openNotificationDialog(getResources().getString(R.string.privacyPolicySwitchedOff));
                }
            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRotateProfilePic();
            }
        });


        return fragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serverHandler = new ServerHandler();

        editBtn = view.findViewById(R.id.btnEdit);
        userProfilePic = view.findViewById(R.id.imgProfile);


        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e(TAG, "userUrl: " + fbUser.getPhotoUrl());
        if (fbUser.getPhotoUrl() != null) {
            Picasso.get().load(fbUser.getPhotoUrl()).transform(new CircleTransform()).into(userProfilePic);
        } else {
            userProfilePic.setImageResource(R.drawable.ic_launcher_foreground);
        }
        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onImageClicked >>");
                if (Build.VERSION.SDK_INT >= 22) {
                    Log.e(TAG, "onRequestPermission>>");
                    checkAndRequestForPermission();
                } else {
                    Log.e(TAG, "onOpenGallery>>");
                    openGallery();
                }
                if (mOnProfileImageUpdate != null)
                    mOnProfileImageUpdate.onProfileImageUpdate();

            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.isEnabled()){

                    name.setEnabled(false);
                    editBtn.setText("עריכה");

                    user.setM_name(name.getText().toString());
                    serverHandler.writeUser(user);
                }
                else{
                    name.setEnabled(true);
                    editBtn.setText("סיים עריכה");

                }

            }
        });
        name.setEnabled(false);
        email.setEnabled(false);
        updateUserUI();

        serverHandler.SetOnUserFetchedListener(new ServerHandler.OnUserFetchedListener() {
            @Override
            public void OnUserFetch(User i_user) {
                user = i_user;
                Log.e(TAG,"User is: "+i_user.toString());
                updateUserUI();
            }
        });
        serverHandler.fetchUser(fbUser.getUid());
        serverHandler.SetOnPrivacyPolicyFetchedListener(new ServerHandler.onPrivacyPolicyFetchedListener() {
            @Override
            public void OnPrivacyPolicyFetched(boolean i_privacyPolicy) {
                bool = i_privacyPolicy;
                Log.e(TAG, "pv: " + bool);
                if (i_privacyPolicy)
                    switchButton.setChecked(true);
                serverHandler.removePrivacyListener();
            }
        });
        serverHandler.fetchUserPrivacyPolicy();


    }


    public ProfileFrag() {

    }


    private void updateUserUI(){
        if (user != null) {
            name.setText(user.getM_name());
            setProgressBar(user.getM_points());
            email.setText(user.getM_email());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // the user has successfully picked an image
        // we need to save its reference to a Uri variable
        pickedImgUri = data.getData();

        Picasso.get().load(pickedImgUri).transform(new CircleTransform()).fit().into(userProfilePic);
        updateUserInfo();


        Log.e(TAG, "onActivityResult in frag");
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                //Toast.makeText(MenuListActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
                openGallery();
            }

        } else
            openGallery();

    }


    private void updateUserInfo() {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");

        final StorageReference imageFilePath = mStorage.child(fbUser.getUid());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded succesfully
                // now we can get our image url
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contain user image url
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                        fbUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                        }
                                    }
                                });
                    }
                });
            }
        });


    }

    public void openNotificationDialog(String i_message) {

        NotificationDialog mNotificationDialog = new NotificationDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message", i_message);
        mNotificationDialog.setArguments(bundle);
        mNotificationDialog.SetOnPrivacyPolicyClicked(new NotificationDialog.OnPrivacyPolicyClicked() {
            @Override
            public void OnApprovedPrivacyChanges() {
                if (switchButton.isChecked()) {
                    serverHandler.onPrivacyPolicySwitchedOn();
                } else {
                    serverHandler.onPrivacyPolicySwitchedOff();
                }
            }
        });
        mNotificationDialog.show(getActivity().getSupportFragmentManager(), "");

    }

    public void onRotateProfilePic() {
        userProfilePic.setRotation(userProfilePic.getRotation() + 90);

    }

    private void setProgressBar(int i_currentPoints)
    {
        int startLevel=0;
        int endLevel=0;
        int progressToSet=0;
        double db;
        if (i_currentPoints>=0&&i_currentPoints<=200)
        {
            //level 1
            tvProgressLevel.setText("טירון");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            startLevel=0;
            endLevel = 200;
        }
        else if(i_currentPoints>200&&i_currentPoints<=300){
            //level 2
            tvProgressLevel.setText("ותיק בתחום");

            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

            startLevel=200;
            endLevel = 300;
        }
        else{
            //level 3
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
            tvProgressLevel.setText("סבא ג'פטו");

            startLevel=300;
            endLevel = 500;
            if (i_currentPoints>endLevel)
            {
                endLevel = startLevel+1;
            }
        }
        db = ((double)i_currentPoints-startLevel)/(endLevel-startLevel);
        db = db*100;
        progressToSet = (int)db;

        tvProgress.setText(i_currentPoints+"/"+endLevel);

        Log.e(TAG,"prog is: "+progressToSet+"start "+startLevel+"end "+endLevel+" db "+db);
        progressBar.setProgress(progressToSet);
    }


}
