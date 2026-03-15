package com.user.anypair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SettingsActivity extends Activity implements View.OnClickListener {
    private SharedPreferences prefs;
    private RadioGroup rgTiempo;
    private EditText etSubtitulo, etBoton;
    private Switch swBlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) getActionBar().hide();
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("AnyPairConfig", Context.MODE_PRIVATE);
        
        rgTiempo = (RadioGroup) findViewById(R.id.rg_tiempo);
        etSubtitulo = (EditText) findViewById(R.id.et_subtitulo);
        etBoton = (EditText) findViewById(R.id.et_boton);
        swBlur = (Switch) findViewById(R.id.sw_blur);

        int tiempo = prefs.getInt("tiempo_popup", 7000);
        if (tiempo == 3000) ((RadioButton) findViewById(R.id.rb_3s)).setChecked(true);
        else if (tiempo == 0) ((RadioButton) findViewById(R.id.rb_inf)).setChecked(true);
        else ((RadioButton) findViewById(R.id.rb_7s)).setChecked(true);

        etSubtitulo.setText(prefs.getString("subtitulo_popup", "Dispositivo conectado"));
        etBoton.setText(prefs.getString("texto_boton", "Listo"));
        swBlur.setChecked(prefs.getBoolean("usar_blur", true));

        findViewById(R.id.btn_guardar).setOnClickListener(this);
        findViewById(R.id.btn_permisos).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_permisos) {
            mostrarMenuPermisos();
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
                .putBoolean("usar_blur", swBlur.isChecked())
                .apply();

            finish(); 
        }
    }

    private void mostrarMenuPermisos() {
        String[] opciones = {
            "Permiso de Superposición (Mostrar sobre apps)",
            "Deshabilitar Optimización de Batería",
            "Ajustes Generales de la App"
        };
        new AlertDialog.Builder(this)
            .setTitle("Gestionar Permisos")
            .setItems(opciones, new DialogClickListener(this))
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private static class DialogClickListener implements DialogInterface.OnClickListener {
        private final Context context;
        public DialogClickListener(Context context) { this.context = context; }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = null;
            if (which == 0) intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            else if (which == 1) intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + context.getPackageName()));
            else if (which == 2) intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
            if (intent != null) context.startActivity(intent);
        }
    }
}
