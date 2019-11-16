package com.barcode.qrcodereader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String db_name="Barcode_db";
    private static final String table_name="Scan_detail";
    private static final String table_settings="Settings_detail";
    private static final int version_no=4;

    private static final String ID="Id";
    private static final String BarcodeType="BarcodeType";
    private static final String OrgName="OrgName";
    private static final String Title="Title";
    private static final String Address="Address";
    private static final String Email="Email";
    private static final String Name="Name";
    private static final String Phone="Phone";
    private static final String Url="Url";
    private static final String Text1="Text1";
    private static final String Text2="Text2";
    private static final String Text3="Text3";
    private static final String ScanDateTime="ScanDateTime";
    private static final String Result="Result";
    private static final String Status="Status";

    private static final String upgrade_table="create table "+table_name+"("+ID+" integer primary key autoincrement," +
            ""+BarcodeType+" nvarchar(100),"+OrgName+" text,"+Title+" text,"+Address+" text,"+Email+" text,"+Name+" text,"
            +Phone+" text,"+Url+" text,"+Text1+" text,"+Text2+" text,"+Text3+" text,"+ScanDateTime+" text,"+Result+" text)";

    private static final String settings_table="create table "+table_settings+"("+ID+" integer primary key autoincrement,"
            +Name+" nvarchar(100),"+Status+" integer)";

    private static final String delete_table_data="delete from "+table_name;

    private Context context;

    private static final String drop_table="drop table if exists "+table_name;

    public SQLiteHelper(Context context){
        super(context,db_name,null,version_no);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(upgrade_table);
            db.execSQL(settings_table);
            //Toast.makeText(context,"onCreate called",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            //Toast.makeText(context,"Exception"+e,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            Toast.makeText(context,"onUpgrade called",Toast.LENGTH_SHORT).show();
            db.execSQL(drop_table);
            onCreate(db);
        }catch (Exception e){
            //Toast.makeText(context,"Exception "+e,Toast.LENGTH_SHORT).show();
        }
    }

    /*public void insertSettingsData() {
        String sqlText="insert into Settings_detail(name,Status)VALUES(\"Auto Focus\",1),(\"Flash Light\",0),(\"Copy to ClipBoard\",0),(\"Beep\",0)";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(sqlText);
    }*/

   /* public int getCount(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from Settings_detail",null);
        int result=0;
        if(cursor!=null){
            cursor.moveToFirst();
            result=cursor.getInt(0);
        }

        return result;
    }*/

   public int getDuplicateCount(String currentDateTime,String resultHistory){
       int result=0;
       try{
           SQLiteDatabase db=this.getReadableDatabase();
           Cursor cursor=db.rawQuery("select * from "+table_name+" where ScanDateTime='"+currentDateTime+"' and Result='"+resultHistory+"'",null);
           //Cursor cursor=db.rawQuery("select * from "+table_name+" where ScanDateTime='"+currentDateTime+"'",null);
           if(cursor!=null && cursor.moveToFirst()){
               result=1;
           }
       }catch (Exception e){
           Log.e("Exp",e.getMessage());
       }

       return result;
   }

    public long insertData(String barcodeType,String orgName,String title,String address,String email,String name,String phone,String url,String text1,String text2,String text3,String scanDateTime,String result){
        try {
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

            ContentValues contentValues=new ContentValues();
            contentValues.put(BarcodeType,barcodeType);
            contentValues.put(OrgName,orgName);
            contentValues.put(Title,title);
            contentValues.put(Address,address);
            contentValues.put(Email,email);
            contentValues.put(Name,name);
            contentValues.put(Phone,phone);
            contentValues.put(Url,url);
            contentValues.put(Text1,text1);
            contentValues.put(Text2,text2);
            contentValues.put(Text3,text3);
            contentValues.put(ScanDateTime,scanDateTime);
            contentValues.put(Result,result);
            return sqLiteDatabase.insert(table_name,null,contentValues);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            return 0;
        }
    }
    /*public Cursor showAllData(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+table_name,null);
        return cursor;
    }*/


    public ArrayList<ScanData> listOfScanData(){
        String sql="select * from "+table_name;
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<ScanData> listData=new ArrayList<>();

        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                int id=Integer.parseInt(cursor.getString(0));
                String barcodeType=cursor.getString(1);
                String result=cursor.getString(13);
                String scanTime=cursor.getString(12);
                listData.add(new ScanData(id,barcodeType,result,scanTime));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return listData;
    }

    public ScanData getSingleScanData(int id){
        //String sql="select * from "+table_name+" where id="+id;
        ScanData scanData=new ScanData();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =
                db.query(table_name, // a. table
                        new String[]{ID,BarcodeType,OrgName,Title,Address,Email,Name,Phone,Url,Text1,Text2,Text3,ScanDateTime,Result}, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if(cursor!=null){
            cursor.moveToFirst();
            scanData.setId(cursor.getInt(0));
            scanData.setBarcodeType(cursor.getString(1));
            scanData.setOrgName(cursor.getString(2));
            scanData.setTitle(cursor.getString(3));
            scanData.setAddress(cursor.getString(4));
            scanData.setEmail(cursor.getString(5));
            scanData.setName(cursor.getString(6));
            scanData.setPhone(cursor.getString(7));
            scanData.setUrl(cursor.getString(8));
            scanData.setText1(cursor.getString(9));
            scanData.setText2(cursor.getString(10));
            scanData.setText3(cursor.getString(11));
            scanData.setScanDateTime(cursor.getString(12));
            scanData.setResult(cursor.getString(13));
        }
        cursor.close();
        return scanData;

    }

    public int deleteData(String id){
        try{
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            return  sqLiteDatabase.delete(table_name,ID+"=?",new String[]{id});
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            return 0;
        }
    }

    public boolean deleteAllDataFromTable(){
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL(delete_table_data);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
