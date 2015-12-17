package com.framgia.elsytem;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    ListView listViewProfile;
    String[] formattedDate=new String[1];
    int images[]={R.drawable.e,R.drawable.b,R.drawable.ic_a};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate[0] = df.format(c.getTime());

        listViewProfile=(ListView)findViewById(R.id.listview);
        ViewAdapter v=new ViewAdapter(this,formattedDate);
        listViewProfile.setAdapter(v);
    }
}
class  ViewAdapter extends ArrayAdapter<String>
{
    Context c;
    String[] formattedDate;

    ViewAdapter(Context c,String[] formattedDate)
    {
     super(c,R.layout.profile_list,R.id.textView2,formattedDate);
     this.c=c;
        this.formattedDate=formattedDate;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflate= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflate.inflate(R.layout.profile_list,parent,false);
        ImageView i= (ImageView) row.findViewById(R.id.imageView);
        TextView t1= (TextView) row.findViewById(R.id.textView);
        TextView t2=(TextView)row.findViewById(R.id.textView2);
        t1.setText(formattedDate[position]);

        return row;
    }
}
