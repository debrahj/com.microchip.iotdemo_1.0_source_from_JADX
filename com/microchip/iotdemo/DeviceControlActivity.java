package com.microchip.iotdemo;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.microchip.iotdemo.BluetoothLeService.LocalBinder;
import java.util.List;

public class DeviceControlActivity extends Activity {
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    private static final String MLDP_CONTROL_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a0003ff";
    private static final String MLDP_DATA_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a000301";
    private static final String MLDP_PRIVATE_SERVICE = "00035b03-58e6-07dd-021a-08123a000300";
    private static final String TAG = DeviceControlActivity.class.getSimpleName();
    private Button BtnDecrypt;
    private Button BtnEncrypt;
    private final OnClickListener DecryptClickListener;
    private final OnClickListener EncryptClickListener;
    private EditText data;
    private EditText data1;
    public String dec_data = "";
    private EditText decdata;
    private TextView decrypted;
    private TextView device_mode;
    public String enc_data = "";
    private TextView encrypted;
    private ImageView imageGreenLight1;
    private ImageView imageGreenLight2;
    private ImageView imageLED1;
    private final OnClickListener imageLED1ClickListener;
    private ImageView imageLED2;
    private final OnClickListener imageLED2ClickListener;
    private ImageView imageSwitchOn1;
    private ImageView imageSwitchOn2;
    private String incomingMessage;
    private EditText key;
    private boolean led1_On = false;
    private boolean led2_On = false;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private TextView mConnectionState;
    private BluetoothGattCharacteristic mControlMLDP;
    private BluetoothGattCharacteristic mDataMLDP;
    private String mDeviceAddress;
    private String mDeviceName;
    private final BroadcastReceiver mGattUpdateReceiver;
    private final ServiceConnection mServiceConnection;
    private ImageView sleepState;
    private Switch switch1;
    private Switch switch2;
    private TextView textTemperature;
    private boolean writeComplete = false;

    class C00631 implements ServiceConnection {
        C00631() {
            C00631 c00631 = this;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ComponentName componentName2 = componentName;
            IBinder service = iBinder;
            int d = Log.d(DeviceControlActivity.TAG, "onServiceConnected Start");
            BluetoothLeService access$102 = DeviceControlActivity.access$102(DeviceControlActivity.this, ((LocalBinder) service).getService());
            if (!DeviceControlActivity.this.mBluetoothLeService.initialize()) {
                d = Log.e(DeviceControlActivity.TAG, "Unable to initialize Bluetooth");
                DeviceControlActivity.this.finish();
            }
            boolean connect = DeviceControlActivity.this.mBluetoothLeService.connect(DeviceControlActivity.this.mDeviceAddress);
            d = Log.d(DeviceControlActivity.TAG, "onServiceConnected End");
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ComponentName componentName2 = componentName;
            int d = Log.d(DeviceControlActivity.TAG, "onServiceDisconnected Start");
            BluetoothLeService access$102 = DeviceControlActivity.access$102(DeviceControlActivity.this, null);
            d = Log.d(DeviceControlActivity.TAG, "onServiceDisconnected End");
        }
    }

    class C00642 extends BroadcastReceiver {
        C00642() {
            BroadcastReceiver broadcastReceiver = this;
        }

