package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;
import io.catalyze.sdk.android.CatalyzeUser;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Simple screen allowing the user's name to be updated.
 * 
 * @author uphoff
 * 
 */
public class UserInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		// Get the Catalyze instance from the intent and fail if it is not
		// present
		Catalyze catalyze = (Catalyze) getIntent().getSerializableExtra(
				"catalyze");
		if (catalyze == null) {
			Toast.makeText(this, "No 'catalyze' provided. ", Toast.LENGTH_SHORT)
					.show();

			finish();
		}

		final CatalyzeUser user = catalyze.getAuthenticatedUser();

		TextView userNameTextView = (TextView) findViewById(R.id.userUserNameTextView);
		userNameTextView.setText("User name: " + user.getUsername());

		final EditText firstName = (EditText) findViewById(R.id.userFirstNameEditText);
		firstName.setText(user.getFirstName());

		final EditText lastName = (EditText) findViewById(R.id.userLastNameEditText);
		lastName.setText(user.getLastName());

		final Button updateButton = (Button) findViewById(R.id.userUpdateButton);

		// Create a handler for updates to the backend
		final CatalyzeListener<CatalyzeUser> handler = new CatalyzeListener<CatalyzeUser>(
				this) {

			@Override
			public void onError(CatalyzeException ce) {
				Toast.makeText(UserInfoActivity.this,
						"Update user failed: " + ce.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(CatalyzeUser response) {
				Toast.makeText(UserInfoActivity.this,
						"User details updated successfully.",
						Toast.LENGTH_SHORT).show();
			}
		};

		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Update the fields in the instance
				// Changes are only local until update() is called and the
				// handler returns successfully.
				user.setFirstName(firstName.getText().toString());
				user.setLastName(lastName.getText().toString());

				// Update on the backend using the provided handler
				user.update(handler);
			}
		});
	}

}
