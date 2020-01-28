package ma.moqf.moqf.detail;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import ma.moqf.moqf.ActivityDetail;
import ma.moqf.moqf.R;
import ma.moqf.moqf.models.Article;
import ma.moqf.moqf.parsers.myHtmlParser;
import ma.moqf.moqf.utils.HttpHelper;
import ma.moqf.moqf.utils.RequestPackage;


public class BackTask2 extends AsyncTask<String, Integer, Void> {
    AppCompatActivity context;
    ProgressBar pd;
    Article article;

    public BackTask2(AppCompatActivity context, Article article){
        this.context = context;
        this.article = article;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        //display progress dialog
        pd = (ProgressBar) context.findViewById(R.id.progressBar);
        pd.bringToFront();
        pd.setVisibility(View.VISIBLE);
    }

    protected Void doInBackground(String... params) {
//        URL url;
        try {
            //create url object to point to the file location on internet
//            url = new URL(params[0]);
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(article.getUrl());
            requestPackage.setMethod("GET");
            String response = HttpHelper.downloadFromFeed(requestPackage);
            myHtmlParser.parseArticle(response, article);
//            articles = myXmlParser.parsefeed(new InputSource(url.openStream()));
        }catch (Exception ex){
            ex.printStackTrace();
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.load_problem,
                            Toast.LENGTH_LONG).show();
                    if (pd != null) pd.setVisibility(View.GONE);
                }
            });

            Log.i("Unable to load ", "RSS");
            this.cancel(false);
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        //close dialog
            if (pd != null)
                pd.setVisibility(View.GONE);
        Intent myintent = new Intent(context, ActivityDetail.class);
        myintent.putExtra("article", article);
        context.startActivity(myintent);
        }
    }