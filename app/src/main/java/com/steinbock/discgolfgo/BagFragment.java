package com.steinbock.discgolfgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.ArrayList;
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

        List<DiscModel> driverList = new ArrayList<>();
        driverList.add(new DiscModel("Ape", "Get primal! A disc for the knuckle draggers. Speed and stability to overpower wind."));
        driverList.add(new DiscModel("Boss", "Let the Boss work for you. Excellent control and range sidearm or backhand."));
        driverList.add(new DiscModel("Dominator", "Be the master. A fast control driver to conquer the course."));

        List<DiscModel> midrangeList = new ArrayList<>();
        midrangeList.add(new DiscModel("Gator", "Quick and powerful, the Gator will wrestle the wind."));
        midrangeList.add(new DiscModel("Cro", "Count on the Cro. It was made for power lines and power players."));
        midrangeList.add(new DiscModel("Spider", "Weave your way to the target with this versatile Mid-Range."));
        midrangeList.add(new DiscModel("Skeeter", "Bitten by the disc golf bug? The Skeeter will repel bogeys."));
        midrangeList.add(new DiscModel("Panther", "A sleek hunter for wooded courses. For low, flat drives."));

        List<DiscModel> putterList = new ArrayList<>();
        putterList.add(new DiscModel("Hydra", "The Hydra floats in water. Three heads are better than one. It floats, it putts, and it approaches"));
        putterList.add(new DiscModel("Dart", "Zero in on the target. A straight flyer that will hit the mark"));
        putterList.add(new DiscModel("Aero", "First in flight. The original golf disc is still the straightest."));

        mList.add(new CategoryModel(driverList, "Drivers"));
        mList.add(new CategoryModel(midrangeList, "Mid-Range Discs"));
        mList.add(new CategoryModel(putterList, "Putters"));

        adapter = new CategoryAdapter(mList);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.addToBagButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getContext();
                main.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                        new AddToBagFragment()).commit();
                main.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}