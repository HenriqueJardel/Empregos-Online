package com.henrique.empregosonline.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.henrique.empregosonline.R;
import com.henrique.empregosonline.fragment.EmpregoFragment;
import com.henrique.empregosonline.fragment.PessoaFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0 :
                fragment = new PessoaFragment();
                break;
            case 1 :
                fragment = new EmpregoFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Pessoas";
            case 1:
                return "Empregos";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}