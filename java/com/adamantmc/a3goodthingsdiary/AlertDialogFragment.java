package com.adamantmc.a3goodthingsdiary;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by Adamantios on 2/5/2016.
 */
public class AlertDialogFragment extends DialogFragment {

    private long id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        id = getArguments().getLong(DatabaseContract.Entry._ID);

        builder.setMessage(getString(R.string.deleteMessage));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                db.delete(DatabaseContract.Entry.TABLE_NAME,"_ID="+id,null);
                getActivity().finish();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

}
