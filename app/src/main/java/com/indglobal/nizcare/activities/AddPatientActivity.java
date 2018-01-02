package com.indglobal.nizcare.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.indglobal.nizcare.BuildConfig;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by readyassist on 1/2/18.
 */

public class AddPatientActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;
    RippleView rplBack,rplAddPtnt;

    Spinner spinClinics;
    EditText etFName,etLName,etEmail,etAdrs,etHeight,etWeight,etBldGrp,etRfrdby,etTypeofVisit,etMdclInfo;
    ImageView ivPrfl;
    TextView tvAddlogo,tvDob,tvLangs;
    RadioGroup rgGndr;
    RadioButton rbMale,rbFemale;

    String mCurrentPhotoPath="",forWhich="1";
    public static Bitmap logo;
    String gender="Male";

    String[] PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.SET_ALARM, Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rgGndr = (RadioGroup)findViewById(R.id.rgGndr);
        tvAddlogo = (TextView)findViewById(R.id.tvAddlogo);

        rgGndr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId==R.id.rbMale){
                    gender = "Male";
                }else {
                    gender = "Female";
                }
            }
        });


        rplBack.setOnRippleCompleteListener(this);
        tvAddlogo.setOnClickListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){

            case R.id.rplBack:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvSpeclty:
                Intent ii = new Intent(AddPatientActivity.this,SpecialityActivity.class);
                startActivityForResult(ii,3);
                break;

            case R.id.tvAddlogo:
                if (!Comman.verifyStoragePermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                }else {
                    forWhich="1";
                    openEditPicBottomSheet();
                }
                break;
        }
    }

    public void openEditPicBottomSheet () {

        View view = getLayoutInflater ().inflate (R.layout.edit_pic_bottumsheet, null);

        TextView tvBtmShtCancle = (TextView)view.findViewById(R.id.tvBtmShtCancle);
        LinearLayout llFromGallery = (LinearLayout)view.findViewById(R.id.llFromGallery);
        LinearLayout llCapturePic = (LinearLayout)view.findViewById(R.id.llCapturePic);

        final Dialog mBottomSheetDialog = new Dialog (AddPatientActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show ();


        tvBtmShtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        llFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                mBottomSheetDialog.dismiss();
            }
        });

        llCapturePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(AddPatientActivity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(AddPatientActivity.this,"Camera folder not found in your mobile please open your camera and take a pic!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (photoFile != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(AddPatientActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 1);
                        mBottomSheetDialog.dismiss();
                    }
                }

            }
        });

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "NizcareApp");
        if (!storageDir.exists()){
            if (!storageDir.mkdirs()) {
                Log.d("LOCHA", "Failed to create directory: " + storageDir.getAbsolutePath());
            }
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                //tvSpeclty.setText(result);
            }
        }else if (requestCode==1){
            if (data!=null){
                Uri imguri = Uri.parse(mCurrentPhotoPath);

                if (!Comman.isConnectionAvailable(AddPatientActivity.this)){
                    Toast.makeText(AddPatientActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    logo = Comman.readBitmap(AddPatientActivity.this, imguri, 0);
                    ivPrfl.setImageBitmap(logo);
                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.imguploadscs),Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == 2){
            if (data!=null){
                Uri imguri = data.getData();

                if (!Comman.isConnectionAvailable(AddPatientActivity.this)){
                    Toast.makeText(AddPatientActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    logo = Comman.readBitmap(AddPatientActivity.this, imguri, 0);
                    ivPrfl.setImageBitmap(logo);
                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.imguploadscs),Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

//    private void getDocuments() {
//
//        String url = getResources().getString(R.string.getdoctypeApi);
//
//        String DOCTYPEHIT = "docs_hit";
//        VolleySingleton.getInstance(AddPatientActivity.this).cancelRequestInQueue(DOCTYPEHIT);
//        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String result) {
//                try {
//                    JSONObject response  = new JSONObject(result);
//                    boolean isSuccess = response.getBoolean("success");
//
//                    if (isSuccess){
//
//                        JSONArray data = response.getJSONArray("data");
//                        for (int i=0;i<data.length();i++){
//
//                            JSONObject object = data.getJSONObject(i);
//
//                            String id = object.getString("document_id");
//                            String name = object.getString("document_name");
//
//                            doctypeItem = new DoctypeItem(id,name);
//                            doctypeItemArrayList.add(doctypeItem);
//                        }
//
//                        docTypeAdapter = new DocTypeAdapter(AddPatientActivity.this,doctypeItemArrayList);
//                        spinIdType.setAdapter(docTypeAdapter);
//
//                        if (!Comman.isConnectionAvailable(AddPatientActivity.this)){
//                            Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
//                        }else {
//                            prgLoading.setVisibility(View.VISIBLE);
//                            getCities();
//                        }
//
//                    }else {
//                        Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
//                }
//
//                prgLoading.setVisibility(View.GONE);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
//                prgLoading.setVisibility(View.GONE);
//            }
//        });
//
//        request.setTag(DOCTYPEHIT);
//        request.setRetryPolicy(new DefaultRetryPolicy(15000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        VolleySingleton.getInstance(AddPatientActivity.this).addToRequestQueue(request);
//    }
//
//    private void getCities() {
//
//        String url = getResources().getString(R.string.getCitiesApi);
//
//        String CITYHIT = "cities_hit";
//        VolleySingleton.getInstance(AddPatientActivity.this).cancelRequestInQueue(CITYHIT);
//        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String result) {
//                try {
//                    JSONObject response  = new JSONObject(result);
//                    boolean isSuccess = response.getBoolean("success");
//
//                    if (isSuccess){
//
//                        JSONArray data = response.getJSONArray("data");
//                        for (int i=0;i<data.length();i++){
//
//                            JSONObject object = data.getJSONObject(i);
//
//                            String id = object.getString("city_id");
//                            String name = object.getString("city_name");
//                            String state = object.getString("city_state");
//
//                            cityItem = new CityItem(id,name,state);
//                            cityItemArrayList.add(cityItem);
//                        }
//
//                        cityAdapter = new CityAdapter(AddPatientActivity.this,cityItemArrayList);
//                        spinCity.setAdapter(cityAdapter);
//
//                        prgLoading.setVisibility(View.GONE);
//                        rlCongrts.setVisibility(View.GONE);
//                        rlAcntStup.setVisibility(View.VISIBLE);
//
//                    }else {
//                        Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
//                }
//
//                prgLoading.setVisibility(View.GONE);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
//                prgLoading.setVisibility(View.GONE);
//            }
//        });
//
//        request.setTag(CITYHIT);
//        request.setRetryPolicy(new DefaultRetryPolicy(15000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        VolleySingleton.getInstance(AddPatientActivity.this).addToRequestQueue(request);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
