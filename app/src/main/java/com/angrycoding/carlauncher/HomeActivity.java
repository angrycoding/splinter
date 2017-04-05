package com.angrycoding.carlauncher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.angrycoding.carlauncher.arduino.Arduino;
import com.angrycoding.carlauncher.arduino.ArduinoListener;
import com.angrycoding.carlauncher.webview.WebView;


public class HomeActivity extends Activity {

    private WebView homeScreenView;

    private ArduinoListener arduinoListener = new ArduinoListener() {
        @Override
        public void onReceive() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeScreenView = new WebView(this, null);
        homeScreenView.setBackgroundColor(Color.TRANSPARENT);
        homeScreenView.loadUrl("file:///android_asset/index.html");
        this.setContentView(homeScreenView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Arduino.removeListener(arduinoListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Arduino.addListener(arduinoListener);
    }

}