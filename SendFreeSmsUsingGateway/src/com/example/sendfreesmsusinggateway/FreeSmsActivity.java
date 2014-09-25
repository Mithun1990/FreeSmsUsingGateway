package com.example.sendfreesmsusinggateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FreeSmsActivity extends Activity {
	Button b1;
	EditText e1, e2;
	/** Bulk sms Register:: https://bulksms.vsms.net/register/ **/
	private static final String USER_NAME = "YOUR_BULK_SMS_USER_NAME";
	private static final String USER_PASSWORD = "YOUR_BULK_SMS_PASSWORD";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_sms);
		b1 = (Button) findViewById(R.id.button1);
		e1 = (EditText) findViewById(R.id.editText1);
		e2 = (EditText) findViewById(R.id.editText2);

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						sendSms();

					}
				}).start();

			}
		});
	}

	public void sendSms() {
		try {
			// Construct data
			String data = "";
			data += "username=" + URLEncoder.encode(USER_NAME, "ISO-8859-1");
			data += "&password="
					+ URLEncoder.encode(USER_PASSWORD, "ISO-8859-1");
			data += "&message="
					+ URLEncoder.encode(e1.getText().toString(), "ISO-8859-1");
			data += "&want_report=1";
			data += "&msisdn=" + e2.getText().toString();// relace with the
															// number

			// Send data
			URL url = new URL(
					"http://bulksms.vsms.net:5567/eapi/submission/send_sms/2/2.0");

			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				// Print the response output...
				System.out.println(line);
			}
			wr.close();
			rd.close();

			Thread th = new Thread(new Runnable() {
				private long startTime = System.currentTimeMillis();

				public void run() {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast toast = Toast.makeText(FreeSmsActivity.this,
									"Sms send successfully!",
									Toast.LENGTH_SHORT);

							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();

							e1.setText("");
							e2.setText("");
							e1.setHint("Enter Message");
							e2.setHint("Enter Number");
						}
					});

				}
			});
			th.start();

		} catch (Exception e) {
			Thread th = new Thread(new Runnable() {
				private long startTime = System.currentTimeMillis();

				public void run() {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast toast = Toast.makeText(FreeSmsActivity.this,
									"Sms send failed!", Toast.LENGTH_SHORT);

							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();

						}
					});

				}
			});
			th.start();
			e.printStackTrace();

		}
	}

}
