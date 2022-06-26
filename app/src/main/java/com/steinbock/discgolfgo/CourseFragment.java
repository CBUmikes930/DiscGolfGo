package com.steinbock.discgolfgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CourseFragment extends Fragment {

    MapFragment mapFragment = new MapFragment();
    ScoresheetFragment scoresheetFragment = new ScoresheetFragment();

    public CourseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.map_frame, mapFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.score_frame, scoresheetFragment).commit();

        return view;
    }
}