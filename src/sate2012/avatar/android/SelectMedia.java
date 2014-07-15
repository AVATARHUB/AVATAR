package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;

import java.io.File;
import java.net.URI;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.android.gms.maps.model.LatLng;

/**
 * The Upload Menu Allows the user to select different media types to upload to
 * the server
 */
public class SelectMedia extends Activity implements OnClickListener {
	private File sd;
	private File storageFolder;
	private File mediaFolder;
	private ImageButton pictureB;
	private ImageButton videoB;
	private ImageButton audioB;
	private ImageButton commentB;
	private String dataType;
	private String media_filepath;
//	private String media_filename;
//	private String media_extension;
	private static String image_filepath;
	public static Context thisContext;
	public static String ptName;
	public static boolean isEmergency = false;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisContext = getApplicationContext();
		setContentView(R.layout.avatar_upload_menu);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		createStorageDirectory();
		pictureB = (ImageButton) findViewById(R.id.cameraButton);
		pictureB.setOnClickListener(this);
		
		videoB = (ImageButton) findViewById(R.id.videoButton);
		videoB.setOnClickListener(this);
		
		audioB = (ImageButton) findViewById(R.id.audioButton);
		audioB.setOnClickListener(this);
		
		commentB = (ImageButton) findViewById(R.id.commentButton);
		commentB.setOnClickListener(this);
		
//		emergency = (Button) findViewById(R.id.emergency);
//		emergency.setOnClickListener(this);

	}

	/**
	 * Responds to whatever button is pressed
	 * 
	 * @param v
	 *            v - the button clicked
	 */
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case (R.id.cameraButton):
			dataType = getResources().getString(R.string.type_picture);
			i = new Intent(SelectMedia.this, RecordPhoto.class);
			startActivityForResult(i, Constants.CAMERA_REQUEST);
			break;
		case (R.id.videoButton):
			dataType = getResources().getString(R.string.type_video);
			i = new Intent(SelectMedia.this, RecordVideo.class);
			startActivityForResult(i, Constants.VIDEO_REQUEST);
			break;
		case (R.id.audioButton):
			dataType = getResources().getString(R.string.type_audio);
			i = new Intent(SelectMedia.this, RecordAudio.class);
			startActivityForResult(i, Constants.VOICE_REQUEST);
			break;
		case (R.id.commentButton):
			dataType = getResources().getString(R.string.type_comment);
			i = new Intent(SelectMedia.this, RecordComment.class);
			startActivityForResult(i, Constants.COMMENT_REQUEST);
			break;
			
//			i = new Intent(getApplicationContext(), MailSenderActivity.class);
//			i.putExtra("Type", dataType);
//			Intent intent = getIntent();
//			i.putExtra("LatLng", intent.getParcelableExtra("LatLng"));
//			startActivity(i);
//			break;
//		case (R.id.emergency):
//			dataType = getResources().getString(R.string.type_emergency);
//			
//			Intent emergencyIntent = getIntent();
//
//			LatLng latlng = (LatLng) emergencyIntent.getParcelableExtra("LatLng");
//			HttpSender connect = new HttpSender();
//			connect.execute("EMERGENCY", latlng.latitude + "",
//					latlng.longitude + "", "0", "EMERGENCY");
//			UploadMedia.isEmergency = true;
//			finish();
//			break;
		}
	}

	/**
	 * Called when the individual activities (picture, video, audio) finish.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.VIDEO_REQUEST) 
				media_filepath = RecordVideo.getPath();
			if (requestCode == Constants.VOICE_REQUEST) 
				media_filepath = RecordAudio.getPath();
			if (requestCode == Constants.CAMERA_REQUEST) 
				media_filepath = RecordPhoto.getPath();
			if (requestCode == Constants.COMMENT_REQUEST) 
				media_filepath = RecordComment.getPath();
			
			Intent i = getIntent();
			
			UploadToFTP ftp = new UploadToFTP();
			ftp.execute(media_filepath);
			
			LatLng latlng = (LatLng) i.getParcelableExtra("LatLng");
			Intent MailIntent = new Intent(getApplicationContext(),
					UploadToSQL.class);
			MailIntent.putExtra("Type", dataType);
			MailIntent.putExtra("Filepath", media_filepath);
			MailIntent.putExtra("LatLng", latlng);
			startActivity(MailIntent);
			finish();
		}
	}

	public static void setImage_filepath(String fp) {
		image_filepath = fp;
	}

	public String getImage_filepath() {
		return image_filepath;
	}
	
	public static void setPtName(String name){
		ptName = name;
		System.out.println("NAME IS: " + ptName);
	}
	
	public static String getPtName(){
		return ptName;
	}

	@Override
	public void onDestroy() {
		finish();
		super.onDestroy();
	}

	public void createStorageDirectory() {
		sd = Environment.getExternalStorageDirectory();
		storageFolder = new File(sd, Constants.STORAGE_DIRECTORY);
		if (sd.canWrite()) {
			if (!storageFolder.exists())
				storageFolder.mkdir();
			mediaFolder = new File(sd, Constants.STORAGE_DIRECTORY
					+ Constants.MEDIA_DIRECTORY);
			if (!mediaFolder.exists())
				mediaFolder.mkdir();
		}
	}

	public static class HttpSender extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			params[0] = params[0].replaceAll(" ", "%20");
			params[4] = params[4].replaceAll(" ", "%20");
			try {
				HttpClient client = new DefaultHttpClient();
				File path = new File(params[4]);
				String fileName = path.getName();
				String url = "http://" + Constants.SERVER_ADDRESS + "/media/" + fileName;
				HttpGet get = new HttpGet(new URI(
						"http://" + Constants.SERVER_ADDRESS + "/scripts/sqlAddRow.php?name=" + params[0]
								+ "&lat=" + params[1] + "&long=" + params[2]
								+ "&alt=" + params[3] + "&url=" + url));
				System.out.println(
						"http://" + Constants.SERVER_ADDRESS + "/scripts/sqlAddRow.php?name=" + params[0]
								+ "&lat=" + params[1] + "&long=" + params[2]
								+ "&alt=" + params[3] + "&url=" + url);
				client.execute(get);
				System.out.println("THINGS ARE HAPPENING");
			} catch (Exception e) {
				System.out.println("SOMETHING WENT BOOM!");
				e.printStackTrace();
			}
			return null;
		}
	}
}
