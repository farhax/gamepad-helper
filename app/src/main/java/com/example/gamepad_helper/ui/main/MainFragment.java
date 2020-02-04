package com.example.gamepad_helper.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gamepad_helper.ButtonInterface;
import com.example.gamepad_helper.MainActivity;
import com.example.gamepad_helper.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements ButtonInterface {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }
    private static final String TAG = "asd";

    View aButton;
    View bButton;
    View xButton;
    View yButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        aButton = view.findViewById(R.id.aButton);
        bButton = view.findViewById(R.id.bButton);
        xButton = view.findViewById(R.id.xButton);
        yButton = view.findViewById(R.id.yButton);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel



        ((MainActivity) getActivity()).setListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Integer> gamepads = getGameControllerIds();
        if (gamepads.isEmpty()) {
            Log.e(TAG, "no gamepads found! ");
            Toast.makeText(getContext(), "No gamepad found!", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getContext(), "Gamepad found!", Toast.LENGTH_LONG).show();

    }

    public ArrayList<Integer> getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<Integer>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    Log.e(TAG, "found gamepad id: " + deviceId);
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    }

    @Override
    public boolean buttonPressed(KeyEvent keyEvent) {
        boolean handled = true;
        int keyCode = keyEvent.getKeyCode();

        if ((keyEvent.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (keyEvent.getRepeatCount() == 0) {
                switch (keyCode) {

                    case KeyEvent.KEYCODE_BUTTON_A:
                        handleInput(keyEvent, aButton);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_B:
                        handleInput(keyEvent, bButton);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_X:
                        handleInput(keyEvent, xButton);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_Y:
                        handleInput(keyEvent, yButton);
                        break;
                    default:
                        handled = false;
                }
            }
        }

        return handled;
    }

    private void handleInput(KeyEvent keyEvent, View button) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    private static boolean isFireKey(int keyCode) {
        // Here we treat Button_A and DPAD_CENTER as the primary action
        // keys for the game.
        return keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_BUTTON_A;
    }

}
