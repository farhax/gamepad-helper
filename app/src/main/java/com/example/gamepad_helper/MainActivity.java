package com.example.gamepad_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.example.gamepad_helper.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "qwe";
    private ButtonInterface listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    public void setListener(ButtonInterface listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (listener != null) {
            return listener.buttonPressed(event);
        }

        return super.dispatchKeyEvent(event);
    }
}
