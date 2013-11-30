Using the catalyze.io Android SDK
=======
The first thing you must do is set-up an Application on the developer console. You will need the following information about your Application: api key and application id. After this is complete, you must make a Catalyze instance with your API key and App ID like this:

    Catalyze catalyze = new Catalyze( API KEY, APPID);

Users 
---

###Log in

To log in a user call

    CatalyzeUser myUser;
    Catalyze.getUser("John@gmail.com", "password", createExampleResponseHandler());

Here the CatalyzeListener passed to the log in method is almost exactly the same as it was before for CustomClass, with the exception that it must expect a response of type CatalyzeUser, whereas before a CustomClass response was expected.

    private CatalyzeListener<CatalyzeUser> createExampleResponseHandler() {
        new CatalyzeListener<CatalyzeUser>() {
        
                @Override
                public void onError(CatalyzeError response) {
                    //What to do if error occurs
                }
    
                @Override
                public void onSuccess(CatalyzeUser response) {
                    //What to do with successful response
                    myUser = response;
                }
            };

###Logout

To logout call

    catalyze.logoutCurrentUser(catalyzeListener);

This will clear all locally stored information about the User including session information and tell the API to destroy your session token.

###Updating and Saving your User

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

    currentUser.setFirstName("John");
    currentUser.setLastName("Smith");
    currentUser.setAge("55");
    currentUser.setExtra("on_medication", true);

Custom Classes
----
A CustomClass by itself, represents a CustomClass that can be stored on the catalyze.io API.  These custom classes must be created in the developer console before being used or referenced within an app or the API will return a 4XX status code. 

    CustomClass myClass = CustomClass.getInstance(user);

Now that you have a CustomClass you can save values, retrieve values, and create new entries on the catalyze.io API.


    customClass.putContent("favorite color", "blue");
    customClass.createEntry(catalyzeListener);

When calling createEntry, the unique id of the class will automatically be saved, however if you wish to perform an API call on an existing the CustomClass entry you will need to first set the Custom Class Id.

    myClass.setID(id);

###Saving Your Data

All networking calls made using the Catalyze Android SDK to save data to the Catalyze API are made asynchronously. To simplify the process of making Catalyze API calls we have provided a callback interface that allows you to easily define what you want to do with.

To make Catalyze API calls you will need to provide a CatalyzeListener of the appropriate type to the SDK method making the API call. CatalyzeListener is an abstract class with the methods onSuccess() and onError(), that you must define for handling asynchronous responses from API calls. The type that is returned when onSuccess is called is determined by the individual method call, while onError will always receive a CatalyzeError, which is an error wrapper class that can contain a wide variety of possible errors.

Here is an example of how to create and use a CatalyzeListener to retrieve a CustomClass:

    myClass.getEntry(entryId, createExampleResponseHandler());

    private CatalyzeListener<CustomClass> createExampleResponseHandler() {
        new CatalyzeListener<CustomClass>() {
        
                @Override
                public void onError(CatalyzeError response) {
                    //What to do if error occurs
                }
    
                @Override
                public void onSuccess(CustomClass response) {
                    //What to do with successful response
                    myClass = response;
                }
            };

Alternatively, if you prefer you can delcare a CatalyzeListener in line as well:

    myClass.getEntry(entryId, new CatalyzeListener<CustomClass>() {

                @Override
                public void onError(CatalyzeError response) {
                    //What to do if error occurs
                }
                @Override
                public void onSuccess(CustomClass response) {
                    //What to do with successful response
                    myClass = response;
                }
            };
        );

###Referenced Objects
On the catalyze.io API you can also have references to a custom class inside another custom class.  Let's say you have a custom class called "MovieStar" and another called "Address".  When you create "MovieStar" in the developer console, you can specify a column as being a reference.  References can be retrieved by using the CustomClass reference methods.

Query
------

A CatalyzeQuery is how you manage searching a custom class. If a custom class HAS NOT been marked as PHI you can query any Entry in the class and it will be returned to you. If a class HAS been marked as PHI, you may only query your own Entries. You don'92t need to worry about doing this however, as the API takes care of it for you. There are four parts to a CatalyzeQuery.

The first two are "pageSize" and "pageNumber". "pageSize" is the amount of Entries you want to receive back from the API. Note that you will not always receive "pageSize" number of Entries in return. The actual number depends on the "pageNumber" requested and the amount of Entries in the custom class. "pageNumber" is used to specify how many Entries should be skipped. For example, say there are 50 Entries in a custom class numbered 0 to 49. "pageNumber" of 1 and a "pageSize" of 20 will return Entries 0 through 19. A "pageNumber" of 2 and a "pageSize" of 20 will return Entries 20 through 39.

The second two are "queryField" and "queryValue". "queryField" is the data column of the custom class that is to be searched and "queryValue" is the actual value that is to be looked for in the "queryField" column.

To use CatalyzeQuery you must initialize the object with the name of the custom class that is being queried.

    Query query = new Query("myCustomClass", authenticatedUser);  
    query.setSearchBy(data);
    query.setField(name);
    query.setPageSize(pageSize);
    query.setPageNumber(pageNumber);
    query.executeQuery(catalyzeListener);

If you want to query your own Entries, you simply set the "queryField" to be "parentId" and the "queryValue" to the User's id.

    query.setField(parentId);
    query.setValue(userId);

