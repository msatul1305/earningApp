package com.example.earningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

public class WatchAndLearn extends AppCompatActivity {

    private WebView mywebview;
//    String Url="https://www.youtube.com/channel/UCMgqH1dSrZ_sktn5KLijXVw";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_and_learn);

        mywebview=(WebView)findViewById(R.id.webView);
        mywebview.getSettings().setJavaScriptEnabled(true);
//        mywebview.setWebViewClient(new MyWebViewClient());

        mywebview.loadUrl("https://www.youtube.com/channel/UCMgqH1dSrZ_sktn5KLijXVw");
    }
//    public class MyWebViewClient extends WebViewClient {
////        @Override
//        public boolean shouldOverrideURLLoading(WebView view,String url){
//            if (url.parse(url).getHost().equals("wwww.youtube.com")){
//                return false;
//            }
//            Intent intent=new Intent(Intent.ACTION_VIEW,Url.parse(url));
//            startActivity(intent);
//            return true;
//        }
//    }
}
