package ma.moqf.moqf.categories;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ma.moqf.moqf.MainActivity;
import ma.moqf.moqf.R;
import ma.moqf.moqf.models.theCategory;
import ma.moqf.moqf.news.FirstFragment;

public class SecondFragment extends Fragment {
    ArrayList<theCategory> categories = new ArrayList<>();
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("التصنيفات");

        categories.add(new theCategory("كل التصنيفات", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", null);put("sFeed", "rss");}},
                R.drawable.ic__all_48px));

        categories.add(new theCategory("النجارة - الحدادة ", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "114");put("sFeed", "rss");}},
                R.drawable.cat1));

        categories.add(new theCategory("الزليج و الرخام", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "141");put("sFeed", "rss");}},
                R.drawable.cat2));

        categories.add(new theCategory("الماء والكهرباء", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "99");put("sFeed", "rss");}},
                R.drawable.cat3));

        categories.add(new theCategory("البناء", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "1");put("sFeed", "rss");}},
                R.drawable.cat4));

        categories.add(new theCategory("سمسار", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "4");put("sFeed", "rss");}},
                R.drawable.cat5));
//
        categories.add(new theCategory("دروس ودورات", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "3");put("sFeed", "rss");}},
                R.drawable.cat6));

        categories.add(new theCategory("ميكانيك", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "2");put("sFeed", "rss");}},
                R.drawable.cat7));

        categories.add(new theCategory("إلكترونيك", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "104");put("sFeed", "rss");}},
                R.drawable.cat8));

        categories.add(new theCategory("الصباغة - الجبص", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "6");put("sFeed", "rss");}},
                R.drawable.cat9));

        categories.add(new theCategory("السائقين", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "120");put("sFeed", "rss");}},
                R.drawable.cat10));

        categories.add(new theCategory("الخياطة والنسيج", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "131");put("sFeed", "rss");}},
                R.drawable.cat11));

        categories.add(new theCategory("الحلاقة و التجميل", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "151");put("sFeed", "rss");}},
                R.drawable.cat12));

        categories.add(new theCategory("الأفراح والمناسبات", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "150");put("sFeed", "rss");}},
                R.drawable.cat13));

        categories.add(new theCategory("خدمات فنية و أدبية", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "155");put("sFeed", "rss");}},
                R.drawable.cat14));

        categories.add(new theCategory("خدمات الحدائق و ديكورات", new HashMap<String, String>()
        {{put("page", "search");put("sCategory", "156");put("sFeed", "rss");}},
                R.drawable.cat15));

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_layout, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        CategoryAdapter myadapter = new CategoryAdapter(getActivity().getApplicationContext(), categories);
        ListView l = (ListView)getView().findViewById(R.id.list) ;
        l.setAdapter(myadapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent myintent = new Intent(view.getContext(), WebViewActivity.class);
//                myintent.putExtra("url", categories.get(position).getLink());
//                view.getContext().startActivity(myintent);
                FirstFragment.category = categories.get(position).getTitle();
                int size = MainActivity.navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    MainActivity.navigationView.getMenu().getItem(i).setChecked(false);
                }
                FirstFragment.loaderManager.destroyLoader(1);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, FirstFragment.newInstance(
                                (HashMap<String, String>) categories.get(position).getLink()))
                        .addToBackStack(null).commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
