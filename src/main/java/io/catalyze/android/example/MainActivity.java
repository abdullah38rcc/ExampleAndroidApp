package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Simple screen with button to trigger core functionality.
 * 
 * @author uphoff
 * 
 */
public class MainActivity extends Activity {

	// The authenticated connection to the backend. Passed via Intent.
	Catalyze catalyze = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get the Catalyze instance
		catalyze = Catalyze.getInstance(this);

		final Button logoutButton = (Button) findViewById(R.id.mainLogoutButton);
		final Button getUserButton = (Button) findViewById(R.id.mainGetUserButton);
		final Button customClassButton = (Button) findViewById(R.id.mainCustomClassesButton);
		final Button fileButton = (Button) findViewById(R.id.mainFileButton);

		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                CatalyzeListener<String> handler = new CatalyzeListener<String>() {
                    @Override
                    public void onError(CatalyzeException response) {
                        Toast.makeText(MainActivity.this,
                                "Log out failed: " + response.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        Intent intent = new Intent(MainActivity.this,
                                LoginActivity.class);
                        startActivity(intent);

                        MainActivity.this.finish();
                    }
                };

				// Sign out the user via the above handler.
				catalyze.signOut(handler);
			}
		});

		getUserButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Launch the user screen allowing the name information to be
				// changed.
				Intent intent = new Intent(MainActivity.this,
						UserInfoActivity.class);
				startActivity(intent);

			}
		});

		customClassButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// Launch the CustomClass screen
				Intent intent = new Intent(MainActivity.this,
						CustomClassActivity.class);
				startActivity(intent);
			}
		});

		fileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
                // Launch the FileManagement screen
                Intent intent = new Intent(MainActivity.this,
                        FileManagementActivity.class);
                startActivity(intent);
			}
		});
	}
}
