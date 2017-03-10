package com.codepath.apps.mysimpletweets.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.HomeFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragmentFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lin1000 on 2017/3/10.
 */

public class TimelineFragmentAdapter  extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "@Mentions"};
    private Class tabClasses[] = new Class[] {HomeFragment.class, MentionFragment.class};
    private Class tabFactories[] = new Class[] {TweetsListFragmentFactory.class,TweetsListFragmentFactory.class};
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
        Object o = null;
        try {
            Method m = tabFactories[position].getDeclaredMethod("newInstance", int.class, Class.class);
            o = m.invoke(null, position+1, tabClasses[position]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (Fragment) o;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
