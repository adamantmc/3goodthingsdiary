package com.adamantmc.a3goodthingsdiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DayView extends AppCompatActivity {

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Day view on create called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        Intent intent = getIntent();

        Bundle data = intent.getExtras();
        id = intent.getIntExtra(DatabaseContract.Entry._ID, -1);
        setData(id);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        System.out.println("Day view saved");
        // Save the app state
        savedInstanceState.putLong(DatabaseContract.Entry._ID, id);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("Day view restored");
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        id = savedInstanceState.getLong(DatabaseContract.Entry._ID);
        setData(id);
    }

    public void onEditClick(View view) {
        Intent intent = new Intent(this, DayEdit.class);
        intent.putExtra(DatabaseContract.Entry._ID,id);
        startActivityForResult(intent, Common.REQUEST_CODE_FROM_VIEW);
    }

    public void onDeleteClick(View view) {
        AlertDialogFragment alert = new AlertDialogFragment();

        Bundle data = new Bundle();
        data.putLong(DatabaseContract.Entry._ID,id);
        alert.setArguments(data);

        alert.show(getFragmentManager(),"alertDialog");
    }

    public void setData(long row_id) {
        TextView date_view, thing1_view, thing2_view, thing3_view;

        date_view = (TextView) findViewById(R.id.day_view);
        thing1_view = (TextView) findViewById(R.id.thing1_view);
        thing2_view = (TextView) findViewById(R.id.thing2_view);
        thing3_view = (TextView) findViewById(R.id.thing3_view);

        Cursor cursor = Common.getQueryCursor(this,Common.all_columns,"_ID="+row_id);

        if(cursor.moveToFirst()) {
            date_view.setText(Common.dateToFormattedString(cursor.getString(1)));
            thing1_view.setText(cursor.getString(2));
            thing2_view.setText(cursor.getString(3));
            thing3_view.setText(cursor.getString(4));
        }

        cursor.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Common.REQUEST_CODE_FROM_VIEW)
        if(resultCode == Common.RESULT_EDIT) {
            setData(id);
        }
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
