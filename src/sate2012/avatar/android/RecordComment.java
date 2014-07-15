package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class RecordComment extends Activity implements View.OnClickListener {
	private EditText etContents;
	private static String OUTPUT_FILE = System.currentTimeMillis() + "_T.txt";
	private static File txtFile = new File(Environment.getExternalStorageDirectory() + 
			Constants.STORAGE_DIRECTORY + Constants.MEDIA_DIRECTORY, OUTPUT_FILE);
	private Button ibSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avatar_comment);
		etContents = (EditText) findViewById(R.id.pointCont);
		ibSave = (Button) findViewById(R.id.Save);
		ibSave.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.Save):
				try {
					FileWriter writer = new FileWriter(txtFile);
					writer.append(etContents.getText().toString());
					writer.flush();
					writer.close();
					
					Intent data = new Intent();
					setResult(Activity.RESULT_OK, data);
					finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
		}
	}

	public static String getPath() {
		return txtFile.getAbsolutePath();
	}
}