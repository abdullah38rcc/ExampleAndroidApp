package io.catalyze.android.example;

import io.catalyze.sdk.android.CatalyzeEntry;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This example shows how to edit and create CustomClass objects. Results are
 * returned to the calling activity.
 * 
 * @author uphoff
 * 
 */
public class CustomClassEditActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_class_edit);

		// For selecting custom class entry type on a new entry
		final Spinner classSpinner = (Spinner) this
				.findViewById(R.id.ccSelectSpinner);

		// Provides a mechanism for deleting existing custom class entries
		final Button deleteButton = (Button) this
				.findViewById(R.id.ccDeleteButton);

		// A custom class instance, if any, can be found in the extras of the Intent 
		// When missing indicates that this operation is for creating a new entry
		final CatalyzeEntry customClass = (CatalyzeEntry)this.getIntent()
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
            customClass.setClassName(this.getIntent().getStringExtra("className"));

			this.setTitle("Edit " + customClass.getClassName() + " Entry");
			classSpinner.setVisibility(View.GONE); // Don't need the class selector
			
			EditText jsonEditText = (EditText) this
					.findViewById(R.id.ccEditJsonTextView);

            // set the value of the text field to the current content of the custom class entry
            try {
                // first, try and pretty print it
                jsonEditText.setText(new JSONObject(customClass.getContent()).toString(2));
            } catch (JSONException je) {
                jsonEditText.setText(new JSONObject(customClass.getContent()).toString());
            }

			deleteButton.setVisibility(View.VISIBLE);
			deleteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("customClass", customClass); // The
																		// instance
																		// to
																		// delete
                    returnIntent.putExtra("className", customClass.getClassName());
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

                    // have to convert the JSON to a Map
                    Map<String, Object> map = new HashMap<String, Object>();
                    Iterator<String> iter = obj.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            map.put(key, obj.get(key));
                        } catch (JSONException je) {
                            System.out.println("Error converting text to Map at key " + key);
                        }
                    }

                    CatalyzeEntry entry = customClass;
					if (entry == null) {
                        entry = new CatalyzeEntry(
                                (String) classSpinner.getSelectedItem());
                    }
                    entry.setContent(map);
                    returnIntent.putExtra("customClass", entry);
                    returnIntent.putExtra("className", entry.getClassName());

					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}

		});
	}
}
