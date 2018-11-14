package com.simap.dishub.far;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Denny on 22/03/2016.
 */
public class Root extends Fragment {

    public Root() {
    }

    View rootView;

    public static Root newInstance(int navposisi) {
        Bundle args = new Bundle();
        args.putInt("id", navposisi);
        Root fragment = new Root();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.root, container, false);

        return rootView;
    }
}