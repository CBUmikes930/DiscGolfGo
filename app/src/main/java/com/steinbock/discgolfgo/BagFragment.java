package com.steinbock.discgolfgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BagFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CategoryModel> mList;
    CategoryAdapter adapter;

    public BagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        recyclerView = view.findViewById(R.id.bag_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mList = new ArrayList<>();
        CategoryModel[] temp = new CategoryModel[3];

        DBHelper.getInstance().getDiscsByType("Driver", discs -> {
            temp[0] = new CategoryModel(discs, "Drivers");
            mList = Arrays.asList(temp);
            adapter = new CategoryAdapter(mList);
            recyclerView.setAdapter(adapter);
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
        });

        DBHelper.getInstance().getDiscsByType("Mid-Range", discs -> {
            temp[1] = new CategoryModel(discs, "Mid-Range");
            mList = Arrays.asList(temp);
            adapter = new CategoryAdapter(mList);
            recyclerView.setAdapter(adapter);
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
        });

        DBHelper.getInstance().getDiscsByType("Putter", discs -> {
            temp[2] = new CategoryModel(discs, "Putters");
            mList = Arrays.asList(temp);
            adapter = new CategoryAdapter(mList);
            recyclerView.setAdapter(adapter);
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
        });


        view.findViewById(R.id.addToBagButton).setOnClickListener(view1 -> {
            MainActivity main = (MainActivity) getContext();
            main.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                    new AddToBagFragment(), "ADD_TO_BAG").commit();
            main.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        });

        return view;
    }
}