package com.indglobal.nizcare.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.BuildConfig;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.CertificateAdapter;
import com.indglobal.nizcare.adapters.CityAdapter;
import com.indglobal.nizcare.adapters.DocTypeAdapter;
import com.indglobal.nizcare.adapters.DocumentAdapter;
import com.indglobal.nizcare.adapters.RegistrationAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleyMultipartRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.CertificateItem;
import com.indglobal.nizcare.model.CityItem;
import com.indglobal.nizcare.model.DoctypeItem;
import com.indglobal.nizcare.model.DocumentItem;
import com.indglobal.nizcare.model.RegistrationItem;
import com.indglobal.nizcare.model.SpecialityItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.indglobal.nizcare.commons.Comman.getFileDataFromDrawable;

/**
 * Created by readyassist on 12/13/17.
 */

public class AccountSetupActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;
    RelativeLayout rlCongrts,rlAcntStup;
    RippleView rplStrtStup,rplSubmit,rplBack,rplUpldCrtfcte,rplSave,rplDiscard,rplRegUpldCrtfcte,
            rplRegSave,rplRegDiscard,rplIduUpldCrtfcte,rplIduSave,rplIduDiscard;

    public static Spinner spinTitle,spinCity,spinExp,spinIdType;
    public static EditText etName,etEmail,etDegree,etClg,etYear,etRegno,etCouncil,etRegYear;
    public static ImageView ivPrfl;
    TextView tvAddlogo,tvSpeclty;
    RadioGroup rgGndr;
    RadioButton rbMale,rbFemale;
    RecyclerView rvEducation,rvRegistration,rvIDProof;

    CityItem cityItem;
    ArrayList<CityItem> cityItemArrayList = new ArrayList<>();
    CityAdapter cityAdapter;
    DoctypeItem doctypeItem;
    public static ArrayList<DoctypeItem> doctypeItemArrayList = new ArrayList<>();
    DocTypeAdapter docTypeAdapter;
    public static ArrayList<SpecialityItem> mySpecialityItems = new ArrayList<>();

    String mCurrentPhotoPath="",forWhich="1";
    public static Bitmap logo,certificate_image,registration_image,document_image;
    String gender="Male";

    CertificateItem certificateItem;
    public static ArrayList<CertificateItem> certificateItemArrayList = new ArrayList<>();
    CertificateAdapter certificateAdapter;
    RegistrationItem registrationItem;
    public static ArrayList<RegistrationItem> registrationItemArrayList = new ArrayList<>();
    RegistrationAdapter registrationAdapter;
    DocumentItem documentItem;
    public static ArrayList<DocumentItem> documentItemArrayList = new ArrayList<>();
    DocumentAdapter documentAdapter;

    String[] PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.SET_ALARM, Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acntsetup_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rlCongrts = (RelativeLayout)findViewById(R.id.rlCongrts);
        rlAcntStup = (RelativeLayout)findViewById(R.id.rlAcntStup);
        rplStrtStup = (RippleView)findViewById(R.id.rplStrtStup);
        rplSubmit = (RippleView)findViewById(R.id.rplSubmit);
        rplBack = (RippleView)findViewById(R.id.rplBack);

        spinTitle = (Spinner)findViewById(R.id.spinTitle);
        etName = (EditText)findViewById(R.id.etName);
        ivPrfl = (ImageView)findViewById(R.id.ivPrfl);
        tvAddlogo = (TextView)findViewById(R.id.tvAddlogo);
        rgGndr = (RadioGroup)findViewById(R.id.rgGndr);
        rbMale = (RadioButton)findViewById(R.id.rbMale);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        etEmail = (EditText)findViewById(R.id.etEmail);
        spinCity = (Spinner) findViewById(R.id.spinCity);
        tvSpeclty = (TextView)findViewById(R.id.tvSpeclty);
        spinExp = (Spinner)findViewById(R.id.spinExp);
        rvEducation = (RecyclerView)findViewById(R.id.rvEducation);
        etDegree = (EditText)findViewById(R.id.etDegree);
        etClg = (EditText)findViewById(R.id.etClg);
        etYear = (EditText)findViewById(R.id.etYear);
        rplUpldCrtfcte = (RippleView)findViewById(R.id.rplUpldCrtfcte);
        rplSave = (RippleView)findViewById(R.id.rplSave);
        rplDiscard = (RippleView)findViewById(R.id.rplDiscard);
        rvRegistration = (RecyclerView)findViewById(R.id.rvRegistration);
        etRegno = (EditText)findViewById(R.id.etRegno);
        etCouncil = (EditText)findViewById(R.id.etCouncil);
        etRegYear = (EditText)findViewById(R.id.etRegYear);
        rplRegUpldCrtfcte = (RippleView)findViewById(R.id.rplRegUpldCrtfcte);
        rplRegSave = (RippleView)findViewById(R.id.rplRegSave);
        rplRegDiscard = (RippleView)findViewById(R.id.rplRegDiscard);
        rvIDProof = (RecyclerView)findViewById(R.id.rvIDProof);
        spinIdType = (Spinner)findViewById(R.id.spinIdType);
        rplIduUpldCrtfcte = (RippleView)findViewById(R.id.rplIduUpldCrtfcte);
        rplIduSave = (RippleView)findViewById(R.id.rplIduSave);
        rplIduDiscard = (RippleView)findViewById(R.id.rplIduDiscard);

        ArrayAdapter adapterTitle = ArrayAdapter.createFromResource(this, R.array.prefix_array, R.layout.spinner_dropdown_item);
        adapterTitle.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinTitle.setAdapter(adapterTitle);

        ArrayAdapter adapterExp = ArrayAdapter.createFromResource(this, R.array.exp_array, R.layout.spinner_dropdown_item);
        adapterExp.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinExp.setAdapter(adapterExp);


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
        rplStrtStup.setOnRippleCompleteListener(this);
        rplSubmit.setOnRippleCompleteListener(this);
        tvSpeclty.setOnClickListener(this);
        tvAddlogo.setOnClickListener(this);
        rplUpldCrtfcte.setOnRippleCompleteListener(this);
        rplSave.setOnRippleCompleteListener(this);
        rplDiscard.setOnRippleCompleteListener(this);
        rplRegUpldCrtfcte.setOnRippleCompleteListener(this);
        rplRegSave.setOnRippleCompleteListener(this);
        rplRegDiscard.setOnRippleCompleteListener(this);
        rplIduUpldCrtfcte.setOnRippleCompleteListener(this);
        rplIduSave.setOnRippleCompleteListener(this);
        rplIduDiscard.setOnRippleCompleteListener(this);
        rplSubmit.setOnRippleCompleteListener(this);

    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplStrtStup:
                if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getDocuments();
                }
                break;

            case R.id.rplBack:
                onBackPressed();
                break;

            case R.id.rplUpldCrtfcte:
                if (!Comman.verifyStoragePermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                }else {
                    forWhich="2";
                    openEditPicBottomSheet();
                }
                break;

            case R.id.rplRegUpldCrtfcte:
                if (!Comman.verifyStoragePermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                }else {
                    forWhich="3";
                    openEditPicBottomSheet();
                }
                break;

            case R.id.rplSave:
                String degree = etDegree.getText().toString();
                String college = etClg.getText().toString();
                String year = etYear.getText().toString();

                if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (degree.length()<2){
                    etDegree.setError("Enter degree!");
                    etDegree.requestLayout();
                }else if (college.length()<2){
                    etClg.setError("Enter college name!");
                    etClg.requestLayout();
                }else if (year.length()<4){
                    etYear.setError("Enter college passout year!");
                    etYear.requestLayout();
                }else if (certificate_image==null){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvdclgcrtfct),Toast.LENGTH_SHORT).show();
                }else {
                    if (certificateItemArrayList.size()==0){
                        certificateItem = new CertificateItem(degree,college,year,certificate_image);
                        certificateItemArrayList.add(certificateItem);
                        certificateAdapter = new CertificateAdapter(AccountSetupActivity.this,certificateItemArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AccountSetupActivity.this);
                        rvEducation.setLayoutManager(layoutManager);
                        rvEducation.setAdapter(certificateAdapter);

                    }else {
                        certificateItem = new CertificateItem(degree,college,year,certificate_image);
                        certificateItemArrayList.add(certificateItem);
                        certificateAdapter = new CertificateAdapter(AccountSetupActivity.this,certificateItemArrayList);
                        rvEducation.invalidate();
                        rvEducation.getAdapter().notifyItemInserted(certificateItemArrayList.size()-1);
                    }

                    etDegree.setText("");
                    etClg.setText("");
                    etYear.setText("");
                    certificate_image=null;

                }
                break;

            case R.id.rplDiscard:
                etDegree.setText("");
                etClg.setText("");
                etYear.setText("");
                certificate_image=null;
                break;

            case R.id.rplRegSave:
                String registration_no = etRegno.getText().toString();
                String council_name = etCouncil.getText().toString();
                String regyear = etRegYear.getText().toString();

                if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (registration_no.length()<2){
                    etRegno.setError("Enter Registration number!");
                    etRegno.requestLayout();
                }else if (council_name.length()<2){
                    etCouncil.setError("Enter council name!");
                    etCouncil.requestLayout();
                }else if (regyear.length()<4){
                    etRegYear.setError("Enter registration year!");
                    etRegYear.requestLayout();
                }else if (registration_image==null){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvdregstn),Toast.LENGTH_SHORT).show();
                }else {
                    if (registrationItemArrayList.size()==0){
                        registrationItem = new RegistrationItem(registration_no,council_name,regyear,registration_image);
                        registrationItemArrayList.add(registrationItem);
                        registrationAdapter = new RegistrationAdapter(AccountSetupActivity.this,registrationItemArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AccountSetupActivity.this);
                        rvRegistration.setLayoutManager(layoutManager);
                        rvRegistration.setAdapter(registrationAdapter);

                    }else {
                        registrationItem = new RegistrationItem(registration_no,council_name,regyear,registration_image);
                        registrationItemArrayList.add(registrationItem);
                        registrationAdapter = new RegistrationAdapter(AccountSetupActivity.this,registrationItemArrayList);
                        rvRegistration.invalidate();
                        rvRegistration.getAdapter().notifyItemInserted(registrationItemArrayList.size()-1);
                    }

                    etRegno.setText("");
                    etCouncil.setText("");
                    etRegYear.setText("");
                    registration_image=null;

                }
                break;

            case R.id.rplRegDiscard:
                etRegno.setText("");
                etCouncil.setText("");
                etRegYear.setText("");
                registration_image=null;
                break;

            case R.id.rplIduUpldCrtfcte:
                if (!Comman.verifyStoragePermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                }else {
                    forWhich="4";
                    openEditPicBottomSheet();
                }
                break;

            case R.id.rplIduSave:
                String document_id = doctypeItemArrayList.get(spinIdType.getSelectedItemPosition()).getId();
                String document_name = doctypeItemArrayList.get(spinIdType.getSelectedItemPosition()).getName();
                if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (document_name.length()<2){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.doctypenot),Toast.LENGTH_SHORT).show();
                }else if (document_image==null){
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvddocs),Toast.LENGTH_SHORT).show();
                }else {
                    if (documentItemArrayList.size()==0){
                        documentItem = new DocumentItem(document_id,document_name,document_image);
                        documentItemArrayList.add(documentItem);
                        documentAdapter = new DocumentAdapter(AccountSetupActivity.this,documentItemArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AccountSetupActivity.this);
                        rvIDProof.setLayoutManager(layoutManager);
                        rvIDProof.setAdapter(documentAdapter);

                    }else {
                        documentItem = new DocumentItem(document_id,document_name,document_image);
                        documentItemArrayList.add(documentItem);
                        documentAdapter = new DocumentAdapter(AccountSetupActivity.this,documentItemArrayList);
                        rvIDProof.invalidate();
                        rvIDProof.getAdapter().notifyItemInserted(documentItemArrayList.size()-1);
                    }
                    spinIdType.setSelection(0);
                    document_image=null;
                }
                break;

            case R.id.rplIduDiscard:
                spinIdType.setSelection(0);
                document_image=null;
                break;

            case R.id.rplSubmit:
                try {
                    String title = spinTitle.getSelectedItem().toString();
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String city_id = cityItemArrayList.get(spinCity.getSelectedItemPosition()).getId();
                    JSONArray speciality_id = new JSONArray();
                    for (int i=0;i<mySpecialityItems.size();i++){
                        speciality_id.put(mySpecialityItems.get(i).getId());
                    }
                    String experience = spinExp.getSelectedItem().toString();
                    JSONArray education_details = new JSONArray();
                    for (int j=0;j<certificateItemArrayList.size();j++){
                        JSONObject object = new JSONObject();
                        object.put("degree",certificateItemArrayList.get(j).getDegree());
                        object.put("college",certificateItemArrayList.get(j).getCollege());
                        object.put("year",certificateItemArrayList.get(j).getYear());
                        object.put("certificate_image",Base64.encodeToString(getFileDataFromDrawable(AccountSetupActivity.this,certificateItemArrayList.get(j).getImage()),Base64.DEFAULT));
                        education_details.put(object);
                    }

                    JSONArray registration_details = new JSONArray();
                    for (int k=0;k<registrationItemArrayList.size();k++){
                        JSONObject object = new JSONObject();
                        object.put("registration_no",registrationItemArrayList.get(k).getRegno());
                        object.put("council_name",registrationItemArrayList.get(k).getCouncilname());
                        object.put("year",registrationItemArrayList.get(k).getYear());
                        object.put("registration_image",Base64.encodeToString(getFileDataFromDrawable(AccountSetupActivity.this,registrationItemArrayList.get(k).getImage()),Base64.DEFAULT));
                        registration_details.put(object);
                    }

                    JSONArray document_details = new JSONArray();
                    for (int l=0;l<documentItemArrayList.size();l++){
                        JSONObject object = new JSONObject();
                        object.put("document_id",documentItemArrayList.get(l).getDocument_id());
                        object.put("document_image",Base64.encodeToString(getFileDataFromDrawable(AccountSetupActivity.this,documentItemArrayList.get(l).getDocument_image()),Base64.DEFAULT));
                        document_details.put(object);
                    }


                    if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else if (name.equalsIgnoreCase("")){
                        etName.setError("Enter your name!");
                        etName.requestLayout();
                    }else if (!Comman.emailValidator(email)){
                        etEmail.setError("Enter proper email, id!");
                        etEmail.requestLayout();
                    }else if (logo==null){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvdimage),Toast.LENGTH_SHORT).show();
                    }else if (city_id.equalsIgnoreCase("")){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }else if (speciality_id.length()==0){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.slctspeclty),Toast.LENGTH_SHORT).show();
                    }else if (experience.equalsIgnoreCase("")){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvdexp),Toast.LENGTH_SHORT).show();
                    }else if (education_details.length()==0){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvedu),Toast.LENGTH_SHORT).show();
                    }else if (registration_details.length()==0){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvdregdtl),Toast.LENGTH_SHORT).show();
                    }else if (document_details.length()==0){
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.prvdocs),Toast.LENGTH_SHORT).show();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        submitAccountSetup(logo,title,name,email,city_id,speciality_id,experience,education_details,registration_details,document_details);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.novaliddata),Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvSpeclty:
                Intent ii = new Intent(AccountSetupActivity.this,SpecialityActivity.class);
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

        final Dialog mBottomSheetDialog = new Dialog (AccountSetupActivity.this, R.style.MaterialDialogSheet);
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
                if (intent.resolveActivity(AccountSetupActivity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(AccountSetupActivity.this,"Camera folder not found in your mobile please open your camera and take a pic!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (photoFile != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(AccountSetupActivity.this,
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
                tvSpeclty.setText(result);
            }
        }else if (requestCode==1){
            if (data!=null){
                Uri imguri = Uri.parse(mCurrentPhotoPath);

                if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                    Toast.makeText(AccountSetupActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    if (forWhich.equalsIgnoreCase("1")){
                        logo = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                        ivPrfl.setImageBitmap(logo);
                    }else if (forWhich.equalsIgnoreCase("2")){
                        certificate_image = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                    }else if (forWhich.equalsIgnoreCase("3")){
                        registration_image = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                    }else if (forWhich.equalsIgnoreCase("4")){
                        document_image = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                    }
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.imguploadscs),Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == 2){
            if (data!=null){
                Uri imguri = data.getData();

                if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                    Toast.makeText(AccountSetupActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    if (forWhich.equalsIgnoreCase("1")){
                        logo = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                        ivPrfl.setImageBitmap(logo);
                    }else if (forWhich.equalsIgnoreCase("2")){
                        certificate_image = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                    }else if (forWhich.equalsIgnoreCase("3")){
                        registration_image = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                    }else if (forWhich.equalsIgnoreCase("4")){
                        document_image = Comman.readBitmap(AccountSetupActivity.this, imguri, 0);
                    }
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.imguploadscs),Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void getDocuments() {

        String url = getResources().getString(R.string.getdoctypeApi);

        String DOCTYPEHIT = "docs_hit";
        VolleySingleton.getInstance(AccountSetupActivity.this).cancelRequestInQueue(DOCTYPEHIT);
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

                            String id = object.getString("document_id");
                            String name = object.getString("document_name");

                            doctypeItem = new DoctypeItem(id,name);
                            doctypeItemArrayList.add(doctypeItem);
                        }

                        docTypeAdapter = new DocTypeAdapter(AccountSetupActivity.this,doctypeItemArrayList);
                        spinIdType.setAdapter(docTypeAdapter);

                        if (!Comman.isConnectionAvailable(AccountSetupActivity.this)){
                            Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                        }else {
                            prgLoading.setVisibility(View.VISIBLE);
                            getCities();
                        }

                    }else {
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(DOCTYPEHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AccountSetupActivity.this).addToRequestQueue(request);
    }

    private void getCities() {

        String url = getResources().getString(R.string.getCitiesApi);

        String CITYHIT = "cities_hit";
        VolleySingleton.getInstance(AccountSetupActivity.this).cancelRequestInQueue(CITYHIT);
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

                            String id = object.getString("city_id");
                            String name = object.getString("city_name");
                            String state = object.getString("city_state");

                            cityItem = new CityItem(id,name,state);
                            cityItemArrayList.add(cityItem);
                        }

                        cityAdapter = new CityAdapter(AccountSetupActivity.this,cityItemArrayList);
                        spinCity.setAdapter(cityAdapter);

                        prgLoading.setVisibility(View.GONE);
                        rlCongrts.setVisibility(View.GONE);
                        rlAcntStup.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(CITYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AccountSetupActivity.this).addToRequestQueue(request);
    }

    private void submitAccountSetup(final Bitmap logo, final String title, final String name, final String email, final String city_id,
                                    final JSONArray speciality_id, final String experience, JSONArray education_details,
                                    final JSONArray registration_details, final JSONArray document_details) {

        String url = getResources().getString(R.string.sbmtDctrDtlsApi);
        String token = Comman.getPreferences(AccountSetupActivity.this,"token");
        url = url+"?token="+token;

        String SBMTDCTRDTLSHIT = "sbmt_doctrdtl_hit";
        VolleySingleton.getInstance(AccountSetupActivity.this).cancelRequestInQueue(SBMTDCTRDTLSHIT);
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse result) {
                String resultResponse = new String(result.data);
                try {
                    JSONObject response  = new JSONObject(resultResponse);
                    boolean isSuccess = response.getBoolean("success");
                    String message = response.getString("message");

                    if (isSuccess){

                        Toast.makeText(AccountSetupActivity.this,message,Toast.LENGTH_SHORT).show();
                        prgLoading.setVisibility(View.GONE);

                        Comman.setPreferences(AccountSetupActivity.this,"loggedIn","1");
                        Intent ii = new Intent(AccountSetupActivity.this,BaseActivity.class);
                        startActivity(ii);

                    }else {
                        Toast.makeText(AccountSetupActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AccountSetupActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AccountSetupActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title",title);
                params.put("name",name);
                params.put("email",email);
                if (gender.equalsIgnoreCase("Male")){
                    params.put("gender","1");
                }else {
                    params.put("gender","2");
                }
                params.put("city_id",city_id);
                params.put("speciality_id",speciality_id+"");
                params.put("experience",experience);
                params.put("registration_details",registration_details+"");
                params.put("document_details",document_details+"");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("profile_image", new DataPart("profile_image.jpg", getFileDataFromDrawable(getBaseContext(), logo), "image/jpeg"));
                return params;
            }
        };

        request.setTag(SBMTDCTRDTLSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AccountSetupActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
