package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;
import io.catalyze.sdk.android.CatalyzeUser;
import io.catalyze.sdk.android.UMLS;
import io.catalyze.sdk.android.UmlsResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

		// Get the Catalyze instance from the intent and fail if it is not
		// present
		catalyze = (Catalyze) getIntent().getSerializableExtra("catalyze");
		if (catalyze == null) {
			Toast.makeText(this, "No 'catalyze' provided. ", Toast.LENGTH_SHORT)
					.show();

			finish();
		}

		final Button logoutButton = (Button) findViewById(R.id.mainLogoutButton);
		final Button getUserButton = (Button) findViewById(R.id.mainGetUserButton);
		final Button umlsButton = (Button) findViewById(R.id.mainUmlsButton);
		final Button umlsTestButton = (Button) findViewById(R.id.mainUmlsTestButton);
		final Button customClassButton = (Button) findViewById(R.id.mainCustomClassesButton);
		final Button uploadButton = (Button) findViewById(R.id.mainFileUploadButton);
		final Button downloadButton = (Button) findViewById(R.id.mainFileDownloadButton);

		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalyzeListener<CatalyzeUser> handler = new CatalyzeListener<CatalyzeUser>(
						MainActivity.this) {

					@Override
					public void onError(CatalyzeException error) {
						Toast.makeText(MainActivity.this,
								"Log out failed: " + error.getMessage(),
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onSuccess(CatalyzeUser response) {
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
				intent.putExtra("catalyze", catalyze);
				startActivity(intent);

			}
		});

		umlsTestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Do a couple quick UMLS lookups
				UMLS u = catalyze.getUmlsInstance();

				// Lookup city information
				u.valueLookup("city", "1581834", listenForUmlsResult());

				// Lookup clinical drug information
				u.searchByKeyword("rxnorm", "Acetaminophen",
						listenForUmlsResults());
			}
		});

		customClassButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// Launch the CustomClass screen
				Intent intent = new Intent(MainActivity.this,
						CustomClassActivity.class);
				intent.putExtra("catalyze", catalyze);
				startActivity(intent);
			}
		});

		uploadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// A future version will support this operation
				Toast.makeText(
						MainActivity.this,
						"Sorry file upload/download is not yet implemented in the SDK",
						Toast.LENGTH_SHORT).show();
			}
		});

		downloadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// A future version will support this operation
				Toast.makeText(
						MainActivity.this,
						"Sorry file upload/download is not yet implemented in the SDK",
						Toast.LENGTH_SHORT).show();
			}

		});

		umlsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// UMLS autocomplete needs some more UI work
				Toast.makeText(
						MainActivity.this,
						"Sorry the UMLS autocomplete example is not ready in the app.",
						Toast.LENGTH_SHORT).show();
				// Intent intent = new Intent(MainActivity.this,
				// UmlsActivity.class);
				// startActivity(intent);
			}
		});
	}

	/**
	 * Helper method for generating a UMLS callback.
	 * 
	 * @return The callback handler
	 */
	private CatalyzeListener<UmlsResult[]> listenForUmlsResults() {
		return new CatalyzeListener<UmlsResult[]>(MainActivity.this) {

			@Override
			public void onError(CatalyzeException ce) {
				Toast.makeText(MainActivity.this,
						"UMLS operation failed: " + ce.getMessage(),
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(UmlsResult[] response) {
				// Just log these search results
				for (UmlsResult result : response) {
					Log.i("Catallyze", result.toString());
				}
			}
		};
	}

	/**
	 * Helper method for generating a UMLS callback.
	 * 
	 * @return The callback handler
	 */
	private CatalyzeListener<UmlsResult> listenForUmlsResult() {
		return new CatalyzeListener<UmlsResult>(MainActivity.this) {

			@Override
			public void onError(CatalyzeException ce) {
				Toast.makeText(MainActivity.this,
						"UMLS operation failed: " + ce.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(UmlsResult response) {
				// Display the result of a lookup
				Toast.makeText(MainActivity.this,
						"UMLS result: " + response.toString(2),
						Toast.LENGTH_SHORT).show();
			}
		};
	}

}
