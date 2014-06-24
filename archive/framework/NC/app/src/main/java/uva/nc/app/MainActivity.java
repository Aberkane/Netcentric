package uva.nc.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import uva.nc.ServiceActivity;
import uva.nc.bluetooth.BluetoothService;
import uva.nc.bluetooth.MasterManager;
import uva.nc.bluetooth.SlaveManager;
import uva.nc.mbed.MbedManager;
import uva.nc.mbed.MbedRequest;
import uva.nc.mbed.MbedResponse;
import uva.nc.mbed.MbedService;

import com.zerokol.views.JoystickView;


public class MainActivity extends ServiceActivity {
    // Joystick gedeelte.
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;
    private TextView changeTextView;
    private TextView rijdenTextView;
    private int numberOfChanges = 0;
    private int previousChange = 9;
    private JoystickView joystick;
    private Button scanButton;
    private Button controlButton;
    final Context context = this;
    private String robotNumber = "Robot 0";
    private String tikkerNumber = "Robot 1";

    private static final String TAG = MainActivity.class.getName();

    // Receiver implemented in separate class, see bottom of file.
    private final MainActivityReceiver receiver = new MainActivityReceiver();

    // ID's for commands on mBed.
    private static final int COMMAND_SUM = 1;
    private static final int COMMAND_AVG = 2;
    private static final int COMMAND_LED = 3;

    // BT Controls.
    private TextView listenerStatusText;
    private TextView ownAddressText;
    private TextView deviceCountText;
    private Button listenerButton;
    private Button devicesButton;
    private Button pingMasterButton;
    private Button pingSlavesButton;

    // mBed controls.
    private TextView mbedConnectedText;
    private Button mbedSumButton;
    private Button mbedAvgButton;
    private Button mbedLedButton;

    // Random data for sample events.
    private Random random = new Random();

    // Accessory to connect to when service is connected.
    private UsbAccessory toConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachControls();

        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        changeTextView = (TextView) findViewById(R.id.changeTextView);
        rijdenTextView = (TextView) findViewById(R.id.rijdenTextView);
        scanButton = (Button) findViewById(R.id.scan_button);
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);

        //Vraag om een nummer om mee te spelen
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
                            scanButton.setClickable(true);
                        }
                        else
                        {
                            scanButton.setText(getResources().getString(R.string.ren_voor) + " " + tikkerNumber);
                            scanButton.setClickable(false);
                        }
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanButton.setClickable(false);
                joystick.setClickable(false);
                BluetoothService bluetooth = getBluetooth();
                if (bluetooth != null) {
                    bluetooth.slave.sendToMaster(-1);
                }
            }
        });

        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setClickable(true);
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if(power > 30)
                {
                    if(previousChange != direction)
                    {
                        previousChange = direction;
                        numberOfChanges++;
                        if(joystick.isClickable()) {
                            BluetoothService bluetooth = getBluetooth();
                            if (bluetooth != null) {
                                bluetooth.slave.sendToMaster(direction);
                            }
                        }
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
                        if(joystick.isClickable()) {
                            BluetoothService bluetooth = getBluetooth();
                            if (bluetooth != null) {
                                bluetooth.slave.sendToMaster(0);
                            }
                        }
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

        // If this intent was started with an accessory, store it temporarily and clear once connected.
        UsbAccessory accessory = getIntent().getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
        if (accessory != null) {
            this.toConnect = accessory;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, receiver.getIntentFilter());
        refreshBluetoothControls();
        refreshMbedControls();
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

    @Override
    protected void onMbedReady(MbedService mbed) {
        if (toConnect != null) {
            mbed.manager.attach(toConnect);
            toConnect = null;
        }
        refreshMbedControls();
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
        mbedConnectedText = (TextView)findViewById(R.id.mbed_connected);
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

        // mBed controls.
        mbedSumButton = (Button)findViewById(R.id.mbed_sum);
        mbedSumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] args = getRandomFloatArray(10);
                toastShort("Sum of: \n" + Arrays.toString(args));
                getMbed().manager.write(new MbedRequest(COMMAND_SUM, args));
            }
        });
        mbedAvgButton = (Button)findViewById(R.id.mbed_avg);
        mbedAvgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] args = getRandomFloatArray(10);
                toastShort("Avg of:\n" + Arrays.toString(args));
                getMbed().manager.write(new MbedRequest(COMMAND_AVG, args));
            }
        });
        mbedLedButton = (Button)findViewById(R.id.mbed_led);
        mbedLedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] args = getRandomLedArray();
                getMbed().manager.write(new MbedRequest(COMMAND_LED, args));
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

    private void refreshMbedControls() {
        String connText = getString(R.string.not_connected); // if you want to localize
        boolean enableButtons = false;

        MbedService mbed = getMbed();
        if (mbed != null && mbed.manager.areChannelsOpen()) {
            connText = getString(R.string.connected);
            enableButtons = true;
        }

        mbedConnectedText.setText(connText);
        mbedAvgButton.setEnabled(enableButtons);
        mbedSumButton.setEnabled(enableButtons);
        mbedLedButton.setEnabled(enableButtons);
    }


    // mBed random data.
    private float[] getRandomFloatArray(int maxLength) {
        if (maxLength < 1) {
            maxLength = 1;
        }

        int length = random.nextInt(maxLength + 1);
        float[] arr = new float[length];
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextFloat();
        }

        return arr;
    }

    private float[] getRandomLedArray() {
        float[] arr = new float[4];
        for (int i = 0; i < 4; i++) {
            arr[i] = random.nextBoolean() ? 0.0f : 1.0f;
        }
        return arr;
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
                    refreshMbedControls();
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
                        scanButton.setClickable(true);
                        joystick.setClickable(true);
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
                                scanButton.setClickable(true);
                            }
                            else
                            {
                                toastShort(tikkerNumber + " is getikt!");
                                scanButton.setText(getResources().getString(R.string.ren_voor) + " " + tikkerNumber);
                            }
                        }
                        joystick.setClickable(true);
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
            } else if (action.equals(MbedManager.DATA_READ)) {

                // mBed data received.
                MbedResponse response = intent.getParcelableExtra(MbedManager.EXTRA_DATA);
                if (response != null) {
                    // Errors handled as separate case, but this is just sample code.
                    if (response.hasError()) {
                        toastLong("Error! " + response);
                        return;
                    }

                    float[] values = response.getValues();
                    if (response.getCommandId() == COMMAND_AVG) {
                        if (values == null || values.length != 1) {
                            toastShort("Error!");
                        } else {
                            toastShort("AVG: " + String.valueOf(values[0]));
                        }
                    } else if (response.getCommandId() == COMMAND_SUM) {
                        if (values == null || values.length != 1) {
                            toastShort("Error!");
                        } else {
                            toastShort("SUM: " + String.valueOf(values[0]));
                        }
                    }
                }
            }
        }
    }
}
