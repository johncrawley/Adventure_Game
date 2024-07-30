package com.jcrawley.adventuregame.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.jcrawley.adventuregame.R;

public class MainMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        setupViews(parentView);
        return parentView;
    }


    private void setupViews(View parentView){
        Button startGameButton = parentView.findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(v -> FragmentUtils.loadGame(this));
    }


}
