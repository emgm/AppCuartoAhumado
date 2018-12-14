package co.edu.ufpso.appcuartoahumado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    Button btnSetting;
    TextView tvFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar my_ToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(my_ToolBar); // se da soporte para todas las versiones
        my_ToolBar.setPadding(0, 0, 0, 0);

        btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SettingBluetooth.class));

            }
        });


        tvFooter = findViewById(R.id.tvFooter);
        tvFooter.setText(Html.fromHtml("Desarrollado por <br/><b> Punto Vive Digital Lab Ocaña </b>"));
    }
}
