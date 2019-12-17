package com.barcode.qrcodereader;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import helper.CameraSource;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ResultFragment extends Fragment{
    private SQLiteHelper sqLiteHelper;
    DrawerActivity.ScanHelper scanHelper;
    private GridView iconGridView;
    private GridViewAdapter gridViewAdapter;
    private String result,resultHistory;
    private TextView headerDetail,scanHeader,scanResult;
    //private ImageView headerImg;
    private String[] contactIcons,emailIcons,smsIcons,urlIcons,otherIcons,wifiIcons;
    private int[] contactImages={R.drawable.ic_person_add,R.drawable.ic_email_black,R.drawable.ic_local_phone,
            R.drawable.ic_sms,R.drawable.ic_place,R.drawable.ic_launch_24dp,R.drawable.ic_content_copy,R.drawable.ic_share};
    private int[] emailImages={R.drawable.ic_email_black,R.drawable.ic_content_copy,R.drawable.ic_share};
    private int[] smsImages={R.drawable.ic_sms,R.drawable.ic_local_phone,R.drawable.ic_content_copy,R.drawable.ic_share};
    private int[] urlImages={R.drawable.ic_launch_24dp,R.drawable.ic_content_copy,R.drawable.ic_share};
    private int[] otherImages={R.drawable.ic_search_black,R.drawable.ic_content_copy,R.drawable.ic_share};
    private int[] wifiImages={R.drawable.ic_wifi,R.drawable.ic_share};
    private int insertVal;
    View view;
    private String rr;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try{
            bundle=this.getArguments();
            rr="";
            if(bundle!=null){
                rr=bundle.getString("insertTag");
                Log.v("ResultFragment",rr);
                //Toast.makeText(getContext(),rr,Toast.LENGTH_SHORT).show();
            }

            setHasOptionsMenu(true);
            view=inflater.inflate(R.layout.result_fragment,container,false);
            getActivity().setTitle("Result");
            sqLiteHelper=new SQLiteHelper(getContext());

            scanResult=view.findViewById(R.id.scanResult);
            scanHeader=view.findViewById(R.id.headerContent);
            headerDetail=view.findViewById(R.id.headerDetail);
            iconGridView=view.findViewById(R.id.iconGridView);
            //headerImg=view.findViewById(R.id.headerImg);

            Date date= Calendar.getInstance().getTime();
            String currentDateTime= DateFormat.getDateTimeInstance().format(date);
            scanHelper.setScanDateTime(currentDateTime);

            insertVal=scanHelper.getId();
            scanHelper.setId(0);

            if(scanHelper.getBarcodeType().equals("Contact Info")){
                result="";
                resultHistory=scanHelper.getName()+"\t \t"
                        +scanHelper.getOrgName()+"\t \t"+scanHelper.getEmail();
                if(!scanHelper.getName().isEmpty()){
                    result=scanHelper.getName()+"\n";
                }
                if(!scanHelper.getOrgName().isEmpty()){
                    result=result+scanHelper.getOrgName()+"\n";
                }
                if(!scanHelper.getEmail().isEmpty()){
                    result=result+scanHelper.getEmail()+"\n";
                }
                if(!scanHelper.getPhone().isEmpty()){
                    result=result+scanHelper.getPhone()+"\n";
                }
                if(scanHelper.getTitle()!=""){
                    result=result+scanHelper.getTitle()+"\n";
                }
                if(scanHelper.getAddress()!=""){
                    result=result+scanHelper.getAddress()+"\n";
                }
                if(!scanHelper.getUrl().isEmpty()){
                    result=result+scanHelper.getUrl();
                }
                //headerImg.setBackgroundResource(R.drawable.ic_contacts);
                contactIcons=getResources().getStringArray(R.array.icon_contacts);
                gridViewAdapter=new GridViewAdapter(getActivity(),contactImages,contactIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Email")){
                result=scanHelper.getEmail()+"\n"
                        +scanHelper.getText2()+"\n"
                        +scanHelper.getText3();

                resultHistory=scanHelper.getEmail()+"\t \t"
                        +scanHelper.getText2()+"\t \t"
                        +scanHelper.getText3();
                //headerImg.setBackgroundResource(R.drawable.ic_email_black);
                emailIcons=getResources().getStringArray(R.array.icon_email);
                gridViewAdapter=new GridViewAdapter(getActivity(),emailImages,emailIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Sms")){
                result=scanHelper.getPhone()+"\n"
                        //+scanHelper.getName()+"\n"
                        +scanHelper.getText2();

                resultHistory=scanHelper.getPhone()+"\t \t"
                        +scanHelper.getText2();
                //headerImg.setBackgroundResource(R.drawable.ic_sms);
                smsIcons=getResources().getStringArray(R.array.icon_sms);
                gridViewAdapter=new GridViewAdapter(getActivity(),smsImages,smsIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Url")){
                scanHelper.setBarcodeType("Url");
                result=scanHelper.getUrl();
                resultHistory=scanHelper.getUrl();
                urlIcons=getResources().getStringArray(R.array.icon_url);
                gridViewAdapter=new GridViewAdapter(getActivity(),urlImages,urlIcons);
            }
            else if(scanHelper.getBarcodeType().equals("WiFi")){
                result=scanHelper.getText3()+"\n"+scanHelper.getText1()+"\n"
                        +scanHelper.getText2();
                resultHistory=scanHelper.getText3()+"\t \t"+scanHelper.getText1()+"\t \t"
                        +scanHelper.getText2();
                //headerImg.setBackgroundResource(R.drawable.ic_wifi);
                wifiIcons=getResources().getStringArray(R.array.icon_wifi);
                gridViewAdapter=new GridViewAdapter(getActivity(),wifiImages,wifiIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Geo")){
                result=scanHelper.getText1()+"\n"
                        +scanHelper.getText2();
                resultHistory=scanHelper.getText1()+"\t \t"
                        +scanHelper.getText2();
                //headerImg.setBackgroundResource(R.drawable.ic_place);
                otherIcons=getResources().getStringArray(R.array.icon_others);
                gridViewAdapter=new GridViewAdapter(getActivity(),otherImages,otherIcons);
            }
            else if(scanHelper.getBarcodeType().equals("ISBN")){
                scanHelper.setBarcodeType("ISBN");
                result=scanHelper.getText1();
                resultHistory=scanHelper.getText1();
                otherIcons=getResources().getStringArray(R.array.icon_others);
                gridViewAdapter=new GridViewAdapter(getActivity(),otherImages,otherIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Phone")){
                scanHelper.setBarcodeType("Phone");
                result=scanHelper.getText1();
                resultHistory=scanHelper.getText1();
                otherIcons=getResources().getStringArray(R.array.icon_others);
                gridViewAdapter=new GridViewAdapter(getActivity(),otherImages,otherIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Product")){
                scanHelper.setBarcodeType("Product");
                result=scanHelper.getText1();
                resultHistory=scanHelper.getText1();
                otherIcons=getResources().getStringArray(R.array.icon_others);
                gridViewAdapter=new GridViewAdapter(getActivity(),otherImages,otherIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Calendar Event")){
                scanHelper.setBarcodeType("Calendar Event");
                result=scanHelper.getText1();
                resultHistory=scanHelper.getText1();
                otherIcons=getResources().getStringArray(R.array.icon_others);
                gridViewAdapter=new GridViewAdapter(getActivity(),otherImages,otherIcons);
            }
            else if(scanHelper.getBarcodeType().equals("Driver License")){
                scanHelper.setBarcodeType("Driver License");
                result=scanHelper.getText1();
                resultHistory=scanHelper.getText1();
                otherIcons=getResources().getStringArray(R.array.icon_others);
                gridViewAdapter=new GridViewAdapter(getActivity(),otherImages,otherIcons);
            }
            else{
                scanHelper.setBarcodeType("Text");
                result=scanHelper.getText1();
                resultHistory=scanHelper.getText1();
                otherIcons=getResources().getStringArray(R.array.icon_others);
                gridViewAdapter=new GridViewAdapter(getActivity(),otherImages,otherIcons);
            }

            if(insertVal==0 && rr=="qr"){
                //int duplicateCheck=sqLiteHelper.getDuplicateCount(currentDateTime,resultHistory);
                //if(duplicateCheck==0){
                sqLiteHelper.insertData(scanHelper.getBarcodeType(),scanHelper.getOrgName(),scanHelper.getTitle(),scanHelper.getAddress(),
                        scanHelper.getEmail(),scanHelper.getName(),scanHelper.getPhone(),scanHelper.getUrl(),scanHelper.getText1(),scanHelper.getText2(),scanHelper.getText3(),currentDateTime,resultHistory);
                //}
                bundle=null;
            }



            headerDetail.setText(scanHelper.getScanDateTime());
            scanHeader.setText(scanHelper.getBarcodeType());
            scanHeader.setTextColor(getResources().getColor(R.color.colorBlue));
            scanResult.setText(result.trim());

            iconGridView.setAdapter(gridViewAdapter);
            iconGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemText=parent.getItemAtPosition(position).toString();

                    if(itemText.equals("Add")){
                        // Creates a new Intent to insert a contact
                        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        // Sets the MIME type to match the Contacts Provider
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                        intent.putExtra(ContactsContract.Intents.Insert.NAME,scanHelper.getName());
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY,scanHelper.getOrgName());
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL,scanHelper.getEmail());
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE,scanHelper.getPhone());
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE,scanHelper.getTitle());
                        intent.putExtra(ContactsContract.Intents.Insert.NOTES,scanHelper.getAddress());
                        //intent.putExtra(ContactsContract.Intents.Insert.CON,scanHelper.getUrl());
                        startActivity(intent);
                    }else if(itemText.equals("Email")){
                        String emailAddress=scanHelper.getEmail();
                        String[] emails = new String[]{
                                emailAddress
                        };
                        if(emailAddress==null || emailAddress.isEmpty()){
                            Toast.makeText(getContext(),"No Email Address found",Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.putExtra(Intent.EXTRA_EMAIL, emails);
                            intent.putExtra(Intent.EXTRA_SUBJECT, scanHelper.getText2());
                            intent.putExtra(Intent.EXTRA_TEXT, scanHelper.getText3());
                            intent.setData(Uri.parse("mailto:"));

                            if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                                startActivity(intent);
                            }else{
                                // If there are no email client installed in this device
                                Toast.makeText(getContext(),"No email client installed in this device.",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else if(itemText.equals("Open Url")){
                        String url= scanHelper.getUrl();
                        if(url==null || url.isEmpty()){
                            Toast.makeText(getContext(),"No Url found",Toast.LENGTH_SHORT).show();
                        }else{
                            if(!url.startsWith("http://") && !url.startsWith("https://")){
                                url="http://"+url;
                            }
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    }
                    else if(itemText.equals("Call")){
                        String phoneNum= scanHelper.getPhone();
                        if(phoneNum==null || phoneNum.isEmpty()){
                            Toast.makeText(getContext(),"No Phone number found",Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent=new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+phoneNum));
                            startActivity(intent);
                        }
                    }else if(itemText.equals("SMS")){
                        String phoneNum= scanHelper.getPhone();
                        String smsText=scanHelper.getText2();
                        if(phoneNum==null || phoneNum.isEmpty()){
                            Toast.makeText(getContext(),"No Phone number found",Toast.LENGTH_SHORT).show();
                        }else{
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.setData(Uri.parse("sms:"+phoneNum));
                            sendIntent.putExtra("sms_body", smsText);
                            startActivity(sendIntent);
                        }
                    }else if(itemText.equals("Copy")){
                        String srcText=scanResult.getText().toString();
                        ClipboardManager clipboardManager=(ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData=ClipData.newPlainText("Source",srcText);
                        clipboardManager.setPrimaryClip(clipData);
                        Snackbar.make(view,"Text Copied",Snackbar.LENGTH_SHORT).show();
                    }else if(itemText.equals("Share")){
                        String shareText=scanResult.getText().toString();
                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        share.putExtra(Intent.EXTRA_TEXT, shareText);
                        startActivity(Intent.createChooser(share, "Share Text!"));
                    }else if(itemText.equals("Search on Google")){
                        String browseText=scanResult.getText().toString();
                        Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                        searchIntent.putExtra(SearchManager.QUERY, browseText);
                        if (searchIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(searchIntent);
                        }

                    }else if(itemText.equals("Find Place")){
                        String searchStr=scanHelper.getAddress();
                        try {
                            if(searchStr==null || searchStr.isEmpty()){
                                Toast.makeText(getActivity(),"No Place Found",Toast.LENGTH_SHORT).show();
                            }else{
                                String encodedStr= URLEncoder.encode(searchStr,"UTF-8");
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("https://www.google.com/maps/search/?api=1&query="+encodedStr));
                                startActivity(intent);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                    }else if(itemText.equals("Connect to Network")){
                        /*Log.v("nn1",scanHelper.getText1());
                        Log.v("nn2",scanHelper.getText2());
                        Log.v("nn3",scanHelper.getText3());*/

                        try{
                            //WifiConfiguration configuration=new WifiConfiguration();
                            //configuration.SSID="\""+scanHelper.getText1()+"\"";

                            if(scanHelper.getText3().equals("WEP")){
                                ConnectToNetworkWEP(scanHelper.getText1(),scanHelper.getText2());
                                /*configuration.wepKeys[0] = "\"" + scanHelper.getText2() + "\"";
                                configuration.wepTxKeyIndex = 0;
                                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);*/
                            }else if(scanHelper.getText3().equals("WPA/WPA2")){
                                ConnectToNetworkWPA(scanHelper.getText1(),scanHelper.getText2());
                                //configuration.preSharedKey = "\""+ scanHelper.getText2() +"\"";
                            }else{
                                //configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                            }

                            /*WifiManager wifiManager = (WifiManager)getContext().getApplicationContext().getSystemService(getContext().getApplicationContext().WIFI_SERVICE);
                            wifiManager.addNetwork(configuration);

                            if(configuration.SSID!=null && configuration.SSID.equals("\""+scanHelper.getText1()+"\"")){
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                wifiManager.disconnect();
                                wifiManager.enableNetwork(configuration.networkId,true);
                                wifiManager.reconnect();
                            }*/
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private boolean ConnectToNetworkWEP( String networkSSID, String password )
    {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain SSID in quotes
            conf.wepKeys[0] = "\"" + password + "\""; //Try it with quotes first

            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.SHARED);


            WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);

            if (networkId == -1){
                //Try it again with no quotes in case of hex password
                conf.wepKeys[0] = password;
                networkId = wifiManager.addNetwork(conf);
            }

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    break;
                }
            }

            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean ConnectToNetworkWPA( String networkSSID, String password )
    {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain SSID in quotes

            conf.preSharedKey = "\"" + password + "\"";

            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            Log.d("connecting", conf.SSID + " " + conf.preSharedKey);

            WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            wifiManager.addNetwork(conf);
            boolean chk= wifiManager.isWifiEnabled();

            Log.d("after connecting", conf.SSID + " " + conf.preSharedKey);


            if(conf.SSID != null && conf.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(conf.networkId, true);
                wifiManager.reconnect();
                Log.d("re connecting", conf.SSID + " " + conf.preSharedKey);
            }

            /*List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    Log.d("re connecting", i.SSID + " " + conf.preSharedKey);
                    break;
                }
            }*/


            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        try{
            if(menu.hasVisibleItems()){
                menu.findItem(R.id.delete).setVisible(false);
                menu.findItem(R.id.action_settings).setVisible(false);
            }
            super.onPrepareOptionsMenu(menu);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
