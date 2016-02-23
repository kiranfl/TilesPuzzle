package com.games.tilespuzzle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.games.tilespuzzle.R;

import java.util.ArrayList;

/**
 * @author  kiran on 1/18/2016.
 */
public class GridAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<Integer> resIds;

    public GridAdapter(Context c, ArrayList<Integer> resIds) {
        mContext = c;
        this.resIds = resIds;
    }

    @Override
    public int getCount() {
        return resIds.size();
    }

    @Override
    public Integer getItem(int position) {
        return resIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.grid_item, null);

        ImageView img = (ImageView) view.findViewById(R.id.imageicon);
        img.setImageResource(getItem(position));

        return view;
    }


}
