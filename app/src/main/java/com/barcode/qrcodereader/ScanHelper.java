package com.barcode.qrcodereader;

public class ScanHelper {
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
