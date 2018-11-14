package com.simap.dishub.far;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Denny on 10/24/2016.
 */
public class Logout extends Fragment {

    public Logout(){}
    RelativeLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.laporan, container, false);

        getActivity().setTitle("Logout");

        return view;
    }
}