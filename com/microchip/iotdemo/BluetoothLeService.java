package com.microchip.iotdemo;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.microchip.android.btle.example.ACTION_DATA_AVAILABLE";
    public static final String ACTION_DATA_WRITTEN = "com.microchip.android.btle.example.ACTION_DATA_WRITTEN";
    public static final String ACTION_GATT_CONNECTED = "com.microchip.android.btle.example.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.microchip.android.btle.example.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.microchip.android.btle.example.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String EXTRA_DATA = "com.microchip.android.btle.example.EXTRA_DATA";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final String TAG = BluetoothLeService.class.getSimpleName();
    public static final UUID UUID_CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR = UUID.fromString(GattAttributes.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR);
    public static final UUID UUID_MLDP_DATA_PRIVATE_CHARACTERISTIC = UUID.fromString(GattAttributes.MLDP_DATA_PRIVATE_CHAR);
    private final IBinder mBinder;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private int mConnectionState = 0;
    private final BluetoothGattCallback mGattCallback;

    class C00621 extends BluetoothGattCallback {
        C00621() {
            BluetoothGattCallback bluetoothGattCallback = this;
        }

        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            C00621 this = this;
            BluetoothGatt gatt = bluetoothGatt;
            int status = i;
            int newState = i2;
            int d = Log.d(BluetoothLeService.TAG, "BluetoothGattCallback.onConnectionStateChange Start & End");
            String intentAction;
            if (newState == 2) {
                intentAction = BluetoothLeService.ACTION_GATT_CONNECTED;
                d = BluetoothLeService.access$102(BluetoothLeService.this, 2);
                BluetoothLeService.this.broadcastUpdate(intentAction);
                d = Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
                String access$000 = BluetoothLeService.TAG;
                StringBuilder stringBuilder = r8;
                StringBuilder stringBuilder2 = new StringBuilder();
                d = Log.i(access$000, stringBuilder.append("Attempting to start service discovery: ").append(BluetoothLeService.this.mBluetoothGatt.discoverServices()).toString());
            } else if (newState == 0) {
                intentAction = BluetoothLeService.ACTION_GATT_DISCONNECTED;
                d = BluetoothLeService.access$102(BluetoothLeService.this, 0);
                d = Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
                BluetoothLeService.this.broadcastUpdate(intentAction);
            }
        }

        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            C00621 this = this;
            BluetoothGatt gatt = bluetoothGatt;
            int status = i;
            int d = Log.d(BluetoothLeService.TAG, "BluetoothGattCallback.onServicesDiscovered Start & End");
            if (status == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
                return;
            }
            String access$000 = BluetoothLeService.TAG;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            d = Log.w(access$000, stringBuilder.append("onServicesDiscovered received: ").append(status).toString());
        }

        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            C00621 this = this;
            BluetoothGatt gatt = bluetoothGatt;
            BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
            int status = i;
            int d = Log.d(BluetoothLeService.TAG, "BluetoothGattCallback.onCharacteristicRead Start & End");
            if (status == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            C00621 this = this;
            BluetoothGatt gatt = bluetoothGatt;
            BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
            int status = i;
            int d = Log.d(BluetoothLeService.TAG, "BluetoothGattCallback.onCharacteristicWrite");
            if (status == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_WRITTEN, characteristic);
            }
        }

        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BluetoothGatt gatt = bluetoothGatt;
            BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
            int d = Log.d(BluetoothLeService.TAG, "BluetoothGattCallback.onCharacteristicChanged");
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, characteristic);
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
            Binder binder = this;
        }

        BluetoothLeService getService() {
            int d = Log.d(BluetoothLeService.TAG, "LocalBinder.getService Start & End");
            return BluetoothLeService.this;
        }
    }

    public BluetoothLeService() {
        Service service = this;
        LocalBinder localBinder = r5;
        LocalBinder localBinder2 = new LocalBinder();
        this.mBinder = localBinder;
        BluetoothGattCallback bluetoothGattCallback = r5;
        BluetoothGattCallback c00621 = new C00621();
        this.mGattCallback = bluetoothGattCallback;
    }

    static /* synthetic */ int access$102(BluetoothLeService bluetoothLeService, int i) {
        int i2 = i;
        int i3 = i2;
        int i4 = i2;
        bluetoothLeService.mConnectionState = i4;
        return i3;
    }

    public IBinder onBind(Intent intent) {
        Intent intent2 = intent;
        int d = Log.d(TAG, "onBind Start & End");
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        Intent intent2 = intent;
        int d = Log.d(TAG, "onUnbind Start & End");
        close();
        return super.onUnbind(intent2);
    }

    private void broadcastUpdate(String str) {
        String action = str;
        int d = Log.d(TAG, "broadcastUpdate1");
        Intent intent = r6;
        Intent intent2 = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothLeService this = this;
        String action = str;
        BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
        int d = Log.d(TAG, "broadcastUpdate");
        Intent intent = r8;
        Intent intent2 = new Intent(action);
        Intent intent3 = intent;
        if (!action.equals(ACTION_DATA_AVAILABLE)) {
            String str2 = TAG;
            StringBuilder stringBuilder = r8;
            StringBuilder stringBuilder2 = new StringBuilder();
            d = Log.d(str2, stringBuilder.append("Action: ").append(action).toString());
        } else if (UUID_MLDP_DATA_PRIVATE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            intent = intent3.putExtra(EXTRA_DATA, characteristic.getStringValue(0));
        }
        sendBroadcast(intent3);
    }

    public boolean initialize() {
        int d = Log.d(TAG, "initialize Start & End");
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                d = Log.e(TAG, "Unable to initialize BluetoothManager.");
                return null;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter != null) {
            return 1;
        }
        d = Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return null;
    }

    public boolean connect(String str) {
        String address = str;
        int d = Log.d(TAG, "connect Start & End");
        if (this.mBluetoothAdapter == null || address == null) {
            d = Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return null;
        } else if (this.mBluetoothDeviceAddress == null || !address.equals(this.mBluetoothDeviceAddress) || this.mBluetoothGatt == null) {
            BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
            if (device == null) {
                d = Log.w(TAG, "Device not found.  Unable to connect.");
                return null;
            }
            this.mBluetoothGatt = device.connectGatt(this, false, this.mGattCallback);
            d = Log.d(TAG, "Trying to create a new connection.");
            this.mBluetoothDeviceAddress = address;
            this.mConnectionState = 1;
            return 1;
        } else {
            d = Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (!this.mBluetoothGatt.connect()) {
                return null;
            }
            this.mConnectionState = 1;
            return 1;
        }
    }

    public void disconnect() {
        int d = Log.d(TAG, "disconnect Start & End");
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            d = Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.disconnect();
        }
    }

    public void close() {
        int d = Log.d(TAG, "close Start & End");
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
        int d = Log.d(TAG, "readCharacteristic Start & End");
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            d = Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            boolean readCharacteristic = this.mBluetoothGatt.readCharacteristic(characteristic);
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
        int d = Log.d(TAG, "writeCharacteristic Start");
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            d = Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        int test = characteristic.getProperties();
        if ((test & 8) != 0 || (test & 4) != 0) {
            if (this.mBluetoothGatt.writeCharacteristic(characteristic)) {
                d = Log.d(TAG, "writeCharacteristic successful");
            } else {
                d = Log.d(TAG, "writeCharacteristic failed");
            }
            d = Log.d(TAG, "writeCharacteristic End");
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
        boolean enabled = z;
        int d = Log.d(TAG, "setCharacteristicNotification Start & End");
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            d = Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        boolean characteristicNotification = this.mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (UUID_MLDP_DATA_PRIVATE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR);
            characteristicNotification = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            characteristicNotification = this.mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void setCharacteristicIndication(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristic;
        boolean enabled = z;
        int d = Log.d(TAG, "setCharacteristicIndication");
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            d = Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        boolean characteristicNotification = this.mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (UUID_MLDP_DATA_PRIVATE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR);
            characteristicNotification = descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            characteristicNotification = this.mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        int d = Log.d(TAG, "getSupportedGattServices Start & End");
        if (this.mBluetoothGatt == null) {
            return null;
        }
        return this.mBluetoothGatt.getServices();
    }
}
