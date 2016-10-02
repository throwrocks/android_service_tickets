package rocks.athrow.android_service_tickets.activity;

/**
 * Created by josel on 10/1/2016.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import rocks.athrow.android_service_tickets.R;

/**
 * createNoteDialog
 */
public class NoteDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.note_dialog, null));
        builder.setMessage(R.string.create_note)
                .setPositiveButton(R.string.save_note, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: Save the note

                    }
                })
                .setNegativeButton(R.string.cancel_note, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}