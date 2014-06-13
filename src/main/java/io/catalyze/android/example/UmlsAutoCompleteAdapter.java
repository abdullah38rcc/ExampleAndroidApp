package io.catalyze.android.example;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import io.catalyze.sdk.android.Umls;
import io.catalyze.sdk.android.UmlsResult;

/**
 * Copied from https://developers.google.com/places/training/autocomplete-android.
 */
public class UmlsAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> resultList;
    private Spinner codeSetSpinner;

    public UmlsAutoCompleteAdapter(Context context, int textViewResourceId, Spinner codeSetSpinner) {
        super(context, textViewResourceId);
        this.codeSetSpinner = codeSetSpinner;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = new ArrayList<String>();

        List<UmlsResult> results = Umls.searchByKeyword(codeSetSpinner.getSelectedItem().toString(), input);

        for (UmlsResult umlsResult : results) {
            String resultString = String.format("%s - %s",
                    umlsResult.getCode(), umlsResult.getDesc());
            resultList.add(resultString);
        }

        return resultList;
    }
}