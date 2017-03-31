package com.example.android.whizbang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.android.whizbang.database.WhizBangContract;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    SearchView mSearchView;
    ListView mListView;
    public static final int LOADER_ID = 26;
    private static final String TAG = "MainActivity";
    public static final String EMAIL_SUBJECT = "Main Street Cafe Billing";
    ArrayList<String> itemsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemsArrayList);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String[] selectedFromList = new String[]{mListView.getItemAtPosition(position).toString()};
                String[] projection = new String[]{WhizBangContract.WhizBangEntry.EMAIL_COLUMN};

                Cursor cr = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI,
                        projection,
                        null,
                        selectedFromList,
                        null);


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"thisistrevor4@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                intent.putExtra(Intent.EXTRA_TEXT, "This is the body of the email");

                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Snackbar.make(view, "There are no email clients installed.", Snackbar.LENGTH_LONG).show();
                }
                cr.close();
            }
        });

        mListView.setTextFilterEnabled(true);
        setupSearchView();
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try{

                    Cursor cr;
                    String[] projection = new String[]{WhizBangContract.WhizBangEntry.NAME_COLUMN};
                    cr = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI,
                            projection,
                            null,
                            null,
                            null);
                    return cr;

                }catch (Exception e){
                    Log.e(TAG, "loadInBackground: Failed to load data from database" );
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data == null){
            Log.e(TAG, "onLoadFinished: Error getting data" );
        }else {
            data.moveToFirst();
            while(!data.isAfterLast()){
                String info = data.getString(0);
                itemsArrayList.add(info);
                data.moveToNext();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
            return true;
        }

        if(id == R.id.add_applicant){
            Intent intent = new Intent(MainActivity.this, AddApplicantActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSearchView(){
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
    }
}