        public void onReceive(Context context, Intent intent) {
            C00642 this = this;
            Context context2 = context;
            Intent intent2 = intent;
            int d = Log.d(DeviceControlActivity.TAG, "BroadcastReceiver.onReceive Start");
            String action = intent2.getAction();
            boolean access$302;
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                d = Log.d(DeviceControlActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_CONNECTED");
                access$302 = DeviceControlActivity.access$302(DeviceControlActivity.this, true);
                DeviceControlActivity.this.updateConnectionState(C0073R.string.connected);
                DeviceControlActivity.this.mConnectionState.setTextColor(-16749051);
                DeviceControlActivity.this.invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                d = Log.d(DeviceControlActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_DISCONNECTED");
                access$302 = DeviceControlActivity.access$302(DeviceControlActivity.this, false);
                DeviceControlActivity.this.updateConnectionState(C0073R.string.disconnected);
                DeviceControlActivity.this.mConnectionState.setTextColor(SupportMenu.CATEGORY_MASK);
                DeviceControlActivity.this.invalidateOptionsMenu();
                DeviceControlActivity.this.device_mode.setText("N/A");
                DeviceControlActivity.this.device_mode.setTextColor(SupportMenu.CATEGORY_MASK);
                DeviceControlActivity.this.imageGreenLight1.setVisibility(4);
                DeviceControlActivity.this.imageGreenLight2.setVisibility(4);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                d = Log.d(DeviceControlActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_SERVICES_DISCOVERED");
                DeviceControlActivity.this.findMldpGattService(DeviceControlActivity.this.mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                d = Log.d(DeviceControlActivity.TAG, " BroadcastReceiver.onReceive ACTION_DATA_AVAILABLE");
                DeviceControlActivity.this.processIncomingPacket(intent2.getStringExtra(BluetoothLeService.EXTRA_DATA));
            } else if (BluetoothLeService.ACTION_DATA_WRITTEN.equals(action)) {
                d = Log.d(DeviceControlActivity.TAG, " BroadcastReceiver.onReceive ACTION_DATA_WRITTEN");
                access$302 = DeviceControlActivity.access$1102(DeviceControlActivity.this, true);
            }
            d = Log.d(DeviceControlActivity.TAG, "BroadcastReceiver.onReceive End");
        }
    }

    class C00664 implements OnClickListener {
        C00664() {
            C00664 c00664 = this;
        }

        public void onClick(View view) {
            View view2 = view;
            if (DeviceControlActivity.this.mDataMLDP == null) {
                int d = Log.d(DeviceControlActivity.TAG, "LED pressed before MLDP Service found");
                return;
            }
            boolean access$1302 = DeviceControlActivity.access$1302(DeviceControlActivity.this, !DeviceControlActivity.this.led1_On);
            if (DeviceControlActivity.this.led1_On) {
                DeviceControlActivity.this.imageGreenLight1.setVisibility(0);
                access$1302 = DeviceControlActivity.this.mDataMLDP.setValue("D1ON\r\n");
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
                return;
            }
            DeviceControlActivity.this.imageGreenLight1.setVisibility(4);
            access$1302 = DeviceControlActivity.this.mDataMLDP.setValue("D1OFF\r\n");
            DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
        }
    }

    class C00675 implements OnClickListener {
        C00675() {
            C00675 c00675 = this;
        }

        public void onClick(View view) {
            View view2 = view;
            if (DeviceControlActivity.this.mDataMLDP == null) {
                int d = Log.d(DeviceControlActivity.TAG, "LED pressed before MLDP Service found");
                return;
            }
            boolean access$1402 = DeviceControlActivity.access$1402(DeviceControlActivity.this, !DeviceControlActivity.this.led2_On);
            if (DeviceControlActivity.this.led2_On) {
                DeviceControlActivity.this.imageGreenLight2.setVisibility(0);
                access$1402 = DeviceControlActivity.this.mDataMLDP.setValue("D2ON\r\n");
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
                return;
            }
            DeviceControlActivity.this.imageGreenLight2.setVisibility(4);
            access$1402 = DeviceControlActivity.this.mDataMLDP.setValue("D2OFF\r\n");
            DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
        }
    }

    class C00686 implements OnClickListener {
        C00686() {
            C00686 c00686 = this;
        }

        public void onClick(View view) {
            View view2 = view;
            if (DeviceControlActivity.this.mDataMLDP == null) {
                int d = Log.d(DeviceControlActivity.TAG, "LED pressed before MLDP Service found");
            }
            if (DeviceControlActivity.this.data.getText().length() > 32) {
                Toast.makeText(DeviceControlActivity.this.getApplicationContext(), "Data field cannot be more than 32 in size", 0).show();
                return;
            }
            if (String.valueOf(DeviceControlActivity.this.data.getText()).compareTo("") == 0) {
                Toast.makeText(DeviceControlActivity.this.getApplicationContext(), "Data field cannot be blank!", 0).show();
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean value = DeviceControlActivity.this.mDataMLDP.setValue("ENCRYPTION_ON\r\n");
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                StringBuilder stringBuilder = r9;
                StringBuilder stringBuilder2 = new StringBuilder();
                String temp = stringBuilder.append(String.valueOf(DeviceControlActivity.this.data.getText())).append("00000000000000000000000000000000").toString().substring(0, 32);
                String PlainString = "";
                value = DeviceControlActivity.this.mDataMLDP.setValue(temp.substring(0, 16));
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e22) {
                    e22.printStackTrace();
                }
                PlainString = temp.substring(16, 32);
                stringBuilder = r9;
                stringBuilder2 = new StringBuilder();
                value = DeviceControlActivity.this.mDataMLDP.setValue(stringBuilder.append(PlainString).append("\r\n").toString());
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
            }
        }
    }

    class C00697 implements OnClickListener {
        C00697() {
            C00697 c00697 = this;
        }

        public void onClick(View view) {
            View view2 = view;
            if (DeviceControlActivity.this.mDataMLDP == null) {
                int d = Log.d(DeviceControlActivity.TAG, "LED pressed before MLDP Service found");
            }
            if (DeviceControlActivity.this.data1.getText().length() > 32) {
                Toast.makeText(DeviceControlActivity.this.getApplicationContext(), "Data field cannot be more than 32 in size", 0).show();
                return;
            }
            if (String.valueOf(DeviceControlActivity.this.data1.getText()).compareTo("") == 0) {
                Toast.makeText(DeviceControlActivity.this.getApplicationContext(), "Data field cannot be blank!", 0).show();
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean value = DeviceControlActivity.this.mDataMLDP.setValue("DECRYPT\r\n");
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                StringBuilder stringBuilder = r9;
                StringBuilder stringBuilder2 = new StringBuilder();
                String temp = stringBuilder.append(String.valueOf(DeviceControlActivity.this.data1.getText())).append("00000000000000000000000000000000").toString().substring(0, 32);
                String CipherString = "";
                value = DeviceControlActivity.this.mDataMLDP.setValue(temp.substring(0, 16));
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e22) {
                    e22.printStackTrace();
                }
                CipherString = temp.substring(16, 32);
                stringBuilder = r9;
                stringBuilder2 = new StringBuilder();
                value = DeviceControlActivity.this.mDataMLDP.setValue(stringBuilder.append(CipherString).append("\r\n").toString());
                DeviceControlActivity.this.mBluetoothLeService.writeCharacteristic(DeviceControlActivity.this.mDataMLDP);
            }
        }
    }

