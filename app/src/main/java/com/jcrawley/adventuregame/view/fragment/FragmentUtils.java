package com.jcrawley.adventuregame.view.fragment;


import android.app.Activity;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcrawley.adventuregame.R;

import java.util.function.Consumer;

public class FragmentUtils {


    public static void showDialog(Fragment parentFragment, DialogFragment dialogFragment, String tag, Bundle bundle) {
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentTransaction, tag);
    }


    public static void loadGame(Fragment parentFragment) {
        loadFragment(parentFragment, new GameFragment(), "game_fragment");
    }


    public static void loadAbout(Fragment parentFragment) {
        loadFragment(parentFragment, new GameFragment(), "about_fragment");
    }


    public static void loadGameOver(Fragment parentFragment) {
        loadFragment(parentFragment, new GameOverFragment(), "game_over_fragment");
    }


    public static void loadMainMenu(Fragment parentFragment) {
        loadFragment(parentFragment, new MainMenuFragment(), "main_menu_fragment");
    }


    public static void loadFragmentOnBackButtonPressed(Fragment parentFragment, Fragment destinationFragment, String fragmentTag) {
        onBackButtonPressed(parentFragment, () -> loadFragment(parentFragment, destinationFragment, fragmentTag));
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag, Bundle bundle) {
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    public static void onBackButtonPressed(Fragment parentFragment, Runnable action) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                action.run();
            }
        };
        parentFragment.requireActivity().getOnBackPressedDispatcher().addCallback(parentFragment.getViewLifecycleOwner(), callback);
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag) {
        loadFragment(parentFragment, fragment, tag, new Bundle());
    }


    private static void removePreviousFragmentTransaction(FragmentManager fragmentManager, String tag, FragmentTransaction fragmentTransaction) {
        Fragment prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
    }


    public static void setListener(Fragment fragment, String key, Consumer<Bundle> consumer) {
        fragment.getParentFragmentManager().setFragmentResultListener(key, fragment, (requestKey, bundle) -> consumer.accept(bundle));
    }


    public static void setListener(Fragment fragment, Message message, Consumer<Bundle> consumer) {
        fragment.getParentFragmentManager().setFragmentResultListener(message.name(), fragment, (requestKey, bundle) -> consumer.accept(bundle));
    }


    public static void sendMessage(Fragment fragment, String key) {
        sendMessage(fragment, key, new Bundle());
    }


    public static void sendMessage(Fragment fragment, String key, Bundle bundle) {
        fragment.getParentFragmentManager().setFragmentResult(key, bundle);
    }


    public static void sendMessage(AppCompatActivity activity, Message message) {
        activity.getSupportFragmentManager().setFragmentResult(message.name(), new Bundle());
    }


    public static int getInt(Bundle bundle, Enum<?> tag) {
        return bundle.getInt(tag.toString());
    }


    public static String getStr(Bundle bundle, Enum<?> tag) {
        return bundle.getString(tag.toString());
    }


    public static boolean getBoolean(Bundle bundle, Enum<?> tag) {
        return bundle.getBoolean(tag.toString());
    }
}


