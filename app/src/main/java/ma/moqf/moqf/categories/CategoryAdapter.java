package ma.moqf.moqf.categories;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ma.moqf.moqf.R;
import ma.moqf.moqf.models.theCategory;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<theCategory> categories;
    public CategoryAdapter(Context context, ArrayList<theCategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount(){
        return categories.size();
    }

    @Override
    public Object getItem(int position){
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.category_layout,parent, false);
        TextView t = (TextView)view.findViewById(R.id.title);
        ImageView img = view.findViewById(R.id.cat_img);
        img.setImageResource(categories.get(position).getIcon());
        t.setText(categories.get(position).getTitle());

        return view;
    }
}
