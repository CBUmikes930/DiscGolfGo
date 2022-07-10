package com.steinbock.discgolfgo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    List<CategoryModel> mList;
    List<DiscModel> list;

    public CategoryAdapter(List<CategoryModel> mList) {
        this.mList = mList;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel model = mList.get(position);
        if (model == null)
            return;

        holder.textView.setText(model.getCategory());

        boolean isExpanded = model.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if (isExpanded) {
            holder.imageView.setImageResource(R.drawable.dropup_arrow);
        } else {
            holder.imageView.setImageResource(R.drawable.dropdown_arrow);
        }

        DiscAdapter adapter = new DiscAdapter(list, holder.getAdapterPosition());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));;
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(adapter);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setExpanded(!model.isExpanded());
                list = model.getDiscs();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private TextView textView;
        private ImageView imageView;
        private RecyclerView recyclerView;

        public CategoryViewHolder(@NonNull View categoryView) {
            super(categoryView);

            linearLayout = itemView.findViewById(R.id.bag_category_lin);
            expandableLayout = itemView.findViewById(R.id.bag_category_rel);
            textView = itemView.findViewById(R.id.bag_category_txt);
            imageView = itemView.findViewById(R.id.bag_category_img);
            recyclerView = itemView.findViewById(R.id.bag_category_recycler_view);
        }
    }
}
