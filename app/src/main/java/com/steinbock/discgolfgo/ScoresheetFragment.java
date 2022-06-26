package com.steinbock.discgolfgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ScoresheetFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ScoresheetModel> mList;
    ScoresheetAdapter adapter;

    public ScoresheetFragment() {
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
        View view = inflater.inflate(R.layout.fragment_scoresheet, container, false);

        recyclerView = view.findViewById(R.id.scoresheet_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mList = new ArrayList<>();

        int[] scores2 = new int[9];
        for (int i = 0; i < scores2.length; i++) {
            scores2[i] = (int) Math.round(Math.random() * 7);
        }
        mList.add(new ScoresheetModel("Micah", scores2));

        int[] scores3 = new int[9];
        for (int i = 0; i < scores3.length; i++) {
            scores3[i] = (int) Math.round(Math.random() * 7);
        }
        mList.add(new ScoresheetModel("Haley", scores3));

        adapter = new ScoresheetAdapter(mList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}