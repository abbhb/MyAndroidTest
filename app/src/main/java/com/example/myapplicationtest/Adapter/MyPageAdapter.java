package com.example.myapplicationtest.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.List;

public class MyPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<Integer> mItemIdList = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private int id = 0;
    private FragmentManager mFm;
    /**
     *
     * @param fm
     * @param fragmentList  页面集合
     */
    public MyPageAdapter(FragmentManager fm, @NonNull Lifecycle lifecycle, @NonNull List<Fragment> fragmentList) {
        super(fm);
        this.mFm = fm;
        for (Fragment fragment : fragmentList) {
            this.mFragmentList.add(fragment);
            mItemIdList.add(id++);
        }
    }


//    public ShopPageAdapter(FragmentManager fm, @NonNull List<Fragment> fragmentList,List<String> titles) {
//        super(fm);
//        this.mFm = fm;
//        for (Fragment fragment : fragmentList) {
//            this.mFragmentList.add(fragment);
//            mItemIdList.add(id++);
//        }
//        this.titles = titles;
//    }
//
//    public ShopPageAdapter(FragmentManager fm) {
//        super(fm);
//    }

    public List<Fragment> getFragmentList() {
        return mFragmentList;
    }

    /**
     * 添加一个页面（插入某个下标位置）
     * @param index
     * @param fragment
     */
    public void addPage(int index, Fragment fragment) {
        mFragmentList.add(index, fragment);
        mItemIdList.add(index, id++);
        notifyDataSetChanged();
    }

    /**
     * 添加一个页面
     * @param fragment
     */
    public void addPage(Fragment fragment) {
        mFragmentList.add(fragment);
        mItemIdList.add(id++);
        notifyDataSetChanged();
    }

    /**
     * 删除某个页面
     * @param index
     */
    public void delPage(int index) {
        mFragmentList.remove(index);
        mItemIdList.remove(index);
        notifyDataSetChanged();
    }

    /**
     * 更新页面
     * @param fragmentList
     */
    public void updatePage(List<Fragment> fragmentList) {
        mFragmentList.clear();
        mItemIdList.clear();
        for (int i = 0; i < fragmentList.size(); i++) {
            mFragmentList.add(fragmentList.get(i));
            mItemIdList.add(id++);
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    /**
     * 返回值有三种，
     * POSITION_UNCHANGED  默认值，位置没有改变
     * POSITION_NONE       item已经不存在
     * position            item新的位置
     * 当position发生改变时这个方法应该返回改变后的位置，以便页面刷新。
     */
    @Override
    public int getItemPosition(Object object) {
        if (object instanceof Fragment) {
            if (mFragmentList.contains(object)) {
                return mFragmentList.indexOf(object);
            } else {
                return POSITION_NONE;
            }

        }
        return super.getItemPosition(object);
    }

    @Override
    public long getItemId(int position) {
        return mItemIdList.get(position);
    }

}
