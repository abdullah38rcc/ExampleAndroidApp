package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;
import io.catalyze.sdk.android.CatalyzeEntry;
import io.catalyze.sdk.android.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

/**
 * An example of how to Query, update and add entries to CustomClasses.
 * 
 * @author uphoff
 * 
 */
public class CustomClassActivity extends Activity {


	// Handles data inside the ExpandableListView
	private ExpandableListAdapter listAdapter;

	// Map of child elements (custom classes) to display
	private HashMap<String, List<CatalyzeEntry>> listDataChild = new HashMap<String, List<CatalyzeEntry>>();

	// Number of custom class operations left to perform
	private int fetchCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_class);

		// Launches the custom class entry Activity to create a new instance
		Button addButton = (Button) this.findViewById(R.id.ccAddEntry);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CustomClassActivity.this,
						CustomClassEditActivity.class);
				startActivityForResult(intent, 1);
			}

		});

		// Query the backend to get custom class information
		fetchCustomClassInformation();

	}

	/**
	 * Example method showing how multiple Query requests can be made
	 * simultaneously using the integrated Volley request queue.
	 * 
	 * Used to populate the ExapndableListView. 
	 */
	private synchronized void fetchCustomClassInformation() {

		if (fetchCount > 0) {
			// Another fetch is in progress
			return;
		}

		// Need to make this many calls to the backend and wait for their return
		this.fetchCount = MyApplication.CUSTOM_CLASS_NAMES.length;

		// Fire off one Query per class. This will be done concurrently if
		// possible via the Volley networking library. Results may be returned
		// in any order so be careful about the assumptions you make.
		for (final String className : MyApplication.CUSTOM_CLASS_NAMES) {
			// Create a new Query for this custom class
			// No criteria are specified so it will return any entry
			Query ccQuery = new Query(className);
			ccQuery.setPageSize(5); // Max of 5 entries returned
            ccQuery.setField("parentId");
            ccQuery.setSearchBy(Catalyze.getInstance(this).getAuthenticatedUser().getUsersId());

			// Add the query to the networking queue (Volley).
			ccQuery.executeQuery(new CatalyzeListener<List<CatalyzeEntry>>() {
                @Override
                public void onError(CatalyzeException e) {
                    Toast.makeText(CustomClassActivity.this,
                            "Query failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    // Add any empty entry as a placeholder
                    addToExpandableListView(className, new ArrayList<CatalyzeEntry>());
                }

                @Override
                public void onSuccess(List<CatalyzeEntry> catalyzeEntries) {
                    addToExpandableListView(className, catalyzeEntries);
                }
            });
		}
	}

	/**
	 * Adds data to the expanding list's backing data source for a given custom
	 * class name.
	 * 
	 * @param name
	 *            The custom class to add data for
	 * @param customClasses
	 *            The entries for this custom class
	 */
	protected synchronized void addToExpandableListView(String name,
			List<CatalyzeEntry> customClasses) {
        for (CatalyzeEntry ce : customClasses) {
            ce.setClassName(name);
        }

		listDataChild.put(name, customClasses);

		fetchCount -= 1;

		if (fetchCount == 0) {
			// Ready to display.

            ExpandableListView expListView = (ExpandableListView) findViewById(R.id.ccExpandableListView);

			// preparing list data
			// prepareListData();

			listAdapter = new ExpandableListAdapter(this,
					new ArrayList<String>(Arrays
							.asList(MyApplication.CUSTOM_CLASS_NAMES)),
					listDataChild);

			// Set the list view's backing data
			expListView.setAdapter(listAdapter);

			// Set the behavior for when an entry is clicked. 
			expListView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {

					// Edit the selected entry
					Intent intent = new Intent(CustomClassActivity.this,
							CustomClassEditActivity.class);
					intent.putExtra("customClass", listAdapter.getCustomClass(
							groupPosition, childPosition));
                    intent.putExtra("className", listAdapter.getCustomClass(
                            groupPosition, childPosition).getClassName());
					startActivityForResult(intent, 2);

					return false;
				}

			});

		}
	}

	/**
	 * This allows us to process the results of the new/edit activity that just
	 * returned.
	 * 
	 * @param requestCode
	 *            Request code 1 is for a new entry and code 2 is an edit, which
	 *            also may trigger a deletion.
	 * @param resultCode
	 *            Either RESULT_OK or RESULT_CANCELLED
	 * @param data
	 *            The intent returning containing the custom class data in its
	 *            extras.
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) { // New entry

			if (resultCode == RESULT_OK) {

				final CatalyzeEntry customClass = (CatalyzeEntry)data
						.getSerializableExtra("customClass");
                // we must also fetch the className variable because this is not serialized with
                // the custom class entry.
                customClass.setClassName(data.getStringExtra("className"));
				
				// Save a new CustomClass Entry
                customClass.create(new CatalyzeListener<CatalyzeEntry>() {
                    @Override
                    public void onError(CatalyzeException e) {
                        Toast.makeText(
                                CustomClassActivity.this,
                                "Custom Class entry creation failed: "
                                        + e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onSuccess(CatalyzeEntry catalyzeEntry) {
                        Toast.makeText(CustomClassActivity.this,
                                "Custom class entry created successfully.",
                                Toast.LENGTH_SHORT).show();

                        fetchCustomClassInformation();
                    }
                });

			}
			if (resultCode == RESULT_CANCELED) {
				// Handle cases with no result here
			}
		} else if (requestCode == 2) { // Update

			if (resultCode == RESULT_OK) {

				// Get the custom class that was edited
                CatalyzeEntry cc = (CatalyzeEntry)data
						.getSerializableExtra("customClass");
                // we must also fetch the className variable because this is not serialized with
                // the custom class entry.
                cc.setClassName(data.getStringExtra("className"));

				// If delete is true the user clicked Delete and this should be
				// removed
				boolean delete = data.getBooleanExtra("delete", false);

				if (cc != null && !delete) {
					// Update an existing CustomClass entry
                    cc.update(new CatalyzeListener<CatalyzeEntry>() {
                        @Override
                        public void onError(CatalyzeException e) {
                            Toast.makeText(
                                    CustomClassActivity.this,
                                    "Custom Class update failed: "
                                            + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        @Override
                        public void onSuccess(CatalyzeEntry catalyzeEntry) {
                            Toast.makeText(CustomClassActivity.this,
                                    "Custom class updated successfully.",
                                    Toast.LENGTH_SHORT).show();

                            fetchCustomClassInformation();
                        }
                    });

				} else if (cc != null && delete) {
					// Delete a CustomClass
                    cc.delete(new CatalyzeListener<CatalyzeEntry>() {
                        @Override
                        public void onError(CatalyzeException e) {
                            Toast.makeText(
                                    CustomClassActivity.this,
                                    "Custom Class delete failed: "
                                            + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(CatalyzeEntry catalyzeEntry) {
                            Toast.makeText(CustomClassActivity.this,
                                    "Custom class deleted successfully.",
                                    Toast.LENGTH_SHORT).show();

                            fetchCustomClassInformation();
                        }
                    });

				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Handle cases with no result here
			}
		}
	}

}
