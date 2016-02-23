package com.fission.tilespuzle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fission.tilespuzle.R;

/**
 * @author kiran on 1/28/2016.
 */
public class GameBoardAdapter extends BaseAdapter {


    private Context mContext;
    private Bitmap mBitmap[];

    public GameBoardAdapter(Context c, Bitmap bitmap[]) {
        mContext = c;
        mBitmap = bitmap;
    }

    @Override
    public int getCount() {
        return mBitmap.length;
    }

    @Override
    public Bitmap getItem(int position) {
        return mBitmap[position];
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
        view = inflater.inflate(R.layout.game_board_item, null);

        ImageView img = (ImageView) view.findViewById(R.id.imageicon);
        img.setImageBitmap(getItem(position));

        return view;
    }


}



