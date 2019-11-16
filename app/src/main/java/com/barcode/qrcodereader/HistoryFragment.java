package com.barcode.qrcodereader;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment{

    private RecyclerView barcodeHistoryView;
    private List<ScanData> scanDataList=new ArrayList<>();
    private ScanAdapter scanAdapter;
    private SQLiteHelper mDatabase;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try{
            setHasOptionsMenu(true);
            view=inflater.inflate(R.layout.history_fragment,container,false);
            getActivity().setTitle("History");
            barcodeHistoryView=view.findViewById(R.id.historyRecyclerView);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
            barcodeHistoryView.setLayoutManager(linearLayoutManager);
            barcodeHistoryView.setHasFixedSize(true);
            barcodeHistoryView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));


            mDatabase=new SQLiteHelper(getContext());
            scanDataList=mDatabase.listOfScanData();

            if(scanDataList.size()>0){
                barcodeHistoryView.setVisibility(View.VISIBLE);
                scanAdapter=new ScanAdapter(scanDataList);
                barcodeHistoryView.setAdapter(scanAdapter);
            }else{
                barcodeHistoryView.setVisibility(View.GONE);
            }



            // row click listener
            barcodeHistoryView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), barcodeHistoryView, new RecyclerTouchListener.ClickListener() {

                @Override
                public void onClick(View view, int position) {
                    ScanData scanData=mDatabase.getSingleScanData(scanDataList.get(position).getId());
                    Fragment fragment=new ResultFragment();

                    DrawerActivity.ScanHelper scanHelper =new DrawerActivity.ScanHelper();
                    scanHelper.setId(scanData.getId());
                    scanHelper.setBarcodeType(scanData.getBarcodeType());
                    scanHelper.setOrgName(scanData.getOrgName());
                    scanHelper.setTitle(scanData.getTitle());
                    scanHelper.setAddress(scanData.getAddress());
                    scanHelper.setEmail(scanData.getEmail());
                    scanHelper.setName(scanData.getName());
                    scanHelper.setPhone(scanData.getPhone());
                    scanHelper.setUrl(scanData.getUrl());
                    scanHelper.setText1(scanData.getText1());
                    scanHelper.setText2(scanData.getText2());
                    scanHelper.setText3(scanData.getText3());
                    scanHelper.setScanDateTime(scanData.getScanDateTime());
                    //scanHelper.setResult(scanData.getResult());

                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                }

                @Override
                public void onLongClick(View view, final int position) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setMessage("Delete this?").
                            setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ScanData scanData=mDatabase.getSingleScanData(scanDataList.get(position).getId());
                                    int returnVal=mDatabase.deleteData(String.valueOf(scanData.getId()));
                                    if(returnVal==1){
                                        scanDataList.remove(position);
                                        scanAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(),  "Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).
                            setNegativeButton("No",null).
                            show();
                }
            }));
        }catch (Exception e){
            e.printStackTrace();
        }
       return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        try{
            MenuItem item=menu.findItem(R.id.action_settings);
            if(item!=null){
                item.setVisible(false);
            }
            super.onPrepareOptionsMenu(menu);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()){
                case R.id.delete:
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setMessage("Clear all?").
                            setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if(scanDataList.size()>0){
                                        boolean isDeleted=mDatabase.deleteAllDataFromTable();
                                        if(isDeleted){
                                            scanDataList.removeAll(scanDataList);
                                            scanAdapter.notifyDataSetChanged();
                                            Toast.makeText(getContext(),  "Deleted!", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getContext(),  "No Data for Clear!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", null).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

}
