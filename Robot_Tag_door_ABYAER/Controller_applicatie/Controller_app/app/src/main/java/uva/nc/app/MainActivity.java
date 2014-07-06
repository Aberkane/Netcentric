package uva.nc.app;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.zerokol.views.JoystickView;

import java.io.Serializable;
import java.util.Random;

import uva.nc.ServiceActivity;
import uva.nc.bluetooth.BluetoothService;
import uva.nc.bluetooth.MasterManager;
import uva.nc.bluetooth.SlaveManager;
import uva.nc.mbed.MbedManager;
/*
Controller app die de robot bestuurt en aan de gebruiker laat zien wie de tikker is.

Auteurs: Yadvir Singh, Abdel Aberkane, Erik van der Schaaf.

 */


public class MainActivity extends ServiceActivity {
    // Joystick gedeelte.
    private int previousChange = 9;
    private JoystickView joystick;
    private Button scanButton;
    private Button controlButton;
    final Context context = this;
    private String robotNumber = "Robot 0";
    private String tikkerNumber = "Robot 1";

    // Receiver implemented in separate class, see bottom of file.
    private final MainActivityReceiver receiver = new MainActivityReceiver();

    // BT Controls.
    private TextView listenerStatusText;
    private TextView ownAddressText;
    private TextView deviceCountText;
    private Button listenerButton;
    private Button devicesButton;
    private Button pingMasterButton;
    private Button pingSlavesButton;

