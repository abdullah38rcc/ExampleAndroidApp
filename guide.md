Catalyze.io Android SDK
=======

Thanks for your interest in the [Catalyze](http://www.catalyze.io) Android SDK. Feel free to reach out to us at <mailto:support@catalyze.io> if you have questions. 

Getting Started
----------
Before working through this guide you will need a developer account. Please sign up at [https://dashboard.catalyze.io/](https://dashboard.catalyze.io/). You may also want to grab the [ExamplAndroidApp](https://github.com/catalyzeio/ExampleAndroidApp) project from [GitHub](https://github.com/catalyzeio) for more examples and a working shell application that makes use of the SDK. The project's README.md provides details for getting started as well. 

The first thing you must do is set-up an Application on the dashboard. You will need the following information about your Application: api key and application id. 
After this is complete, you must set these values in the apps manifest file:

    <meta-data
            android:name="io.catalyze.android.sdk.v2.API_KEY"
            android:value="YOUR_API_KEY"/>
    <meta-data
            android:name="io.catalyze.android.sdk.v2.APP_ID"
            android:value="YOUR_APP_ID"/>

Now you are ready to use the SDK. You will next need to acquire an authenticated Catalyze instance by logging in a user or creating a new user. The next section covers these steps.  

All networking calls made using the Catalyze Android SDK are made asynchronously and sent to the Catalyze API. To simplify the process of making Catalyze API calls we have provided a callback interface that allows you to easily define what you want to do with the results.

To make Catalyze API calls you will need to provide a CatalyzeListener of the appropriate type to the SDK method making the API call. CatalyzeListener is an abstract class with the methods onSuccess() and onError(), that you must define for handling asynchronous responses from API calls. The type that is returned when onSuccess is called is determined by the individual method call, while onError will always receive a CatalyzeException, which is an exception wrapper class that can contain a wide variety of possible errors.


Log In or Create a User
-------------

Here is an example of how to create and use a CatalyzeListener to log in a user:
To log in a user call

    Catalyze.getInstance().authenticate("John@example.com", "password", new CatalyzeListener<CatalyzeUser>() {
	    @Override
	    public void onError(CatalyzeException response) {
	        // Could be a bad user/password or a user that
	        // does not exists, show an error like this
	        Toast.makeText(LoginActivity.this,
	                "Login failed: " + response.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onSuccess(CatalyzeUser response) {
	        // The user is authenticated.
	    }
	});

To sign up a new user you will need an email, password, first name, last name, and a username

    Catalyze.getInstance().signUp("Bob@example.com", "password", "Bob", "Jones", "bobJones123", new CatalyzeListener<CatalyzeUser>() {
	    @Override
	    public void onError(CatalyzeException response) {
	        // Could be a bad user/password or a user that
	        // does not exists or their account has not been confirmed
	        // show an error something like this
	        Toast.makeText(LoginActivity.this,
	                "Login failed: " + response.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onSuccess(CatalyzeUser response) {
	        // The user is signed up and needs to validate their 
	        // account through email confirmation.
	        Toast.makeText(LoginActivity.this,
	                "Thanks for signing up! Please check your email and click the confirmation link before you can login",
	                Toast.LENGTH_SHORT).show();
	    }
	});

In both cases a CatalyzeListener is created to provide callbacks when the background network operation completes. The onSuccess() method will be called if the user is successfully authenticated. Otherwise onError() will be called with whatever error information is available.

Logout the Current User
------------

To logout the user assocoated with an authenticated Catalyze instance call

    Catalyze.getInstance().signOut(new CatalyzeListener<String>() {
	    @Override
	    public void onError(CatalyzeException response) {
	        Toast.makeText(MainActivity.this,
	                "Log out failed: " + response.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onSuccess(String response) {
	        // you may ignore the string response
	    }
	});

This will clear all locally stored information about the User including session information and tell the API to destroy your session token. 

Updating the User
--------------

A CatalyzeUser also has a list of supported fields that are validated by the catalyze.io API.  These are:

* username
* email (an Email object)
* name (a Name object)
* phoneNumber (a PhoneNumber object)
* dob (date of birth)
* age
* addresses (a list of Address objects)
* gender
* maritalStatus
* religion
* race
* ethnicity
* guardians (a list of Guardian objects)
* confCode
* languages (a list of Language objects)
* socialIds (a list of SocialId objects)
* mrns (a list of Mrn objects)
* healthPlans (a list of HealthPlan objects)
* avatar
* ssn
* profilePhoto
* extras

Now that we are logged into an Application lets save some supported fields to our User. If we wanted to update our first name, last name, age, and an extra field:

    CatalyzeUser currentUser = catalyze.getAuthenticatedUser();
    currentUser.getName().setFirstName("John");
    currentUser.getName().setLastName("Smith");
    currentUser.setAge("55");
    currentUser.getExtras().put("on_medication", true);
    currentUser.update(new CatalyzeListener<CatalyzeUser>() {
	    @Override
	    public void onError(CatalyzeException response) {
	        Toast.makeText(UserInfoActivity.this,
	                "Update user failed: " + response.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onSuccess(CatalyzeUser response) {
	        Toast.makeText(UserInfoActivity.this,
	                "User details updated successfully.",
	                Toast.LENGTH_SHORT).show();
	    }
	};

Custom Classes
----
A CustomClass by itself, represents a CustomClass that can be stored on the catalyze.io API.  These custom classes must be created in the dashboard before being used or referenced within an app or the API will return a 4XX status code. Create an entry that will be saved under the CustomClass with the name `class_name`

    CatalyzeEntry newEntry = new CatalyzeEntry("class_name");

Now that you have a CatalyzeEntry you can save values, retrieve values, and create the entry on the catalyze.io API.

	newEntry.getContent().put("favorite color", "blue");
    newEntry.createEntry(new CatalyzeListener<CatalyzeEntry>() {
	    @Override
	    public void onError(CatalyzeException e) {
	        Toast.makeText(
	                CustomClassActivity.this,
	                "Custom Class entry creation failed: "
	                        + e.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onSuccess(CatalyzeEntry catalyzeEntry) {
	        Toast.makeText(CustomClassActivity.this,
	                "Custom class entry created successfully.",
	                Toast.LENGTH_SHORT).show();
	    }
	});

When calling createEntry, the unique id of the class will automatically be saved, however if you wish to perform an API call on an existing the CustomClass entry you will need to first set the CatalyzeEntry Id.

    customClass.setEntryId("id");

For examples of working with CatalyzeEntries see the ExampleAndroidApp's CustomClassActivity.java source. 

Query
----

A Query is how you manage searching a custom class. If a custom class HAS NOT been marked as PHI you can query any Entry in the class and it will be returned to you. If a class HAS been marked as PHI, you may only query your own Entries. You don't need to worry about doing this however, as the API takes care of it for you. There are four parts to a Query.

The first two are `pageSize` and `pageNumber`. `pageSize` is the amount of CatalyzeEntries you want to receive back from the API. Note that you will not always receive `pageSize` number of CatalyzeEntries in return. The actual number depends on the `pageNumber` requested and the amount of CatalyzeEntries in the custom class. `pageNumber` is used to specify how many CatalyzeEntries should be skipped. For example, say there are 50 CatalyzeEntries in a custom class numbered 0 to 49. `pageNumber` of 1 and a `pageSize` of 20 will return CatalyzeEntries 0 through 19. A `pageNumber` of 2 and a `pageSize` of 20 will return CatalyzeEntries 20 through 39.

The second two are `field` and `searchBy`. `field` is the data column of the custom class that is to be searched and `searchBy` is the actual value that is to be looked for in the `field` column.

To use Query you must initialize the object with the name of the custom class that is being queried.

    Query query = new Query("myCustomClass");  
    query.setSearchBy("blue");
    query.setField("favorite color");
    query.setPageSize(10);
    query.setPageNumber(1);
    query.executeQuery(new CatalyzeListener<List<CatalyzeEntry>>() {
	    @Override
	    public void onError(CatalyzeException e) {
	        Toast.makeText(CustomClassActivity.this,
	                "Query failed: " + e.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onSuccess(List<CatalyzeEntry> catalyzeEntries) {
	        // do something with the list of entries
	    }
	});

See the ExampleAndroidApp's CustomClassActivity.java source for an example of how to run concurrent queries and process the results. 

File Management
---------
The android SDK supports file listing, upload, download, and deletion. To upload a file, you will need a java File object while downloading a file gives you an InputStream. All file management is done through the `FileManager` class. Here is an example of how to download a file and set it as the image of an ImageView instance

	FileManager.getFile("idOfFile", new CatalyzeListener<InputStream>() {
	    @Override
	    public void onError(CatalyzeException e) {
	        Toast.makeText(FileManagementActivity.this,
	                "file download failed: " + e.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onSuccess(InputStream inputStream) {
	        try {
	            image.setImageBitmap(BitmapFactory.decodeStream(inputStream));
	        } catch (Exception e) {
	            Toast.makeText(FileManagementActivity.this,
	                    "image processing failed: " + e.getMessage(),
	                    Toast.LENGTH_SHORT).show();
	        }
	    }
	});

One other aspect of the Catalyze API is the concept of an author and concept of an owner. The android SDK supports this notion with file management. You can upload a file to another user as long as their usersId is known. Your user would be considered the author while the owner would be the user that you are uploading the file for. These methods are the ones with `ToUser` or `FromUser` in their method names. Such as `FileManager.uploadFileToUser()` or `FileManager.deleteFileFromUser()`.

Other Features
---------
The SDK has numerous other features that are shown by example in the ExampleAndroidApp's source. Please refer to that code for additional examples. Let us know if you don't see a feature that you need in your app. 

Contact
------

We are eager to help you build powerful mobile health care apps. Feel free to contact us at <mailto:support@catalyze.io> with any feature requests, suggestions, etc. 