    public DeviceControlActivity() {
        Activity activity = this;
        C00631 c00631 = r5;
        C00631 c006312 = new C00631();
        this.mServiceConnection = c00631;
        BroadcastReceiver broadcastReceiver = r5;
        BroadcastReceiver c00642 = new C00642();
        this.mGattUpdateReceiver = broadcastReceiver;
        C00664 c00664 = r5;
        C00664 c006642 = new C00664();
        this.imageLED1ClickListener = c00664;
        C00675 c00675 = r5;
        C00675 c006752 = new C00675();
        this.imageLED2ClickListener = c00675;
        C00686 c00686 = r5;
        C00686 c006862 = new C00686();
        this.EncryptClickListener = c00686;
        C00697 c00697 = r5;
        C00697 c006972 = new C00697();
        this.DecryptClickListener = c00697;
    }

    static /* synthetic */ BluetoothLeService access$102(DeviceControlActivity deviceControlActivity, BluetoothLeService bluetoothLeService) {
        ContextWrapper contextWrapper = bluetoothLeService;
        ContextWrapper contextWrapper2 = contextWrapper;
        ContextWrapper contextWrapper3 = contextWrapper;
        deviceControlActivity.mBluetoothLeService = contextWrapper3;
        return contextWrapper2;
    }

    static /* synthetic */ boolean access$1102(DeviceControlActivity deviceControlActivity, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        deviceControlActivity.writeComplete = z4;
        return z3;
    }

