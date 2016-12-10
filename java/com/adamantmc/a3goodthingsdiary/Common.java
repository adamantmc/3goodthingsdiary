package com.adamantmc.a3goodthingsdiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Adamantios on 2/5/2016.
 */
public class Common {
    public static String ADD_MODE = "ADD_MODE";
    public static int RESULT_ADD  = 1;
    public static int RESULT_CANCEL = 2;
    public static int RESULT_EDIT = 3;
    public static int REQUEST_CODE_FROM_VIEW = 4;
    public static int REQUEST_CODE_FROM_MAIN = 5;

    public static String[] all_columns = new String[] {
            DatabaseContract.Entry._ID,
            DatabaseContract.Entry.COLUMN_NAME_DATE,
            DatabaseContract.Entry.COLUMN_NAME_THING1,
            DatabaseContract.Entry.COLUMN_NAME_THING2,
            DatabaseContract.Entry.COLUMN_NAME_THING3
    };

    public static String[] id_date = new String[] {
            DatabaseContract.Entry._ID,
            DatabaseContract.Entry.COLUMN_NAME_DATE,
    };

    public static String dateToFormattedString(String date_str) {
        String output_format = "E d, LLLL, yyyy";

        String db_format = "yyyy-MM-d";
        SimpleDateFormat sdf = new SimpleDateFormat(db_format);

        try {
            Date date = sdf.parse(date_str);
            sdf = new SimpleDateFormat(output_format);
            return sdf.format(date);

        } catch (ParseException e) {
        }

        return date_str; //In case a parse exception happens, just return something
    }


    public static Cursor getQueryCursor(Context context, String[] columns, String where_clause_cols) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseContract.Entry.TABLE_NAME,
                columns,
                where_clause_cols,
                null,
                null,
                null,
                DatabaseContract.Entry.COLUMN_NAME_DATE + " DESC"
        );

        return cursor;
    }

    public static void printToast(Context context, String msg) {
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

}
