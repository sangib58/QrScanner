package com.barcode.qrcodereader;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class QrGenerateFragment extends Fragment {

    private String[] qrTypeNames={"Select QR Type","Contact Information","URL","E-mail","Telephone","SMS","Geo","Wi-Fi","Text"};
    private int images[]={R.drawable.ic_expand_more_24dp, R.drawable.ic_contacts, R.drawable.ic_web, R.drawable.ic_email_black, R.drawable.ic_local_phone,
            R.drawable.ic_sms, R.drawable.ic_place, R.drawable.ic_wifi, R.drawable.ic_text};


    private final static int WHITE = 0xFFFFFFFF;
    private final static int BLACK = 0xFF000000;
    //private final static int WIDTH = 500;
    //private final static int HEIGHT = 500;
    private int width,height;
    //public final static String STR = "MECARD:N:Owen,Sean;ADR:76 9th Avenue, 4th Floor, New York, NY 10011;TEL:12125551212;EMAIL:srowen@example.com;";

    private LinearLayout contactLayout,emailLayout,smsLayout,geoLayout,wifiLayout;
    private EditText text,url,phone,contactName,contactTitle,contactOrganization,contactPhone,
                        contactEmail,contactWeb,contactAddress,EmailMailTo,EmailCC,EmailSubject,EmailBody,
                        smsNumber,smsText,geoLatitude,geoLongitude,wifiType,wifiSSID,wifiPassword;
    private Button btnQrGenerate;
    private ImageView imageView,saveIconImageView,shareIconImageView;
    private int qrTypePosition,imageSizePosition;
    private String qrString;
    private Spinner qrTypeSpinner,imgSizSpinner;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;
    private View view;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            //QR Generate View
            setHasOptionsMenu(true);
            getActivity().setTitle("QR Generator");

            view=inflater.inflate(R.layout.fragment_qr_generate, container, false);

            btnQrGenerate=view.findViewById(R.id.btn_generate_qr);

            imageView = view.findViewById(R.id.resultImgId);
            saveIconImageView=view.findViewById(R.id.saveImageId);
            shareIconImageView=view.findViewById(R.id.shareImgId);

            imageView.setVisibility(View.GONE);
            saveIconImageView.setVisibility(View.GONE);
            shareIconImageView.setVisibility(View.GONE);

            contactLayout=view.findViewById(R.id.contactLayoutId);
            emailLayout=view.findViewById(R.id.emailLayoutId);
            smsLayout=view.findViewById(R.id.smsLayoutId);
            geoLayout=view.findViewById(R.id.geoLayoutId);
            wifiLayout=view.findViewById(R.id.wifiLayoutId);

            text=view.findViewById(R.id.textInputId);
            url=view.findViewById(R.id.urlInputId);
            phone=view.findViewById(R.id.telephoneInputId);

            contactName=view.findViewById(R.id.nameId);
            contactTitle=view.findViewById(R.id.titleId);
            contactOrganization=view.findViewById(R.id.organizationId);
            contactPhone=view.findViewById(R.id.phoneId);
            contactEmail=view.findViewById(R.id.emailId);
            contactWeb=view.findViewById(R.id.webId);
            contactAddress=view.findViewById(R.id.addressId);

            EmailMailTo=view.findViewById(R.id.mailtoId);
            EmailCC=view.findViewById(R.id.ccId);
            EmailSubject=view.findViewById(R.id.subjectId);
            EmailBody=view.findViewById(R.id.bodyId);

            smsNumber=view.findViewById(R.id.smsNumberId);
            smsText=view.findViewById(R.id.smsTextId);

            geoLatitude=view.findViewById(R.id.latitudeId);
            geoLongitude=view.findViewById(R.id.longitudeId);

            wifiType=view.findViewById(R.id.wifiTypeId);
            wifiSSID=view.findViewById(R.id.ssidId);
            wifiPassword=view.findViewById(R.id.passwordId);

            contactLayout.setVisibility(View.GONE);
            emailLayout.setVisibility(View.GONE);
            smsLayout.setVisibility(View.GONE);
            geoLayout.setVisibility(View.GONE);
            wifiLayout.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            url.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);


            imgSizSpinner=view.findViewById(R.id.spinner_img_size);
            ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),R.array.image_size,android.R.layout.simple_selectable_list_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            imgSizSpinner.setAdapter(adapter);
            imgSizSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    imageSizePosition=i;
                    if(imageSizePosition==1){
                        width=250;
                        height=250;
                    }else if(imageSizePosition==2){
                        width=300;
                        height=300;
                    }else if(imageSizePosition==3){
                        width=350;
                        height=350;
                    }else if(imageSizePosition==4){
                        width=400;
                        height=400;
                    }else if(imageSizePosition==5){
                        width=450;
                        height=450;
                    }else if(imageSizePosition==6){
                        width=600;
                        height=600;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            qrTypeSpinner=view.findViewById(R.id.spinner_qr_type);
            qrTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    qrTypePosition=i;
                    contactLayout.setVisibility(View.GONE);
                    emailLayout.setVisibility(View.GONE);
                    smsLayout.setVisibility(View.GONE);
                    geoLayout.setVisibility(View.GONE);
                    wifiLayout.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    url.setVisibility(View.GONE);
                    phone.setVisibility(View.GONE);

                    if(i==0){
                        contactLayout.setVisibility(View.GONE);
                        emailLayout.setVisibility(View.GONE);
                        smsLayout.setVisibility(View.GONE);
                        geoLayout.setVisibility(View.GONE);
                        wifiLayout.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                        url.setVisibility(View.GONE);
                        phone.setVisibility(View.GONE);
                    }else if(i==1){
                        contactLayout.setVisibility(View.VISIBLE);
                    }else if(i==2){
                        url.setVisibility(View.VISIBLE);
                    }else if(i==3){
                        emailLayout.setVisibility(View.VISIBLE);
                    }else if(i==4){
                        phone.setVisibility(View.VISIBLE);
                    }else if(i==5){
                        smsLayout.setVisibility(View.VISIBLE);
                    }else if(i==6){
                        geoLayout.setVisibility(View.VISIBLE);
                    }else if(i==7){
                        wifiLayout.setVisibility(View.VISIBLE);
                    }else if(i==8){
                        text.setVisibility(View.VISIBLE);
                    }

                    if((i>=1 &&i<=8) && imageView.getVisibility()==View.VISIBLE){
                        imageView.setVisibility(View.GONE);
                        saveIconImageView.setVisibility(View.GONE);
                        shareIconImageView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //Toast.makeText(getContext(),"onNothingSelected",Toast.LENGTH_SHORT).show();
                }
            });

            SpinnerAdapter spinnerAdapter=new SpinnerAdapter(getActivity(),images,qrTypeNames);
            qrTypeSpinner.setAdapter(spinnerAdapter);

            btnQrGenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        qrString="";
                        if(qrTypePosition==0){
                            Toast.makeText(getContext(),"Please Select QR Type",Toast.LENGTH_SHORT).show();
                        }
                        else if(imageSizePosition==0){
                            Toast.makeText(getContext(),"Please Select Image size",Toast.LENGTH_SHORT).show();
                        }else if(qrTypePosition==1){
                            String name,title,organization,phnNo,email,web,address;
                            name=contactName.getText().toString();
                            title=contactTitle.getText().toString();
                            organization=contactOrganization.getText().toString();
                            phnNo=contactPhone.getText().toString();
                            email=contactEmail.getText().toString();
                            web=contactWeb.getText().toString();
                            address=contactAddress.getText().toString();

                            qrString="BIZCARD:N:"+name+";T:"+title+";C:"+organization+";B:"+phnNo+";E:"+email+";U:"+web+";A:"+address+";";
                        }else if(qrTypePosition==2){
                            qrString="URL:"+url.getText().toString();
                        }else if(qrTypePosition==3){
                            String mailTo,cc,subject,body;
                            mailTo=EmailMailTo.getText().toString();
                            cc=EmailCC.getText().toString();
                            subject=EmailSubject.getText().toString();
                            body=EmailBody.getText().toString();

                            cc=cc.isEmpty()?"":cc;
                            subject=subject.isEmpty()?"":subject;
                            body=body.isEmpty()?"":body;

                            if(mailTo.isEmpty()){
                                Toast.makeText(getContext(),"Mail to cannot be empty",Toast.LENGTH_SHORT).show();
                            }else{
                                qrString="mailto:"+mailTo;
                                if(!cc.isEmpty()||!subject.isEmpty()||!body.isEmpty()){
                                    qrString+="?cc="+cc+"&subject="+subject+"&body="+body;
                                }
                            }
                        }else if(qrTypePosition==4){
                            qrString="tel:"+phone.getText().toString();
                        }else if(qrTypePosition==5){
                            String number,text;
                            number=smsNumber.getText().toString();
                            text=smsText.getText().toString();

                            number=number.isEmpty()?"":number;

                            if(text.isEmpty()){
                                Toast.makeText(getContext(),"Sms Text cannot be empty",Toast.LENGTH_SHORT).show();
                            }else{
                                qrString+="smsto:"+number+":"+text;
                            }

                        }else if(qrTypePosition==6){
                            String lat,longi;
                            lat=geoLatitude.getText().toString();
                            longi=geoLongitude.getText().toString();

                            lat=lat.isEmpty()?"":lat;
                            longi=longi.isEmpty()?"":longi;

                            qrString+="geo:"+lat+","+longi;
                            //qrString="geo:40.71872,-73.98905,100";
                        }else if(qrTypePosition==7){
                            String type,ssid,pass;
                            type=wifiType.getText().toString();
                            ssid=wifiSSID.getText().toString();
                            pass=wifiPassword.getText().toString();

                            type=type.isEmpty()?"":type;
                            ssid=ssid.isEmpty()?"":ssid;
                            pass=pass.isEmpty()?"":pass;

                            qrString+="WIFI:"+"T:"+type+";S:"+ssid+";P:"+pass+";";

                        }else if(qrTypePosition==8){
                            qrString=text.getText().toString();
                        }

                        if(qrTypePosition>=1&&qrTypePosition<=8){
                            if(qrString!=null && !qrString.isEmpty()){
                                try{
                                    imageView.setVisibility(View.VISIBLE);
                                    saveIconImageView.setVisibility(View.VISIBLE);
                                    shareIconImageView.setVisibility(View.VISIBLE);

                                    Bitmap bitmap = encodeAsBitmap(qrString,width,height);
                                    imageView.setImageBitmap(bitmap);
                                    clearSelection();
                                }catch (Exception e){
                                    Log.e("Error:",e.getMessage());
                                }
                            }else{
                                Log.e("Msg","Empty String");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            saveIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else{
                            BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
                            Bitmap bitmap = draw.getBitmap();

                            FileOutputStream outStream = null;
                            File sdCard = Environment.getExternalStorageDirectory();
                            File dir = new File(sdCard.getAbsolutePath() + "/QrScanner");
                            dir.mkdirs();
                            String fileName = String.format("%d.jpg", System.currentTimeMillis());
                            File outFile = new File(dir, fileName);
                            outStream = new FileOutputStream(outFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            outStream.flush();
                            outStream.close();

                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            intent.setData(Uri.fromFile(outFile));
                            getContext().sendBroadcast(intent);
                            Toast.makeText(getContext(),"Image saved",Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            shareIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap=draw.getBitmap();
                        File file = new File(getActivity().getExternalCacheDir(),"QRImg.png");
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        file.setReadable(true, false);
                        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        intent.setType("image/png");
                        startActivity(Intent.createChooser(intent, "Share image via"));
                    }catch (Exception e){
                        Log.e("TAG",e.getMessage());
                    }
                }
            });

        }catch (Exception e){
            //return e.getMessage();
            Log.e("MESSAGE",e.getMessage());
        }
        return view;
    }


    private Bitmap encodeAsBitmap(String str,int width,int height) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, height, null);
            //new MultiFormatWriter().encode()
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private void clearSelection(){
        qrTypeSpinner.setSelection(0);
        imgSizSpinner.setSelection(0);
        text.setText("");
        url.setText("");
        phone.setText("");
        contactName.setText("");
        contactTitle.setText("");
        contactOrganization.setText("");
        contactPhone.setText("");
        contactEmail.setText("");
        contactWeb.setText("");
        contactAddress.setText("");
        EmailMailTo.setText("");
        EmailCC.setText("");
        EmailSubject.setText("");
        EmailBody.setText("");
        smsNumber.setText("");
        smsText.setText("");
        geoLatitude.setText("");
        geoLongitude.setText("");
        wifiType.setText("");
        wifiSSID.setText("");
        wifiPassword.setText("");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(menu.hasVisibleItems()){
            menu.findItem(R.id.delete).setVisible(false);
            menu.findItem(R.id.action_settings).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }
}
