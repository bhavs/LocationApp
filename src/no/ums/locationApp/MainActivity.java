package no.ums.locationApp;

import com.example.locationapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {
	
	public static String phoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("UMS:BPH", "Calling main activity");
		final EditText editText = (EditText) findViewById(R.id.ph_num_edittext);
		editText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					phoneNumber=editText.getText().toString();
					Log.d("UMS:BPH", "reading information "
							+ editText.getText().toString());
					editText.clearFocus();
					editText.setText("");
				}
				return false;
			}
		});
		Intent locationIntent = new Intent(this.getApplicationContext(),
				LocationService.class);
		startService(locationIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
