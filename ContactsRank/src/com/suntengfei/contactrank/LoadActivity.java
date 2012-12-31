package com.suntengfei.contactrank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);		
        new Handler().postDelayed(new Runnable() {
             public void run() {
              /* Create an Intent that will start the Main WordPress Activity. */
                 Intent mainIntent = new Intent(LoadActivity.this, ContactsRankActivity.class);
                 LoadActivity.this.startActivity(mainIntent);
                 LoadActivity.this.finish();
             }
        }, 1000); //1500 for release
	}
	
}
