package com.example.myapplicationtest.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplicationtest.Fragment.MyFragment;
import com.example.myapplicationtest.Fragment.MyFragmentForCZ;
import com.example.myapplicationtest.Fragment.MyFragmentForWQ;

import java.util.List;

public class MyAdapter extends FragmentStateAdapter {
    List<Fragment> fragments;
    private List<Integer> mFragmentHashCodes;//数据源中fragment的hashcode 一对一 增删时注意保持一致

    public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragments,List<Integer> mFragmentHashCodes) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
        this.mFragmentHashCodes = mFragmentHashCodes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
    @Override
    public long getItemId(int position) {
        return fragments.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return mFragmentHashCodes.contains(itemId);
    }
}