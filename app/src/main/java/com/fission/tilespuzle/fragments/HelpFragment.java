package com.fission.tilespuzle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fission.tilespuzle.HomeActivity;
import com.fission.tilespuzle.R;

import java.util.ArrayList;

/**
 * @author kiran on 2/20/2016.
 */
public class HelpFragment extends Fragment {
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentsAL;
    private Button mNextBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.vpager);
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        mNextBtn = (Button) view.findViewById(R.id.nextBtn);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() == 2) {
                    ((HomeActivity) getActivity()).startFragment(R.layout.fragment_chooseimage, null);
                    return;
                }
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        });
        mFragmentsAL = new ArrayList<>();
        mFragmentsAL.add(new HelpScreen1Fragment());
        mFragmentsAL.add(new HelpScreen2Fragment());
        mFragmentsAL.add(new HelpScreen3Fragment());
        FragmentsPagerAdapter fragmentsPagerAdapter = new FragmentsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(fragmentsPagerAdapter);

        return view;
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position == 2){
                mNextBtn.setText("Got it");
            }
            else{
                mNextBtn.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class FragmentsPagerAdapter extends FragmentPagerAdapter {
        public FragmentsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsAL.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsAL.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
