package com.example.ben.recipebook.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ben.recipebook.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StoreCupboardActivity extends ActionBarActivity {

    private TabLayout tabLayout;

    @Bind(R.id.viewpager)
    public ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    @Bind(R.id.add_store_cupboard_item)
    FloatingActionButton addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_cupboard);
        ButterKnife.bind(this);

        setupViewPager(viewPager);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    StoreCupboardIngredientFragment ingredientFragment = (StoreCupboardIngredientFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem());
                    ingredientFragment.showNewIngredientDialog();
                } else {
                    Toast.makeText(getApplication(), "Adding Equipment", Toast.LENGTH_LONG).show();
                }
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_store_cupboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_store_cupboard:
                viewPagerAdapter.saveContents();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new StoreCupboardIngredientFragment(), "Ingredients");
        viewPagerAdapter.addFragment(new StoreCupboardEquipmentFragment(), "Equipment");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void saveContents() {
            for (Fragment fragment : mFragmentList) {
                ((Saveable) fragment).save();
            }
        }
    }
}