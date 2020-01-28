package ma.moqf.moqf.news;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import ma.moqf.moqf.R;
import ma.moqf.moqf.WebViewActivity;
import ma.moqf.moqf.models.Article;


public class FirstFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>>{
    View view;
    ProgressDialog pd;
    static boolean firstTime = true;
    private SearchView searchView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static String category="الكل";
    public static String city = "الكل";
    private static final String ARG_PARAM = "rss";
    public static HashMap<String, String> param = new HashMap<String, String>();
    private MyAdapter adapter;
    public static LoaderManager loaderManager;

    public FirstFragment() {
    }
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FirstFragment newInstance(HashMap<String, String> param) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        param.putAll((HashMap<String, String>)getArguments().getSerializable(ARG_PARAM));
        param.values().removeAll(Collections.singleton(null));
        loaderManager = getLoaderManager();
        adapter = new MyAdapter(null);
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = getView().findViewById(R.id.container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
        if (firstTime) {
            pd = new ProgressDialog(getActivity());
            pd.setTitle("تحميل البيانات جارٍ");
            pd.setMessage("انتظر رجاء");
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();
            firstTime = false;
        } else mSwipeRefreshLayout.setRefreshing(true);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getContext(), WebViewActivity.class);
                myintent.putExtra("url", "https://www.moqf.ma/index.php?page=item&action=item_add");
                startActivity(myintent);
            }
        });

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.list);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        loaderManager.initLoader(1, null, this);
//        refresh();
    }


    public void refresh() {
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> loader = loaderManager.getLoader(1);
        mSwipeRefreshLayout.setRefreshing(true);
        if(loader==null){
            loaderManager.initLoader(1, null, this).forceLoad();
        } else{
            loader.onContentChanged();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_refresh).setVisible(true);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchitem = menu.findItem(R.id.search);
        searchitem.setVisible(true);
        searchView = (SearchView) searchitem
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader with no arguments, so it is simple.
        return new BackTaskLoader((AppCompatActivity) getActivity(), param);
    }

    @Override public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        // Set the new data in the adapter.
        Log.i("Load", "finished");
        if (pd!=null && pd.isShowing()) pd.dismiss();
        mSwipeRefreshLayout.setRefreshing(false);
        if(data!=null) {
            adapter.setItems(data);
            adapter.notifyDataSetChanged();
        }

    }

    @Override public void onLoaderReset(Loader<List<Article>> loader) {
        // Clear the data in the adapter.
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.setItems(null);
        adapter.notifyDataSetChanged();
    }



}
