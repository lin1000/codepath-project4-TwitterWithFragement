package com.codepath.apps.mysimpletweets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.MyTweetsFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragmentFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lin1000 on 2017/3/10.
 */

public class ProfileFragmentAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 1;
    private String tabTitles[] = new String[] { "My Tweets"};
    private Class tabClasses[] = new Class[] { MyTweetsFragment.class};
    private Class tabFactories[] = new Class[] {TweetsListFragmentFactory.class};
    private int tabIcons[] = {R.drawable.ic_home};

    //Arg send to Fragment
    private String screenName;

    public ProfileFragmentAdapter(FragmentManager m, String screenName) {
        super(m);
        Log.d("DEBUG","ProfileFragmentAdapter ProfileFragmentAdapter.screenName="+ screenName);
        this.screenName = screenName;
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
            MyTweetsFragment myTweetsFragment = (MyTweetsFragment) o;
            Log.d("DEBUG","getItem ProfileFragmentAdapter.screenName="+ screenName);
            myTweetsFragment.setScreenName(screenName);
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
