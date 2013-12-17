package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Defines application-specific details, all of which need to be defined in the developer portal:
 * - Custom class names (CUSTOM_CLASS_NAMES)
 * - API key (API_KEY)
 * - App name (IDENTIFIER)
 */
public class MyApplication extends Application {

	public static final String[] CUSTOM_CLASS_NAMES = { "address", "visits" };
	
    @Override
    public void onCreate() {
        super.onCreate();

        Catalyze.API_KEY = "1f077962-18cc-4ade-8075-d9fa1642f316";
        Catalyze.IDENTIFIER = "android.example";
        
        
        
    }


}
