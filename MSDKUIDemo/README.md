# HERE Mobile SDK UI Kit - Android Demo Application

Along with the Developer's Guide, accompanying example apps and code snippets, we also provide a more complex demo app showing a complete and fully functional flow.

## How to Build the Demo
In order to build the demo app with Android Studio you need to integrate the HERE Mobile SDK (Premium), version 3.8. Additionally you need to integrate the HERE Mobile SDK UI Kit library.

You also need valid app credentials from https://developer.here.com. If not already happened, please register at https://developer.here.com. Have your appid, token and license key in place (we need it in the first steps).

1. Download the HERE Mobile SDK for Android and put the HERE-sdk.aar file from your HERE SDK package to source/android/MSDKUIDemo/libs

2. Copy the MSDKUILib.aar to source/android/MSDKUIDemo/libs

3. Open the MSDKUIDemo project in Android Studio.

4. Add your credentials to the manifest file. Make that the credentials match the package name. You may need to refactor the package name of the demo app to match the one you are registered to:
```
 <meta-data
            android:name="com.here.android.maps.appid"
            android:value="@string/HERE_SDK_APP_ID" />
        <meta-data
            android:name="com.here.android.maps.apptoken"
            android:value="@string/HERE_SDK_APP_TOKEN" />
        <meta-data
            android:name="com.here.android.maps.license.key"
            android:value="@string/HERE_SDK_LICENSE_KEY" />
```

OR in replace "APP_ID", "APP_TOKEN" and "APP_LICENSE"  in build.gradle of MSDKUIAPP folder:
```
resValue 'string', 'HERE_SDK_APP_ID', System.getenv("MSDKUI_APP_ID_ANDROID") ?: "APP_ID"
        resValue 'string', 'HERE_SDK_APP_TOKEN', System.getenv("MSDKUI_APP_TOKEN_ANDROID") ?: "APP_TOKEN"
        resValue 'string', 'HERE_SDK_LICENSE_KEY', System.getenv("MSDKUI_APP_LICENSE_ANDROID") ?: "APP_LICENSE"
```
Run the application.

For more informtion please refer to the Developer's Guide.
