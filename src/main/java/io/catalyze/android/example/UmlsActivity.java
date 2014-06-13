package io.catalyze.android.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * UMLS autocomplete example.
 * 
 * @author uphoff
 */
public class UmlsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_umls);

        // copied from https://developers.google.com/places/training/autocomplete-android
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.umlsAutoCompleteTextView);
        Spinner spinner = (Spinner) findViewById(R.id.umlsCodeSetSpinner);
        autoCompleteTextView.setAdapter(new UmlsAutoCompleteAdapter(this, android.R.layout.simple_list_item_1, spinner));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String str = (String) adapterView.getItemAtPosition(position);
                Toast.makeText(UmlsActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });
	}
}
