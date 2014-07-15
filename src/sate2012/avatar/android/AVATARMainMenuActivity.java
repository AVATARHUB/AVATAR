package sate2012.avatar.android;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;

import gupta.ashutosh.avatar.R;
import sate2012.avatar.android.googlemaps.MapViewer;
import sate2012.avatar.android.googlemaps.MapViewerTC;
import sate2012.avatar.android.googlemaps.augmentedreality.CameraView;
import DialogFragments.PANICDialogFragment;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * The main menu of the AVATAR Application This menu navigates users to
 * different parts of the program.
 */
public class AVATARMainMenuActivity extends Activity implements OnClickListener {
	
	/*
	 * Button that switches to a menu that lets the user upload different types 
	 * of media
	 */
//	private Button uploadB;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.avatar_main);
		
		FragmentManager fragMgr = getFragmentManager();
		
		FragmentTransaction xact = fragMgr.beginTransaction();

		Bundle bundle = new Bundle();
		bundle.putInt("MAP_TYPE", 1);
		MapViewer tempMap = new MapViewer();
		tempMap.setArguments(bundle);
		xact.add(R.id.container, tempMap, "AVATAR_MAP");
		
		xact.add(R.id.menu, new Frag(R.layout.map_menu_frag), "MENU");
		xact.commit();
		
		
		
		//Snippet to check that the applciation can connect to the Google Play Services
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
//		if (resultCode == ConnectionResult.SUCCESS) {
////		    this.selectMap();
//		} else if (resultCode == ConnectionResult.SERVICE_MISSING ||
//		           resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
//		           resultCode == ConnectionResult.SERVICE_DISABLED) {
//		    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
//		    dialog.show();
//		}
	}

	/**
	 * Called when a button is clicked by the user. Navigates to the appropriate
	 * Activity
	 * 
	 * @param v
	 *            v - the button clicked
	 */
	public void myClickMethod(View v) {
		getActionBar().show();
//		Intent i;
		FragmentManager fragMgr;
		FragmentTransaction xact;
		switch (v.getId()) {
		case R.id.map:
			fragMgr = getFragmentManager();
			xact = fragMgr.beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putInt("MAP_TYPE", 1);
			MapViewer tempMap;
			
			if(fragMgr.findFragmentByTag("AVATAR_MAP") != null){
				tempMap = (MapViewer) fragMgr.findFragmentByTag("AVATAR_MAP");
				xact.detach(tempMap);
			}
			tempMap = new MapViewer();
			xact.replace(R.id.container,tempMap, "AVATAR_MAP");
			xact.replace(R.id.menu, fragMgr.findFragmentByTag("MENU"), "MENU");
			xact.addToBackStack(null);
			tempMap.setArguments(bundle);
			xact.commit();
			break;
		case R.id.augmentedReality:
			fragMgr = getFragmentManager();
			xact = fragMgr.beginTransaction();
			if(fragMgr.findFragmentByTag("AVATAR_AUGMENTED_REALITY") != null){
				xact.replace(R.id.container, fragMgr.findFragmentByTag("AVATAR_AUGMENTED_REALITY"), "AVATAR_AUGMENTED_REALITY");
			}else{
				xact.replace(R.id.container, new CameraView(), "AVATAR_AUGMENTED_REALITY");
			}
			if(fragMgr.findFragmentByTag("AUG_MENU") != null){
				xact.replace(R.id.menu, fragMgr.findFragmentByTag("AUG_MENU"));
			}else{
				xact.replace(R.id.menu, new Frag(R.layout.augmented_reality_menu_frag), "AUG_MENU");
				xact.addToBackStack(null);
			}
			xact.commit();
			break;
		case R.id.emergencyCall:
			fragMgr = getFragmentManager();
			DialogFragment dialog = new PANICDialogFragment();
			dialog.show(fragMgr, "PANIC");
			break;
		case R.id.exit:
			this.finish();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		//When button was clicked.
		
	}
	
	/**
	 * Called when the Tricorder button is pressed
	 * 
	 * @param v
	 * 			The button clicked.
	 */
	public void tricorderOnClick(View v){
		FragmentManager fragMgr = getFragmentManager();
		
		((MapViewerTC)fragMgr.findFragmentByTag("TRICORDER_MAP")).tricorderOnClick(v);
	}

}
