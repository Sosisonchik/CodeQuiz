package com.example.apple.codequiz.Tools;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.apple.codequiz.Fragments.CategoriesFragment;
import com.example.apple.codequiz.Fragments.RankFragment;

public class NavigationAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments = {new CategoriesFragment(),new RankFragment()};
    private BottomNavigationView navigationView;

    public NavigationAdapter(FragmentManager fm, BottomNavigationView navigationView) {
        super(fm);
        this.navigationView = navigationView;
    }

    @Override
    public Fragment getItem(int i) {
        navigationView.setSelectedItemId(i);
        return fragments[i];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
