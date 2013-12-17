package io.catalyze.android.example;

import io.catalyze.sdk.android.Catalyze;
import android.app.Application;

/**
 * Defines application-specific details, all of which need to be defined in the developer portal:
 * - Custom class names (CUSTOM_CLASS_NAMES)
 * - API key (API_KEY)
 * - App name (IDENTIFIER)
 */
public class MyApplication extends Application {

	// Define your custom class names below
	public static final String[] CUSTOM_CLASS_NAMES = { };
	
    @Override
    public void onCreate() {
        super.onCreate();

        // Enter your API key here
        Catalyze.API_KEY = "your API key";
        
        // Enter your app name here
        Catalyze.IDENTIFIER = "your app name";
        
        
        
    }


}
