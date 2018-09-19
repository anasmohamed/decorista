package com.decorista.anas.decorista;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DepartmentFragment departmentFragment = new DepartmentFragment();
                return departmentFragment;
            case 1:
                PrivacyFragment policy = new PrivacyFragment();
                return policy;
            case 2:
                ConnectFragment connectFragment = new ConnectFragment();
                return connectFragment;
            case 3:
                AboutFragment aboutFragment = new AboutFragment();
                return aboutFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return context.getString(R.string.departments);

            case 1:
                return context.getString(R.string.agrement);

            case 2:
                return context.getString(R.string.connect);
            case 3:
                return context.getString(R.string.we_are);

            default:
                return null;
        }

    }
}
