package com.example.calculator.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.calculator.R;
import com.example.calculator.ui.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set up ViewPager adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Bottom navigation item clicks
        navigationItemSelector();
        // Sync bottom navigation when swiping
        viewPagerPageChangeRegister();
    }

    private void viewPagerPageChangeRegister() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_fieldtrip);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_expenses);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_analysis);
                        break;
                }
            }
        });
    }

    private void navigationItemSelector() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_fieldtrip) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (item.getItemId() == R.id.nav_expenses) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (item.getItemId() == R.id.nav_analysis) {
                viewPager.setCurrentItem(2);
                return true;
            }
            return false;
        });
    }
}