package sate2012.avatar.android;

import java.io.File;
import com.google.android.gms.maps.model.LatLng;

import gupta.ashutosh.avatar.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

public class UploadToSQL extends Activity implements OnClickListener {
	//Fields in xml file
	public EditText etPTName;
	public EditText etPTUrl;
	
	//Information from SelectMedia.java
	public String ptType;
	public String ptFilePath;
	public LatLng ptlatlng; 
	public String ptFinalName;
	
	//Button
	private Button btSend;
	private Button btBack;
	
	private File mediaFile;
	private String fileName;
	private String fileNameOnFTP;
	
	@SuppressWarnings("unused")
	private String ptLatBackup;
	@SuppressWarnings("unused")
	private String ptLongBackup;
	
	private Context c;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = super.getApplicationContext();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		ptLat = Constants.lat;
//		ptLng = Constants.lng;
		final LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				(long) 2000, (float) 1.0, mlocListener);
		
		setContentView(R.layout.avatar_media_sqlupload);
		
		Intent thisIntent = getIntent();
		ptType = thisIntent.getStringExtra("Type");
		ptFilePath = thisIntent.getStringExtra("Filepath");
		ptlatlng = thisIntent.getParcelableExtra("LatLng");
		
		mediaFile = new File(ptFilePath);
		fileName = mediaFile.getName();
		fileNameOnFTP = "http://" + Constants.SERVER_ADDRESS + "/media/" + fileName;
		
		etPTName = (EditText) findViewById(R.id.pointName);
		etPTName.setText(fileName);
		etPTUrl = (EditText) findViewById(R.id.pointUrl);
		etPTUrl.setText(fileNameOnFTP);
		
		btSend = (Button) findViewById(R.id.Send);
		btSend.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.Send):
			Toast.makeText(getApplicationContext(),
					"Sending, it may take a little while", Toast.LENGTH_LONG)
					.show();
			try {
				InputMethodManager inputManager = (InputMethodManager) c
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
				SelectMedia.HttpSender httpSender = new SelectMedia.HttpSender();
				ptFinalName = etPTName.getText().toString();
				httpSender.execute(ptFinalName, ptlatlng.latitude + "", 
						ptlatlng.longitude + "", "0", etPTUrl.getText().toString());
				
				setContentView(R.layout.avatar_sent);
				btBack = (Button) findViewById(R.id.Back);
				btBack.setOnClickListener(this);
			} catch (Exception e) {
				System.out.println("EXCEPTION: " + e);
				e.printStackTrace();
				setContentView(R.layout.avatar_send_failed);
			}
			break;
		case (R.id.Back):
			System.out.println("Exiting");
			finish();
			break;
		}
	}
	
	public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }
	
	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			Toast.makeText(c, "Getting Location.", Toast.LENGTH_SHORT).show();
			ptLatBackup = "" + loc.getLatitude();
			ptLongBackup = "" + loc.getLongitude();
		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(c, "GPS Disabled", Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(c, "GPS Enabled", Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}