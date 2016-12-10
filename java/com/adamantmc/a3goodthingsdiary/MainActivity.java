package com.adamantmc.a3goodthingsdiary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /*
        TODO:
            Check null values in columns when adding or editing
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addAdapter();
    }

    public void addAdapter() {

        Cursor cursor = Common.getQueryCursor(this, Common.all_columns,null);

        String[] fromCols = {DatabaseContract.Entry.COLUMN_NAME_DATE};
        int[] toViews = {R.id.item};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, fromCols, toViews, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int column) {
                if( column == 1 ){ //Column 1 is the date
                    TextView tv = (TextView) view;
                    String date = cursor.getString(cursor.getColumnIndex(DatabaseContract.Entry.COLUMN_NAME_DATE));
                    tv.setText(Common.dateToFormattedString(date));

                    if(cursor.getPosition() % 2 != 0) tv.setBackgroundResource(R.color.rowSecondaryColor);
                    else tv.setBackgroundResource(R.color.rowPrimaryColor);

                    return true;
                }
                return false;
            }
        });

        ListView lv = (ListView) findViewById(R.id.daylist);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, DayView.class);

                Cursor cursor = Common.getQueryCursor(MainActivity.this,Common.id_date,null);
                boolean cursorValid = cursor.move(position+1);

                if(cursorValid) {
                    intent.putExtra(DatabaseContract.Entry._ID,cursor.getInt(0));
                    startActivityForResult(intent, Common.REQUEST_CODE_FROM_MAIN);
                }
            }
        });

        lv.setAdapter(adapter);
    }

    public void addClicked(View view) {
        Intent intent = new Intent(this,DayEdit.class);
        intent.putExtra(Common.ADD_MODE,true);
        startActivityForResult(intent, Common.REQUEST_CODE_FROM_MAIN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Common.REQUEST_CODE_FROM_MAIN)
        {
            //If any child activity ended, reset data
            ListView lv = (ListView) findViewById(R.id.daylist);
            addAdapter();
        }
    }


}
