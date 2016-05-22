package com.example.najiwon.jsoup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {

    String url = "http://m.lottemart.com/mobile/cate/PMWMCAT0004_New.do?CategoryID=C001001700140006&ProductCD=8801166050784";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button titleButton = (Button) findViewById(R.id.titlebutton);
        Button descButton = (Button)findViewById(R.id.descbutton);
        Button logoButton = (Button)findViewById(R.id.logobutton);

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                url="http://m.lottemart.com/mobile/cate/PMWMCAT0004_New.do?CategoryID=C001001700140006&ProductCD="+editText.getText().toString();
                new Title().execute();
            }
        });

        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Description().execute();
            }
        });

        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logo().execute();
            }
        });
    }

    private class Title extends AsyncTask<Void, Void, Void> //첫번째 텍스트
    {
        String title;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Title");//이름 설정
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void ... params){
            try {
                Document document = Jsoup.connect(url).get();
                title = document.title();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            TextView textTitle = (TextView) findViewById(R.id.titletxt);
            textTitle.setText(title);
            progressDialog.dismiss();
        }
    }

    private class Description extends AsyncTask<Void, Void, Void> //두번째 텍스트
    {
        String desc;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("goodstitle");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void ... params){
            try {
                Document document = Jsoup.connect(url).get();
                Elements description = document.select("div.title-bar h2.goodstitle");//제품명 출력
                desc = description.text();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

            TextView txtDesc = (TextView) findViewById(R.id.desctxt);
            txtDesc.setText(desc);
            progressDialog.dismiss();
        }
    }

    private class Logo extends AsyncTask<Void, Void, Void> //이미지 출
    {
        Bitmap bitMap;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Logo");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void ... params){
            try {
                Document document = Jsoup.connect(url).get();
                Element img = document.select("div#product-list ul.swiper-wrapper li.swiper-slide img").first();
                String srcValue = img.attr("src");
                InputStream input = new URL(srcValue).openStream();
                bitMap = BitmapFactory.decodeStream(input);

            } catch(IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            ImageView logoImg = (ImageView) findViewById(R.id.logo);
            logoImg.setImageBitmap(bitMap);
            progressDialog.dismiss();
        }
    }
}





