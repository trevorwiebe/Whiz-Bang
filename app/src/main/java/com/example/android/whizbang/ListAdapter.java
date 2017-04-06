package com.example.android.whizbang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thisi on 4/5/2017.
 */
//
//public class ListAdapter extends BaseAdapter {
//
//    private ArrayList<String> mList;
//    private Context mContext;
//    private LayoutInflater mLayoutInflater = null;
//
//    public ListAdapter(Context context, ArrayList<String> list){
//
//        mContext = context;
//        mList = list;
//        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//    }
//
//    @Override
//    public int getCount() {
//        return mList.size();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
//        ListAdapter viewHolder;
//        if(convertView == null){
//            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = layoutInflater.inflate(R.layout.list_layout, null);
//            v.setTag(viewHolder);
//        }else{
//            viewHolder = (ListAdapter)v.getTag();
//        }
//        viewHolder.mT
//    }
//
//    private class ListAdapterViewHolder{
//        public TextView mTextView;
//        public ListAdapterViewHolder(View base){
//            mTextView = (TextView) base.findViewById(R.id.listview_item);
//        }
//    }
//}

public class ListAdapter extends BaseAdapter {
    private Activity mContext;
    private List<String> mList;
    private LayoutInflater mLayoutInflater = null;

    public ListAdapter(Activity context, List<String> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.mTVItem.setText(mList.get(position));
        return v;
    }
}

class CompleteListViewHolder {
    public TextView mTVItem;

    public CompleteListViewHolder(View base) {
        mTVItem = (TextView) base.findViewById(R.id.listview_item);
    }
}
