package com.framgia.elsytem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.framgia.elsytem.R;
import com.framgia.elsytem.jsonResponse.ShowUserResponse;
import com.framgia.elsytem.jsonResponse.ShowUserResponse.UserEntity.ActivitiesEntity;

import java.util.List;

/**
 * Created by avishek on 1/4/16.
 */
public class ProfileActivityListAdapter extends BaseAdapter {
    private List<ShowUserResponse.UserEntity.ActivitiesEntity> mActivities;
    private LayoutInflater myLayoutInflater;

    public ProfileActivityListAdapter(Context context, List<ShowUserResponse.UserEntity.ActivitiesEntity> activityList) {
        mActivities = activityList;
        myLayoutInflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mActivities.size();
    }

    @Override
    public Object getItem(int position) {
        return mActivities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mActivities.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mViewHolder holder;
        if (convertView == null) {
            holder = new mViewHolder();
            convertView = myLayoutInflater.inflate(R.layout.profile_list, null);
            holder.itemName = (TextView) convertView.findViewById(R.id.text_activity_name);
            holder.itemDate = (TextView) convertView.findViewById(R.id.text_date);
            convertView.setTag(holder);
        } else holder = (mViewHolder) convertView.getTag();
        ActivitiesEntity activityEntity = mActivities.get(position);
        String activityName = activityEntity.getContent();
        String activityDate = activityEntity.getCreated_at().substring(0, 10);
        holder.itemName.setText(activityName);
        holder.itemDate.setText(activityDate);
        return convertView;
    }

    private static class mViewHolder {
        TextView itemName;
        TextView itemDate;
    }
}
