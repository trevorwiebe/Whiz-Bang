package com.example.android.whizbang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.android.whizbang.database.WhizBangContract;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView mSearchView;
    ListView mListView;
    private static final String TAG = "MainActivity";
    ArrayList<String> itemsArrayList = new ArrayList<>();
    private ListAdapter mListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getNewData();

        mSearchView = (SearchView) findViewById(R.id.search_view);

        mListView = (ListView) findViewById(R.id.listview);

        mListAdapter = new ListAdapter(this, itemsArrayList);
        mListView.setAdapter(mListAdapter);

        setUp();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selected = mListView.getItemAtPosition(position).toString();

                Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);
                intent.putExtra("name", selected);
                startActivity(intent);

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = mListView.getItemAtPosition(position).toString();
                Intent editIntent = new Intent(MainActivity.this, EditDeleteActivity.class);
                editIntent.putExtra("name", selected);
                startActivity(editIntent);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemsArrayList.clear();
        getNewData();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        }else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(settings_intent);
            return false;
        }

        if(id == R.id.add_applicant){
            Intent intent = new Intent(MainActivity.this, AddNewClient.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUp() {
        mListView.setTextFilterEnabled(true);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
    }

    private void getNewData() {
        Cursor cr;
        String[] projection = new String[]{WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN, WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN};
        cr = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY,
                projection,
                null,
                null,
                null);

        if (cr == null) {
            Log.e(TAG, "onLoadFinished: Error getting data");
        } else {
            cr.moveToFirst();
            while (!cr.isAfterLast()) {
                String first_name = cr.getString(cr.getColumnIndex(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN));
                String last_name = cr.getString(cr.getColumnIndex(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN));
                String total_name = first_name + " " + last_name;
                itemsArrayList.add(total_name);
                cr.moveToNext();
            }
        }
        if (mListAdapter != null) {
            mListAdapter.notifyDataSetChanged();
        }
        cr.close();
    }
}
