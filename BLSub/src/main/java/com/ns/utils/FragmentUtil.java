package com.ns.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class FragmentUtil {

    public static final int FRAGMENT_NO_ANIMATION = -1;
    public static final int FRAGMENT_ANIMATION = FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

    public static void pushFragmentFromFragment( Fragment fragmentParent, int resId, Fragment fragment) {
        FragmentTransaction trasaction = fragmentParent.getChildFragmentManager().beginTransaction();
        trasaction.replace(resId, fragment, fragmentParent.getClass().getCanonicalName());
        trasaction.commit();
    }

    public static void pushFragment( AppCompatActivity activity, int resId, Fragment fragment) {
        FragmentTransaction trasaction = activity.getSupportFragmentManager().beginTransaction();
        trasaction.replace(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
        trasaction.addToBackStack(null);
        trasaction.commit();
    }

    public static void pushFragmentAnim(AppCompatActivity activity, int resId, Fragment fragment,
                                        final int animationSet, final boolean isRoot) {
        FragmentTransaction trasaction = activity.getSupportFragmentManager().beginTransaction();
        if (animationSet != FRAGMENT_NO_ANIMATION) {
            trasaction.setTransition(animationSet);
        }

        if (isRoot) {
            clearAllBackStack(activity);
            trasaction.replace(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
        } else {
            trasaction.replace(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
            trasaction.addToBackStack(null);
        }

        trasaction.commit();
    }

    public static void addFragmentAnim(AppCompatActivity activity, int resId, Fragment fragment,
                                        final int animationSet, final boolean isRoot) {
        FragmentTransaction trasaction = activity.getSupportFragmentManager().beginTransaction();
        if (animationSet != FRAGMENT_NO_ANIMATION) {
            trasaction.setTransition(animationSet);
        }

        if (isRoot) {
            clearAllBackStack(activity);
            trasaction.add(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
        } else {
            trasaction.add(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
            trasaction.addToBackStack(null);
        }

        trasaction.commit();
    }

    public static  void clearAllBackStack(AppCompatActivity activity) {
        activity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static  void clearSingleBackStack(AppCompatActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }


}
