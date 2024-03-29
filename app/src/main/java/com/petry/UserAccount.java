package com.petry;

/*
    * 사용자 게정 정보 모델 클래스
*/


public class UserAccount {

    private String idToken; //고유 토큰 (UID)
    private String emailId;
    private String password;

    public UserAccount() { }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
