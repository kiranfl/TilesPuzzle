package com.fission.tilespuzle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fission.tilespuzle.fragments.ChooseImageFragment;
import com.fission.tilespuzle.fragments.GameBoard1Fragment;
import com.fission.tilespuzle.fragments.GameBoard2Fragment;
import com.fission.tilespuzle.fragments.HelpFragment;

/**
 * @author kiran on 1/17/2016.
 */
public class HomeActivity extends AppCompatActivity {
    private ChooseImageFragment mChooseImageFragment;
    private GameBoard1Fragment mGameBoard1Fragment;
    private GameBoard2Fragment mGameBoardFragment2;
    private HelpFragment mHelpFragment;
    private Fragment mSelectedFragment;
    private FragmentTransaction ft;
    private FragmentManager mFragmentManager;

    private ActionBar mActionBar;

    public Bitmap splitImages[];
    public Bitmap previewImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mActionBar = getSupportActionBar();
        mActionBar.hide();

        SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        mFragmentManager = getSupportFragmentManager();
        if (isFirstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            startFragment(R.layout.fragment_help, null);
        } else {
            startFragment(R.layout.fragment_chooseimage, null);
        }
    }

    public void startFragment(int resId, Bundle bundle) {
        switch (resId) {
            case R.layout.fragment_chooseimage:
                mChooseImageFragment = new ChooseImageFragment();
                mSelectedFragment = mChooseImageFragment;
                break;
            case R.layout.fragment_gameboard1:
                mGameBoard1Fragment = new GameBoard1Fragment();
                mSelectedFragment = mGameBoard1Fragment;
                break;
            case R.layout.fragment_gameboard2:
                mGameBoardFragment2 = new GameBoard2Fragment();
                mSelectedFragment = mGameBoardFragment2;
                break;
            case R.layout.fragment_help:
                mHelpFragment = new HelpFragment();
                mSelectedFragment = mHelpFragment;
                break;
            default:
                break;
        }
        ft = mFragmentManager.beginTransaction();
        if (bundle != null) {
            mSelectedFragment.setArguments(bundle);
        }
        ft.replace(R.id.container, mSelectedFragment);
        if (!(mSelectedFragment instanceof HelpFragment)) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (mSelectedFragment instanceof GameBoard1Fragment) {
            if (mGameBoard1Fragment.previewBigImg.getVisibility() == View.VISIBLE) {
                mGameBoard1Fragment.previewBigImg.callOnClick();
                return;
            }
        }
        if (mSelectedFragment instanceof GameBoard2Fragment) {
            if (mGameBoardFragment2.previewBigImg.getVisibility() == View.VISIBLE) {
                mGameBoardFragment2.previewBigImg.callOnClick();
                return;
            }
        }
        if (mFragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
