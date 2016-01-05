package com.framgia.elsytem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.elsytem.utils.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sharma on 12/24/15.
 */
public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<CategoriesReturnFromPages> categoryResponses;

    public CategoryAdapter(Context mContext, ArrayList<CategoriesReturnFromPages> categoryResponses) {
        this.mContext = mContext;
        this.categoryResponses = categoryResponses;
    }

    @Override
    public int getCount() {
        return categoryResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoriesReturnFromPages item = (CategoriesReturnFromPages) getItem(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.categories_list, parent, false);
        TextView title = (TextView) row.findViewById(R.id.textHeader);
        TextView body = (TextView) row.findViewById(R.id.textBody);
        ImageView image = (ImageView) row.findViewById(R.id.image_category);
        //title.setText(item.getId());
        title.setText(item.getCategoriesName());
        Picasso.with(mContext)
                .load(categoryResponses.get(position).getmCategoryImage())
                .resize(100, 100)
                .centerCrop()
                .transform(new RoundedCornersTransformation(100, 5))
                .into(image);
        return row;
    }
}