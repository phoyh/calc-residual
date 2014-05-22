package com.example.residual;

import android.app.Activity;
import android.os.Bundle;

public class FullscreenActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);
		//StandardFragment.onCreate(this.findViewById(R.id.fragment_main_standard));

	}

}
