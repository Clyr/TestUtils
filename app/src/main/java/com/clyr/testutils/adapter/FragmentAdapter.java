package com.clyr.testutils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by M S I of clyr on 2019/5/29.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    FragmentManager fragmentManager;
    Fragment fg;
    List<Fragment> list;
    public FragmentAdapter(FragmentManager fm, Fragment fg) {
        super(fm);
        fragmentManager = fm;
        this.fg = fg;
    }
    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        fragmentManager = fm;
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
