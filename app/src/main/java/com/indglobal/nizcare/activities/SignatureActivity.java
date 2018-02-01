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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.indglobal.nizcare.adapters.BankAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleyMultipartRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.BankItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.indglobal.nizcare.commons.Comman.getFileDataFromDrawable;

/**
 * Created by readyassist on 2/1/18.
 */

public class SignatureActivity extends Activity implements RippleView.OnRippleCompleteListener{

    public ProgressBar prgLoading;
    RippleView rplBack,rplCapture,rplGallery,rplRemove,rplChange;
    RelativeLayout rlNotSign,rlSigned;
    ImageView ivSign;

    String[] PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.SET_ALARM, Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    String mCurrentPhotoPath="";
    Bitmap signature_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rplCapture = (RippleView)findViewById(R.id.rplCapture);
        rplGallery = (RippleView)findViewById(R.id.rplGallery);
        rplRemove = (RippleView)findViewById(R.id.rplRemove);
        rplChange = (RippleView)findViewById(R.id.rplChange);
        rlNotSign = (RelativeLayout)findViewById(R.id.rlNotSign);
        rlSigned = (RelativeLayout)findViewById(R.id.rlSigned);
        ivSign  = (ImageView)findViewById(R.id.ivSign);

        if (!Comman.isConnectionAvailable(SignatureActivity.this)){
            Toast.makeText(SignatureActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getSignature();
        }

        rplBack.setOnRippleCompleteListener(this);
        rplCapture.setOnRippleCompleteListener(this);
        rplGallery.setOnRippleCompleteListener(this);
        rplRemove.setOnRippleCompleteListener(this);
        rplChange.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplBack:
                onBackPressed();
                break;

            case R.id.rplCapture:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(SignatureActivity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(SignatureActivity.this,"Camera folder not found in your mobile please open your camera and take a pic!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (photoFile != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(SignatureActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 1);
                    }
                }

                break;

            case R.id.rplGallery:
                Intent intentGlry = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentGlry.setType("image/*");
                startActivityForResult(intentGlry, 2);
                break;

            case R.id.rplRemove:

                break;

            case R.id.rplChange:
                if (!Comman.verifyStoragePermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                }else {
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

        final Dialog mBottomSheetDialog = new Dialog (SignatureActivity.this, R.style.MaterialDialogSheet);
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
                if (intent.resolveActivity(SignatureActivity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(SignatureActivity.this,"Camera folder not found in your mobile please open your camera and take a pic!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (photoFile != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(SignatureActivity.this,
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

        if (requestCode==1){
            if (data!=null){
                Uri imguri = Uri.parse(mCurrentPhotoPath);

                if (!Comman.isConnectionAvailable(SignatureActivity.this)){
                    Toast.makeText(SignatureActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    signature_image = Comman.readBitmap(SignatureActivity.this, imguri, 0);
                    prgLoading.setVisibility(View.VISIBLE);
                    changeSignature(signature_image);
                }
            }
        }else if (requestCode == 2){
            if (data!=null){
                Uri imguri = data.getData();

                if (!Comman.isConnectionAvailable(SignatureActivity.this)){
                    Toast.makeText(SignatureActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    signature_image = Comman.readBitmap(SignatureActivity.this, imguri, 0);
                    prgLoading.setVisibility(View.VISIBLE);
                    changeSignature(signature_image);
                }
            }

        }
    }

    private void getSignature() {

        String url = getResources().getString(R.string.getSignApi);
        String token = Comman.getPreferences(SignatureActivity.this,"token");
        url = url+"?token="+token;

        String GETSIGNSHIT = "get_signs_hit";
        VolleySingleton.getInstance(SignatureActivity.this).cancelRequestInQueue(GETSIGNSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        JSONObject data = response.getJSONObject("data");
                        String media = data.getString("media");

                        Picasso.with(SignatureActivity.this).load(media).into(ivSign);

                        rlNotSign.setVisibility(View.GONE);
                        rlSigned.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        rlSigned.setVisibility(View.GONE);
                        rlNotSign.setVisibility(View.VISIBLE);
                        String message = response.getString("message");
                        Toast.makeText(SignatureActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(SignatureActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse.data!=null){
                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(SignatureActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(SignatureActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SignatureActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETSIGNSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(SignatureActivity.this).addToRequestQueue(request);
    }

    private void changeSignature(final Bitmap signature_image) {

        String url = getResources().getString(R.string.sbmtSignApi);
        String token = Comman.getPreferences(SignatureActivity.this,"token");
        url = url+"?token="+token;

        String SBMTSIGNATRHIT = "sbmt_signature_hit";
        VolleySingleton.getInstance(SignatureActivity.this).cancelRequestInQueue(SBMTSIGNATRHIT);
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse result) {
                String resultResponse = new String(result.data);
                try {
                    JSONObject response  = new JSONObject(resultResponse);
                    boolean isSuccess = response.getBoolean("success");
                    String message = response.getString("message");

                    if (isSuccess){

                        rlNotSign.setVisibility(View.GONE);
                        rlSigned.setVisibility(View.VISIBLE);
                        ivSign.setImageBitmap(signature_image);
                        Toast.makeText(SignatureActivity.this,message,Toast.LENGTH_SHORT).show();

                        prgLoading.setVisibility(View.GONE);


                    }else {
                        Toast.makeText(SignatureActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(SignatureActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(SignatureActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(SignatureActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignatureActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("signature_image", new DataPart("signature_image.jpg", getFileDataFromDrawable(getBaseContext(), signature_image), "image/jpeg"));
                return params;
            }
        };

        request.setTag(SBMTSIGNATRHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(SignatureActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
