package com.selfowner.krypto_tutor;

public class UploadNotes {
    String FileName;
    String Url;
    String Date;
    UploadNotes(){

    }

    public UploadNotes(String fileName, String url,String date) {
        FileName = fileName;
        Url = url;
        Date=date;
    }

    public String getFileName() {
        return FileName;
    }

    public String getUrl() {
        return Url;
    }

    public String getDate() {
        return Date;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setDate(String date) {
        Date = date;
    }
}
