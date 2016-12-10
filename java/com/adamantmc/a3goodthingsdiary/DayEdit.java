package com.adamantmc.a3goodthingsdiary;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class DayEdit extends AppCompatActivity {

    private static String date;
    private long id;
    private boolean add_mode = false, edit_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_edit);

        Intent intent = getIntent();

        Bundle data = intent.getExtras();
        id = data.getLong(DatabaseContract.Entry._ID);

        EditText date_field, thing1, thing2, thing3;
        date_field = (EditText) findViewById(R.id.day_edit);
        thing1 = (EditText) findViewById(R.id.thing1_edit);
        thing2 = (EditText) findViewById(R.id.thing2_edit);
        thing3 = (EditText) findViewById(R.id.thing3_edit);

        if(data.getBoolean(Common.ADD_MODE)) {
            add_mode = true;
            edit_mode = false;

            Button doneButton = (Button) findViewById(R.id.doneDay_edit);
            doneButton.setText(getString(R.string.addEditView));
            doneButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_white,0,0,0);

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            month++;

            String month_str = Integer.toString(month);
            String day_str = Integer.toString(day);

            if(day < 10) day_str = "0"+day_str;
            if(month < 10) month_str = "0"+month_str;

            date = year + "-" + month_str + "-" + day_str; //Set the date variable
            date_field.setText(Common.dateToFormattedString(date));

        }
        else {
            edit_mode = true;
            add_mode = false;

            setData(id);
        }

        addDateTouchListener();
    }

    public void addDateTouchListener() {
        EditText date = (EditText) findViewById(R.id.day_edit);
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void setData(long row_id) {
        EditText date_view, thing1_view, thing2_view, thing3_view;

        date_view = (EditText) findViewById(R.id.day_edit);
        thing1_view = (EditText) findViewById(R.id.thing1_edit);
        thing2_view = (EditText) findViewById(R.id.thing2_edit);
        thing3_view = (EditText) findViewById(R.id.thing3_edit);

        Cursor cursor = Common.getQueryCursor(this,Common.all_columns,"_ID="+row_id);

        if(cursor.moveToFirst()) {
            date = cursor.getString(1);
            date_view.setText(Common.dateToFormattedString(cursor.getString(1)));
            thing1_view.setText(cursor.getString(2));
            thing2_view.setText(cursor.getString(3));
            thing3_view.setText(cursor.getString(4));
        } else {
            //Toast that something went horribly wrong
        }

        cursor.close();
    }

    //Getter & Setter for date, static so date fragment can access it
    public static void setDate(String date_str) {
        date = date_str;
    }
    public static String getDate() { return date; }

    public void doneOrAddClicked(View view) {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        boolean error = false;
        String thing1,thing2,thing3;
        thing1 = ((EditText) findViewById(R.id.thing1_edit)).getText().toString();
        thing2 = ((EditText) findViewById(R.id.thing2_edit)).getText().toString();
        thing3 = ((EditText) findViewById(R.id.thing3_edit)).getText().toString();

        System.out.println("Things: "+thing1+" "+thing2+" "+thing3 + " Date: "+date);

        if(thing1.trim().equals("") && thing2.trim().equals("") && thing3.trim().equals("")) {
            error = true;
            Common.printToast(this,getString(R.string.nogoodthings));
        }

        Cursor cursor = db.query(DatabaseContract.Entry.TABLE_NAME, Common.id_date, DatabaseContract.Entry.COLUMN_NAME_DATE + " = '" + date +"'", null, null, null, null);

        if(cursor.getCount() > 0 && !error && !edit_mode) {
            error = true;
            Common.printToast(this,getString(R.string.datealreadyexists));
        }

        if(!error) {
            values.put(DatabaseContract.Entry.COLUMN_NAME_DATE,date);
            values.put(DatabaseContract.Entry.COLUMN_NAME_THING1,thing1);
            values.put(DatabaseContract.Entry.COLUMN_NAME_THING2,thing2);
            values.put(DatabaseContract.Entry.COLUMN_NAME_THING3,thing3);

            if (edit_mode) {
                db.update(DatabaseContract.Entry.TABLE_NAME, values, "_ID=" + id, null);
                setResult(Common.RESULT_EDIT);
            } else if (add_mode) {
                db.insert(DatabaseContract.Entry.TABLE_NAME, null, values);
                setResult(Common.RESULT_ADD);
            }
        }

        db.close();

        if(!error) finish();
    }

    public void cancelClicked(View view) {
        setResult(Common.RESULT_CANCEL);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return (true);
        }
        return(super.onOptionsItemSelected(item));
    }

}
