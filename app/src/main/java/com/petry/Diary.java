package com.petry;

import java.util.Date;

public class Diary {
    private int id;
    private String diaryId;
    private String dTitle;
    private String dContent;
    private String dImgUrl;
    private String dImgName;
    private String dUploadDate;

    public Diary() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getdTitle() {
        return dTitle;
    }

    public void setdTitle(String dTitle) {
        this.dTitle = dTitle;
    }

    public String getdContent() {
        return dContent;
    }

    public void setdContent(String dContent) {
        this.dContent = dContent;
    }

    public String getdImgUrl() {
        return dImgUrl;
    }

    public void setdImgUrl(String dImgUrl) {
        this.dImgUrl = dImgUrl;
    }

    public String getdImgName() {
        return dImgName;
    }

    public void setdImgName(String dImgName) {
        this.dImgName = dImgName;
    }

    public String getdUploadDate() {
        return dUploadDate;
    }

    public void setdUploadDate(String dUploadDate) {
        this.dUploadDate = dUploadDate;
    }
}
