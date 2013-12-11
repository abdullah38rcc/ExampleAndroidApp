package io.catalyze.android.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import io.catalyze.sdk.android.*;

public class LoginActivity extends Activity {    

	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);  

		final EditText userEditText = (EditText) findViewById(R.id.userNameTextField);
		final EditText passwordEditText = (EditText) findViewById(R.id.passwordTextField);
		final Button loginButton = (Button)findViewById(R.id.login);
		final Button signUpButton = (Button)findViewById(R.id.signUp);
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String userName = userEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				
				Catalyze.authenticate(userName, password, LoginActivity.this,
						new CatalyzeListener<Catalyze>() {

							@Override
							public void onError(CatalyzeException ce) {
								Toast.makeText(LoginActivity.this,
										"Login failed: " + ce.getMessage(), Toast.LENGTH_SHORT)
										.show();
								controlsEnabled(true);
							}    

							@Override
							public void onSuccess(Catalyze catalyze) {
								// Set the singleton Catalyze instance (there are other ways to do this!)
								MyApplication.catalyze = catalyze;
								
								Intent intent = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(intent);
								
								LoginActivity.this.finish();
							}

						});
				
				// Disable controls while authenticating in the background
				controlsEnabled(false);
			}
		});

		signUpButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String userName = userEditText.getText().toString();
						String password = passwordEditText.getText().toString();

						// TODO signup method should allow more fields? why
						// restrict?

						String firstName = "Bob";   
						String lastName = "Jones";

						Catalyze.signUp(userName, password, firstName,
								lastName, LoginActivity.this,
								new CatalyzeListener<Catalyze>() {

									@Override
									public void onError(CatalyzeException ce) {
										Toast.makeText(
												LoginActivity.this,
												"Signup failed: "
														+ ce.getMessage(), Toast.LENGTH_SHORT)
												.show();
										controlsEnabled(true);
									}   

									@Override
									public void onSuccess(Catalyze catalyze) {

										Intent intent = new Intent(
												LoginActivity.this,
												MainActivity.class);
										MyApplication.catalyze = catalyze;
										startActivity(intent);
										
										LoginActivity.this.finish();
									}

								});
						
						// Disable controls during signup
						controlsEnabled(false);
					}
					
				});

	}
	
	protected void controlsEnabled(boolean enabled) {
		EditText userEditText = (EditText) findViewById(R.id.userNameTextField);
		EditText passwordEditText = (EditText) findViewById(R.id.passwordTextField);
		Button loginButton = (Button)findViewById(R.id.login);
		Button signUpButton = (Button)findViewById(R.id.signUp);
		
		userEditText.setEnabled(enabled);
		passwordEditText.setEnabled(enabled);
		loginButton.setEnabled(enabled);
		signUpButton.setEnabled(enabled);		
	}

}
