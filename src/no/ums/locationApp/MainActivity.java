package no.ums.locationApp;

import com.example.locationapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class MainActivity extends Activity {


	static String phoneNumber; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("UMS:BPH", "Calling main activity");
		final Context c = this.getBaseContext();
		final EditText editText = (EditText) findViewById(R.id.ph_num_edittext);
		editText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					phoneNumber = editText.getText().toString();
					Log.d("UMS:BPH", "reading information "
							+ editText.getText().toString());
					editText.clearFocus();
					editText.setText("");
					Intent locationIntent = new Intent(c.getApplicationContext(),
							LocationService.class);
					locationIntent.putExtra("phoneNumber", phoneNumber);
					startService(locationIntent);
//					Toast t = new Toast(c.getApplicationContext());
//					t.setText("Phone Number entered is"+phoneNumber);
//					t.setDuration(10);
//					t.show();
					Intent actionComplete = new Intent(c.getApplicationContext(), PhoneNumberCaptured.class);
					startActivity(actionComplete);
				}
				return false;
			}
		});
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static String getPhoneNumber(){
		return phoneNumber;
	}
}
