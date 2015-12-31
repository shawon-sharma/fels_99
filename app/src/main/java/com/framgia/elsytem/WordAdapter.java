package com.framgia.elsytem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sharma on 12/28/15.
 */
public class WordAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<WordReturnByCategory> al;

    public WordAdapter(Context mContext, ArrayList<WordReturnByCategory> al) {
        //  super(mContext, R.layout.word, al);
        this.mContext = mContext;
        this.al = al;
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WordReturnByCategory item = (WordReturnByCategory) getItem(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.word, parent, false);
        TextView title = (TextView) row.findViewById(R.id.wordid);
        TextView name = (TextView) row.findViewById(R.id.textword);
        name.setText("Vietnamese");
        title.setText(item.getWordname());
        return row;
    }
}