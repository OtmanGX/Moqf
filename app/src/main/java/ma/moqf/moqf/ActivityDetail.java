/*
 *   Copyright 2018 BENAYAD OTMANE
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package ma.moqf.moqf;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ma.moqf.moqf.models.Article;
import me.relex.circleindicator.CircleIndicator;

public class ActivityDetail extends AppCompatActivity {

    public static Article article;
    private AppCompatActivity context;
    private ProgressBar pd;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> IMGArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        forceRTLIfSupported();
        article = getIntent().getParcelableExtra("article");
        IMGArray.add(article.getImageUrl());
        if (article.getOtherimages()!=null)
            for (String img: article.getOtherimages()) {
                IMGArray.add(img);
            }
        setWidgets();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, article.getUrl());

                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");

                startActivity(Intent.createChooser(intent, "Share"));
            }
        });
    }

    public void setWidgets() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ImageAdapter(this,IMGArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == IMGArray.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500);
        TextView textView1 = (TextView) findViewById(R.id.title);
        TextView textView2 = (TextView) findViewById(R.id.description);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        TextView textView3 = (TextView) findViewById(R.id.textView1);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        Button button= (Button) findViewById(R.id.button3);
        textView3.setText(textView3.getText()+article.getAuthor());
        textView1.setText(article.getTitle());
        textView2.setText(article.getDescription());
        textView4.setText(textView4.getText()+article.getCategory());
        textView5.setText(textView5.getText()+article.getDate());
        textView6.setText(textView6.getText()+article.getPrice());
        textView7.setText(textView7.getText()+article.getCity());
        if(!article.getEmail().equals("")) {
            button.setText(article.getEmail());
            button.setVisibility(Button.VISIBLE);
        }

        Button b = findViewById(R.id.button);
        b.setText(article.getTel());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri tel = Uri.parse("tel:"+((Button)v).getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, tel);
                startActivity(callIntent);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public void sendEmail(View view) {
        String[] TO = {article.getEmail()};
//        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
