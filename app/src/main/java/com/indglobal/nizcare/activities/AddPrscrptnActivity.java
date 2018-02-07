package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.AutoCmpleteAdapter;
import com.indglobal.nizcare.adapters.ClinicAdapter;
import com.indglobal.nizcare.adapters.DosageTypeAdapter;
import com.indglobal.nizcare.adapters.MedicineAdapter;
import com.indglobal.nizcare.adapters.TreatmentAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.DosageTypeItem;
import com.indglobal.nizcare.model.MedicineItem;
import com.indglobal.nizcare.model.MedicineNameItem;
import com.indglobal.nizcare.model.TreatmentItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by readyassist on 2/5/18.
 */

public class AddPrscrptnActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;
    RippleView rplBack,rplAdd,rplCancel;

    ScrollView scrlMain;
    ImageView ivPatient;
    TextView tvDone,tvName,tvGndrAge;
    LinearLayout llSelectPtnt,llNewPtnt,llBreakfast,llLunch,llDinner;
    Spinner spinDuratn,spinDosagetype,spinBDosage,spinLDosage,spinDDosage;
    AppCompatCheckBox cbBreakfast,cbLunch,cbDinner;
    RadioGroup rgBTimings,rgLTimings,rgDTimings;
    RadioButton rbBBefore,rbBAfter,rbBWith,rbLBefore,rbLAfter,rbLWith,rbDBefore,rbDAfter,rbDWith;
    EditText etMedcnName,etAddNote;
    RecyclerView rvMedcnName,rvMedicine;

    DosageTypeItem dosageTypeItem;
    ArrayList<DosageTypeItem> dosageTypeItemArrayList = new ArrayList<>();
    DosageTypeAdapter dosageTypeAdapter;


    String patient_id="",patient_name,patient_img,patient_age,patient_gender,medicine_id="",visit_date="",treatment="",patient_complaint="";
    public static boolean setTexted = false;

    private AutoCmpleteAdapter mAutoCompleteAdapter;
    ArrayList<MedicineNameItem> medicineNameItemArrayList = new ArrayList<>();
    MedicineNameItem medicineNameItem;

    String breakfast = "Before",bdosage="1",lunch = "Before",ldosage="1",dinner = "Before",ddosage="1";

    MedicineItem medicineItem;
    public static ArrayList<MedicineItem> medicineItemArrayList = new ArrayList<>();
    MedicineAdapter medicineAdapter;

    boolean isFromEdit = false;
    MedicineItem editItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_prscrptn_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rplAdd = (RippleView)findViewById(R.id.rplAdd);
        rplCancel = (RippleView)findViewById(R.id.rplCancel);
        scrlMain = (ScrollView) findViewById(R.id.scrlMain);
        llSelectPtnt = (LinearLayout)findViewById(R.id.llSelectPtnt);
        llNewPtnt = (LinearLayout)findViewById(R.id.llNewPtnt);
        llBreakfast = (LinearLayout)findViewById(R.id.llBreakfast);
        llLunch = (LinearLayout) findViewById(R.id.llLunch);
        llDinner = (LinearLayout) findViewById(R.id.llDinner);

        spinDuratn = (Spinner)findViewById(R.id.spinDuratn);
        spinDosagetype = (Spinner)findViewById(R.id.spinDosagetype);
        spinBDosage = (Spinner)findViewById(R.id.spinBDosage);
        spinLDosage = (Spinner)findViewById(R.id.spinLDosage);
        spinDDosage = (Spinner)findViewById(R.id.spinDDosage);
        ivPatient = (ImageView)findViewById(R.id.ivPatient);
        tvName = (TextView)findViewById(R.id.tvName);
        tvGndrAge = (TextView)findViewById(R.id.tvGndrAge);
        tvDone = (TextView)findViewById(R.id.tvDone);
        cbBreakfast = (AppCompatCheckBox)findViewById(R.id.cbBreakfast);
        cbLunch = (AppCompatCheckBox)findViewById(R.id.cbLunch);
        cbDinner = (AppCompatCheckBox)findViewById(R.id.cbDinner);
        rgBTimings = (RadioGroup)findViewById(R.id.rgBTimings);
        rgLTimings = (RadioGroup)findViewById(R.id.rgLTimings);
        rgDTimings = (RadioGroup)findViewById(R.id.rgDTimings);
        rbBBefore = (RadioButton)findViewById(R.id.rbBBefore);
        rbBAfter = (RadioButton)findViewById(R.id.rbBAfter);
        rbBWith = (RadioButton)findViewById(R.id.rbBWith);
        rbLBefore = (RadioButton)findViewById(R.id.rbLBefore);
        rbLAfter = (RadioButton)findViewById(R.id.rbLAfter);
        rbLWith = (RadioButton)findViewById(R.id.rbLWith);
        rbDBefore = (RadioButton)findViewById(R.id.rbDBefore);
        rbDAfter = (RadioButton)findViewById(R.id.rbDAfter);
        rbDWith = (RadioButton)findViewById(R.id.rbDWith);
        etMedcnName = (EditText)findViewById(R.id.etMedcnName);
        etAddNote = (EditText)findViewById(R.id.etAddNote);
        rvMedcnName = (RecyclerView)findViewById(R.id.rvMedcnName);
        rvMedicine = (RecyclerView)findViewById(R.id.rvMedicine);

        ArrayAdapter adapterDuration = ArrayAdapter.createFromResource(this, R.array.duration_array, R.layout.spinner_dropdown_item);
        adapterDuration.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinDuratn.setAdapter(adapterDuration);

        ArrayAdapter adapterDosage = ArrayAdapter.createFromResource(this, R.array.dosage_array, R.layout.spinner_dropdown_item);
        adapterDosage.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinBDosage.setAdapter(adapterDosage);
        spinLDosage.setAdapter(adapterDosage);
        spinDDosage.setAdapter(adapterDosage);

        if (!Comman.isConnectionAvailable(AddPrscrptnActivity.this)){
            scrlMain.setVisibility(View.GONE);
            Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getDosageType();
        }

        etMedcnName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                etMedcnName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etMedcnName.getText().length() > 1) {
                    if (setTexted){
                        if (mAutoCompleteAdapter != null) {
                            medicineNameItemArrayList.clear();
                            rvMedcnName.setAdapter(null);
                            rvMedcnName.invalidate();
                        }
                        setTexted = false;
                    }else {
                        if (!Comman.isConnectionAvailable(AddPrscrptnActivity.this)){
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(AddPrscrptnActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                        }else {
                            medicine_id="";
                            medicineSuggestion(etMedcnName.getText().toString());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    if (mAutoCompleteAdapter != null) {
                        medicineNameItemArrayList.clear();
                        rvMedcnName.setAdapter(null);
                        rvMedcnName.invalidate();
                    }
                }
            }

        });

        cbBreakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    llBreakfast.setVisibility(View.VISIBLE);
                }else {
                    llBreakfast.setVisibility(View.GONE);
                }
            }
        });

        cbLunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    llLunch.setVisibility(View.VISIBLE);
                }else {
                    llLunch.setVisibility(View.GONE);
                }
            }
        });

        cbDinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    llDinner.setVisibility(View.VISIBLE);
                }else {
                    llDinner.setVisibility(View.GONE);
                }
            }
        });

        rgBTimings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rbBBefore){
                    breakfast = "Before";
                }else if (checkedId==R.id.rbBAfter){
                    breakfast = "After";
                }else {
                    breakfast = "With";
                }
            }
        });

        rgLTimings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rbBBefore){
                    lunch = "Before";
                }else if (checkedId==R.id.rbBAfter){
                    lunch = "After";
                }else {
                    lunch = "With";
                }
            }
        });

        rgDTimings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rbBBefore){
                    dinner = "Before";
                }else if (checkedId==R.id.rbBAfter){
                    dinner = "After";
                }else {
                    dinner = "With";
                }
            }
        });

        rplBack.setOnRippleCompleteListener(this);
        tvDone.setOnClickListener(this);
        llSelectPtnt.setOnClickListener(this);
        llNewPtnt.setOnClickListener(this);
        rplAdd.setOnRippleCompleteListener(this);
        rplCancel.setOnRippleCompleteListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvDone:

                String prescription_note = etAddNote.getText().toString();

                if (!Comman.isConnectionAvailable(AddPrscrptnActivity.this)){
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (patient_id.equalsIgnoreCase("")){
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.slctpatnt),Toast.LENGTH_SHORT).show();
                }else if (medicineItemArrayList.size()==0){
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.addmedicines),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    JSONArray medicine_info = new JSONArray();
                    try{
                        for (int i=0;i<medicineItemArrayList.size();i++){
                            MedicineItem medicineItem = medicineItemArrayList.get(i);
                            JSONObject object = new JSONObject();
                            object.put("medicine_id",medicineItem.getMedicine_id());
                            object.put("duration",medicineItem.getDuration());
                            object.put("dosage_type_id",medicineItem.getDosageId());
                            object.put("breakfast",medicineItem.getBreakfast());
                            object.put("breakfast_dosage",medicineItem.getBdosage());
                            object.put("lunch",medicineItem.getLunch());
                            object.put("lunch_dosage",medicineItem.getLdosage());
                            object.put("dinner",medicineItem.getDinner());
                            object.put("dinner_dosage",medicineItem.getDdosage());

                            medicine_info.put(object);
                        }

                        addPrescription(patient_id,medicine_info,prescription_note);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.llSelectPtnt:
                Intent ii = new Intent(AddPrscrptnActivity.this,PatientActivity.class);
                startActivityForResult(ii,3);
                break;

            case R.id.llNewPtnt:
                Intent iiAddPtnt = new Intent(AddPrscrptnActivity.this,AddPatientActivity.class);
                startActivity(iiAddPtnt);
                break;
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplBack:
                onBackPressed();
                break;

            case R.id.rplAdd:
                String medicineName = etMedcnName.getText().toString();
                String duration = spinDuratn.getSelectedItem().toString();
                String dosageType = dosageTypeItemArrayList.get(spinDosagetype.getSelectedItemPosition()).getName();
                String dosageId = dosageTypeItemArrayList.get(spinDosagetype.getSelectedItemPosition()).getId();
                boolean isMorning = cbBreakfast.isChecked();
                boolean isAfternoon = cbLunch.isChecked();
                boolean isNight = cbDinner.isChecked();
                bdosage = (spinBDosage.getSelectedItemPosition()+1)+"";
                ldosage = (spinLDosage.getSelectedItemPosition()+1)+"";
                ddosage = (spinDDosage.getSelectedItemPosition()+1)+"";

                if (medicineName.equalsIgnoreCase("")){
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.prvdmedcnname),Toast.LENGTH_SHORT).show();
                }else if (!isMorning && !isAfternoon && !isNight){
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.prvdmdcntimeing),Toast.LENGTH_SHORT).show();
                }else {

                    medicineItem = new MedicineItem(medicine_id,medicineName,duration,dosageType,dosageId,
                            breakfast,bdosage,lunch,ldosage,dinner,ddosage,isMorning,isAfternoon,isNight);

                    if (medicineItemArrayList.size()==0){
                        medicineItemArrayList.add(medicineItem);
                        medicineAdapter = new MedicineAdapter(AddPrscrptnActivity.this,medicineItemArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        rvMedicine.setLayoutManager(layoutManager);
                        rvMedicine.setAdapter(medicineAdapter);
                    }else {
                        medicineItemArrayList.add(medicineItem);
                        medicineAdapter = new MedicineAdapter(AddPrscrptnActivity.this,medicineItemArrayList);
                        rvMedicine.invalidate();
                        rvMedicine.getAdapter().notifyItemInserted(medicineItemArrayList.size()-1);
                    }

                    etMedcnName.setText("");
                    setTexted = false;
                    spinDuratn.setSelection(0);
                    spinDosagetype.setSelection(0);
                    cbBreakfast.setChecked(false);
                    cbLunch.setChecked(false);
                    cbDinner.setChecked(false);
                    etAddNote.setText("");

                }
                break;

            case R.id.rplCancel:
                etMedcnName.setText("");
                setTexted = false;
                spinDuratn.setSelection(0);
                spinDosagetype.setSelection(0);
                cbBreakfast.setChecked(false);
                cbLunch.setChecked(false);
                cbDinner.setChecked(false);
                etAddNote.setText("");
                if (isFromEdit){
                    if (medicineItemArrayList.size()==0){
                        medicineItemArrayList.add(editItem);
                        medicineAdapter = new MedicineAdapter(AddPrscrptnActivity.this,medicineItemArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        rvMedicine.setLayoutManager(layoutManager);
                        rvMedicine.setAdapter(medicineAdapter);
                    }else {
                        medicineItemArrayList.add(editItem);
                        medicineAdapter = new MedicineAdapter(AddPrscrptnActivity.this,medicineItemArrayList);
                        rvMedicine.invalidate();
                        rvMedicine.getAdapter().notifyItemInserted(medicineItemArrayList.size()-1);
                    }
                }

                break;

        }
    }

    public void editMedicineItem(MedicineItem medicineItem) {
        isFromEdit = true;
        editItem = medicineItem;
        setTexted = true;
        etMedcnName.setText(medicineItem.getMedicine_name());
        medicine_id = medicineItem.getMedicine_id();

        List<String> durationArray = Arrays.asList(getResources().getStringArray(R.array.duration_array));
        int intexDuration = durationArray.indexOf(medicineItem.getDuration());
        spinDuratn.setSelection(intexDuration);

        for(int i=0;i<dosageTypeItemArrayList.size();i++){
            DosageTypeItem dosageTypeItem = dosageTypeItemArrayList.get(i);
            if ((dosageTypeItem.getId()).equalsIgnoreCase(medicineItem.getDosageId())){
                spinDosagetype.setSelection(i);
                break;
            }
        }

        cbBreakfast.setChecked(medicineItem.isMorning());
        if (medicineItem.isMorning()){
            spinBDosage.setSelection(Integer.parseInt(medicineItem.getBdosage())-1);
            switch (medicineItem.getBreakfast()){
                case "Before":
                    rbBBefore.setChecked(true);
                    break;
                case "After":
                    rbBAfter.setChecked(true);
                    break;
                case "With":
                    rbBWith.setChecked(true);
                    break;
            }
        }
        cbLunch.setChecked(medicineItem.isAfternoon());
        if (medicineItem.isAfternoon()){
            spinLDosage.setSelection(Integer.parseInt(medicineItem.getLdosage())-1);
            switch (medicineItem.getLunch()){
                case "Before":
                    rbLBefore.setChecked(true);
                    break;
                case "After":
                    rbLAfter.setChecked(true);
                    break;
                case "With":
                    rbLWith.setChecked(true);
                    break;
            }
        }
        cbDinner.setChecked(medicineItem.isNight());
        if (medicineItem.isNight()){
            spinDDosage.setSelection(Integer.parseInt(medicineItem.getDdosage())-1);
            switch (medicineItem.getDinner()){
                case "Before":
                    rbDBefore.setChecked(true);
                    break;
                case "After":
                    rbDAfter.setChecked(true);
                    break;
                case "With":
                    rbDWith.setChecked(true);
                    break;
            }
        }
    }

    private void getDosageType() {

        String url = getResources().getString(R.string.getDosageTypeApi);

        String DOSAGETYPEHIT = "get_dosagetype_hit";
        VolleySingleton.getInstance(AddPrscrptnActivity.this).cancelRequestInQueue(DOSAGETYPEHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");

                    if (isSuccess){

                        dosageTypeItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String dosage_type_id = object.getString("dosage_type_id");
                            String dasage_name = object.getString("dasage_name");

                            dosageTypeItem = new DosageTypeItem(dosage_type_id,dasage_name);
                            dosageTypeItemArrayList.add(dosageTypeItem);
                        }

                        dosageTypeAdapter = new DosageTypeAdapter(AddPrscrptnActivity.this,dosageTypeItemArrayList);
                        spinDosagetype.setAdapter(dosageTypeAdapter);

                        prgLoading.setVisibility(View.GONE);
                        tvDone.setVisibility(View.VISIBLE);
                        scrlMain.setVisibility(View.VISIBLE);

                    }else {
                        scrlMain.setVisibility(View.GONE);
                        Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    scrlMain.setVisibility(View.GONE);
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                scrlMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(DOSAGETYPEHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddPrscrptnActivity.this).addToRequestQueue(request);
    }

    private void medicineSuggestion(String text) {

        String url = getResources().getString(R.string.getMedicineNameApi);
        url = url+"?param="+text;

        String MDCNNAMEHIT = "get_medicinename_hit";
        VolleySingleton.getInstance(AddPrscrptnActivity.this).cancelRequestInQueue(MDCNNAMEHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");

                    if (isSuccess){

                        medicineNameItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String medicine_id = object.getString("medicine_id");
                            String medicine_name = object.getString("medicine_name");

                            medicineNameItem = new MedicineNameItem(medicine_id,medicine_name);
                            medicineNameItemArrayList.add(medicineNameItem);
                        }

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        mAutoCompleteAdapter = new AutoCmpleteAdapter(AddPrscrptnActivity.this, medicineNameItemArrayList, new AutoCmpleteAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(MedicineNameItem medicineNameItem) {
                                setTexted = true;
                                etMedcnName.setText(Comman.capitalize(medicineNameItem.getMedicine_name()));
                                medicine_id = medicineNameItem.getMedicine_id();
                                medicineNameItemArrayList.clear();
                                rvMedcnName.setAdapter(null);
                                rvMedcnName.invalidate();
                                Comman.hideKeyboard(AddPrscrptnActivity.this);

                            }
                        });
                        rvMedcnName.setLayoutManager(layoutManager);
                        rvMedcnName.setAdapter(mAutoCompleteAdapter);
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                scrlMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(MDCNNAMEHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddPrscrptnActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                patient_id = data.getStringExtra("patient_id");
                patient_name = data.getStringExtra("patient_name");
                patient_img = data.getStringExtra("patient_img");
                patient_age = data.getStringExtra("patient_age");
                patient_gender = data.getStringExtra("patient_gender");

                Picasso.with(AddPrscrptnActivity.this).load(patient_img).into(ivPatient);
                tvName.setText(Comman.capitalize(patient_name));
                tvGndrAge.setText(Comman.capitalize(patient_gender)+" * "+patient_age+" Years old");
                tvGndrAge.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addPrescription(final String patient_ids, JSONArray medicine_info, String prescription_note) {

        String url = getResources().getString(R.string.addPrscrptnApi);
        String token = Comman.getPreferences(AddPrscrptnActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("patient_id",patient_ids);
        params.put("medicine_info",medicine_info+"");
        params.put("prescription_note",prescription_note);

        String ADDPRSCRPTNHIT = "add_prscrptn_hit";
        VolleySingleton.getInstance(AddPrscrptnActivity.this).cancelRequestInQueue(ADDPRSCRPTNHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){

                        medicineItemArrayList.clear();
                        medicineAdapter = new MedicineAdapter(AddPrscrptnActivity.this,medicineItemArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        rvMedicine.setLayoutManager(layoutManager);
                        rvMedicine.setAdapter(medicineAdapter);

                        medicine_id="";
                        etMedcnName.setText("");
                        setTexted = false;
                        spinDuratn.setSelection(0);
                        spinDosagetype.setSelection(0);
                        cbBreakfast.setChecked(false);
                        cbLunch.setChecked(false);
                        cbDinner.setChecked(false);
                        etAddNote.setText("");
                    }

                    prgLoading.setVisibility(View.GONE);
                    Toast.makeText(AddPrscrptnActivity.this,msg,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AddPrscrptnActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddPrscrptnActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(ADDPRSCRPTNHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddPrscrptnActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
