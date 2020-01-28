package ma.moqf.moqf.news;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ma.moqf.moqf.R;
import ma.moqf.moqf.models.Article;
import ma.moqf.moqf.parsers.myXmlParser;
import ma.moqf.moqf.utils.HttpHelper;
import ma.moqf.moqf.utils.RequestPackage;


public class BackTaskLoader extends AsyncTaskLoader<List<Article>> {
    AppCompatActivity context;
    private List<Article> articles;
    public HashMap<String, String> param;


    public BackTaskLoader(AppCompatActivity context, HashMap param){
        super(context);
        this.context = context;
        this.param = param;
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (articles != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(articles);
        }
        else {
            forceLoad();
        }


    }

    @Override
    public List<Article> loadInBackground() {
//        URL url;
        List<Article> result;
        try {
            //create url object to point to the file location on internet
//            url = new URL(params[0]);
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint("http://www.moqf.ma/index.php");
            requestPackage.setMethod("GET");
            requestPackage.setParams(param);
            String response = HttpHelper.downloadFromFeed(requestPackage);
            result = myXmlParser.parsefeed(response);
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            final int msg ;
            if (ex.getMessage()!=null && ex.getMessage().equals("Exception: response code " + 404))
                msg =R.string.load_problem2;
            else msg = R.string.load_problem;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg,
                            Toast.LENGTH_LONG).show();
                }
            });

//            onCanceled(null);
        }
        return null;
    }



    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<Article> result) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }



    @Override public void deliverResult(List<Article> result) {

        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (result != null) {
                onReleaseResources(result);
            }
        }
        articles = result;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(result);
        }

    }



    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(List<Article> result) {
        super.onCanceled(result);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(result);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (articles != null) {
            onReleaseResources(articles);
            articles = null;
        }
    }



}