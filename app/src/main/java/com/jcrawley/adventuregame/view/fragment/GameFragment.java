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
        log("Entered onCreateView()");
        getGameService().ifPresent(GameService::startGame);
        if(!getGameService().isPresent()) {
            log("GameService is unavailable!");
        }
        setupListeners();
        return parentView;
    }


    private void setupViews(View parentView){
        gameTextView = parentView.findViewById(R.id.gameText);
        choicesLayout = parentView.findViewById(R.id.choicesLayout);
    }


    private void log(String msg){
        System.out.println("^^^ GameFragment: " + msg);
    }



    private void setupListeners(){
        FragmentUtils.setListener(this, Message.NOTIFY_PAGE_LOADED, b -> processNextPage());
    }


    private void addChoicesToLayout(){
        getGameService().ifPresent(GameService::getCurrentPage);
    }


    private void processNextPage(){
        log("Entered processNextPage()");
        getGameService().ifPresent(gs ->{
            Page page = gs.getCurrentPage();
            if(page != null){
                assignPageToView(page);
            }
            else{
                log("processNextPage() page is null!");
            }
        });
    }


    private void assignPageToView(Page page){
        assignText(page.text());
        assignChoices(page.choices());
    }


    private void assignText(String text){
        log("Entered assignText()");
        gameTextView.setText(text);
    }


    private void assignChoices(List<Choice> choices){
        log("Entered assignChoices, number of choices: " + choices.size());
        choicesLayout.removeAllViews();
        for(Choice choice : choices){
            choicesLayout.addView(createButtonFor(choice));
        }
    }


    public Button createButtonFor(Choice choice){
        Button button = new Button(getContext());
        log("choice label: " + choice.label() + " choice destination: " + choice.destinationPageNumber());
        button.setTag(choice.label());
        button.setText(choice.label());
        button.setOnClickListener(v -> selectChoice(choice.destinationPageNumber()));
        return button;
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