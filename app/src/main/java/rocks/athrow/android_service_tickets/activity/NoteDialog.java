package rocks.athrow.android_service_tickets.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import rocks.athrow.android_service_tickets.R;

/**
 * createNoteDialog
 */
public class NoteDialog extends DialogFragment {

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.note_dialog, null));
        builder.setMessage(R.string.create_note)
                .setPositiveButton(R.string.save_note, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        EditText noteEntry = (EditText) f.findViewById(R.id.note_entry);
                        String note = noteEntry.getText().toString();
                        ServiceTicketDetailActivity callingActivity = (ServiceTicketDetailActivity) getActivity();
                        callingActivity.onNoteCreated(note);
                    }
                })
                .setNegativeButton(R.string.cancel_note, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}