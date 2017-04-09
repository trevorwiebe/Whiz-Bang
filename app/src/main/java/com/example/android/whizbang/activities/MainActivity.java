package com.example.android.whizbang.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
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
import android.view.View;

import com.example.android.whizbang.utils.ItemClickListener;
import com.example.android.whizbang.utils.ListAdapter;
import com.example.android.whizbang.R;
import com.example.android.whizbang.database.WhizBangContract;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    public static final int LOADER_ID = 37;
    private ListAdapter mAdapter;
    RecyclerView mRecyclerView;
    private ArrayList<String> clientListFirst = new ArrayList<>();
    private ArrayList<String> clientListInt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Vibrator mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new ItemClickListener(this, mRecyclerView, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = clientListInt.get(position);
                String name = clientListFirst.get(position);
                Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                String selectedInt = clientListInt.get(position);
                Intent intent = new Intent(MainActivity.this, EditDeleteActivity.class);
                intent.putExtra("int", selectedInt);
                startActivity(intent);
                mVibrator.vibrate(25);
            }
        }));
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

                    return getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY,
                            null,
                            null,
                            null,
                            WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN + " ASC");

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
        clientListFirst.clear();
        clientListInt.clear();
        if (data.moveToFirst()) {
            while (!data.isAfterLast()) {
                String first_name = data.getString(data.getColumnIndex(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN));
                String last_name = data.getString(data.getColumnIndex(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN));
                String id = data.getString(data.getColumnIndex(WhizBangContract.WhizBangEntry._ID));
                clientListFirst.add(first_name);
                clientListInt.add(id);
                data.moveToNext();
            }
        }
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
