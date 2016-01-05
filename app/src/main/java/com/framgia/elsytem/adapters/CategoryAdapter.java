package com.framgia.elsytem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.elsytem.CategoriesReturnFromPages;
import com.framgia.elsytem.R;
import com.framgia.elsytem.utils.Constants;
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
        title.setText(item.getCategoriesName());
        body.setText(Constants.Learned_words + item.getmLearnedWords());
        Picasso.with(mContext)
                .load(categoryResponses.get(position).getmCategoryImage())
                .resize(Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS, Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS)
                .centerCrop()
                .transform(new RoundedCornersTransformation(Constants
                        .AVATAR_WIDTH_HEIGHT_AND_RADIUS, Constants.ROUNDED_AVATAR_MARGIN))
                .into(image);
        return row;
    }
}
