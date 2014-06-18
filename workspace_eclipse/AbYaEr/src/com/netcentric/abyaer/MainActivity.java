package com.netcentric.abyaer;

import com.netcentric.abyaer.R;
import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;
    private TextView changeTextView;
    private TextView rijdenTextView;
    private int numberOfChanges = 0;
    private int previousChange = 9;
    // Importing also other views
    private JoystickView joystick;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        changeTextView = (TextView) findViewById(R.id.changeTextView);
        rijdenTextView = (TextView) findViewById(R.id.rijdenTextView);
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);

        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
            	if(power > 30)
            	{
            		if(previousChange != direction)
            		{
            			previousChange = direction;
            			numberOfChanges++;
            		}
            		rijdenTextView.setText(R.string.powerHoog);
            		//TODO sturen wat hij nu moet zijn
            		
            	}
            	else
            	{
            		if(previousChange != 9)
            		{
            			previousChange = 9;
            			numberOfChanges++;
            		}
            		rijdenTextView.setText(R.string.powerLaag);
            		//TODO sturen wat hij nu moet zijn
            	}
                angleTextView.setText("Angle: " + String.valueOf(angle) + "°");
                powerTextView.setText("Power: " + String.valueOf(power) + "%");
                changeTextView.setText(getResources().getString(R.string.change) + " " + String.valueOf(numberOfChanges));
                switch (direction) {
                case JoystickView.FRONT:
                    directionTextView.setText(R.string.front_lab);
                    break;
                case JoystickView.FRONT_RIGHT:
                    directionTextView.setText(R.string.front_right_lab);
                    break;
                case JoystickView.RIGHT:
                    directionTextView.setText(R.string.right_lab);
                    break;
                case JoystickView.RIGHT_BOTTOM:
                    directionTextView.setText(R.string.right_bottom_lab);
                    break;
                case JoystickView.BOTTOM:
                    directionTextView.setText(R.string.bottom_lab);
                    break;
                case JoystickView.BOTTOM_LEFT:
                    directionTextView.setText(R.string.bottom_left_lab);
                    break;
                case JoystickView.LEFT:
                    directionTextView.setText(R.string.left_lab);
                    break;
                case JoystickView.LEFT_FRONT:
                    directionTextView.setText(R.string.left_front_lab);
                    break;
                default:
                    directionTextView.setText(R.string.center_lab);
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }
}