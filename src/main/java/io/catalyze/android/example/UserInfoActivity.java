package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;
import io.catalyze.sdk.android.CatalyzeUser;
import io.catalyze.sdk.android.UMLS;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		Catalyze catalyze = (Catalyze)getIntent().getSerializableExtra("catalyze");
		
		final CatalyzeUser user = catalyze.getAuthenticatedUser();

		TextView userNameTextView = (TextView) findViewById(R.id.userUserNameTextView);
		userNameTextView.setText("User name: " + user.getUsername());
		
		final EditText firstName = (EditText) findViewById(R.id.userFirstNameEditText);
		firstName.setText(user.getFirstName());

		final EditText lastName = (EditText) findViewById(R.id.userLastNameEditText);
		lastName.setText(user.getLastName());

		final Button updateButton = (Button) findViewById(R.id.userUpdateButton);

		final CatalyzeListener<CatalyzeUser> handler = new CatalyzeListener<CatalyzeUser>(this) {

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
				user.setFirstName(firstName.getText().toString());
				user.setLastName(lastName.getText().toString());
				user.update(handler);
			}
		});
	}

}
