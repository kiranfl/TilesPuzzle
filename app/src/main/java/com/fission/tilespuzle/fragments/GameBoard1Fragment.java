package com.fission.tilespuzle.fragments;

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

import com.fission.tilespuzle.HomeActivity;
import com.fission.tilespuzle.R;
import com.fission.tilespuzle.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author kiran on 1/28/2016.
 */
public class GameBoard1Fragment extends Fragment implements View.OnTouchListener {
    private ImageView img[];
    private int ids[];
    private int blankId;
    private RelativeLayout helpRL;
    int right, left, down, up;
    public ImageView blankImage, previewSmallImg, previewBigImg;
    private ArrayList<ImageView> neighbourViews = new ArrayList<>();
    private Random random = new Random();
    private int moves = 0;
    int id;
    float tempX, tempY;
    private View view;
    private int num_cols;
    private SharedPreferences mSharedPreferences;
    private int mIdsArray[] = {R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9, R.id.img10,
            R.id.img11, R.id.img12, R.id.img13, R.id.img14, R.id.img15, R.id.img16, R.id.img17, R.id.img18, R.id.img19, R.id.img20, R.id.img21,
            R.id.img22, R.id.img23, R.id.img24, R.id.img25};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gameboard1, container, false);
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
        blankId = img[(num_cols * num_cols) - 1].getId();

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                moveTilesOnTouch(v, 200);
                break;
        }
        return true;
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

    private void moveTilesOnTouch(final View v, int animDuration) {
        {
            if (blankId == v.getId() + 1) {
                if ((view.findViewById(v.getId()).getX() + view.findViewById(v.getId()).getWidth()) < (view.findViewById(mIdsArray[4]).getX() + view.findViewById(mIdsArray[4]).getWidth()))
                    right = 1;
                up = 0;
                down = 0;
                left = 0;
            }
            if (blankId == v.getId() - 1) {
                if (view.findViewById(v.getId()).getX() != view.findViewById(R.id.img1).getX())
                    left = 1;
                right = 0;
                up = 0;
                down = 0;
            }
            if (blankId == v.getId() - 5) {
                up = 1;
                left = 0;
                right = 0;
                down = 0;
            }
            if (blankId == v.getId() + 5) {
                down = 1;
                up = 0;
                left = 0;
                right = 0;
            }


            tempX = v.getX();
            tempY = v.getY();
            id = v.getId();


            blankImage = (ImageView) view.findViewById(blankId);


            if (right == 1) {
//		    	 if(event.getRawX()>=xPos+5)
                {
                    right = 0;
                    disableAll();
                    v.animate().x(blankImage.getX()).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator anim) {
                            v.setZ(1);blankImage.setZ(0);
                        }

                        @Override
                        public void onAnimationEnd(Animator anim) {
                            super.onAnimationEnd(anim);

                            v.setX(blankImage.getX());
                            v.setId(blankId);
                            blankImage.setX(tempX);
                            blankImage.setId(id);
                            blankId = blankImage.getId();
                            right = 0;
                            left = 0;
                            up = 0;
                            down = 0;
                            enableAll();
                            v.setZ(0);blankImage.setZ(1);
                            checkIsGameOver();
                        }
                    }).start();
                }
            }
            if (left == 1) {
                left = 0;
                disableAll();
                v.animate().x(blankImage.getX()).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator anim) {
                        v.setZ(1);blankImage.setZ(0);
                    }

                    @Override
                    public void onAnimationEnd(Animator anim) {
                        super.onAnimationEnd(anim);
                        v.setX(blankImage.getX());
                        v.setId(blankId);
                        blankImage.setX(tempX);
                        blankImage.setId(id);
                        blankId = blankImage.getId();
                        right = 0;
                        left = 0;
                        up = 0;
                        down = 0;
                        enableAll();
                        v.setZ(0);blankImage.setZ(1);
                        checkIsGameOver();
                    }
                }).start();
            }
            if (up == 1) {
                up = 0;
                disableAll();
                v.animate().y(blankImage.getY()).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator anim) {
                        v.setZ(1);blankImage.setZ(0);
                    }

                    @Override
                    public void onAnimationEnd(Animator anim) {
                        super.onAnimationEnd(anim);
                        v.setY(blankImage.getY());
                        v.setId(blankId);
                        blankImage.setY(tempY);
                        blankImage.setId(id);
                        blankId = blankImage.getId();
                        right = 0;
                        left = 0;
                        up = 0;
                        down = 0;
                        enableAll();
                        v.setZ(0);blankImage.setZ(1);
                        checkIsGameOver();
                    }
                }).start();
            }
            if (down == 1) {
                down = 0;
                disableAll();
                v.animate().y(blankImage.getY()).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator anim) {
                     v.setZ(1);blankImage.setZ(0);
                    }

                    @Override
                    public void onAnimationEnd(Animator anim) {
                        super.onAnimationEnd(anim);
                        v.setY(blankImage.getY());
                        v.setId(blankId);
                        blankImage.setY(tempY);
                        blankImage.setId(id);
                        blankId = blankImage.getId();
                        right = 0;
                        left = 0;
                        up = 0;
                        down = 0;
                        enableAll();
                        v.setZ(0);blankImage.setZ(1);
                        checkIsGameOver();
                    }
                }).start();
            }

        }
    }

    private void moveTilesAutomatically(final View v) {
        moves++;
        if (blankId == v.getId() + 1) {
            if ((view.findViewById(v.getId()).getX() + view.findViewById(v.getId()).getWidth()) < (view.findViewById(mIdsArray[4]).getX() + view.findViewById(mIdsArray[4]).getWidth()))
                right = 1;
            up = 0;
            down = 0;
            left = 0;
        }
        if (blankId == v.getId() - 1) {
            if (view.findViewById(v.getId()).getX() != view.findViewById(R.id.img1).getX())
                left = 1;
            right = 0;
            up = 0;
            down = 0;
        }
        if (blankId == v.getId() - 5) {
            up = 1;
            left = 0;
            right = 0;
            down = 0;
        }
        if (blankId == v.getId() + 5) {
            down = 1;
            up = 0;
            left = 0;
            right = 0;
        }


        tempX = v.getX();
        tempY = v.getY();
        id = v.getId();


        blankImage = (ImageView) view.findViewById(blankId);


        if (right == 1) {
                right = 0;
                v.setX(blankImage.getX());
                v.setId(blankId);
                blankImage.setX(tempX);
                blankImage.setId(id);
                blankId = blankImage.getId();
                right = 0;
                left = 0;
                up = 0;
                down = 0;
                checkNeighbourViews();
        }
        if (left == 1) {
            left = 0;
            v.setX(blankImage.getX());
            v.setId(blankId);
            blankImage.setX(tempX);
            blankImage.setId(id);
            blankId = blankImage.getId();
            right = 0;
            left = 0;
            up = 0;
            down = 0;
            checkNeighbourViews();
        }
        if (up == 1) {
            up = 0;
            v.setY(blankImage.getY());
            v.setId(blankId);
            blankImage.setY(tempY);
            blankImage.setId(id);
            blankId = blankImage.getId();
            right = 0;
            left = 0;
            up = 0;
            down = 0;
            checkNeighbourViews();
        }
        if (down == 1) {
            down = 0;
            v.setY(blankImage.getY());
            v.setId(blankId);
            blankImage.setY(tempY);
            blankImage.setId(id);
            blankId = blankImage.getId();
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
            if (blankImage.getX() != view.findViewById(mIdsArray[0]).getX()) {
                neighbourViews.add((ImageView) view.findViewById(blankId - 1));
            }
            if ((blankImage.getX() + blankImage.getWidth()) < (view.findViewById(mIdsArray[4]).getX() + view.findViewById(mIdsArray[4]).getWidth())) {
                neighbourViews.add((ImageView) view.findViewById(blankId + 1));
            }
            if (blankImage.getY() != view.findViewById(mIdsArray[0]).getY()) {
                neighbourViews.add((ImageView) view.findViewById(blankId - 5));
            }
            if ((blankImage.getY() + blankImage.getHeight()) < (view.findViewById(mIdsArray[24]).getY() + view.findViewById(mIdsArray[24]).getHeight())) {
                neighbourViews.add((ImageView) view.findViewById(blankId + 5));
            }

            int index = random.nextInt(neighbourViews.size());
            moveTilesAutomatically(neighbourViews.get(index));
        } else {
            Utils.hideProgressBar();
            enableAll();
            if(mSharedPreferences.getBoolean("showhelp_1", true)) {
                SharedPreferences.Editor edit= mSharedPreferences.edit();
                edit.putBoolean("showhelp_1", false);
                edit.apply();
                showHelpDialog();
            }
        }

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
        dialog.setContentView(R.layout.type1_help);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
