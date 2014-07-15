package sate2012.avatar.android;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;

public class UploadToFTP extends AsyncTask<String, String, String> {

	public String doInBackground(String... params) {
		System.out.println("FTP START");
		FTPClient ftpClient = new FTPClient();
		Log.d("HELP", ftpClient.toString());
		String retVal = params[0];
		try {
			ftpClient.connect(Constants.SERVER_ADDRESS, 21);
//			ftpClient.enterLocalPassiveMode();
			ftpClient.login("loumcgu", "apple123");
//			System.out.println(ftpClient.getStatus());
//			System.out.println(ftpClient.printWorkingDirectory());
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			File file = new File(params[0]);
			String objectName = file.getName();
			InputStream input = new FileInputStream(file);
			ftpClient.storeFile(objectName, input);

			ftpClient.logout();
			ftpClient.disconnect();
			
		} catch (IOException e) {
			System.out.println("WHOOPS");
			e.printStackTrace();
		}
		System.out.println("COMPLETE LOOPER");
		return retVal;
	}
}