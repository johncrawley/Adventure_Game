package com.jcrawley.adventuregame.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawley.adventuregame.MainActivity;
import com.jcrawley.adventuregame.R;
import com.jcrawley.adventuregame.service.GameService;
import com.jcrawley.adventuregame.service.level.Choice;
import com.jcrawley.adventuregame.service.level.Page;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameFragment extends Fragment {

    private final AtomicBoolean isGameStartInitiated = new AtomicBoolean(false);

    private TextView gameTextView;
    private ViewGroup choicesLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_game, container, false);
        setupViews(parentView);
        getGameService().ifPresent(GameService::startGame);
        return parentView;
    }

    private void setupViews(View parentView){
        gameTextView = parentView.findViewById(R.id.gameText);
        choicesLayout = parentView.findViewById(R.id.choicesLayout);
    }


    private void setupListeners(){
        //FragmentUtils.setListener(this, Message.NOTIFY_PAGE_LOADED.toString(), b -> process(this::updateTimeRemaining, b));
    }



    private void addChoicesToLayout(){
        getGameService().ifPresent(
                GameService::getCurrentPage
        );
    }


    private void assignPageToView(Page page){
        assignText(page.text());
        assignChoices(page.choices());
    }


    private void assignText(String text){
        gameTextView.setText(text);
    }


    private void assignChoices(List<Choice> choices){
        choicesLayout.removeAllViews();
        for(Choice choice : choices){
            addChoiceToLayout(choice);
        }
    }

    private void addChoiceToLayout(Choice choice){
        Button button = new Button(getContext());
        button.setTag(choice.label());
        button.setOnClickListener(v -> selectChoice(choice.destinationPageNumber()));

    }


    private void selectChoice(int destinationPageNumber){
        getGameService().ifPresent(gs -> gs.selectChoice(destinationPageNumber));

    }


    private Optional<GameService> getGameService(){
        MainActivity activity = (MainActivity) getActivity();
        if(activity == null){
            return Optional.empty();
        }
        return activity.getGameService();
    }
}