package com.indglobal.nizcare.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.BuildConfig;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.CityAdapter;
import com.indglobal.nizcare.adapters.ClinicAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleyMultipartRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.commons.countrycodes.CountryCodePicker;
import com.indglobal.nizcare.fragments.BasePatientFragment;
import com.indglobal.nizcare.model.CityItem;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.LanguageItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.indglobal.nizcare.commons.Comman.getFileDataFromDrawable;

/**
 * Created by readyassist on 1/2/18.
 */

public class AddPatientActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;
    RelativeLayout rlMain;
    RippleView rplBack,rplAddPtnt;
    CountryCodePicker spinCountry;
    Spinner spinBldgrp,spinClinics;

    EditText etFName,etLName,etEmail,etNumber,etAdrs,etHeight,etWeight,etRfrdby,etTypeofVisit,etMdclInfo;
    ImageView ivPrfl;
    TextView tvAddlogo,tvDob,tvLangs;
    RadioGroup rgGndr;

    String mCurrentPhotoPath="",forWhich="1";
    public static Bitmap logo;
    String gender="Male";

    String[] PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.SET_ALARM, Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    ClinicItem clinicItem;
    ArrayList<ClinicItem> clinicItemArrayList = new ArrayList<>();
    ClinicAdapter clinicAdapter;

    public static ArrayList<LanguageItem> myLanguagesItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        rgGndr = (RadioGroup)findViewById(R.id.rgGndr);
        ivPrfl = (ImageView)findViewById(R.id.ivPrfl);
        tvAddlogo = (TextView)findViewById(R.id.tvAddlogo);
        spinBldgrp = (Spinner)findViewById(R.id.spinBldgrp);
        spinClinics = (Spinner)findViewById(R.id.spinClinics);
        rplAddPtnt = (RippleView)findViewById(R.id.rplAddPtnt);
        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAdrs = (EditText) findViewById(R.id.etAdrs);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etRfrdby = (EditText) findViewById(R.id.etRfrdby);
        etTypeofVisit = (EditText) findViewById(R.id.etTypeofVisit);
        etMdclInfo = (EditText) findViewById(R.id.etMdclInfo);
        etNumber = (EditText)findViewById(R.id.etNumber);
        tvDob = (TextView)findViewById(R.id.tvDob);
        tvLangs = (TextView)findViewById(R.id.tvLangs);
        spinCountry = (CountryCodePicker) findViewById(R.id.spinCountry);

        ArrayAdapter adapterBldgrp = ArrayAdapter.createFromResource(this, R.array.BloodGroup, R.layout.spinner_dropdown_item);
        adapterBldgrp.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinBldgrp.setAdapter(adapterBldgrp);

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

        Comman.setPreferences(AddPatientActivity.this,"PatientAdded","0");

        if (!Comman.isConnectionAvailable(AddPatientActivity.this)){
            rlMain.setVisibility(View.GONE);
            Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getClinics();
        }

        rplBack.setOnRippleCompleteListener(this);
        tvAddlogo.setOnClickListener(this);
        tvLangs.setOnClickListener(this);
        tvDob.setOnClickListener(this);
        rplAddPtnt.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplBack:
                onBackPressed();
                break;

            case R.id.rplAddPtnt:
                String first_name = etFName.getText().toString();
                String last_name = etLName.getText().toString();
                String dob = tvDob.getText().toString();
                String email = etEmail.getText().toString();
                String country_code = spinCountry.getSelectedCountryCode();
                String mobile_no = etNumber.getText().toString();
                String address = etAdrs.getText().toString();
                String height = etHeight.getText().toString();
                String weight = etWeight.getText().toString();
                String blood_group = spinBldgrp.getSelectedItem().toString();
                String refered_by = etRfrdby.getText().toString();
                String type_of_visit = etTypeofVisit.getText().toString();
                String clinic = clinicItemArrayList.get(spinClinics.getSelectedItemPosition()).getHospital_id();
                String medical_info = etMdclInfo.getText().toString();

                JSONArray language = new JSONArray();
                for (int i=0;i<myLanguagesItems.size();i++){
                    language.put(myLanguagesItems.get(i).getId());
                }

                if (!Comman.isConnectionAvailable(AddPatientActivity.this)){
                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (first_name.equalsIgnoreCase("")){
                    etFName.setError("Enter first name!");
                    etFName.requestLayout();
                }else if (last_name.equalsIgnoreCase("")){
                    etLName.setText("Enter last name!");
                    etLName.requestLayout();
                }else if (logo==null){
                    Toast.makeText(AddPatientActivity.this,"Provide patient image!",Toast.LENGTH_SHORT).show();
                }else if (dob.equalsIgnoreCase("")){
                    Toast.makeText(AddPatientActivity.this,"Provide Date of birth!",Toast.LENGTH_SHORT).show();
                }else if (language.length()==0){
                    Toast.makeText(AddPatientActivity.this,"Provide known languages!",Toast.LENGTH_SHORT).show();
                }else if (!Comman.emailValidator(email)){
                    etEmail.setError("Provide valid email id!");
                    etEmail.requestLayout();
                }else if (mobile_no.length()<6){
                    etNumber.setError("Provide mobile number!");
                    etNumber.requestLayout();
                }else if (address.equalsIgnoreCase("")){
                    etAdrs.setError("Provide patient address!");
                    etAdrs.requestLayout();
                }else if(height.equalsIgnoreCase("")){
                    etHeight.setError("Provide patient height!");
                    etHeight.requestLayout();
                }else if (weight.equalsIgnoreCase("")){
                    etWeight.setError("Provide patient weight!");
                    etWeight.requestLayout();
                }else if (refered_by.equalsIgnoreCase("")){
                    etRfrdby.setError("Provide who refer!");
                    etRfrdby.requestLayout();
                }else if (type_of_visit.equalsIgnoreCase("")){
                    etTypeofVisit.setError("Provide type of visit!");
                    etTypeofVisit.requestLayout();
                }else if (clinic.equalsIgnoreCase("")){
                    Toast.makeText(AddPatientActivity.this,"Select clinic!",Toast.LENGTH_SHORT).show();
                }else if (medical_info.equalsIgnoreCase("")){
                    etMdclInfo.setError("Provide medical info!");
                    etMdclInfo.requestLayout();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    submitPatientDetails(first_name,last_name,logo,gender,dob,email,country_code,mobile_no,
                            address,height,weight,blood_group,refered_by,type_of_visit,clinic,medical_info,language);
                }

                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvLangs:
                Intent ii = new Intent(AddPatientActivity.this,LanguageActivity.class);
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

            case R.id.tvDob:
                openCalander();
                break;
        }
    }

    private void openCalander() {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddPatientActivity.this,  new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mcurrentDate.set(Calendar.YEAR, year);
                mcurrentDate.set(Calendar.MONTH, monthOfYear);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String selectDate = sdf.format(mcurrentDate.getTime());
                tvDob.setText(selectDate);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
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
                tvLangs.setText(result);
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

    private void getClinics() {

        String url = getResources().getString(R.string.getClinicsApi);
        String token = Comman.getPreferences(AddPatientActivity.this,"token");
        url = url+"?token="+token;

        String CLINICSHIT = "get_clinics_hit";
        VolleySingleton.getInstance(AddPatientActivity.this).cancelRequestInQueue(CLINICSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");

                    if (isSuccess){

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String hospital_id = object.getString("hospital_id");
                            String logo = object.getString("logo");
                            String logo_thumb = object.getString("logo_thumb");
                            String name = object.getString("name");
                            String address = object.getString("address");
                            String consultation_fees = object.getString("consultation_fees");

                            clinicItem = new ClinicItem(hospital_id,logo,logo_thumb,name,address,consultation_fees);
                            clinicItemArrayList.add(clinicItem);
                        }

                        clinicAdapter = new ClinicAdapter(AddPatientActivity.this,clinicItemArrayList);
                        spinClinics.setAdapter(clinicAdapter);

                        prgLoading.setVisibility(View.GONE);
                        rlMain.setVisibility(View.VISIBLE);

                    }else {
                        rlMain.setVisibility(View.GONE);
                        Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    rlMain.setVisibility(View.GONE);
                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                rlMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(CLINICSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddPatientActivity.this).addToRequestQueue(request);
    }

    private void submitPatientDetails(final String first_name, final String last_name, final Bitmap logo, final String gender,
                                      final String dob, final String email, final String country_code, final String mobile_no,
                                      final String address, final String height, final String weight, final String blood_group,
                                      final String refered_by, final String type_of_visit, final String clinic, final String medical_info,
                                      final JSONArray language) {

        String url = getResources().getString(R.string.sbmtPatientDtlsApi);
        String token = Comman.getPreferences(AddPatientActivity.this,"token");
        url = url+"?token="+token;

        String SBMTPTNTDTLSHIT = "sbmt_patntdtl_hit";
        VolleySingleton.getInstance(AddPatientActivity.this).cancelRequestInQueue(SBMTPTNTDTLSHIT);
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse result) {
                String resultResponse = new String(result.data);
                try {
                    JSONObject response  = new JSONObject(resultResponse);
                    boolean isSuccess = response.getBoolean("success");
                    String message = response.getString("message");

                    if (isSuccess){

                        etFName.setText("");
                        etLName.setText("");
                        etEmail.setText("");
                        etNumber.setText("");
                        etAdrs.setText("");
                        etHeight.setText("");
                        etWeight.setText("");
                        etRfrdby.setText("");
                        etTypeofVisit.setText("");
                        etMdclInfo.setText("");
                        tvAddlogo.setText("");
                        tvDob.setText("");
                        tvLangs.setText("");
                        myLanguagesItems.clear();
                        ivPrfl.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_b));

                        Comman.setPreferences(AddPatientActivity.this,"PatientAdded","1");
                        Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.addpatient),Toast.LENGTH_SHORT).show();
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(AddPatientActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.data!=null){
                    try {

                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(AddPatientActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddPatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name",first_name);
                params.put("last_name",last_name);
                params.put("email",email);
                if (gender.equalsIgnoreCase("Male")){
                    params.put("gender","1");
                }else {
                    params.put("gender","2");
                }
                params.put("dob",dob);
                params.put("language",language+"");
                params.put("country_code",country_code);
                params.put("mobile_no",mobile_no);
                params.put("address",address);
                params.put("height",height);
                params.put("weight",weight);
                params.put("blood_group",blood_group);
                params.put("refered_by",refered_by);
                params.put("type_of_visit",type_of_visit);
                params.put("clinic",clinic);
                params.put("medical_info",medical_info);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("profile_image", new DataPart("profile_image.jpg", getFileDataFromDrawable(getBaseContext(), logo), "image/jpeg"));
                return params;
            }
        };

        request.setTag(SBMTPTNTDTLSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddPatientActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
