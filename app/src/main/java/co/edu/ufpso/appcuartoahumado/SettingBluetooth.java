package co.edu.ufpso.appcuartoahumado;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import static co.edu.ufpso.appcuartoahumado.Constant.connectedThread;
import java.io.IOException;

public class SettingBluetooth extends AppCompatActivity {

    Switch swStatusBT, swStatusConnectDeviceBT;
    TextView tvStatusBT;

    private static final int SOLICITA_ACTIVACION =  1;
    private static final int SOLICITA_CONEXION   =  2;

    private static String MAC = null;
    private static String NAME_DEVICE_CONNECT_BT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bluetooth);

        Toolbar my_ToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(my_ToolBar); // se da soporte para todas las versiones
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        my_ToolBar.setPadding(0, 0, 0, 0);

        swStatusBT = findViewById(R.id.swStatusBT);
        swStatusConnectDeviceBT = findViewById(R.id.swStatusConnectDeviceBT);

        tvStatusBT = findViewById(R.id.tvStatusBT);

        Constant.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        swStatusBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    if(Constant.mBluetoothAdapter == null) {

                        Toast.makeText(getApplicationContext(), "El Bluetooth no esta disponible en este dispositivo", Toast.LENGTH_LONG).show();
                        swStatusBT.setChecked(false);

                    }else if(!Constant.mBluetoothAdapter.isEnabled()) {

                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); // Solicita al usuario permiso para activar el bluetooth
                        startActivityForResult(enableBtIntent, SOLICITA_ACTIVACION);
                    }
                }else{

                    tvStatusBT.setText("Habilitar Bluetooth");

                    Constant.mBluetoothAdapter.disable();
                }
            }
        });

        swStatusConnectDeviceBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    Intent listDevices = new Intent(SettingBluetooth.this, ListDevices.class);
                    startActivityForResult(listDevices, SOLICITA_CONEXION);

                }else{

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            //Pregunta al usuario si quiere activar el bluetooth

            case SOLICITA_ACTIVACION:

                if (resultCode == Activity.RESULT_OK) {

                    tvStatusBT.setText("Deshabilitar Bluetooth");

                } else {

                    Toast.makeText(getApplicationContext(), "El Bluetooth no fue activado", Toast.LENGTH_SHORT).show();

                   // showStatusBT("Habilitar Bluetooth", false);
                }

                break;

            case SOLICITA_CONEXION:

                if(resultCode == Activity.RESULT_OK){

                    MAC = data.getExtras().getString(ListDevices.DIRECCION_MAC);
                    NAME_DEVICE_CONNECT_BT = data.getExtras().getString("nameDeviceBT");

                    Toast.makeText(getApplicationContext(), "MAC Obtenida: " + MAC, Toast.LENGTH_SHORT).show();

                    Constant.mBTDevice = Constant.mBluetoothAdapter.getRemoteDevice(MAC);

                    try {

                        Constant.mBTSocket = Constant.mBTDevice.createRfcommSocketToServiceRecord(Constant.MEU_UUID);

                        Constant.mBTSocket.connect();

                        //Constant.conexionBluetooth = true;

                        connectedThread = new ConnectedThread(Constant.mBTSocket);
                        connectedThread.start();

                        //connectedThread.enviarDatosArduino("ledsOn", getApplicationContext());

                        //rlDeviceConnectBT.setVisibility(View.VISIBLE);

                       // tvMACDeviceBT.setText(MAC);
                       // tvNameDeviceBT.setText(NAME_DEVICE_CONNECT_BT);

                        Toast.makeText(getApplicationContext(), "Dispositivo conectado con: " + MAC, Toast.LENGTH_SHORT).show();

                        //Constant.editor = Constant.spSettings.edit();

                        //Constant.editor.putString("methodConnect", "Bluetooth");
                        //Constant.editor.putString("ConnectDeviceBT", "ON");
                        //Constant.editor.putString("macDeviceBT", MAC);
                       // Constant.editor.putString("nameDeviceBT", NAME_DEVICE_CONNECT_BT);

                       // Constant.editor.commit();


                    }catch (IOException erro){

                       // Constant.conexionBluetooth = false;
                        swStatusConnectDeviceBT.setChecked(false);

                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error: " + erro, Toast.LENGTH_SHORT).show();
                    }

                }else{

                    swStatusConnectDeviceBT.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Por favor seleccione un dispositivo para establecer la conexión", Toast.LENGTH_SHORT).show();

                }

               // tvLoadConnectDeviceBT.setVisibility(View.GONE);

                break;

        }
    }
}
