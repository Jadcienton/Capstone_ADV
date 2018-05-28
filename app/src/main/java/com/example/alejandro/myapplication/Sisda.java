package com.example.alejandro.myapplication;

public class Sisda {
    private String mSisdas = "";
    private String mPriority = "";
    private String mAddresses = "";
    private String mLevel = "";
    private String mProgresses = "";
    private String mDelay = "";
    private String mTime = "";

    public Sisda(String mSisdas, String mPriority, String mAddresses, String mLevel, String mProgresses, String mDelay, String mTime) {
        this.mSisdas = mSisdas;
        this.mPriority = mPriority;
        this.mAddresses = mAddresses;
        this.mLevel = mLevel;
        this.mProgresses = mProgresses;
        this.mDelay = mDelay;
        this.mTime = mTime;
    }
    public String getmSisdas(){
        return mSisdas;
    }
    public String getmPriority(){
        return mPriority;
    }
    public String getmAddresses(){
        return mAddresses;
    }
    public String getmLevel(){
        return mLevel;
    }
    public String getmProgresses(){
        return mProgresses;
    }
    public String getmDelay(){
        return mDelay;
    }
    public String getmTime(){
        return mTime;
    }

}
