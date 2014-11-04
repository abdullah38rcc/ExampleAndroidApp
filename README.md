Catalyze Example Application 
==========

This repository contains a simple Android application (compatible with versions 2.3.3 and up) that showcases the core functionality of the [http://www.catalyze.io](Catalyze) platform. 

New to Catalyze? Sign up for an account at [https://dashboard.catalyze.io/](https://dashboard.catalyze.io/).

Working functionality: 
* User log in, sign up and log off 
* Updating of user information
* Viewing and adding custom class entries.
* File upload, download, and deletion
* File download

Not working yet:
* Custom class references (not fully tested at the time of this initial release) 

Email us at <mailto:support@catalyze.io> if you are in need of these features. We will do our best to meet your needs. 


Building
--------

The app can be built with *gradle* or using your favorite IDE (Eclipse or Android Studio). 

First make sure ANDROID_HOME points to your Android SDK:

    export ANDROID_HOME=/path/to/sdk

Also make sure that your Android SDK has API level 19 (Android 4.4 - Kit Kat) installed, along with Android SDK Build-tools version 19.1.0 (under Tools in the SDK manager). The example app and SDK support API level 10 (Android 2.3.3) and above but the configurations deault to API 19. 

Next, pull down the repository:

    git clone https://github.com/catalyzeio/ExampleAndroidApp.git

Building with *gradle* is very simple. From the *ExampleAndroidApp* directory run the following command:

    ./gradlew assemble

### Importing into Eclipse 

The following set of steps is necessary for configuring the example app in Eclipse (the Android Studio setup is much simpler if you'd like to try that instead):

1. Run the following:

    ./gradlew eclipse

2. Import the project from the **File** menu by selecting **Import...** | **Android** | **Existing Android Code Into Workspace** and then clicking **Next**. 
3. Click **Browse** to locate the directory of the *ExampleAndroidApp* repository and then click **Open** on the folder. 
4. Uncheck the *build/manifests/debug* "project" leaving only *src/main* selected and click **Finish**. 
5. Lastly, right click on the *java* folder in the new project and select **Build Path** | **Use as Source Folder**.  

### Importing into Android Studio 

The Android Studio setup is very simple thanks to its great gradle integration. Follow these steps to set up the project:

1. Select **Import Project...** and the choose the *build.gradle* file from the *ExampleAndroidApp* directory. 
2. Leave **Use default gradle wrapper** selected and click **OK**. 
3. That's all!
