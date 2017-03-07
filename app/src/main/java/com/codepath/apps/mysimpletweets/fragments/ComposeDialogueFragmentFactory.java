package com.codepath.apps.mysimpletweets.fragments;

/**
 * Created by lin1000 on 2017/3/5.
 */


public class ComposeDialogueFragmentFactory {

    private static ComposeDialogueFragment instance;

    private ComposeDialogueFragmentFactory(){}

    public static ComposeDialogueFragment getInstance(){
        if(instance==null){
            synchronized (ComposeDialogueFragmentFactory.class){
                if(instance==null){
                    instance = new ComposeDialogueFragment();
                }
            }
        }
        return instance;
    }
}
