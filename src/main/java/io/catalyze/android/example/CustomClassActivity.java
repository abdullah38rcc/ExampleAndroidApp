package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;
import io.catalyze.sdk.android.CustomClass;
import io.catalyze.sdk.android.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class CustomClassActivity extends Activity {

	// An authenticated catalyze handle, to be obtained from the calling
	// activity
	private Catalyze catalyze;

	private ExpandableListAdapter listAdapter;   
	private ExpandableListView expListView;

	// List of custom class names for display in the ExpandableListView
	// private List<String> listDataHeader = new ArrayList<String>();

	// Map of child elements (custom classes) to display in the
	// ExpandableListView
	private HashMap<String, List<CustomClass>> listDataChild = new HashMap<String, List<CustomClass>>();

	// Number of custom class operations left to perform
	private int fetchCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_class);

		catalyze = (Catalyze) getIntent().getSerializableExtra("catalyze");
		if (catalyze == null) {
			Toast.makeText(this, "No 'catalyze' provided. ", Toast.LENGTH_SHORT)
					.show();

			finish();
		}

		Button addButton = (Button) this.findViewById(R.id.ccAddEntry);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CustomClassActivity.this,
						CustomClassEditActivity.class);
				intent.putExtra("catalyze", catalyze);
				startActivityForResult(intent, 1);
			}

		});

		fetchCustomClassInformation();

	}

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
			Query ccQuery = new Query(className, catalyze);
			ccQuery.setPageSize(5);

			CatalyzeListener<Query> handler = new CatalyzeListener<Query>(this) {

				@Override
				public void onError(CatalyzeException ce) {
					Toast.makeText(CustomClassActivity.this,
							"Query failed: " + ce.getMessage(),
							Toast.LENGTH_SHORT).show();

					// Add any empty entry as a placeholder
					addToExpandableListView(className,
							new ArrayList<CustomClass>());
				}

				@Override
				public void onSuccess(Query response) {
					ArrayList<CustomClass> results = response.getResults();
					String ccName = response.getCustomClassName();

					// Add these results to the view
					addToExpandableListView(ccName, results);
				}

			};

			// Add the query to the networking queue (Volley).
			ccQuery.executeQuery(handler);
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
			ArrayList<CustomClass> customClasses) {

		listDataChild.put(name, customClasses);

		fetchCount -= 1;

		if (fetchCount == 0) {
			// Ready to display.

			expListView = (ExpandableListView) findViewById(R.id.ccExpandableListView);

			// preparing list data
			// prepareListData();

			listAdapter = new ExpandableListAdapter(this,
					new ArrayList<String>(Arrays
							.asList(MyApplication.CUSTOM_CLASS_NAMES)),
					listDataChild);

			// for (String key: listDataHeader) {
			// Log.i("Catalyze", "Added " + listDataChild.get(key).size() +
			// " items for '" + key + "'");
			// }

			// setting list adapter
			expListView.setAdapter(listAdapter);

			expListView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {

					Intent intent = new Intent(CustomClassActivity.this,
							CustomClassEditActivity.class);
					intent.putExtra("customClass", listAdapter.getCustomClass(
							groupPosition, childPosition));
					intent.putExtra("catalyze", catalyze);
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

				final CustomClass customClass = (CustomClass) data
						.getSerializableExtra("customClass");
				customClass
						.createEntry(new CatalyzeListener<CustomClass>(this) {

							@Override
							public void onError(CatalyzeException ce) {
								Toast.makeText(
										CustomClassActivity.this,
										"Custom Class creation failed: "
												+ ce.getMessage(),
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(CustomClass response) {
								Toast.makeText(CustomClassActivity.this,
										"Custom class created successfully.",
										Toast.LENGTH_SHORT).show();

								fetchCustomClassInformation();
							}
						});

			}
			if (resultCode == RESULT_CANCELED) {
				// Handle cases with no result here
			}
		} else if (requestCode == 2) { // Update - not implemented yet

			if (resultCode == RESULT_OK) {

				// Get the custom class that was edited
				CustomClass cc = (CustomClass) data
						.getSerializableExtra("customClass");

				// If delete is true the user clicked Delete and this should be
				// removed
				boolean delete = data.getBooleanExtra("delete", false);

				if (cc != null && !delete) {
					cc.updateEntry(new CatalyzeListener<CustomClass>(this) {

						@Override
						public void onError(CatalyzeException ce) {
							Toast.makeText(
									CustomClassActivity.this,
									"Custom Class update failed: "
											+ ce.getMessage(),
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(CustomClass response) {
							Toast.makeText(CustomClassActivity.this,
									"Custom class updated successfully.",
									Toast.LENGTH_SHORT).show();

							fetchCustomClassInformation();

						}
					});

				} else if (cc != null && delete) {
					cc.deleteEntry(new CatalyzeListener<CustomClass>(this) {

						@Override
						public void onError(CatalyzeException ce) {
							Toast.makeText(
									CustomClassActivity.this,
									"Custom Class delete failed: "
											+ ce.getMessage(),
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(CustomClass response) {
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