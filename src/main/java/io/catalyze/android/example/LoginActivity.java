package io.catalyze.android.example;

import android.app.Activity;
import android.content.Intent;
//import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
//import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;

import android.widget.Toast;
import io.catalyze.sdk.android.*;
import io.catalyze.sdk.android.user.Gender;
import io.catalyze.sdk.android.user.ZipCode;

public class LoginActivity extends Activity {
	
	//private Catalyze catalyze;
	private static final String API_KEY = "1f077962-18cc-4ade-8075-d9fa1642f316";
	//private Object synchObj = new Object();										
	private Query query;
	private static final String identifier = "android.example";
	private CatalyzeUser otherUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		
		
		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText userEditText = (EditText) findViewById(R.id.userNameTextField);
				String userName = userEditText.getText().toString();
				EditText passwordEditText = (EditText) findViewById(R.id.passwordTextField);
				String password = passwordEditText.getText().toString();
				
				// Create a Catalyze instance based on provided credentials
				Catalyze tempCatalyze = new Catalyze(MyApplication.API_KEY,
						MyApplication.IDENTIFIER, LoginActivity.this);
				
				tempCatalyze.authenticate(userName, password, new CatalyzeListener<CatalyzeUser>() {

					@Override
					public void onError(CatalyzeError arg0) {
						// User/pass failure, reprompt
					}

					@Override
					public void onSuccess(CatalyzeUser catalyzeUser) {
						
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						// Pass the logged in Catalyze instance
						MyApplication.catalyze = catalyzeUser.getCatalyze();
						startActivity(intent);
					}
					
				});
				
			}
		});

		
		findViewById(R.id.signUp).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText userEditText = (EditText) findViewById(R.id.userNameTextField);
						String userName = userEditText.getText().toString();
						EditText passwordEditText = (EditText) findViewById(R.id.passwordTextField);
						String password = passwordEditText.getText().toString();
						
						// TODO signup method should allow more fields? why restrict?
						
						String firstName = "Bob";
						String lastName = "Jones";
								
						Catalyze tempCatalyze = new Catalyze(MyApplication.API_KEY,
								MyApplication.IDENTIFIER, LoginActivity.this);
					
						tempCatalyze.signUp(userName, password, firstName, lastName, new CatalyzeListener<CatalyzeUser>() {

							@Override
							public void onError(CatalyzeError arg0) {
								// User/pass failure, reprompt
							}
  
							@Override
							public void onSuccess(CatalyzeUser catalyzeUser) {
								
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								MyApplication.catalyze = catalyzeUser.getCatalyze();
								startActivity(intent);
							}
							
						});
						
					}
				});
		
	

	}

	
}
