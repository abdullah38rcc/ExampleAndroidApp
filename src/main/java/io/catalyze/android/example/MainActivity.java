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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	Catalyze catalyze = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
				catalyze.signOut(handler);
			}
		});

		getUserButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						UserInfoActivity.class);
				intent.putExtra("catalyze", catalyze);
				startActivity(intent);

			}
		});

		umlsTestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UMLS u = catalyze.getUmlsInstance();
				u.valueLookup("city", "1581834", listenForUmlsResult());
				u.searchByKeyword("rxnorm", "Acetaminophen",
						listenForUmlsResults());
			}
		});

		customClassButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,
						CustomClassActivity.class);
				intent.putExtra("catalyze", catalyze);
				startActivity(intent);
			}
		});

		uploadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this,
						"Sorry file upload/download is not yet implemented in the SDK",
						Toast.LENGTH_SHORT).show();
			}
		});

		downloadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this,
						"Sorry file upload/download is not yet implemented in the SDK",
						Toast.LENGTH_SHORT).show();
			}

		});

		umlsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,
						UmlsActivity.class);
				startActivity(intent);
			}
		});
	}


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
				UmlsResult[] results = response;

			}
		};
	}

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
				Toast.makeText(MainActivity.this,
						"UMLS result: " + response.toString(2),
						Toast.LENGTH_SHORT).show();
			}
		};
	}

}
