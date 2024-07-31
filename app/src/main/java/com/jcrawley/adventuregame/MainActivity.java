package com.jcrawley.adventuregame;

import static com.jcrawley.adventuregame.view.fragment.FragmentUtils.sendMessage;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import com.jcrawley.adventuregame.service.GameService;
import com.jcrawley.adventuregame.service.level.Page;
import com.jcrawley.adventuregame.view.fragment.MainMenuFragment;
import com.jcrawley.adventuregame.view.fragment.Message;

public class MainActivity extends AppCompatActivity {

    private GameService gameService;

    private final AtomicBoolean isServiceConnected = new AtomicBoolean(false);


    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            log("Entered onServiceConnected()");
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            //sendMessage(OptionsFragment.Message.NOTIFY_OF_SERVICE_CONNECTED);
            isServiceConnected.set(true);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            log("Entered onServiceDisconnected()");
            isServiceConnected.set(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupWindowInsetsListener();
        setupFragmentsIf(savedInstanceState == null);
        setupGameService();
    }


    private void setupWindowInsetsListener(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void setupFragmentsIf(boolean isFirstTime) {
        if(!isFirstTime){
            return;
        }
        Fragment mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit();
    }


    public void updatePage(Page page){
        sendMessage(this, Message.NOTIFY_PAGE_LOADED);
    }


    public void notifyPageChange(){
        sendMessage(this, Message.NOTIFY_PAGE_LOADED);
    }


    public Optional<GameService> getGameService(){
        return Optional.ofNullable(gameService);
    }


    private void setupGameService() {
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }

}