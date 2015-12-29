package com.framgia.elsytem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.elsytem.model.Result;

import java.util.ArrayList;

/**
 * Created by ahsan on 12/21/15.
 */
public class ResultAdapter extends ArrayAdapter<Result> {
    ArrayList<Result> results;
    Context context;

    public ResultAdapter(Context context, ArrayList<Result> results) {
        super(context, R.layout.result, results);
        this.context = context;
        this.results = results;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.result, null);
            viewHolder.txtWord = (TextView) view.findViewById(R.id.word_name);
            viewHolder.txtlanguage = (TextView) view.findViewById(R.id.language);
            viewHolder.sign = (ImageView) view.findViewById(R.id.sign);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        Result result = results.get(position);
        viewHolder.txtWord.setText(result.word);
        viewHolder.txtlanguage.setText(result.language);
        int answer = result.state;
        Drawable res_status;
        if (answer == 0)
            res_status = ContextCompat.getDrawable(context, R.drawable.ic_clear_black_24dp);
        else
            res_status = ContextCompat.getDrawable(context, R.drawable.ic_check);
        viewHolder.sign.setImageDrawable(res_status);
        return view;
    }

    class ViewHolder {
        TextView txtWord;
        TextView txtlanguage;
        ImageView sign;
    }
}
