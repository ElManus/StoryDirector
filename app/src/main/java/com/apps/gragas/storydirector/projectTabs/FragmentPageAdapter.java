package com.apps.gragas.storydirector.projectTabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    public FragmentPageAdapter(FragmentManager manager1) {
        super(manager1);

    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title, Bundle Bundle) {

          fragment.setArguments(Bundle); //передаем во фрагмент пакет с именем проекта и общей информацией.
          mFragmentList.add(fragment);
          mFragmentTitleList.add(title);

    }




    public Fragment getMainFragment (){
        return mFragmentList.get(0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

