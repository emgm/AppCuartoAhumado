package co.edu.ufpso.appcuartoahumado;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DESARROLLO-07 on 29/01/2018.
 */
class ConnectedThread extends Thread {

    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public static Handler mHandler;

    public static final int MESSAGE_READ  =  3;

    public ConnectedThread(BluetoothSocket socket) {

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {

        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs

        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

                String datosBT = new String(buffer, 0, bytes);

                // Send the obtained bytes to the UI activity

                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, datosBT)
                            .sendToTarget();

            } catch (IOException e) {
                break;
            }
        }
    }

        /* Call this from the main activity to send data to the remote device */

        public boolean enviarDatosArduino(String datosParaEnviar, Context mContext) {

            boolean statusSend = false;

            byte[] msgBuffer = datosParaEnviar.getBytes();

            try {
                mmOutStream.write(msgBuffer);

                statusSend = true;

            } catch (IOException e) {

               // Constant.editor = Constant.spSettings.edit();

                //Constant.editor.putString("methodConnect", "");
                //Constant.editor.putString("ConnectDeviceBT", "OFF");
                //Constant.editor.putString("macDeviceBT", "");
               // Constant.editor.putString("nameDeviceBT", "");

               // Constant.editor.commit();

             //   mContext.stopService(new Intent(mContext, ServiceQueryLevelGas.class));

                Intent i = new Intent(mContext, Principal.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);

                Toast.makeText(mContext,"Se ha producido perdida de comunicación.", Toast.LENGTH_LONG).show();

                Log.e("mensaje", "detalle error: " + e);

                statusSend = false;
            }

            return  statusSend;
        }


}


