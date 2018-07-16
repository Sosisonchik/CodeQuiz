package com.example.apple.codequiz;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apple.codequiz.Fragments.CategoriesFragment;
import com.example.apple.codequiz.Fragments.RankFragment;
import com.example.apple.codequiz.Tools.NavigationAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.navigation)
    BottomNavigationView navigationView;

    @BindView(R.id.fragment_container)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setDefaultPage();

        initNavigationEvents();
    }

    private void setDefaultPage() {
        navigationView.setSelectedItemId(R.id.home_btn);
        viewPager.setCurrentItem(0);
    }

    private void initNavigationEvents() {
        viewPager.setAdapter(new NavigationAdapter(getSupportFragmentManager(),navigationView));

        navigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.home_btn:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.rank_btn:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return true;
        });
    }
}
