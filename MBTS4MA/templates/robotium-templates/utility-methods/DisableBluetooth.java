BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    

if (!bluetoothAdapter.isEnabled()) {
    bluetoothAdapter.enable(); 
}

solo.sleep(5000);

// <uses-permission android:name="android.permission.BLUETOOTH"/>
// <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>