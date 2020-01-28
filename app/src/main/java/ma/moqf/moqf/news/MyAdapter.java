package ma.moqf.moqf.news;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ma.moqf.moqf.R;
import ma.moqf.moqf.categories.SecondFragment;
import ma.moqf.moqf.cities.ThirdFragment;
import ma.moqf.moqf.detail.BackTask2;
import ma.moqf.moqf.models.Article;

import android.widget.Filter;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    List<Article> list = null;
    List<Article> listFiltered = null;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public MyAdapter(List<Article> list) {
        this.list = list;

    }

    @Override
    public int getItemCount() {
        if (list == null) return 1;
        return list.size()+1;

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void setItems(List<Article> list) {
        this.list = list;
    }

    public void clearData() {
        if (list != null)
            list.clear();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.card_layout, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.header_layout, parent, false);
            return new VHHeader(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder)holder).mItem = list.get(position-1);
            //Article article = list.get(position);
            ((MyViewHolder)holder).display();
            setScaleAnimation(holder.itemView);
        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<Article> filteredList;
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {
                    filteredList = new ArrayList<>();
                    for (Article row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getCategory().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<Article>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView name, city, category;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();
        public String url;
        public ImageView imageView;
        public Article mItem;

        public MyViewHolder(final View itemView) {

            super(itemView);

            name = ((TextView) itemView.findViewById(R.id.name));

            city = ((TextView) itemView.findViewById(R.id.city));

            category = (TextView) itemView.findViewById(R.id.category);


            imageView = ((ImageView) itemView.findViewById(R.id.image));

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {
                    if (selectedItems.get(getAdapterPosition(), false)) {
                        selectedItems.delete(getAdapterPosition());
                        view.setSelected(false);
                    }
                    else {
                        selectedItems.put(getAdapterPosition(), true);
                        view.setSelected(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            StateListAnimator stateListAnimator = AnimatorInflater
                                    .loadStateListAnimator(view.getContext(), R.animator.lift_on_touch);
                            view.setStateListAnimator(stateListAnimator);
                        }
                    }

                    new BackTask2((AppCompatActivity) view.getContext(),mItem).execute();

                }

            });

        }


        public void display() {

            name.setText(mItem.getTitle());
            url  = mItem.getUrl();
            city.setText(mItem.getCity());
            category.setText(mItem.getCategory());
            if (mItem.getImageUrl().isEmpty()) imageView.setImageResource(R.drawable.no_photo);
            else
                Picasso.get().load(mItem.getImageUrl()).placeholder(R.drawable.placeholder)
                        .fit().into(imageView);

        }

    }

    class VHHeader extends RecyclerView.ViewHolder {
        Button button1, button2;

        public VHHeader(final View itemView) {
            super(itemView);
            button1 = (Button) itemView.findViewById(R.id.button1);
            button2 = (Button) itemView.findViewById(R.id.button2);
            button1.setText("الجهة: "+FirstFragment.city);
            button2.setText("التصنيف : "+FirstFragment.category);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)v.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new ThirdFragment()).addToBackStack(null).commit();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)v.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new SecondFragment()).addToBackStack(null).commit();
                }
            });
        }
    }


}
