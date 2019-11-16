package com.barcode.qrcodereader;

public class ScanData {
    private int Id;
    private String BarcodeType;
    private String OrgName;
    private String Title;
    private String Address;
    private String Email;
    private String Name;
    private String Phone;
    private String Url;
    private String Text1;
    private String Text2;
    private String Text3;
    private String ScanDateTime;
    private String Result;

    public ScanData(){

    }

    public ScanData(int id,String BarcodeType,String Result,String ScanDateTime) {
        this.Id=id;
        this.BarcodeType=BarcodeType;
        this.Result=Result;
        this.ScanDateTime=ScanDateTime;
    }

    public String getResult() {
        return Result;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getScanDateTime() {
        return ScanDateTime;
    }

    public void setScanDateTime(String scanDateTime) {
        ScanDateTime = scanDateTime;
    }

    public String getBarcodeType() {
        return BarcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.BarcodeType = barcodeType;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        this.OrgName = orgName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getText1() {
        return Text1;
    }

    public void setText1(String text1) {
        Text1 = text1;
    }

    public String getText2() {
        return Text2;
    }

    public void setText2(String text2) {
        Text2 = text2;
    }

    public String getText3() {
        return Text3;
    }

    public void setText3(String text3) {
        Text3 = text3;
    }
}
