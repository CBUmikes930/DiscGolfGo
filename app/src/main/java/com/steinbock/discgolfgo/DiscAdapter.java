package com.steinbock.discgolfgo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiscAdapter extends RecyclerView.Adapter<DiscAdapter.DiscViewHolder> {

    ViewGroup parent;
    private List<DiscModel> mList;
    private int parentPos;

    public DiscAdapter(List<DiscModel> mList, int parentPos) {
        this.mList = mList;
        this.parentPos = parentPos;
    }

    @NonNull
    @Override
    public DiscViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_disc, parent, false);
        this.parent = parent;
        return new DiscViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscViewHolder holder, int position) {
        DiscModel model = mList.get(position);
        holder.discNameTV.setText(model.getName());
        holder.discDescTV.setText(model.getDesc());

        boolean isExpanded = model.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setExpanded(!model.isExpanded());
                notifyItemChanged(holder.getAdapterPosition());

                // Find the category adapter
                CategoryAdapter a = ((MainActivity)parent.getContext()).bagFragment.adapter;
                // Update the category adapter's list
                a.list = a.mList.get(parentPos).getDiscs();
                // Tell the parent Recycler view that the item has changed
                a.notifyItemChanged(parentPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class DiscViewHolder extends RecyclerView.ViewHolder {

        private TextView discNameTV;
        private TextView discDescTV;
        private ConstraintLayout expandableLayout;
        private LinearLayout linearLayout;

        public DiscViewHolder(@NonNull View itemView) {
            super(itemView);
            discNameTV = itemView.findViewById(R.id.bag_disc_txt);
            discDescTV = itemView.findViewById(R.id.bag_disc_desc);
            expandableLayout = itemView.findViewById(R.id.bag_disc_con);
            linearLayout = itemView.findViewById(R.id.bag_disc_lin);
        }
    }

}
