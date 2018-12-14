package co.edu.ufpso.appcuartoahumado;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by DESARROLLO-07 on 25/01/2018.
 */

public class ListDevices extends ListActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    BluetoothAdapter mBluetoothAdapter2 = null;

    static String DIRECCION_MAC = null;
    ArrayAdapter<String> ArrayBluetooth;

    BroadcastReceiver mReceiver;

    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();


        Set<BluetoothDevice> dispositivosDisponibles = mBluetoothAdapter2.getBondedDevices();


        if(dispositivosDisponibles.size() > 0){

            for(BluetoothDevice dispositivo: dispositivosDisponibles){

                String nameBt = dispositivo.getName();
                String macBt = dispositivo.getAddress();

                ArrayBluetooth.add(nameBt + "\n" + macBt);
            }

            setListAdapter(ArrayBluetooth);

        }else{

            this.setTitle("No hay dispotivos disponibles");
        }

        /*

        if (Build.VERSION.SDK_INT >= 23) {

            //int PERMISSION_REQUEST_CODE = 1;

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

              // requestPermissions(
                      //  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                      // ACCESS_FINE_LOCATION);
            }else{
                searchDevicesBT();
            }


        }else{
            searchDevicesBT();
        }

    } // Fin onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

       switch (requestCode){

           case PERMISSION_REQUEST_CODE:

               // If request is cancelled, the result arrays are empty.
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   // permission was granted, yay! Do the
                   // contacts-related task you need to do.

                   Log.i("Status Permiso", " Aprobado");

                   searchDevicesBT();

               } else {

                   // permission denied, boo! Disable the
                   // functionality that depends on this permission.

                   Log.i("Status Permiso", " No Aprobado o cancelado");

                   this.setTitle("Por favor apruebe el permiso.");
               }

               return;

       }

        */

    }

    private void searchDevicesBT() {

        if (mBluetoothAdapter2.isDiscovering()) {
            // El Bluetooth ya está en modo discover, lo cancelamos para iniciarlo de nuevo
            mBluetoothAdapter2.cancelDiscovery();
        }

        this.setTitle("Buscando dispositivos ...");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                cambioTitulo(0);

                mBluetoothAdapter2.startDiscovery();
                mReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                            // Se ha encontrado un dispositivo Bluetooth
                            // Se obtiene la información del dispositivo del intent
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            Log.i("Dispositivo encontrado:", " " + device.getName() + "; MAC " + device.getAddress());
                            ArrayBluetooth.add(device.getName() + "\n" + device.getAddress());

                            cambioTitulo(1);
                        }
                    }
                };
                // Se registra el broadcast receiver
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

                setListAdapter(ArrayBluetooth);
            }
        }, SCAN_PERIOD);

                    //ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder()
                           // .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);

                    //mScanSettings = scanSettingsBuilder.build();
                   // mBluetoothLeScanner = mBluetoothAdapter2.getBluetoothLeScanner();

                    //scanLeDevice(true);

            /*
            int permissionCheck = ContextCompat.checkSelfPermission(Constant.contexto, Manifest.permission.ACCESS_FINE_LOCATION);
               permissionCheck += ContextCompat.checkSelfPermission(Constant.contexto, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..

                Log.i("BT API > 21", "IF");
            }*/
    }

    private void cambioTitulo(int cantidad) {

        if(cantidad > 0) {
            this.setTitle("Dispositivos disponibles");
        }else{
            this.setTitle("Dispositivo no conectado.");
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String información  = ((TextView) v).getText().toString();

        String direccionMac = información.substring(información.length() - 17);

        String nameDeviceBT = información.substring(0, información.length() - 17);
        nameDeviceBT.trim();

        Intent returDates = new Intent();

        returDates.putExtra("nameDeviceBT", nameDeviceBT);
        returDates.putExtra(DIRECCION_MAC, direccionMac);

        setResult(RESULT_OK, returDates);
        finish();

    }

    @Override
    protected void onStop() {

        if(null != mReceiver){

            unregisterReceiver(mReceiver);
        }

        super.onStop();
    }

}