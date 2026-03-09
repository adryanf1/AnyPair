package com.user.anypair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

// Aplicamos la vacuna Anti-D8: implementamos el Listener aquí mismo
public class SettingsActivity extends Activity implements View.OnClickListener {
    private SharedPreferences prefs;
    private RadioGroup rgTiempo;
    private EditText etSubtitulo, etBoton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("AnyPairConfig", Context.MODE_PRIVATE);
        
        rgTiempo = (RadioGroup) findViewById(R.id.rg_tiempo);
        etSubtitulo = (EditText) findViewById(R.id.et_subtitulo);
        etBoton = (EditText) findViewById(R.id.et_boton);

        // Cargar preferencias guardadas
        int tiempo = prefs.getInt("tiempo_popup", 7000);
        if (tiempo == 3000) ((RadioButton) findViewById(R.id.rb_3s)).setChecked(true);
        else if (tiempo == 0) ((RadioButton) findViewById(R.id.rb_inf)).setChecked(true);
        else ((RadioButton) findViewById(R.id.rb_7s)).setChecked(true);

        etSubtitulo.setText(prefs.getString("subtitulo_popup", "Dispositivo conectado"));
        etBoton.setText(prefs.getString("texto_boton", "Listo"));

        // Asignamos el "this" en lugar del "new View.OnClickListener()"
        findViewById(R.id.btn_guardar).setOnClickListener(this);
        findViewById(R.id.btn_permisos).setOnClickListener(this);
    }

    // El método centralizado para manejar los clics sin que explote el compilador
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_permisos) {
            startActivity(new android.content.Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, android.net.Uri.parse("package:" + getPackageName())));
            return;
        }

        if (v.getId() == R.id.btn_guardar) {
            int nuevoTiempo = 7000;
            int selectedId = rgTiempo.getCheckedRadioButtonId();
            if (selectedId == R.id.rb_3s) nuevoTiempo = 3000;
            else if (selectedId == R.id.rb_inf) nuevoTiempo = 0;

            prefs.edit()
                .putInt("tiempo_popup", nuevoTiempo)
                .putString("subtitulo_popup", etSubtitulo.getText().toString())
                .putString("texto_boton", etBoton.getText().toString())
                .apply();
            
            finish(); // Cierra la pantalla de configuración y vuelve al Dashboard
        }
    }
}
