Catalyze.io Android SDK
=======

Thanks for your interest in the [http://www.catalyze.io](Catalyze) Android SDK. Feel free to reach out to us at <mailto:support@catalyze.io> if you have questions. 

Getting Started
----------
Before working through this guide you will need a developer account. Please sign up at [http://developer.catalyze.io/](http://developer.catalyze.io/). You may also want to grab the ExamplAndroidApp project from [https://github.com/catalyzeio](GitHub) for more examples and a working shell application that makes use of the SDK. The project's README.md provides details for getting started as well. 

The first thing you must do is set-up an Application on the developer console. You will need the following information about your Application: api key and application id. 
After this is complete, you must set these values in the app:

    Catalyze.API_KEY = "your new API key";
    Catalyze.IDENTIFIER = "your app name";

Now you are ready to use the SDK. You will next need to acquire an authenticated Catalyze instance by logging in a user or creating a new user. The next section covers these steps.  

All networking calls made using the Catalyze Android SDK are made asynchronously and sent to the Catalyze API. To simplify the process of making Catalyze API calls we have provided a callback interface that allows you to easily define what you want to do with.

To make Catalyze API calls you will need to provide a CatalyzeListener of the appropriate type to the SDK method making the API call. CatalyzeListener is an abstract class with the methods onSuccess() and onError(), that you must define for handling asynchronous responses from API calls. The type that is returned when onSuccess is called is determined by the individual method call, while onError will always receive a CatalyzeError, which is an error wrapper class that can contain a wide variety of possible errors.


Log In or Create a User
-------------

Here is an example of how to create and use a CatalyzeListener to retrieve a CustomClass:
To log in a user call

    Catalyze.authenticate("John@example.com", "password", createExampleResponseHandler());

To sign up a new user and log in call

    Catalyze.signUp("Bob@example.com", "password", "Bob", "Jones", createExampleResponseHandler());

In both cases a CatalyzeListener is created to provide callbacks when the background network operation completes. The onSuccess() method will be called if the user is successfully authenticated. Otherwise onError() will be called with whatever error information is available.  

    private CatalyzeListener<Catalyze> createExampleResponseHandler(Context context) {

        new CatalyzeListener<Catalyze>(context) {
        
                @Override
                public void onError(CatalyzeException response) {
                    // What to do if error occurs
                }
    
                @Override
                public void onSuccess(Catalyze response) {
                    // Save this authenticated instance for all other operations
                }
            };

Logout the Current User
------------

To logout the user assocoated with an authenticated Catalyze instance call

    catalyze.signOut(catalyzeListener); 

Where *catalyzeListener* is defined as

    CatalyzeListener<CatalyzeUser> catalyzeListener = new CatalyzeListener<CatalyzeUser>(
						MainActivity.this) {

					@Override
					public void onError(CatalyzeException error) {
                        // Handle errors here
					}

					@Override
					public void onSuccess(CatalyzeUser response) {
                        // Returns the user that just logged off
					}
				};

This will clear all locally stored information about the User including session information and tell the API to destroy your session token. The Catalyze instance will no longer be usable.

Updating the User
--------------

A CatalyzeUser also has a list of supported fields that are validated by the catalyze.io API.  These are:

* firstName
* lastName
* dateOfBirth
* age
* phoneNumber
* street
* city
* state
* zipCode
* country
* gender

Now that we are logged into an Application lets save some supported fields to our User. If we wanted to update our first name, last name, age, and an extra field:

    CatalyzeUser currentUser = catalyze.getAuthenticatedUser();
    currentUser.setFirstName("John");
    currentUser.setLastName("Smith");
    currentUser.setAge("55");
    currentUser.setExtra("on_medication", true);
    currentUser.update(catalyzeListener);

Where *catalyzeListener* is defined as

    CatalyzeListener<CatalyzeUser> catalyzeListener = new CatalyzeListener<CatalyzeUser>(
						MainActivity.this) {

					@Override
					public void onError(CatalyzeException error) {
                        // Handle errors here
					}

					@Override
					public void onSuccess(CatalyzeUser response) {
                        // Returns the user that was just updated
					}
				};

Custom Classes
----
A CustomClass by itself, represents a CustomClass that can be stored on the catalyze.io API.  These custom classes must be created in the developer console before being used or referenced within an app or the API will return a 4XX status code. 

    CustomClass myClass = catalyze.getCustomClassInstance("class_name");

Now that you have a CustomClass you can save values, retrieve values, and create new entries on the catalyze.io API.


    customClass.putContent("favorite color", "blue");
    customClass.createEntry(catalyzeListener);

When calling createEntry, the unique id of the class will automatically be saved, however if you wish to perform an API call on an existing the CustomClass entry you will need to first set the Custom Class Id.

    customClass.setID(id);

For examples of working with CustomClasses see the ExampleAndroidApp's CustomClassActivity.java source. 

Query
------

A CatalyzeQuery is how you manage searching a custom class. If a custom class HAS NOT been marked as PHI you can query any Entry in the class and it will be returned to you. If a class HAS been marked as PHI, you may only query your own Entries. You don'92t need to worry about doing this however, as the API takes care of it for you. There are four parts to a CatalyzeQuery.

The first two are "pageSize" and "pageNumber". "pageSize" is the amount of Entries you want to receive back from the API. Note that you will not always receive "pageSize" number of Entries in return. The actual number depends on the "pageNumber" requested and the amount of Entries in the custom class. "pageNumber" is used to specify how many Entries should be skipped. For example, say there are 50 Entries in a custom class numbered 0 to 49. "pageNumber" of 1 and a "pageSize" of 20 will return Entries 0 through 19. A "pageNumber" of 2 and a "pageSize" of 20 will return Entries 20 through 39.

The second two are "queryField" and "queryValue". "queryField" is the data column of the custom class that is to be searched and "queryValue" is the actual value that is to be looked for in the "queryField" column.

To use CatalyzeQuery you must initialize the object with the name of the custom class that is being queried.

    Query query = catalyze.getQueryInstance("myCustomClass");  
    query.setSearchBy(data);
    query.setField(name);
    query.setPageSize(pageSize);
    query.setPageNumber(pageNumber);
    query.executeQuery(catalyzeListener);

See the ExampleAndroidApp's CustomClassActivity.java source for an example of how to run concurrent queries and process the results. 

Other Features
---------
The SDK has numerous other features that are shown by example in the ExampleAndroidApp's source. Please refer to that code for additional examples. Let us know if you don't see a feature that you need in your app. 

Contact
------

We are eager to help you build powerful mobile health care apps. Feel free to contact us at <mailto:support@catalyze.io> with any feature requests, suggestions, etc. 
