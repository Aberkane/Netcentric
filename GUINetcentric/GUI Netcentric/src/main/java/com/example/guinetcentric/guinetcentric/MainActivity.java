package com.example.guinetcentric.guinetcentric;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!

        final View joystickView = findViewById(R.id.joystickView);
        //Log.d("ImageView", "width =  " + joystickView.getWidth() + " en height = " + joystickView.getHeight());

        Canvas joystickCanvas;

        joystickView.setDrawingCacheEnabled(true);
        joystickView.buildDrawingCache();

        joystickCanvas = new Canvas(joystickView.getDrawingCache());

        joystickCanvas.drawColor(Color.BLACK);
/*        Bitmap joystick_bkg, joystick_bkg2;
        Bitmap joystick_bal, joystick_bal2;

        joystick_bkg = BitmapFactory.decodeResource(getResources(), R.drawable.androidpadrealreal);
        joystick_bkg2 = joystick_bkg.copy(Bitmap.Config.ARGB_8888, true);
        joystick_bal = BitmapFactory.decodeResource(getResources(), R.drawable.androidbalreal);
        joystick_bal2 = joystick_bal.copy(Bitmap.Config.ARGB_8888, true);
        joystickCanvas = new Canvas(joystick_bkg2);

        float heightPadMid = joystick_bkg2.getHeight()/2;
        float widthPadMid = joystick_bkg2.getWidth()/2;
        float heightBalMid = joystick_bal2.getHeight()/2;
        float widthBalMid = joystick_bal2.getWidth()/2;

        joystickCanvas.drawBitmap(joystick_bal2, (widthPadMid - widthBalMid), (heightPadMid - heightBalMid), null);

        joystickView.setImageBitmap(joystick_bkg2);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
