package com.barcode.qrcodereader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;


public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
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

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });

            mAdView=findViewById(R.id.adView);
            AdRequest adRequest=new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            //ad script end

            //test ad script start
            /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });

            mAdView=findViewById(R.id.adView);
            AdRequest adRequest=new AdRequest.Builder().addTestDevice("6444B67CB52C4D9C9CC98D93E5BD88FE").build();
            mAdView.loadAd(adRequest);*/
            //test ad script end

            Fragment fragment=new QrScanFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        List fragmentList = getSupportFragmentManager().getFragments();
        String className=fragmentList.get(fragmentList.size()-1).getClass().getName();

        if(className.equals("com.barcode.qrcodereader.QrScanFragment")){
            //super.onBackPressed();
            finish();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new QrScanFragment()).commit();
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
                share.putExtra(Intent.EXTRA_TEXT, "Hey i am using this QR Scanner & Generator App.Try it! https://play.google.com/store/apps/details?id=app.barcode.qrcodereader");
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
    protected void onPause() {
        super.onPause();
    }

}
