package com.kamal.lucid;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;


public class UserPreferences extends AppCompatActivity {
    public static final String SharedPreferencesName="UserPreferncesSharedPreferencesLucidNotes";
    public static final String XShiftOfPen="XShiftfloat";
    public static final String YShiftOfPen="YShiftfloat";

    public float XShift=0f;
    public float YShift=0f;
    UserPreferences(){

    }
    public void SaveData(){
        SharedPreferences.Editor edit = UserPreferences.this.getSharedPreferences(UserPreferences.SharedPreferencesName, 0).edit();
        edit.putFloat(UserPreferences.XShiftOfPen,XShift);
        edit.putFloat(UserPreferences.YShiftOfPen,YShift);
        edit.apply();
    }
    public void loaddata() {
        XShift = UserPreferences.this.getSharedPreferences(SharedPreferencesName, 0).getFloat(XShiftOfPen, 0f);
        YShift = UserPreferences.this.getSharedPreferences(SharedPreferencesName, 0).getFloat(YShiftOfPen, 0f);
    }
    public void SetPenShift(Float shiftX,Float shiftY){
        XShift=shiftX;
        YShift=shiftY;
        SaveData();
    }

}
