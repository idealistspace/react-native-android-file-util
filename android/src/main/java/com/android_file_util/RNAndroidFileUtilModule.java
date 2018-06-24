
package com.android_file_util;

import android.content.Intent;
import android.app.Activity;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RNAndroidFileUtilModule extends ReactContextBaseJavaModule {

  private static final int WRITE_REQUEST_CODE = 43;
  private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
  private static final String E_PICKER_CANCELLED = "E_PICKER_CANCELLED";
  private static final String E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
  private static final String E_NO_DATA_FOUND = "E_NO_DATA_FOUND";

  private final ReactApplicationContext reactContext;

  private Promise mPickerPromise;
  private String fileContent;

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if (requestCode == WRITE_REQUEST_CODE) {
        if (mPickerPromise != null) {
          if (resultCode == Activity.RESULT_CANCELED) {
            mPickerPromise.reject(E_PICKER_CANCELLED, "File picker was cancelled");
          } else if (resultCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();

            if (uri == null) {
              mPickerPromise.reject(E_NO_DATA_FOUND, "No data found");
            } else {
              alterDocument(uri);
              mPickerPromise.resolve(uri.toString());
            }
          }
          mPickerPromise = null;
        } else {
          if (resultCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();
            if (uri != null) {
              alterDocument(uri);
            }
          }
        }
      }
    }

    private void alterDocument(Uri uri) {
      try {
        ParcelFileDescriptor pfd = getCurrentActivity().getContentResolver().openFileDescriptor(uri, "w");
        FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
        fileOutputStream.write(fileContent.getBytes());
        // Let the document provider know you're done by closing the stream.
        fileOutputStream.close();
        pfd.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  };

  public RNAndroidFileUtilModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "RNAndroidFileUtil";
  }

  @ReactMethod
  private void createFile(String mimeType, String fileName, String json, final Promise promise) {
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {
      promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
      return;
    }

    // Store the promise to resolve/reject when picker returns data
    mPickerPromise = promise;
    fileContent = json;

    try {
      Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

      // Filter to only show results that can be "opened", such as
      // a file (as opposed to a list of contacts or timezones).
      intent.addCategory(Intent.CATEGORY_OPENABLE);

      // Create a file with the requested MIME type.
      intent.setType(mimeType);
      intent.putExtra(Intent.EXTRA_TITLE, fileName);

      currentActivity.startActivityForResult(intent, WRITE_REQUEST_CODE);
    } catch (Exception e) {
      mPickerPromise.reject(E_FAILED_TO_SHOW_PICKER, e);
      mPickerPromise = null;
    }
  }
}