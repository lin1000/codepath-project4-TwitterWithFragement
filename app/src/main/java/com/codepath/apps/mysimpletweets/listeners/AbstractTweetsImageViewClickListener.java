package com.codepath.apps.mysimpletweets.listeners;

import android.view.View;

/**
 * Created by lin1000 on 2017/3/11.
 */

abstract public class AbstractTweetsImageViewClickListener implements View.OnClickListener {

    private int position;

    public AbstractTweetsImageViewClickListener(int position){
        this.position = position;
    }

    //override in UI code
    abstract public void onClick(View v);

}
