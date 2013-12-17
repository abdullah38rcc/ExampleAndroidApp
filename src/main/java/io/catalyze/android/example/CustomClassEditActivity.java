package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CustomClass;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This example shows how to edit and create CustomClass objects. Results are
 * returned to the calling activity.
 * 
 * @author uphoff
 * 
 */
public class CustomClassEditActivity extends Activity {

	// Authenticated Catalyze instance (used for creating a new CustomClass instance)
	private Catalyze catalyze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_class_edit);

		// Operations using the Catalyze SDK need an authenticated Catalyze handle.
		// In this case we pass it along via the Intent
		catalyze = (Catalyze) getIntent().getSerializableExtra("catalyze");
		
		// Can't proceed without a Catalyze
		if (catalyze == null) {
			Toast.makeText(this, "No 'catalyze' provided. ", Toast.LENGTH_SHORT)
					.show();

			finish();
		}

		// For selecting custom class entry type on a new entry
		final Spinner classSpinner = (Spinner) this
				.findViewById(R.id.ccSelectSpinner);

		// Provides a mechanism for deleting existing custom class entries
		final Button deleteButton = (Button) this
				.findViewById(R.id.ccDeleteButton);

		// A custom class instance, if any, can be found in the extras of the Intent 
		// When missing indicates that this operation is for creating a new entry
		final CustomClass customClass = (CustomClass) this.getIntent()
				.getSerializableExtra("customClass");

		if (customClass == null) {
			this.setTitle("New Entry");

			// Set up the spinner to allow for the selection of a custom class
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_dropdown_item,
					MyApplication.CUSTOM_CLASS_NAMES);
			classSpinner.setAdapter(adapter);
			classSpinner.setVisibility(View.VISIBLE); 
			
			deleteButton.setVisibility(View.GONE); // Can't delete a new entry
		} else {
			this.setTitle("Edit " + customClass.getName() + " Entry");
			classSpinner.setVisibility(View.GONE); // Don't need the class selector
			
			EditText jsonEditText = (EditText) this
					.findViewById(R.id.ccEditJsonTextView);
			jsonEditText.setText(customClass.toString(2));
			deleteButton.setVisibility(View.VISIBLE);
			deleteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("customClass", customClass); // The
																		// instance
																		// to
																		// delete
					returnIntent.putExtra("delete", true); // Delete this on
															// return
					setResult(RESULT_OK, returnIntent);
					finish();
				}

			});
		}

		// Send back the instance in the extras and return RESULT_OK to the caller
		Button saveButton = (Button) this.findViewById(R.id.ccSaveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				EditText jsonEditText = (EditText) findViewById(R.id.ccEditJsonTextView);

				// Make sure the user's JSON is valid
				JSONObject obj = null;
				try {
					obj = new JSONObject(jsonEditText.getText().toString());
				} catch (JSONException jse) {
					Toast.makeText(CustomClassEditActivity.this,
							"Invalid JSON: " + jse.getMessage(),
							Toast.LENGTH_LONG).show();
				}

				if (obj != null) {
					// Send the updated/new custom class back to the calling
					// activity
					Intent returnIntent = new Intent();
					if (customClass == null) {
						CustomClass ccNew = catalyze.getCustomClassInstance(
								(String) classSpinner.getSelectedItem(), obj);
						returnIntent.putExtra("customClass", ccNew);
					} else {
						returnIntent.putExtra("customClass", customClass);
					}

					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}

		});
	}

}
