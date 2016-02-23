package com.games.tilespuzzle.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.games.tilespuzzle.HomeActivity;
import com.games.tilespuzzle.R;
import com.games.tilespuzzle.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author kiran on 2/17/2016.
 */
public class GameBoard2Fragment extends Fragment implements View.OnTouchListener {
    private ImageView img[];
    private int ids[];
    private int lastTileId;
    private RelativeLayout helpRL;
    int right, left, down, up;
    private SharedPreferences mSharedPreferences;
    public ImageView lastTileIV, previewSmallImg, previewBigImg;
    private ArrayList<ImageView> neighbourViews = new ArrayList<>();
    private Random random = new Random();
    private int moves = 0;
    int id;
    float tempX, tempY;
    int tempId;
    private View view;
    private int num_cols;
    private int mIdsArray[] = {R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9, R.id.img10,
            R.id.img11, R.id.img12, R.id.img13, R.id.img14, R.id.img15, R.id.img16, R.id.img17, R.id.img18, R.id.img19, R.id.img20, R.id.img21,
            R.id.img22, R.id.img23, R.id.img24, R.id.img25};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gameboard2, container, false);
        final Button mStartBtn = (Button) view.findViewById(R.id.startBtn);
        previewSmallImg = (ImageView) view.findViewById(R.id.previewSmall);
        previewBigImg = (ImageView) view.findViewById(R.id.previewBig);
        previewSmallImg.setImageBitmap(((HomeActivity) getActivity()).previewImg);
        previewBigImg.setImageBitmap(((HomeActivity) getActivity()).previewImg);
        helpRL = (RelativeLayout) view.findViewById(R.id.helpLayout);
        previewSmallImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
            }
        });
        previewBigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
            }
        });
        Bundle bundle = getArguments();
        num_cols = bundle.getInt("columns");
        if (num_cols == 5) {
            img = new ImageView[num_cols * num_cols];
            ids = new int[num_cols * num_cols];
            for (int i = 0; i < img.length; i++) {
                img[i] = (ImageView) view.findViewById(mIdsArray[i]);
                img[i].setOnTouchListener(this);
                ids[i] = img[i].getId();
            }

        }
        if (num_cols == 4) {
            img = new ImageView[num_cols * num_cols];
            ids = new int[num_cols * num_cols];
            int j = 0;
            for (int i = 0; i < img.length; i++) {
                img[i] = (ImageView) view.findViewById(mIdsArray[j]);
                j++;
                if (j == 4) j++;
                if (j == 9) j++;
                if (j == 14) j++;
            }


            for (int i = 0; i < img.length; i++) {
                img[i].setOnTouchListener(this);
                ids[i] = img[i].getId();
            }
        }
        if (num_cols == 3) {
            img = new ImageView[num_cols * num_cols];
            ids = new int[num_cols * num_cols];
            int j = 0;
            for (int i = 0; i < img.length; i++) {
                img[i] = (ImageView) view.findViewById(mIdsArray[j]);
                j++;
                if (j == 3) {
                    j += 2;
                }
                if (j == 8) {
                    j += 2;
                }
            }


            for (int i = 0; i < img.length; i++) {
                img[i].setOnTouchListener(this);
                ids[i] = img[i].getId();
            }
        }


        for (int i = 0; i < num_cols * num_cols; i++) {
            img[i].setImageBitmap(((HomeActivity) getActivity()).splitImages[i]);
        }
        lastTileId = img[(num_cols * num_cols) - 1].getId();

        disableAll();

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showProgressBar(getActivity(), "Please Wait");
                moveTilesAutomatically(img[num_cols * num_cols - 2]);
                mStartBtn.setVisibility(View.GONE);
                helpRL.setVisibility(View.VISIBLE);
            }
        });
        mSharedPreferences = ((HomeActivity)getActivity()).sharedPreferences;
        return view;
    }

    private void disableAll() {
        for (ImageView anImg : img) {
            anImg.setEnabled(false);
        }
    }

    private void enableAll() {
        for (ImageView anImg : img) {
            anImg.setEnabled(true);
        }
    }

    private void zoomIn() {
        helpRL.setVisibility(View.GONE);
        previewBigImg.setVisibility(View.VISIBLE);
        ScaleAnimation scaleOutAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 1.0f);
        scaleOutAnimation.setDuration(300);
        scaleOutAnimation.setFillAfter(true);
        previewBigImg.startAnimation(scaleOutAnimation);
    }

    private void zoomOut() {
        ScaleAnimation scaleInAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 1.0f);
        scaleInAnimation.setDuration(300);
        previewBigImg.startAnimation(scaleInAnimation);
        scaleInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                previewBigImg.setVisibility(View.GONE);
                helpRL.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
    }

    private void moveTilesAutomatically(final View v) {
        moves++;
        if (lastTileId == v.getId() + 1) {
            if ((view.findViewById(v.getId()).getX() + view.findViewById(v.getId()).getWidth()) < (view.findViewById(mIdsArray[4]).getX() + view.findViewById(mIdsArray[4]).getWidth()))
                right = 1;
            up = 0;
            down = 0;
            left = 0;
        }
        if (lastTileId == v.getId() - 1) {
            if (view.findViewById(v.getId()).getX() != view.findViewById(R.id.img1).getX())
                left = 1;
            right = 0;
            up = 0;
            down = 0;
        }
        if (lastTileId == v.getId() - 5) {
            up = 1;
            left = 0;
            right = 0;
            down = 0;
        }
        if (lastTileId == v.getId() + 5) {
            down = 1;
            up = 0;
            left = 0;
            right = 0;
        }


        tempX = v.getX();
        tempY = v.getY();
        id = v.getId();


        lastTileIV = (ImageView) view.findViewById(lastTileId);


        if (right == 1) {
            right = 0;
            v.setX(lastTileIV.getX());
            v.setId(lastTileId);
            lastTileIV.setX(tempX);
            lastTileIV.setId(id);
            lastTileId = lastTileIV.getId();
            right = 0;
            left = 0;
            up = 0;
            down = 0;
            checkNeighbourViews();
        }
        if (left == 1) {
            left = 0;
            v.setX(lastTileIV.getX());
            v.setId(lastTileId);
            lastTileIV.setX(tempX);
            lastTileIV.setId(id);
            lastTileId = lastTileIV.getId();
            right = 0;
            left = 0;
            up = 0;
            down = 0;
            checkNeighbourViews();
        }
        if (up == 1) {
            up = 0;
            v.setY(lastTileIV.getY());
            v.setId(lastTileId);
            lastTileIV.setY(tempY);
            lastTileIV.setId(id);
            lastTileId = lastTileIV.getId();
            right = 0;
            left = 0;
            up = 0;
            down = 0;
            checkNeighbourViews();
        }
        if (down == 1) {
            down = 0;
            v.setY(lastTileIV.getY());
            v.setId(lastTileId);
            lastTileIV.setY(tempY);
            lastTileIV.setId(id);
            lastTileId = lastTileIV.getId();
            right = 0;
            left = 0;
            up = 0;
            down = 0;
            checkNeighbourViews();
        }

    }

    private void checkNeighbourViews() {
        neighbourViews.clear();
        if (moves < (num_cols * 30)) {
            if (lastTileIV.getX() != view.findViewById(mIdsArray[0]).getX()) {
                neighbourViews.add((ImageView) view.findViewById(lastTileId - 1));
            }
            if ((lastTileIV.getX() + lastTileIV.getWidth()) < (view.findViewById(mIdsArray[4]).getX() + view.findViewById(mIdsArray[4]).getWidth())) {
                neighbourViews.add((ImageView) view.findViewById(lastTileId + 1));
            }
            if (lastTileIV.getY() != view.findViewById(mIdsArray[0]).getY()) {
                neighbourViews.add((ImageView) view.findViewById(lastTileId - 5));
            }
            if ((lastTileIV.getY() + lastTileIV.getHeight()) < (view.findViewById(mIdsArray[24]).getY() + view.findViewById(mIdsArray[24]).getHeight())) {
                neighbourViews.add((ImageView) view.findViewById(lastTileId + 5));
            }

            int index = random.nextInt(neighbourViews.size());
            moveTilesAutomatically(neighbourViews.get(index));
        } else {
            Utils.hideProgressBar();
            enableAll();
            if(mSharedPreferences.getBoolean("showhelp_2", true)){
                SharedPreferences.Editor edit= mSharedPreferences.edit();
                edit.putBoolean("showhelp_2", false);
                edit.apply();
                showHelpDialog();
            }
        }

    }

    private void checkIsGameOver() {
        int k;
        for (k = 0; k < img.length; k++) {
            if (img[k].getId() != ids[k]) {
                break;
            }
        }
        if (k == img.length) {
            disableAll();
            showAlertDialog();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getRawX() - v.getWidth();
        final int y = (int) event.getRawY() - v.getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                v.setZ(0);
                checkSuitablePlace(v);
                break;
            case MotionEvent.ACTION_DOWN:
                tempX = v.getX();
                tempY = v.getY();
                tempId = v.getId();
                v.setZ(1);
                v.setX(x);
                v.setY(y);
            case MotionEvent.ACTION_MOVE:
                v.setX(x);
                v.setY(y);
                break;
            default:
                break;

        }
        return true;
    }

    private void checkSuitablePlace(View v) {
        int i;
        for (i = 0; i < ids.length; i++) {
            if (v.getId() == ids[i]) {
                continue;
            }
            final View tempView = view.findViewById(ids[i]);
            if ((v.getX() <= (tempView.getX() + (tempView.getWidth() / 2))) && (v.getY() <= (tempView.getY() + (tempView.getHeight() / 2)))) {
                if ((v.getX() >= tempView.getX()) && (v.getY() >= tempView.getY())) {
                    v.setX(tempView.getX());
                    v.setY(tempView.getY());
                    v.setId(tempView.getId());
                    tempView.animate().x(tempX).y(tempY).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator anim) {
                            tempView.setZ(1);
                            disableAll();
                        }

                        @Override
                        public void onAnimationEnd(Animator anim) {
                            super.onAnimationEnd(anim);
                            tempView.setX(tempX);
                            tempView.setY(tempY);
                            tempView.setId(tempId);
                            tempView.setZ(0);
                            enableAll();
                            checkIsGameOver();
                        }
                    }).start();


                    break;
                }
            }
            if ((v.getX() <= (tempView.getX() + (tempView.getWidth() / 2))) && ((v.getY() + v.getHeight()) >= (tempView.getY() + (tempView.getHeight() / 2)))) {
                if ((v.getX() >= tempView.getX()) && (v.getY() <= tempView.getY())) {
                    v.setX(tempView.getX());
                    v.setY(tempView.getY());
                    v.setId(tempView.getId());
                    tempView.animate().x(tempX).y(tempY).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator anim) {
                            tempView.setZ(1);
                            disableAll();
                        }

                        @Override
                        public void onAnimationEnd(Animator anim) {
                            super.onAnimationEnd(anim);
                            tempView.setX(tempX);
                            tempView.setY(tempY);
                            tempView.setId(tempId);
                            tempView.setZ(0);
                            enableAll();
                            checkIsGameOver();
                        }
                    }).start();

                    break;
                }
            }
            if (((v.getX() + v.getWidth()) >= (tempView.getX() + (tempView.getWidth() / 2))) && (v.getY() + v.getHeight()) >= (tempView.getY() + (tempView.getHeight() / 2))) {
                if ((v.getX() <= (tempView.getX())) && (v.getY() <= (tempView.getY()))) {
                    v.setX(tempView.getX());
                    v.setY(tempView.getY());
                    v.setId(tempView.getId());
                    tempView.animate().x(tempX).y(tempY).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator anim) {
                            tempView.setZ(1);
                            disableAll();
                        }

                        @Override
                        public void onAnimationEnd(Animator anim) {
                            super.onAnimationEnd(anim);
                            tempView.setX(tempX);
                            tempView.setY(tempY);
                            tempView.setId(tempId);
                            tempView.setZ(0);
                            enableAll();
                            checkIsGameOver();
                        }
                    }).start();

                    break;
                }
            }
            if (((v.getX() + v.getWidth()) >= (tempView.getX() + (tempView.getWidth() / 2))) && (v.getY() <= (tempView.getY() + (tempView.getHeight() / 2)))) {
                if ((v.getX() <= tempView.getX()) && (v.getY() >= tempView.getY())) {
                    v.setX(tempView.getX());
                    v.setY(tempView.getY());
                    v.setId(tempView.getId());
                    tempView.animate().x(tempX).y(tempY).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator anim) {
                            tempView.setZ(1);
                            disableAll();
                        }

                        @Override
                        public void onAnimationEnd(Animator anim) {
                            super.onAnimationEnd(anim);
                            tempView.setX(tempX);
                            tempView.setY(tempY);
                            tempView.setId(tempId);
                            tempView.setZ(0);
                            enableAll();
                            checkIsGameOver();
                        }
                    }).start();

                    break;
                }
            }
        }
        if (i == ids.length) {
            v.setX(tempX);
            v.setY(tempY);
            v.setId(tempId);
        }
    }

    private void showAlertDialog() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Congrats..");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((HomeActivity) getActivity()).onBackPressed();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Change Level and GameType and play agin with same pic or other");
        alertDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        alertDialog.show();
    }

    private void showHelpDialog() {
        final Dialog dialog = new Dialog(getActivity()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                this.dismiss();
                return true;
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.type2_help);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
