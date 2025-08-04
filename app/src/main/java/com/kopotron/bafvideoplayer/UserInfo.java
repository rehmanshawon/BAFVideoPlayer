package com.kopotron.bafvideoplayer;

public class UserInfo {
    private String userName;
    private String password;
    private Boolean loggedIn=false;

    public void setUserName(String userName){
        this.userName=userName;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setLoggedIn(Boolean loggedIn){
        this.loggedIn=loggedIn;
    }
    public String getUserName(){return userName;}
    public String getPassword(){return password;}
    public Boolean getLoggedIn(){return loggedIn;}

    public UserInfo(String userName,String password,Boolean loggedIn){
        this.userName=userName;
        this.password=password;
        this.loggedIn=loggedIn;
    }

}