    // Random data for sample events.
    private Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachControls();
        scanButton = (Button) findViewById(R.id.scan_button);
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);

        //Vraag de gebruiker om een nummer voor zijn of haar robot.
        final NumberPicker np = new NumberPicker(context);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setValue(1);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setWrapSelectorWheel(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle("Robot Number")
                .setView(np)
                .setCancelable(false)
                .setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        robotNumber = "Robot " + np.getValue();
                        if(robotNumber.equals(tikkerNumber))
                        {
                            scanButton.setText(getResources().getString(R.string.tik));
                            //scanButton.setClickable(true);
                        }
                        else
                        {
                            scanButton.setText(getResources().getString(R.string.ren_voor) + " " + tikkerNumber);
                            //scanButton.setClickable(false);
                        }
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tikkerNumber.equals(robotNumber))
                {
                    //Alleen als de robot de tikker is kan hij tikken.
                    BluetoothService bluetooth = getBluetooth();
                    if (bluetooth != null) {
                        bluetooth.slave.sendToMaster(-1);
                    }
                    /*
                    // Create a handler
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            if(tikkerNumber.equals(robotNumber))
                            {
                                scanButton.setClickable(true);
                            }
                    }}, 3000);
                    */
                }
            }
        });

        //Lees de positie van de joystick uit en verstuur een niewe richting.
        joystick.setClickable(true);
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if(power > 30)
                {

                    //Stuur alleen een niewe richting bij verandering, zo word de
                    //datastroom beperkt.
                    if(previousChange != direction)
                    {
                        previousChange = direction;
                        if(joystick.isClickable()) {
                            BluetoothService bluetooth = getBluetooth();
                            if (bluetooth != null) {
                                bluetooth.slave.sendToMaster(direction);
                            }
                        }
                    }

                }
                else
                {
                    if(previousChange != 9)
                    {
                        previousChange = 9;
                        if(joystick.isClickable()) {
                            BluetoothService bluetooth = getBluetooth();
                            if (bluetooth != null) {
                                bluetooth.slave.sendToMaster(0);
                            }
                        }
                    }
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, receiver.getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onBluetoothReady(BluetoothService bluetooth) {
        refreshBluetoothControls();
    }


    private void attachControls() {
        // Bluetooth controls.
        ownAddressText = (TextView)findViewById(R.id.own_address);
        listenerStatusText = (TextView)findViewById(R.id.listener_status);
        listenerButton = (Button)findViewById(R.id.listener);
        deviceCountText = (TextView)findViewById(R.id.device_count);
        devicesButton = (Button)findViewById(R.id.devices);
        devicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(MainActivity.this, DevicesActivity.class);
                startActivity(launch);
            }
        });
        pingMasterButton = (Button)findViewById(R.id.ping_master);
        pingMasterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothService bluetooth = getBluetooth();
                if (bluetooth != null) {
                    bluetooth.slave.sendToMaster(random.nextInt(2500));
                }
            }
        });
        controlButton = (Button) findViewById(R.id.controlButton);
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothService bluetooth = getBluetooth();
                if (bluetooth != null) {
                    bluetooth.slave.sendToMaster(-2);
                }
            }
        });
        pingSlavesButton = (Button)findViewById(R.id.ping_slaves);
        pingSlavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothService bluetooth = getBluetooth();
                if (bluetooth != null) {
                    bluetooth.master.sendToAll(random.nextInt(10000) + 5000);
                }
            }
        });
    }

    private void refreshBluetoothControls() {
        String slaveStatus = "Status not available";
        String slaveButton = "Start listening";
        String ownAddress = "Not available";
        String connected = "0";
        boolean slaveButtonEnabled = false;
        boolean devicesButtonEnabled = false;
        boolean allowPingMaster = false;
        boolean allowPingSlaves = false;

        // Well it's not pretty, but it (barely) avoids duplicate logic.
        final BluetoothService bluetooth = getBluetooth();
        if (bluetooth != null) {
            slaveButtonEnabled = true;
            devicesButtonEnabled = true;
            ownAddress = bluetooth.utility.getOwnAddress();

            int devConnected = bluetooth.master.countConnected();
            if (bluetooth.master.countConnected() > 0) {
                connected = String.valueOf(devConnected);
                allowPingSlaves = true;
            }

            if (bluetooth.slave.isConnected()) {
                slaveStatus = "Connected to " + bluetooth.slave.getRemoteDevice();
                slaveButton = "Disconnect";
                allowPingMaster = true;
                listenerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bluetooth.slave.disconnect();
                    }
                });
            } else if (bluetooth.slave.isListening()) {
                slaveStatus = "Waiting for connection";
                slaveButton = "Stop listening";
                listenerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bluetooth.slave.stopAcceptOne();
                    }
                });
            } else {
                slaveStatus = "Not listening";
                slaveButton = "Start listening";
                listenerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!bluetooth.utility.isDiscoverable()) {
                            bluetooth.utility.setDiscoverable();
                        }
                        bluetooth.slave.startAcceptOne();
                    }
                });
            }
        }

        listenerStatusText.setText(slaveStatus);
        listenerButton.setText(slaveButton);
        listenerButton.setEnabled(slaveButtonEnabled);
        ownAddressText.setText(ownAddress);
        deviceCountText.setText(connected);
        devicesButton.setEnabled(devicesButtonEnabled);
        pingMasterButton.setEnabled(allowPingMaster);
        controlButton.setEnabled(allowPingMaster);
        pingSlavesButton.setEnabled(allowPingSlaves);
    }


    // Broadcast receiver which handles incoming events. If it were smaller, inline it.
    private class MainActivityReceiver extends BroadcastReceiver {

        // Refresh BT controls on these events.
        private final String BLUETOOTH_REFRESH_ON[] = { MasterManager.DEVICE_ADDED,
                                                        MasterManager.DEVICE_REMOVED,
                                                        MasterManager.DEVICE_STATE_CHANGED,
                                                        SlaveManager.LISTENER_CONNECTED,
                                                        SlaveManager.LISTENER_DISCONNECTED,
                                                        SlaveManager.STARTED_LISTENING,
                                                        SlaveManager.STOPPED_LISTENING };

        private final String MBED_REFRESH_ON[] = {      MbedManager.DEVICE_ATTACHED,
                                                        MbedManager.DEVICE_DETACHED };


        // Returns intents this receiver responds to.
        protected IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();

            // Notification updates.
            for (String action : BLUETOOTH_REFRESH_ON) {
                filter.addAction(action);
            }
            for (String action : MBED_REFRESH_ON) {
                filter.addAction(action);
            }

            // Data received events.
            filter.addAction(MbedManager.DATA_READ);
            filter.addAction(MasterManager.DEVICE_RECEIVED);
            filter.addAction(SlaveManager.LISTENER_RECEIVED);

            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // Refresh on most Bluetooth or mBed events.
            for (String update : BLUETOOTH_REFRESH_ON) {
                if (action.equals(update)) {
                    refreshBluetoothControls();
                    break;
                }
            }
            for (String update : MBED_REFRESH_ON) {
                if (action.equals(update)) {
                    break;
                }
            }

            // Process received data.
            if (action.equals(SlaveManager.LISTENER_RECEIVED)) {

                // Slave received data from master.
                Serializable obj = intent.getSerializableExtra(SlaveManager.EXTRA_OBJECT);
                if (obj != null) {

                    if(String.valueOf(obj).equals("Niks gescand!!"))
                    {
                        toastShort("Tikken is mislukt");
                        //scanButton.setClickable(true);
                        //joystick.setClickable(true);
                    }
                    else if(String.valueOf(obj).contains("Robot "))
                    {
                        if(robotNumber.equals(tikkerNumber))
                        {
                            toastShort("Tikken gelukt!");
                            tikkerNumber = (String.valueOf(obj));
                            scanButton.setText(getResources().getString(R.string.ren_voor) + " " + tikkerNumber);
                        }
                        else
                        {
                            tikkerNumber = (String.valueOf(obj));
                            if(robotNumber.equals(tikkerNumber))
                            {
                                toastShort("Je bent getikt!");
                                scanButton.setText(getResources().getString(R.string.tik));
                                //scanButton.setClickable(true);
                            }
                            else
                            {
                                toastShort(tikkerNumber + " is getikt!");
                                scanButton.setText(getResources().getString(R.string.ren_voor) + " " + tikkerNumber);
                            }
                        }
                        //joystick.setClickable(true);
                    }
                    else
                    {
                        toastShort("From master:\n" + String.valueOf(obj));
                    }
                } else {
                    toastShort("From master:\nnull");
                }
            } else if (action.equals(MasterManager.DEVICE_RECEIVED)) {

                // Master received data from slave.
                Serializable obj = intent.getSerializableExtra(MasterManager.EXTRA_OBJECT);
                BluetoothDevice device = intent.getParcelableExtra(MasterManager.EXTRA_DEVICE);
                if (obj != null) {
                    toastShort("From " + device + "\n" + String.valueOf(obj));
                } else {
                    toastShort("From " + device + "\nnull!");
                }
            }
        }
    }
}
