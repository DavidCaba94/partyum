package com.example.david.partyum;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapterCuenta extends FragmentPagerAdapter {
    int numberOfTabs;
    public PagerAdapterCuenta(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabCuenta1Fragment tab1 = new TabCuenta1Fragment();
                return tab1;
            case 1:
                TabCuenta2Fragment tab2 = new TabCuenta2Fragment();
                return tab2;
            case 2:
                TabCuenta3Fragment tab3 = new TabCuenta3Fragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
