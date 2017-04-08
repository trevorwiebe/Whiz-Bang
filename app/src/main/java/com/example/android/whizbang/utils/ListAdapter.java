package com.example.android.whizbang.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.whizbang.R;
import com.example.android.whizbang.database.WhizBangContract;

/**
 * Created by thisi on 4/5/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private static final String TAG = "ListAdapter";
    private Cursor mCursor;
    private Context mContext;

    public ListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(WhizBangContract.WhizBangEntry._ID);
        int clientFistName = mCursor.getColumnIndex(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN);
        int clientLastName = mCursor.getColumnIndex(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);

        String firstName = mCursor.getString(clientFistName);
        String lastName = mCursor.getString(clientLastName);
        String finishedName = firstName + " " + lastName;

        holder.itemView.setTag(id);
        holder.mDisplayClient.setText(finishedName);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }

        Cursor temp = mCursor;
        this.mCursor = cursor;

        if (cursor != null) {
            this.notifyDataSetChanged();
        }

        return temp;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView mDisplayClient;

        public ListViewHolder(View itemView) {
            super(itemView);

            mDisplayClient = (TextView) itemView.findViewById(R.id.listview_item);
        }
    }
}