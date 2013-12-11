package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mvolkhart on 8/25/13.
 */
public class MyApplication extends Application {

	//public static final String API_KEY = "1f077962-18cc-4ade-8075-d9fa1642f316";
	//public static final String IDENTIFIER = "android.example";
	// Don't set this until a successful Catalyze.authenticate() call has
		// returned
	//protected static Catalyze catalyze = null;
	
	public static final String[] CUSTOM_CLASS_NAMES = { "address", "visits" };
	
    @Override
    public void onCreate() {
        super.onCreate();

        Catalyze.API_KEY = "1f077962-18cc-4ade-8075-d9fa1642f316";
        Catalyze.IDENTIFIER = "android.example";
        
        
        
    }


}
