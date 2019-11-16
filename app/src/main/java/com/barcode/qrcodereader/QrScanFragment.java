package com.barcode.qrcodereader;


import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import helper.CameraSource;

public class QrScanFragment extends Fragment {
    private static final String TAG = "Barcode-reader";
    SurfaceView surfaceView;
    private CameraSource mCameraSource;
    private BarcodeDetector barcodeDetector;
    private String intentData;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            setHasOptionsMenu(true);
            getActivity().setTitle("QR Scanner");
            view = inflater.inflate(R.layout.fragment_qr_scan, container, false);
            int rc = ActivityCompat.checkSelfPermission((DrawerActivity) getActivity(), permission.CAMERA);
            surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
            if (rc == PackageManager.PERMISSION_GRANTED) {
                createCameraSource(true, false);
            } else {
                requestCameraPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            //boolean autoFocus = getActivity().getIntent().getBooleanExtra(AutoFocus,false);
            //boolean useFlash = getActivity().getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(true, false);
            try {
                if (ActivityCompat.checkSelfPermission(getActivity(),permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(surfaceView.getHolder());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_HANDLE_CAMERA_PERM);
        return;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        try {
            MenuItem item = menu.findItem(R.id.delete);
            if (item != null) {
                item.setVisible(false);
            }
            super.onPrepareOptionsMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("InlinedApi")
    private void createCameraSource(final boolean autoFocus, boolean useFlash) {
        try {
            final Context context = getActivity().getApplicationContext();
            barcodeDetector = new BarcodeDetector.Builder(context).build();
            CameraSource.Builder builder = new CameraSource.Builder(getActivity().getApplicationContext(), barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1600, 1024)
                    .setRequestedFps(15.0f);

            // make sure that auto focus is an available option
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                builder = builder.setFocusMode(
                        autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
            }

            mCameraSource = builder
                    .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                    .build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getActivity(),permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mCameraSource.start(surfaceView.getHolder());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    Log.e("TEST","Surface Changed");
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodeSparseArray=detections.getDetectedItems();
                    if(barcodeSparseArray.size()>0){
                        barcodeDetector.release();
                        DrawerActivity.ScanHelper scanHelper =new DrawerActivity.ScanHelper();
                        Fragment fragment=new ResultFragment();
                        Vibrator vibrator= (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(200);
                        }
                        Barcode barcode=barcodeSparseArray.valueAt(0);
                        final int type=barcode.valueFormat;
                        String barcodeType="";
                        String displayValue="";
                        switch (type){
                            case Barcode.CONTACT_INFO:
                                String organization="",title="",addressOne="",emailOne="",formattedName="",phoneOne="",urlOne="";
                                organization=barcode.contactInfo.organization;
                                title=barcode.contactInfo.title;
                                if(barcode.contactInfo.addresses.length>0){
                                    addressOne=barcode.contactInfo.addresses[0].addressLines[0].toString();
                                }
                                if(barcode.contactInfo.emails.length>0){
                                    emailOne=barcode.contactInfo.emails[0].address.toString();
                                }
                                if(barcode.contactInfo.phones.length>0){
                                    phoneOne=barcode.contactInfo.phones[0].number.toString();
                                }
                                if(barcode.contactInfo.urls.length>0){
                                    urlOne=barcode.contactInfo.urls[0].toString();
                                }
                                formattedName=barcode.contactInfo.name.formattedName;
                                barcodeType="Contact Info";

                                scanHelper.setOrgName(organization);
                                scanHelper.setTitle(title);
                                scanHelper.setAddress(addressOne);
                                scanHelper.setEmail(emailOne);
                                scanHelper.setName(formattedName);
                                scanHelper.setPhone(phoneOne);
                                scanHelper.setUrl(urlOne);
                                scanHelper.setBarcodeType(barcodeType);

                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).addToBackStack("test").commit();
                                break;

                            case Barcode.EMAIL:
                                String emailAddress="",emailSubject="",emailBody="",emailCC;
                                emailAddress=barcode.email.address;
                                emailSubject=barcode.email.subject;
                                emailBody=barcode.email.body;
                                barcodeType="Email";

                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setEmail(emailAddress);
                                scanHelper.setText2(emailSubject);
                                scanHelper.setText3(emailBody);

                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.ISBN:
                                displayValue=barcode.displayValue;
                                barcodeType="ISBN";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.PHONE:
                                displayValue=barcode.phone.number;
                                barcodeType="Phone";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.PRODUCT:
                                displayValue=barcode.displayValue;
                                barcodeType="Product";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.SMS:
                                String smsNumber="", smsMessage="";
                                smsNumber=barcode.sms.phoneNumber;
                                smsMessage=barcode.sms.message;
                                barcodeType="Sms";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setPhone(smsNumber);
                                scanHelper.setText2(smsMessage);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.TEXT:
                                displayValue=barcode.displayValue;
                                barcodeType="Text";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.URL:
                                displayValue=barcode.displayValue;
                                barcodeType="Url";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setUrl(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.WIFI:
                                String wifiSSID="",wifiPass="";
                                wifiSSID=barcode.wifi.ssid;
                                wifiPass=barcode.wifi.password;
                                barcodeType="WiFi";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(wifiSSID);
                                scanHelper.setText2(wifiPass);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.GEO:
                                String geoLat="",geoLng="";
                                geoLat= String.valueOf(barcode.geoPoint.lat);
                                geoLng= String.valueOf(barcode.geoPoint.lng);
                                barcodeType="Geo";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(geoLat);
                                scanHelper.setText2(geoLng);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.CALENDAR_EVENT:
                                //String calendarDesc=barcode.calendarEvent.description;
                                barcodeType="Calendar Event";
                                displayValue=barcode.displayValue;
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            case Barcode.DRIVER_LICENSE:
                                //String driverLicenseNum=barcode.driverLicense.licenseNumber;
                                barcodeType="Driver License";
                                displayValue=barcode.displayValue;
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                            default:
                                displayValue=barcode.displayValue;
                                barcodeType="Text";
                                scanHelper.setBarcodeType(barcodeType);
                                scanHelper.setText1(displayValue);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                                break;
                        }
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
