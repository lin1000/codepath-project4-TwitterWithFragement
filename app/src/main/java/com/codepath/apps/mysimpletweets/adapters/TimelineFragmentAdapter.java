package com.codepath.apps.mysimpletweets.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;

/**
 * Created by lin1000 on 2017/3/10.
 */

public class TimelineFragmentAdapter  extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "@Mentions"};
    private int tabIcons[] = {R.drawable.ic_home, R.drawable.ic_mention};

    Drawable myDrawable;
    String title;

    public TimelineFragmentAdapter(FragmentManager m) {
        super(m);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("DEBUG","TimelineFragmentAdapter.getItem() position="+ position);
        return TweetsListFragment.newInstance(position+1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
