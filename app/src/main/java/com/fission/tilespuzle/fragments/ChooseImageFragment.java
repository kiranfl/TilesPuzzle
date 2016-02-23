package com.fission.tilespuzle.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

import com.fission.tilespuzle.HomeActivity;
import com.fission.tilespuzle.R;
import com.fission.tilespuzle.adapter.GridAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author kiran on 1/18/2016.
 */
public class ChooseImageFragment extends Fragment {

    private Spinner mOrderSP, mTypeSP;
    private int mSize, mGameType;
    private TypedArray icons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chooseimage, container, false);
        final GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        mOrderSP = (Spinner) view.findViewById(R.id.orderSp);
        mTypeSP = (Spinner) view.findViewById(R.id.typeSp);

        mTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mTypeSP.getItemAtPosition(position).toString().contains("1")) {
                    mGameType = 1;
                } else {
                    mGameType = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mOrderSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mOrderSP.getItemAtPosition(position).toString().contains("3")) {
                    mSize = 3;
                } else if (mOrderSP.getItemAtPosition(position).toString().contains("4")) {
                    mSize = 4;
                } else {
                    mSize = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<Integer> resIdAL = new ArrayList<>();
        icons = getResources().obtainTypedArray(R.array.selection_targets);
        int i = 0;
        while (true) {
            int id = icons.getResourceId(i, -1);
            if (id == -1) {
                break;
            }
            resIdAL.add(id);
            i++;
        }
        GridAdapter adapter = new GridAdapter(getActivity(), resIdAL);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    int resid = (int) gridView.getItemAtPosition(position);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resid);
                    splitBitmap(bitmap);
                } else {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(i, "select picture"), 1);
                }
            }
        });


        return view;
    }

    private void splitBitmap(Bitmap bitmap) {
        ((HomeActivity) getActivity()).previewImg = bitmap;
        float width = (float) Math.floor(bitmap.getWidth() / mSize);
        float height = (float) Math.floor(bitmap.getHeight() / mSize);
        int x = 0, y = 0;
        int count = 0;
        ((HomeActivity) getActivity()).splitImages = new Bitmap[mSize * mSize];
        for (int i = 0; i < (mSize * mSize); i++) {
            try {
                ((HomeActivity) getActivity()).splitImages[i] = Bitmap.createBitmap(bitmap, x, y,
                        (int) width, (int) height);
                count++;
                x += (int) (width);
                if (count == mSize) {
                    x = 0;
                    y += (int) (height);
                    count = 0;
                }
            } catch (IllegalArgumentException ia) {
                System.out.println("exception " + i);
                i = -1;
                x = 0;
                y = 0;
                width = width - 1;
                count = 0;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putInt("columns", mSize);

        if (mGameType == 1) {
            Bitmap blank = BitmapFactory.decodeResource(getResources(),
                    R.drawable.blank);
            ((HomeActivity) getActivity()).splitImages[(mSize * mSize) - 1] = Bitmap.createScaledBitmap(blank,
                    (int) width, (int) height, false);
            ((HomeActivity) getActivity()).startFragment(R.layout.fragment_gameboard1, bundle);
        } else {
            ((HomeActivity) getActivity()).startFragment(R.layout.fragment_gameboard2, bundle);
        }


    }

    public void onActivityResult(int request, int result, Intent data) {
        if ((request == 1) && (result == Activity.RESULT_OK)) {
            if (data != null) {
                super.onActivityResult(request, result, data);

                Uri selectedImage = data.getData();
                Bitmap yourSelectedImage = null;
                if (selectedImage != null) {
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(),
                            icons.getResourceId(2, 1));
                    try {
                        yourSelectedImage = android.provider.MediaStore.Images.Media
                                .getBitmap(getActivity().getContentResolver(),
                                        selectedImage);
                        yourSelectedImage = Bitmap.createScaledBitmap(
                                yourSelectedImage, originalBitmap.getWidth(),
                                originalBitmap.getHeight(), false);
                        splitBitmap(yourSelectedImage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
