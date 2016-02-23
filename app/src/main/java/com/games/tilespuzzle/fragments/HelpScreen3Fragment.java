package com.games.tilespuzzle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.games.tilespuzzle.R;


/**
 * @author kiran on 2/20/2016.
 */
public class HelpScreen3Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_helpscreen3, container, false);
        return view;
    }
}
