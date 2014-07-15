package tricorder.tecedge;

import sate2012.avatar.android.googlemaps.MapViewerTC;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Refreshm extends Activity {
	Mapm main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapViewerTC.h.sendEmptyMessage(0);
		finish();
	}

}
