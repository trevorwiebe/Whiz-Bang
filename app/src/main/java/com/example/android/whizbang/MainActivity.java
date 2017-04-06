package com.example.android.whizbang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.whizbang.database.WhizBangContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    public static final int LOADER_ID = 37;
    private ListAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


//                String selected = mListView.getItemAtPosition(position).toString();
//                Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);
//                intent.putExtra("name", selected);
//                startActivity(intent);

//                String selected = mListView.getItemAtPosition(position).toString();
//                Intent editIntent = new Intent(MainActivity.this, EditDeleteActivity.class);
//                editIntent.putExtra("name", selected);
//                startActivity(editIntent);
//                return true;
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {

                    String[] projection = {WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN, WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN};

                    return getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "loadInBackground: Error in MainActivity getting data");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
}
