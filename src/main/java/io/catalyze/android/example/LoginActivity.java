package io.catalyze.android.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import io.catalyze.sdk.android.*;

/**
 * A simple Activity that will log in an existing user or create a new one.
 * Launches MainActivity after a successful authentication or user creation
 * operation returns from the backend.
 * 
 * @author uphoff
 */
public class LoginActivity extends Activity {

    Catalyze catalyze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        catalyze = Catalyze.getInstance(this);

		final EditText userEditText = (EditText) findViewById(R.id.userNameTextField);
		final EditText passwordEditText = (EditText) findViewById(R.id.passwordTextField);
        final EditText inviteEditText = (EditText) findViewById(R.id.inviteTextField);
		final Button loginButton = (Button) findViewById(R.id.login);
		final Button signUpButton = (Button) findViewById(R.id.mainUmlsButton);
        final Button invitedButton = (Button) findViewById(R.id.invitedButton);

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String userName = userEditText.getText().toString();
				String password = passwordEditText.getText().toString();

				// To do anything we need an authenticated Catalyze instance
                catalyze.authenticate(userName, password, new CatalyzeListener<CatalyzeUser>() {
                    @Override
                    public void onError(CatalyzeException response) {
                        // Could be a bad user/password or a user that
                        // does not exists
                        Toast.makeText(LoginActivity.this,
                                "Login failed: " + response.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        controlsEnabled(true);
                    }

                    @Override
                    public void onSuccess(CatalyzeUser response) {
                        // The user is authenticated.
                        // Send the instance to the main screen.

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

		signUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String userName = userEditText.getText().toString();
				final String password = passwordEditText.getText().toString();
                final String inviteCode = inviteEditText.getText().toString();

				String firstName = "Bob";
				String lastName = "Jones";

				// Sign up a new user and log in in one shot.
                catalyze.signUp(userName, password, firstName, lastName, userName, inviteCode, new CatalyzeListener<CatalyzeUser>() {
                    @Override
                    public void onError(CatalyzeException response) {
                        Toast.makeText(LoginActivity.this,
                                "Signup failed: " + response.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        controlsEnabled(true);
                    }

                    @Override
                    public void onSuccess(CatalyzeUser response) {
                        // Created and not logged in. Login and show main activity

                        catalyze.authenticate(userName, password, new CatalyzeListener<CatalyzeUser>() {
                            @Override
                            public void onError(CatalyzeException e) {
                                Toast.makeText(LoginActivity.this,
                                        "Signin failed: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                controlsEnabled(true);
                            }

                            @Override
                            public void onSuccess(CatalyzeUser catalyzeUser) {
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);

                                LoginActivity.this.finish();
                            }
                        });
                    }
                });

				// Disable controls during signup
				controlsEnabled(false);
			}

		});

        invitedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteEditText.setVisibility(View.VISIBLE);
            }
        });

	}

	/**
	 * Turn the controls on/off.
	 * 
	 * @param enabled
	 *            If the controls should be turned on (true) or off (false)
	 */
	protected void controlsEnabled(boolean enabled) {
		EditText userEditText = (EditText) findViewById(R.id.userNameTextField);
		EditText passwordEditText = (EditText) findViewById(R.id.passwordTextField);
		Button loginButton = (Button) findViewById(R.id.login);
		Button signUpButton = (Button) findViewById(R.id.mainUmlsButton);

		userEditText.setEnabled(enabled);
		passwordEditText.setEnabled(enabled);
		loginButton.setEnabled(enabled);
		signUpButton.setEnabled(enabled);
	}

}
