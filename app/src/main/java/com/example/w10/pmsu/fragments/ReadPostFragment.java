package com.example.w10.pmsu.fragments;

import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.w10.pmsu.R;

public class ReadPostFragment extends Fragment {

    View view;

    public ReadPostFragment(){

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .addSharedElement(R.id.image_view, "img_transition")
//                .replace(R.id.container, details)
//                .addToBackStack(null)
//                .commit();

//        view = inflater.inflate(R.layout.read_post_fragment,container,false);
//        return view;

        return inflater.inflate(R.layout.read_post_fragment, container, false);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//
//        TextView titleView = view.findViewById(R.id.title_view);
//        titleView.setText(getActivity().getIntent().getStringExtra("title"));
//    }

}
