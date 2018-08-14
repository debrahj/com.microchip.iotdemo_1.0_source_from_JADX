package com.microchip.iotdemo;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class DeviceScanActivity extends ListActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = DeviceScanActivity.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private LeScanCallback mLeScanCallback;
    private boolean mScanning;

    class C00701 implements Runnable {
        C00701() {
            C00701 c00701 = this;
        }

        public void run() {
            boolean access$002 = DeviceScanActivity.access$002(DeviceScanActivity.this, false);
            DeviceScanActivity.this.mBluetoothAdapter.stopLeScan(DeviceScanActivity.this.mLeScanCallback);
            DeviceScanActivity.this.invalidateOptionsMenu();
        }
    }

    class C00722 implements LeScanCallback {
        C00722() {
            C00722 c00722 = this;
        }

        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            BluetoothDevice device = bluetoothDevice;
            int rssi = i;
            byte[] scanRecord = bArr;
            DeviceScanActivity deviceScanActivity = DeviceScanActivity.this;
            C00711 c00711 = r9;
            final BluetoothDevice bluetoothDevice2 = device;
            C00711 c007112 = new Runnable(this) {
                final /* synthetic */ C00722 this$1;

                public void run() {
                    DeviceScanActivity.this.mLeDeviceListAdapter.addDevice(bluetoothDevice2);
                    DeviceScanActivity.this.mLeDeviceListAdapter.notifyDataSetChanged();
                }
            };
            deviceScanActivity.runOnUiThread(c00711);
        }
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;
        private ArrayList<BluetoothDevice> mLeDevices;
        final /* synthetic */ DeviceScanActivity this$0;

        public LeDeviceListAdapter(DeviceScanActivity deviceScanActivity) {
            DeviceScanActivity deviceScanActivity2 = deviceScanActivity;
            this.this$0 = deviceScanActivity2;
            BaseAdapter baseAdapter = this;
            ArrayList arrayList = r5;
            ArrayList arrayList2 = new ArrayList();
            this.mLeDevices = arrayList;
            this.mInflator = deviceScanActivity2.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice bluetoothDevice) {
            BluetoothDevice device = bluetoothDevice;
            if (!this.mLeDevices.contains(device)) {
                boolean add = this.mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int i) {
            return (BluetoothDevice) this.mLeDevices.get(i);
        }

        public void clear() {
            this.mLeDevices.clear();
        }

        public int getCount() {
            return this.mLeDevices.size();
        }

        public Object getItem(int i) {
            return this.mLeDevices.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            LeDeviceListAdapter this = this;
            int i2 = i;
            View view2 = view;
            ViewGroup viewGroup2 = viewGroup;
            if (view2 == null) {
                view2 = this.mInflator.inflate(C0073R.layout.listitem_device, null);
                ViewHolder viewHolder2 = r10;
                ViewHolder viewHolder3 = new ViewHolder();
                viewHolder = viewHolder2;
                viewHolder.deviceAddress = (TextView) view2.findViewById(C0073R.id.device_address);
                viewHolder.deviceName = (TextView) view2.findViewById(C0073R.id.device_name);
                view2.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view2.getTag();
            }
            BluetoothDevice device = (BluetoothDevice) this.mLeDevices.get(i2);
            String deviceName = device.getName();
            if (deviceName == null || deviceName.length() <= 0) {
                viewHolder.deviceName.setText(C0073R.string.unknown_device);
            } else {
                viewHolder.deviceName.setText(deviceName);
            }
            viewHolder.deviceAddress.setText(device.getAddress());
            return view2;
        }
    }

    static class ViewHolder {
        TextView deviceAddress;
        TextView deviceName;

        ViewHolder() {
            ViewHolder viewHolder = this;
        }
    }

    public DeviceScanActivity() {
        ListActivity listActivity = this;
        C00722 c00722 = r5;
        C00722 c007222 = new C00722();
        this.mLeScanCallback = c00722;
    }

    static /* synthetic */ boolean access$002(DeviceScanActivity deviceScanActivity, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        deviceScanActivity.mScanning = z4;
        return z3;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActionBar().setTitle(C0073R.string.title_devices);
        Handler handler = r6;
        Handler handler2 = new Handler();
        this.mHandler = handler;
        if (!getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this, C0073R.string.ble_not_supported, 0).show();
            finish();
        }
        this.mBluetoothAdapter = ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
        if (this.mBluetoothAdapter == null) {
            Toast.makeText(this, C0073R.string.error_bluetooth_not_supported, 0).show();
            finish();
        }
    }

    protected void onResume() {
        super.onResume();
        if (!this.mBluetoothAdapter.isEnabled()) {
            Intent intent = r6;
            Intent intent2 = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            startActivityForResult(intent, 1);
        }
        LeDeviceListAdapter leDeviceListAdapter = r6;
        LeDeviceListAdapter leDeviceListAdapter2 = new LeDeviceListAdapter(this);
        this.mLeDeviceListAdapter = leDeviceListAdapter;
        setListAdapter(this.mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        this.mLeDeviceListAdapter.clear();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Menu menu2 = menu;
        getMenuInflater().inflate(C0073R.menu.main, menu2);
        MenuItem visible;
        if (this.mScanning) {
            visible = menu2.findItem(C0073R.id.menu_stop).setVisible(true);
            visible = menu2.findItem(C0073R.id.menu_scan).setVisible(false);
            visible = menu2.findItem(C0073R.id.menu_refresh).setActionView(C0073R.layout.actionbar_indeterminate_progress);
        } else {
            visible = menu2.findItem(C0073R.id.menu_stop).setVisible(false);
            visible = menu2.findItem(C0073R.id.menu_scan).setVisible(true);
        }
        return 1;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        DeviceScanActivity this = this;
        switch (menuItem.getItemId()) {
            case C0073R.id.menu_scan:
                this.mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case C0073R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return 1;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        ListActivity this = this;
        int requestCode = i;
        int resultCode = i2;
        Intent data = intent;
        if (requestCode == 1 && resultCode == 0) {
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void onListItemClick(ListView listView, View view, int i, long j) {
        ListView l = listView;
        View v = view;
        long id = j;
        BluetoothDevice device = this.mLeDeviceListAdapter.getDevice(i);
        if (device != null) {
            Intent intent = r12;
            Intent intent2 = new Intent(this, DeviceControlActivity.class);
            Intent intent3 = intent;
            intent = intent3.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
            intent = intent3.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
            if (this.mScanning) {
                this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
                this.mScanning = false;
            }
            startActivity(intent3);
        }
    }

    private void scanLeDevice(boolean z) {
        DeviceScanActivity this = this;
        if (z) {
            Handler handler = this.mHandler;
            C00701 c00701 = r6;
            C00701 c007012 = new C00701();
            boolean postDelayed = handler.postDelayed(c00701, SCAN_PERIOD);
            this.mScanning = true;
            postDelayed = this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
        } else {
            this.mScanning = false;
            this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
        }
        invalidateOptionsMenu();
    }
}
