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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import ma.moqf.moqf.categories.SecondFragment;
import ma.moqf.moqf.cities.ThirdFragment;
import ma.moqf.moqf.news.FirstFragment;
import ma.moqf.moqf.utils.NetworkHelper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        forceRTLIfSupported();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        loadfeed();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }


    public void loadfeed() {
        if (!NetworkHelper.hasNetworkAccess(this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("المرجو التأكد من وجود مصدر للوصول للإنترنت")
                    .setTitle("هناك مشكلة في اﻻتصال")
                    .setCancelable(false)
                    .setPositiveButton("خروج",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    finish();
                                }
                            });

            AlertDialog alert = builder.create();
            alert.show();

        } else {
            callFirstFragment();
        }
    }

    public void callFirstFragment() {
        FirstFragment.param.clear();
        FirstFragment.category =  "الكل";
        FirstFragment.city = "الكل" ;
        if (FirstFragment.loaderManager!=null)
            FirstFragment.loaderManager.destroyLoader(1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, FirstFragment.newInstance(new HashMap<String, String>()
                {{put("page", "search");put("sFeed", "rss");}})).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            setTitle(R.string.app_name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(R.string.app_name);

            alertDialog.setMessage(Html.fromHtml(this.getString(R.string.info_text)
                    +"<a href='http://moqf.ma'>Moqf.</a>"));
            alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "موافق",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            callFirstFragment();
        }

        else if (id == R.id.categories) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new SecondFragment())
                    .addToBackStack(null).commit();
        } else if (id == R.id.cities) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new ThirdFragment())
                    .addToBackStack(null).commit();

        } else if (id == R.id.new_post) {
            Intent myintent = new Intent(getApplicationContext(), WebViewActivity.class);
            myintent.putExtra("url", "https://www.moqf.ma/index.php?page=item&action=item_add");
            startActivity(myintent);
        }
        else if (id == R.id.signup) {
            Intent myintent = new Intent(getApplicationContext(), WebViewActivity.class);
            myintent.putExtra("url", "https://www.moqf.ma/index.php?page=register&action=register");
            startActivity(myintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void openWebsite(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.moqf.ma/"));
        startActivity(i);
    }

    public void openFb(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MOQF.ma/"));
        startActivity(i);

    }
}
