package com.barcode.qrcodereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    int images[];
    String[] names;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context,int[] images,String[] names) {
        this.context=context;
        this.images=images;
        this.names=names;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.qr_type_row,null);
        ImageView imageView=view.findViewById(R.id.imageViewId);
        TextView textView=view.findViewById(R.id.spinnerTextId);
        imageView.setImageResource(images[i]);
        textView.setText(names[i]);
        return view;
    }
}
