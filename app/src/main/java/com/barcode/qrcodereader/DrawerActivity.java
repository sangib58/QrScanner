package com.barcode.qrcodereader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.fragment.app.Fragment;

//import com.google.android.gms.vision.CameraSource;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.gms.vision.barcode.Barcode;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.security.Policy;
import java.util.List;

import helper.CameraSource;


public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            //setTheme(R.style.splashScreenTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_drawer);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            //ad script start

           /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });

            mAdView=findViewById(R.id.adView);
            AdRequest adRequest=new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);*/

            //ad script end

            //test ad script start
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });

            mAdView=findViewById(R.id.adView);
            AdRequest adRequest=new AdRequest.Builder().addTestDevice("6444B67CB52C4D9C9CC98D93E5BD88FE").build();
            mAdView.loadAd(adRequest);
            //test ad script end

            Fragment fragment=new QrScanFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        try{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new QrScanFragment()).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        try{
            Fragment fragment=null;
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_scan) {
                fragment=new QrScanFragment();
            }
            else if(id==R.id.nav_generate){
                fragment=new QrGenerateFragment();
            }
            else if (id == R.id.nav_history) {
                fragment=new HistoryFragment();
            }
            else if(id==R.id.nav_like){
                String appPackageName="app.barcode.qrcodereader";
                try {
                    Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                    appStoreIntent.setPackage("com.android.vending");

                    startActivity(appStoreIntent);
                } catch (android.content.ActivityNotFoundException exception) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
            else if (id == R.id.nav_share) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_TEXT, "Hey i am using this QR Scanner App.Try it! https://play.google.com/store/apps/details?id=app.barcode.qrcodereader");
                startActivity(Intent.createChooser(share, "Share with"));
            }
            else if (id == R.id.nav_close) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }

            if(fragment!=null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).addToBackStack("scan").commit();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public static class ScanHelper {
        private static int Id;
        private static String BarcodeType;
        private static String OrgName;
        private static String Title;
        private static String Address;
        private static String Email;
        private static String Name;
        private static String Phone;
        private static String Url;
        private static String Text1;
        private static String Text2;
        private static String Text3;
        private static String ScanDateTime;
        //private static String Result;


        public static String getScanDateTime(){
            return ScanDateTime;
        }

        public static void setScanDateTime(String scanDateTime){
            ScanDateTime=scanDateTime;
        }

        public static int getId() {
            return Id;
        }

        public static void setId(int id) {
            Id = id;
        }

        public static String getBarcodeType() {
            return BarcodeType;
        }

        public static void setBarcodeType(String barcodeType) {
            BarcodeType = barcodeType;
        }

        public static String getOrgName() {
            return OrgName;
        }

        public static void setOrgName(String orgName) {
            OrgName = orgName;
        }

        public static String getTitle() {
            return Title;
        }

        public static void setTitle(String title) {
            Title = title;
        }

        public static String getAddress() {
            return Address;
        }

        public static void setAddress(String address) {
            Address = address;
        }

        public static String getEmail() {
            return Email;
        }

        public static void setEmail(String email) {
            Email = email;
        }

        public static String getName() {
            return Name;
        }

        public static void setName(String name) {
            Name = name;
        }

        public static String getPhone() {
            return Phone;
        }

        public static void setPhone(String phone) {
            Phone = phone;
        }

        public static String getUrl() {
            return Url;
        }

        public static void setUrl(String url) {
            Url = url;
        }

        public static String getText1() {
            return Text1;
        }

        public static void setText1(String text1) {
            Text1 = text1;
        }

        public static String getText2() {
            return Text2;
        }

        public static void setText2(String text2) {
            Text2 = text2;
        }

        public static String getText3() {
            return Text3;
        }

        public static void setText3(String text3) {
            Text3 = text3;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.drawer, menu);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                try{
                    Camera.Parameters parameters = CameraSource.mCamera.getParameters();
                    if(parameters.getFlashMode().equals("off")){
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    }else{
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    }
                    CameraSource.mCamera.setParameters(parameters);
                }catch (Exception e){
                    Log.d("Exp",e.getMessage());
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
