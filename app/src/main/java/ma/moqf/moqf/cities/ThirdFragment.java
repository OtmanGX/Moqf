package ma.moqf.moqf.cities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import ma.moqf.moqf.MainActivity;
import ma.moqf.moqf.R;
import ma.moqf.moqf.news.FirstFragment;


public class ThirdFragment extends Fragment {
    Map<String,String> cities;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("المدن والجهات");
        cities = new HashMap<>();
        cities.put("الرباط سلا القنيطرة",
                    "782786");
        cities.put("طنجة تطوان الحسيمة",
                "782789");
        cities.put("الدارالبيضاء سطات",
                        "782777");
        cities.put("سوس ماسة",
                "782787");
        cities.put("مراكش آسفي",
                "782783");
        cities.put("درعة تافيلالت",
                "782778");
        cities.put("كلميم واد نون",
                "782782");
        cities.put("الداخلة واد الدهب",
                "782792");
        cities.put("بني ملال خنيفرة",
                "782788");
        cities.put("فاس مكناس",
                "782784");
        cities.put("العيون الساقية الحمراء",
                "782791");
        cities.put("الشرق الريف",
                "782785");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView list = getView().findViewById(R.id.list);
        final String[] keys = new String[cities.size()+1];
        keys[0] = "كل الجهات";
        System.arraycopy(cities.keySet().toArray(new String[cities.size()]), 0, keys, 1, cities.size());
//        cities.put("كل الجهات", null);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.city_layout,R.id.textView, keys);

        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Intent myintent = new Intent(view.getContext(), WebViewActivity.class);
//                myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                myintent.putExtra("url", cities.get(keys[position]));
//                view.getContext().startActivity(myintent);
                final String city = keys[position];
                FirstFragment.city = city;
                HashMap<String, String> param = new HashMap<String, String>()
                    {{put("page", "search");put("sRegion", cities.get(city));put("sFeed", "rss");}};

                int size = MainActivity.navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    MainActivity.navigationView.getMenu().getItem(i).setChecked(false);
                }
                FirstFragment.loaderManager.destroyLoader(1);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, FirstFragment.newInstance
                                (param)).addToBackStack(null).commit();
            }
        });

    }
}
