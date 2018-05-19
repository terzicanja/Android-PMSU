package com.example.w10.pmsu.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.w10.pmsu.R;

public class MyFragment extends ListFragment {

    public static String USER_KEY = "rs.reviewer.USER_KEY";

    public static MyFragment newInstance() {

        MyFragment mpf = new MyFragment();

        return mpf;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
//
////        View view = inflater.inflate(R.layout.map_layout, vg, false);
//
//        return view;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.map_layout, vg, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

//        Cinema cinema = Mokap.getCinemas().get(position);
//
//        Intent intent = new Intent(getActivity(), DetailActivity.class);
//        intent.putExtra("name", cinema.getName());
//        intent.putExtra("descr", cinema.getDescription());
//        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();

        //Dodaje se adapter
//        CinemaAdapter adapter = new CinemaAdapter(getActivity());
//        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toast.makeText(getActivity(), "onAttach()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.activity_itemdetail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if(id == R.id.action_about){
//            Toast.makeText(getActivity(), "Refresh App", Toast.LENGTH_SHORT).show();
//        }
        return super.onOptionsItemSelected(item);
    }
}
