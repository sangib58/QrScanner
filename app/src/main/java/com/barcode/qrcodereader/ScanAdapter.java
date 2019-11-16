package com.barcode.qrcodereader;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.MyViewHolder> {

    private List<ScanData> scanDataList;

    public ScanAdapter(List<ScanData> scanDataList){
        this.scanDataList =scanDataList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView type, detail, dateTime;

        public MyViewHolder(View view) {
            super(view);
            type = (TextView) view.findViewById(R.id.barcodeType);
            detail = (TextView) view.findViewById(R.id.barcodeDetail);
            dateTime = (TextView) view.findViewById(R.id.scanDate);
        }
    }

    @NonNull
    @Override
    public ScanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scan_list_history_row,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanAdapter.MyViewHolder myViewHolder, int position) {
        ScanData scanData=scanDataList.get(position);
        myViewHolder.type.setText(scanData.getBarcodeType());
        myViewHolder.detail.setText(scanData.getResult());
        myViewHolder.dateTime.setText(scanData.getScanDateTime());
    }

    @Override
    public int getItemCount() {
        return scanDataList.size();
    }
}
