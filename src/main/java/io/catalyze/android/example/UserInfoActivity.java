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
 */
public class UserInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		// Get the Catalyze instance
		Catalyze catalyze = Catalyze.getInstance(this);

		final CatalyzeUser user = catalyze.getAuthenticatedUser();

		TextView userNameTextView = (TextView) findViewById(R.id.userUserNameTextView);
		userNameTextView.setText("User name: " + user.getUsername());

		final EditText firstName = (EditText) findViewById(R.id.userFirstNameEditText);
		firstName.setText(user.getName().getFirstName());

		final EditText lastName = (EditText) findViewById(R.id.userLastNameEditText);
		lastName.setText(user.getName().getLastName());

		final Button updateButton = (Button) findViewById(R.id.userUpdateButton);

		// Create a handler for updates to the backend
        final CatalyzeListener<CatalyzeUser> handler = new CatalyzeListener<CatalyzeUser>() {
            @Override
            public void onError(CatalyzeException response) {
                Toast.makeText(UserInfoActivity.this,
                        "Update user failed: " + response.getMessage(),
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
				user.getName().setFirstName(firstName.getText().toString());
				user.getName().setLastName(lastName.getText().toString());

				// Update on the backend using the provided handler
				user.update(handler);
			}
		});
	}

}
