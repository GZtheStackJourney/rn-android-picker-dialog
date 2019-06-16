package com.rnandroidpicker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;


public class PickerModule extends ReactContextBaseJavaModule {

    public static final String FRAGMENT_TAG = "PickerAndroid";
    private static final String ERROR_NO_ACTIVITY = "E_NO_ACTIVITY";
    private static final String ERROR_MISMATCH_INPUTS = "ERROR_MISMATCH_INPUTS";
    private static final String ERROR_ARRAY_TOO_LONG = "ERROR_ARRAY_TOO_LONG";
    private PickerDialog pickerDialog = null;

    public PickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        pickerDialog = new PickerDialog();
    }

    @Nonnull
    @Override
    public String getName() {
        return FRAGMENT_TAG;
    }

    private class PickerDialogListener implements PickerDialog.OnPickerListener {

        private final Promise mPromise;
        private boolean mPromiseResolved = false;

        public PickerDialogListener(Promise promise) {
            mPromise = promise;
        }

        @Override
        public void onConfirmSelected(Bundle bundle) {
            if (!mPromiseResolved && getReactApplicationContext().hasActiveCatalystInstance()) {
                ArrayList<Integer> callbackArr = bundle.getIntegerArrayList("selected");
                WritableArray result = new WritableNativeArray();
                for(Integer num: Objects.requireNonNull(callbackArr)) {
                    result.pushInt(num);
                }
                Log.v("on submit", result.toString());
                mPromise.resolve(result);
                mPromiseResolved = true;
            }
        }

        @Override
        public void onDismissSelected() {
            if (!mPromiseResolved && getReactApplicationContext().hasActiveCatalystInstance()) {
                mPromise.resolve(null);
                mPromiseResolved = true;
            }
        }
    }

    @ReactMethod
    public void showDialog(ReadableArray inputs, ReadableArray selectedValues, String dialogTitle, Promise promise) {
        if (pickerDialog != null
                && pickerDialog.getDialog() != null
                && pickerDialog.getDialog().isShowing()
                && !pickerDialog.isRemoving()) {
            return;
        }
        if(inputs.size() > 3) {
            promise.reject(
                    ERROR_ARRAY_TOO_LONG,
                    "input array length must be between 1 and 3");
            return;
        }
        if(selectedValues.size() > 3) {
            promise.reject(
                    ERROR_ARRAY_TOO_LONG,
                    "input array length must be between 1 and 3");
            return;
        }
        if(inputs.size() != selectedValues.size()) {
            promise.reject(
                    ERROR_MISMATCH_INPUTS,
                    "Inputs and selected array must be of the same size");
            return;
        }
        String title = dialogTitle != null ? dialogTitle : "";
        String[][] inputValues = new String[inputs.size()][];
        Integer[] selectedIndex = new Integer[selectedValues.size()];
        try{
            for(int i = 0; i < inputs.size(); i++) {
                ReadableArray child = inputs.getArray(i);
                List<String> childStrings = new ArrayList<String>();
                for(Object object: Objects.requireNonNull(child).toArrayList()) {
                    childStrings.add(object != null ? object.toString() : null);
                }
                inputValues[i] = childStrings.toArray(new String[child.size()]);
            }
            List<Integer> tempIntArr = new ArrayList<Integer>();
            for(int j = 0; j < selectedValues.size(); j++) {
                tempIntArr.add(selectedValues.getInt(j));
            }
            selectedIndex = tempIntArr.toArray(new Integer[selectedValues.size()]);
            createDialog(inputValues, selectedIndex, title, promise);
        } catch (Exception e) {
            Log.v("Error", e.getMessage());
        }
//        Log.v("converted array", Arrays.deepToString(inputValues));
//        Log.v("selectedIndex", Arrays.toString(selectedIndex));
//        Log.v("3rd arg", dialogTitle);

    }

    private void createDialog(String[][] inputValues, Integer[] selectedIndex, String title, Promise promise) {
        FragmentActivity activity = (FragmentActivity) getCurrentActivity();
        if (activity == null) {
            promise.reject(
                    ERROR_NO_ACTIVITY,
                    "Tried to open a TimePicker dialog while not attached to an Activity");
            return;
        }
        FragmentManager fragmentManager = Objects.requireNonNull(activity).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        try{
            bundle.putSerializable("list", inputValues);
            bundle.putSerializable("current values", selectedIndex);
            bundle.putString("title", title);
            PickerDialogListener pickerDialogListener = new PickerDialogListener(promise);
            pickerDialog.setOnConfirmSelectedListener(pickerDialogListener);
            pickerDialog.setArguments(bundle);
            pickerDialog.show(fragmentManager, "Number Dialog");
        } catch (Exception e) {
            Log.v("Error", e.getMessage());
        }
    }

}