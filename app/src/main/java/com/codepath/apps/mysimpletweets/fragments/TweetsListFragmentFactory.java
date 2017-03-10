package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import java.lang.reflect.*;

import static com.codepath.apps.mysimpletweets.fragments.TweetsListFragment.ARG_PAGE;

/**
 * Created by lin1000 on 2017/3/10.
 */

public class TweetsListFragmentFactory {

    public static TweetsListFragment newInstance(int page, Class className) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TweetsListFragment fragment = null;
        try {
            fragment = (TweetsListFragment) className.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        };
        fragment.setArguments(args);
        return fragment;
    }

}
