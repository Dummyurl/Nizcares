package com.indglobal.nizcare.commons;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by indglobal on 5/6/17.
 */

public class Comman {

	public static Context mContext;
	String [] PERMISSIONS = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CALL_PHONE};

	public static boolean verifyStoragePermissions(Activity activity,String... permissions) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null) {

			for (String permission : permissions) {
				if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
					return false;
				}
			}
		}
		return true;
	}

	public static void setPreferences(Context context, String key, String value) {
		mContext = context;
		SharedPreferences.Editor editor = mContext.getSharedPreferences("WED_APP", Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getPreferences(Context context, String key) {
		mContext = context;
		SharedPreferences prefs = mContext.getSharedPreferences("WED_APP", Context.MODE_PRIVATE);
		String text = prefs.getString(key, "");
		return text;
	}

	public static void delPrefences(Context context){
		mContext = context;
		SharedPreferences prefs = mContext.getSharedPreferences("WED_APP", Context.MODE_PRIVATE);
		prefs.edit().clear().commit();


	}

	public static boolean isConnectionAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()
					&& netInfo.isConnectedOrConnecting()
					&& netInfo.isAvailable()) {
				return true;
			}
		}
		return false;
	}

	public static boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		//final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
				+"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
				+"@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}


    public static void hideKeyboard(Context context) {
        try{
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static boolean checkGpsAvailable(Context context) {
        LocationManager locationManager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);

        boolean isGPSEnabled = false;

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            return isGPSEnabled;
        }

        return false;
    }


	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	public static String capitalize(String capString){
		StringBuffer capBuffer = new StringBuffer();
		Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
		while (capMatcher.find()){
			capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
		}

		return capMatcher.appendTail(capBuffer).toString();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			}

			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
