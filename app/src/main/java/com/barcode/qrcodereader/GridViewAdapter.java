package com.barcode.qrcodereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private String[] names;
    private int[] imagesId;
    private LayoutInflater inflater;

    public GridViewAdapter(Context context,int[] imagesId, String[] names) {
        this.context = context;
        this.imagesId = imagesId;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        //return null;
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.gridview_layout,parent,false);
        }

        ImageView imageView= (ImageView) convertView.findViewById(R.id.imageViewId);
        TextView textView= (TextView) convertView.findViewById(R.id.textViewId);

        imageView.setImageResource(imagesId[position]);
        textView.setText(names[position]);

        return convertView;
    }
}
