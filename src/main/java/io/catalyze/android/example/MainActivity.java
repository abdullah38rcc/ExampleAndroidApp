package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;
import io.catalyze.sdk.android.Umls;
import io.catalyze.sdk.android.UmlsResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

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
		final Button umlsButton = (Button) findViewById(R.id.mainUmlsButton);
		final Button umlsTestButton = (Button) findViewById(R.id.mainUmlsTestButton);
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

		umlsTestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                // lookup a predefined value just to see what kind of resuls we get
                Umls.valueLookup("city", "1581834", new CatalyzeListener<UmlsResult>() {
                    @Override
                    public void onError(CatalyzeException e) {
                        Toast.makeText(MainActivity.this,
                                "Umls value lookup failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(UmlsResult umlsResult) {
                        System.out.println("code: " + umlsResult.getCode());
                        System.out.println("descr: " + umlsResult.getDesc());
                    }
                });
                // do a search and see that we get a list of results back
                Umls.searchByKeyword("rxnorm", "Acetaminophen", new CatalyzeListener<List<UmlsResult>>() {
                    @Override
                    public void onError(CatalyzeException e) {
                        Toast.makeText(MainActivity.this,
                                "Umls search by keyword failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(List<UmlsResult> umlsResults) {
                        for (UmlsResult umlsResult : umlsResults) {
                            System.out.println("code: " + umlsResult.getCode());
                            System.out.println("descr: " + umlsResult.getDesc());
                        }
                    }
                });
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

		umlsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, UmlsActivity.class);
				startActivity(intent);
			}
		});
	}
}
