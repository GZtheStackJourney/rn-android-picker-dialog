package com.rnandroidpicker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class PickerDialog extends DialogFragment {

    private int mInputLength = 0;
    @Nullable
    private OnPickerListener mOnPickerListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        final View view = View.inflate(getActivity(), R.layout.picker, null);
        View finalView = initializeContent(view);
        String dialogTitle = Objects.requireNonNull(getArguments()).getString("title");
        builder.setTitle(dialogTitle);
        builder.setView(finalView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    final Integer[] currentSelection = (Integer[]) Objects.requireNonNull(getArguments()).getSerializable("current values");
                    ArrayList<Integer> selectedValues = new ArrayList<Integer>();
                    Log.v("Dialog Picker ok", "array length is" + mInputLength);
                    LinearLayout pickerContainer = view.findViewById(R.id.picker_container);
                    for(int index = 0; index < Objects.requireNonNull(currentSelection).length; ++index) {
                        NumberPicker nextChild = (NumberPicker) pickerContainer.getChildAt(index);
                        int result = nextChild.getValue();
                        selectedValues.add(result);
                        Log.v("picker " + index, "value at " + result);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList("selected", selectedValues);
                    Objects.requireNonNull(mOnPickerListener).onConfirmSelected(bundle);
                } catch (Exception e) {
                    Log.v("Dialog Picker ok error",  e.getMessage());
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    Objects.requireNonNull(mOnPickerListener).onDismissSelected();
                } catch (Exception e) {
                    Log.v("Dialog Picker ok error",  e.getMessage());
                }
            }
        });


        return builder.create();
    }

    private View initializeContent(View parentView) {
        Integer[] ids = {R.id.custom_picker_1, R.id.custom_picker_2, R.id.custom_picker_3};
        LinearLayout pickerContainer = parentView.findViewById(R.id.picker_container);
        String[][] myInputs = (String[][]) Objects.requireNonNull(getArguments()).getSerializable("list");
        String sideText = Objects.requireNonNull(getArguments()).getString("side text");
        mInputLength = Objects.requireNonNull(myInputs).length;
        Integer[] currentSelection = (Integer[]) Objects.requireNonNull(getArguments()).getSerializable("current values");
        for(int i = 0; i <= myInputs.length - 1; i++) {
            NumberPicker picker = new NumberPicker(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            if(i < 2 && myInputs.length > 1) {
                params.setMargins(0, 0, 80, 0);
            }
            picker.setId(ids[i]);
            picker.setMinValue(0);
            picker.setMaxValue(myInputs[i].length - 1);
            picker.setDisplayedValues(myInputs[i]);
            picker.setValue(Objects.requireNonNull(currentSelection)[i]);
            picker.setLayoutParams(params);
            picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            picker.setWrapSelectorWheel(true);
            pickerContainer.addView(picker);
        }
        if(myInputs.length == 1 && !Objects.equals(sideText, "")) {
            TextView tv = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tvParams.setMargins(80, 0, 0, 0);
            tv.setTextSize(20);
            tv.setText(sideText);
            tv.setLayoutParams(tvParams);
            pickerContainer.addView(tv);
        }
        return parentView;
    }

    public interface OnPickerListener {
        void onConfirmSelected(Bundle bundle);
        void onDismissSelected();
    }

    public void setOnConfirmSelectedListener(OnPickerListener listener) {
        mOnPickerListener = listener;
    }
}
