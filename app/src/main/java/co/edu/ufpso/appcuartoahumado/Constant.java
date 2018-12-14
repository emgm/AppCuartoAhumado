package co.edu.ufpso.appcuartoahumado;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;

public class Constant {

    public static BluetoothAdapter mBluetoothAdapter = null; /// Adaptador
    public static BluetoothSocket mBTSocket = null;
    public static BluetoothDevice mBTDevice = null;

    public static UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static ConnectedThread connectedThread;

}
