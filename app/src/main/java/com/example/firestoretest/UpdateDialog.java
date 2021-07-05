package com.example.firestoretest;
/* Assignment: final
Campus: Ashdod
Author: Matan Tal, ID: 201492881
*/
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UpdateDialog extends AppCompatDialogFragment {

    private EditText dialogTitleEditText, dialogDescriptionEditText;
    private NumberPicker dialogNumberPickerPriority;
    private UpdateDialogListener listener;
    private final static String UPDATE_TITLE = "Update";
    private final static String CANCEL_NEGATIVE_BUTTON = "Cancel";
    private final static int NUMBER_PICKER_MAX_VALUE = 10;
    private final static int NUMBER_PICKER_MIN_VALUE = 1;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_note_dialog, null);
        builder.setView(view).setTitle(UPDATE_TITLE)
                .setNegativeButton(CANCEL_NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(UPDATE_TITLE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = dialogTitleEditText.getText().toString().trim();
                        String description = dialogDescriptionEditText.getText().toString().trim();
                        int priority = dialogNumberPickerPriority.getValue();

                        listener.applyUpdate(title,description,priority);
                    }
                });
        //get the dialog id
        dialogTitleEditText = view.findViewById(R.id.dialog_title_editText);
        dialogDescriptionEditText = view.findViewById(R.id.dialog_description_editText);
        dialogNumberPickerPriority = view.findViewById(R.id.dialog_priority_numberPicker);
        dialogNumberPickerPriority.setMinValue(NUMBER_PICKER_MIN_VALUE);
        dialogNumberPickerPriority.setMaxValue(NUMBER_PICKER_MAX_VALUE);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (UpdateDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement UpdateDialogListener");
        }
    }

    public interface UpdateDialogListener {
        void applyUpdate(String title, String description, int priority);
    }
}