    static /* synthetic */ boolean access$1302(DeviceControlActivity deviceControlActivity, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        deviceControlActivity.led1_On = z4;
        return z3;
    }

    static /* synthetic */ boolean access$1402(DeviceControlActivity deviceControlActivity, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        deviceControlActivity.led2_On = z4;
        return z3;
    }

    static /* synthetic */ boolean access$302(DeviceControlActivity deviceControlActivity, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        deviceControlActivity.mConnected = z4;
        return z3;
    }

    public void onCreate(Bundle bundle) {
        Bundle savedInstanceState = bundle;
        int d = Log.d(TAG, "onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(C0073R.layout.mldp_text_screen);
        Intent intent = getIntent();
        this.mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        this.mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        ((TextView) findViewById(C0073R.id.deviceAddress)).setText(this.mDeviceAddress);
        this.mConnectionState = (TextView) findViewById(C0073R.id.connectionState);
        this.decrypted = (TextView) findViewById(C0073R.id.textView11);
        this.data = (EditText) findViewById(C0073R.id.editText2);
        this.data1 = (EditText) findViewById(C0073R.id.editText3);
        this.imageGreenLight1 = (ImageView) findViewById(C0073R.id.imageGreenLight1);
        this.imageGreenLight1.setVisibility(4);
        this.imageGreenLight2 = (ImageView) findViewById(C0073R.id.imageGreenLight2);
        this.imageGreenLight2.setVisibility(4);
        this.imageLED1 = (ImageView) findViewById(C0073R.id.imageLED1);
        this.BtnEncrypt = (Button) findViewById(C0073R.id.Btn_encrypt);
        this.BtnDecrypt = (Button) findViewById(C0073R.id.button2);
        this.imageLED1.setOnClickListener(this.imageLED1ClickListener);
        this.imageLED2 = (ImageView) findViewById(C0073R.id.imageLED2);
        this.imageLED2.setOnClickListener(this.imageLED2ClickListener);
        this.BtnDecrypt.setOnClickListener(this.DecryptClickListener);
        this.BtnEncrypt.setOnClickListener(this.EncryptClickListener);
        String str = r8;
        String str2 = new String();
        this.incomingMessage = str;
        this.device_mode = (TextView) findViewById(C0073R.id.textView7);
        this.switch1 = (Switch) findViewById(C0073R.id.switch_1);
        this.switch2 = (Switch) findViewById(C0073R.id.switch_2);
        getActionBar().setTitle(this.mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent2 = r8;
        Intent intent3 = new Intent(this, BluetoothLeService.class);
        boolean bindService = bindService(intent2, this.mServiceConnection, 1);
        d = Log.d(TAG, "onCreate End");
    }

    protected void onResume() {
        int d = Log.d(TAG, "onResume Start");
        super.onResume();
        Intent registerReceiver = registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (this.mBluetoothLeService != null) {
            boolean result = this.mBluetoothLeService.connect(this.mDeviceAddress);
            String str = TAG;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            d = Log.d(str, stringBuilder.append("Connect request result = ").append(result).toString());
        }
        d = Log.d(TAG, "onResume End");
    }

    protected void onPause() {
        int d = Log.d(TAG, "onPause Start");
        super.onPause();
        unregisterReceiver(this.mGattUpdateReceiver);
        d = Log.d(TAG, "onPause End");
    }

    protected void onDestroy() {
        int d = Log.d(TAG, "onDestroy Start");
        super.onDestroy();
        unbindService(this.mServiceConnection);
        this.mBluetoothLeService = null;
        d = Log.d(TAG, "onDestroy End");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Menu menu2 = menu;
        getMenuInflater().inflate(C0073R.menu.gatt_services, menu2);
        MenuItem visible;
        if (this.mConnected) {
            visible = menu2.findItem(C0073R.id.menu_connect).setVisible(false);
            visible = menu2.findItem(C0073R.id.menu_disconnect).setVisible(false);
        } else {
            visible = menu2.findItem(C0073R.id.menu_connect).setVisible(false);
            visible = menu2.findItem(C0073R.id.menu_disconnect).setVisible(false);
        }
        return 1;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Activity this = this;
        MenuItem item = menuItem;
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return 1;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        int d = Log.d(TAG, "makeGattUpdateIntentFilter Start & End");
        IntentFilter intentFilter = r3;
        IntentFilter intentFilter2 = new IntentFilter();
        IntentFilter intentFilter3 = intentFilter;
        intentFilter3.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter3.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter3.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter3.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter3.addAction(BluetoothLeService.ACTION_DATA_WRITTEN);
        return intentFilter3;
    }

    private void updateConnectionState(int i) {
        int resourceId = i;
        int d = Log.d(TAG, "updateConnectionState Start & End");
        C00653 c00653 = r7;
        final int i2 = resourceId;
        C00653 c006532 = new Runnable(this) {
            final /* synthetic */ DeviceControlActivity this$0;

            public void run() {
                this.this$0.mConnectionState.setText(i2);
            }
        };
        runOnUiThread(c00653);
    }

    private void processIncomingPacket(String str) {
        this.incomingMessage = this.incomingMessage.concat(str);
        while (this.incomingMessage.contains("\r")) {
            int indexEnd = this.incomingMessage.indexOf("\r");
            if (this.incomingMessage.contains("S1ON")) {
                this.switch1.setChecked(true);
            } else if (this.incomingMessage.contains("S1OFF")) {
                this.switch1.setChecked(false);
            } else if (this.incomingMessage.contains("S2ON")) {
                this.switch2.setChecked(true);
            } else if (this.incomingMessage.contains("S2OFF")) {
                this.switch2.setChecked(false);
            } else if (this.incomingMessage.contains("E_D")) {
                temp = this.incomingMessage.replace("E_D", "").replace("\r", "").replace("\n", "");
                r7 = r12;
                r8 = new StringBuilder();
                this.enc_data = r7.append(this.enc_data).append(temp).toString();
                this.data1.setText(this.enc_data);
                this.enc_data = "";
            } else if (this.incomingMessage.contains("D_D")) {
                temp = this.incomingMessage.replace("D_D", "").replace("\r", "").replace("\n", "");
                r7 = r12;
                r8 = new StringBuilder();
                this.dec_data = r7.append(this.dec_data).append(temp).toString();
                this.decrypted.setText(this.dec_data);
                this.dec_data = "";
            } else if (this.incomingMessage.contains("ACTIVE")) {
                this.device_mode.setText("Active");
                this.device_mode.setTextColor(-16749051);
                this.BtnEncrypt.setEnabled(true);
                this.BtnDecrypt.setEnabled(true);
                this.imageLED1.setEnabled(true);
                this.imageLED2.setEnabled(true);
            } else if (this.incomingMessage.contains("SLEEP")) {
                this.device_mode.setText("Sleep");
                this.device_mode.setTextColor(SupportMenu.CATEGORY_MASK);
                this.BtnEncrypt.setEnabled(false);
                this.BtnDecrypt.setEnabled(false);
                this.imageLED1.setEnabled(false);
                this.imageLED2.setEnabled(false);
            } else if (this.incomingMessage.contains("TEMP ") && this.incomingMessage.contains(".")) {
                int indexStart = this.incomingMessage.indexOf("TEMP ");
                if (this.incomingMessage.indexOf(".") == indexStart + 8 && this.incomingMessage.length() >= indexStart + 10) {
                    TextView textView;
                    if (this.incomingMessage.charAt(indexStart + 5) != '0') {
                        textView = this.textTemperature;
                        r7 = r12;
                        r8 = new StringBuilder();
                        textView.setText(r7.append(this.incomingMessage.subSequence(indexStart + 5, indexStart + 10)).append(" °C").toString());
                    } else if (this.incomingMessage.charAt(indexStart + 6) == '0') {
                        textView = this.textTemperature;
                        r7 = r12;
                        r8 = new StringBuilder();
                        textView.setText(r7.append(this.incomingMessage.subSequence(indexStart + 7, indexStart + 10)).append(" °C").toString());
                    } else {
                        textView = this.textTemperature;
                        r7 = r12;
                        r8 = new StringBuilder();
                        textView.setText(r7.append(this.incomingMessage.subSequence(indexStart + 6, indexStart + 10)).append(" °C").toString());
                    }
                }
            }
            this.incomingMessage = this.incomingMessage.substring(indexEnd + 1);
        }
    }

    private void findMldpGattService(List<BluetoothGattService> list) {
        Context this = this;
        List<BluetoothGattService> gattServices = list;
        int d = Log.d(TAG, "displayGattServices");
        if (gattServices == null) {
            d = Log.d(TAG, "findMldpGattService found no Services");
            return;
        }
        this.mDataMLDP = null;
        for (BluetoothGattService gattService : gattServices) {
            if (gattService.getUuid().toString().equals("00035b03-58e6-07dd-021a-08123a000300")) {
                for (BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
                    String uuid = gattCharacteristic.getUuid().toString();
                    if (uuid.equals("00035b03-58e6-07dd-021a-08123a000301")) {
                        this.mDataMLDP = gattCharacteristic;
                        d = Log.d(TAG, "findMldpGattService found MLDP data characteristics");
                    } else if (uuid.equals("00035b03-58e6-07dd-021a-08123a0003ff")) {
                        this.mControlMLDP = gattCharacteristic;
                        d = Log.d(TAG, "findMldpGattService found MLDP control characteristics");
                    }
                    int characteristicProperties = gattCharacteristic.getProperties();
                    if ((characteristicProperties & 16) > 0) {
                        this.mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        d = Log.d(TAG, "findMldpGattService PROPERTY_NOTIFY");
                    }
                    if ((characteristicProperties & 32) > 0) {
                        this.mBluetoothLeService.setCharacteristicIndication(gattCharacteristic, true);
                        d = Log.d(TAG, "findMldpGattService PROPERTY_INDICATE");
                    }
                    if ((characteristicProperties & 8) > 0) {
                        gattCharacteristic.setWriteType(2);
                        d = Log.d(TAG, "findMldpGattService PROPERTY_WRITE");
                    }
                    if ((characteristicProperties & 4) > 0) {
                        gattCharacteristic.setWriteType(1);
                        d = Log.d(TAG, "findMldpGattService PROPERTY_WRITE_NO_RESPONSE");
                    }
                    d = Log.d(TAG, "findMldpGattService found MLDP service and characteristics");
                }
                if (this.mDataMLDP == null) {
                    Toast.makeText(this, C0073R.string.mldp_not_supported, 0).show();
                    d = Log.d(TAG, "findMldpGattService found no MLDP service");
                    finish();
                }
            }
        }
        if (this.mDataMLDP == null) {
            Toast.makeText(this, C0073R.string.mldp_not_supported, 0).show();
            d = Log.d(TAG, "findMldpGattService found no MLDP service");
            finish();
        }
    }
}
