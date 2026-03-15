package com.user.anypair;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {
    private SharedPreferences prefs;
    private ImageView imgPreview;
    private EditText etNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) getActionBar().hide();
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("AnyPairConfig", Context.MODE_PRIVATE);
        imgPreview = (ImageView) findViewById(R.id.img_preview);
        etNombre = (EditText) findViewById(R.id.et_nombre);
        etNombre.setText(prefs.getString("nombre_dispositivo", "GenericBuds"));
        String savedUri = prefs.getString("imagen_uri", "");
        if (!savedUri.isEmpty()) {
            try { imgPreview.setImageURI(Uri.parse(savedUri)); } catch (Exception e) {}
        }
        findViewById(R.id.btn_seleccionar_imagen).setOnClickListener(this);
        findViewById(R.id.btn_test).setOnClickListener(this);
        findViewById(R.id.btn_settings).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_seleccionar_imagen) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        } else if (id == R.id.btn_test) {
            prefs.edit().putString("nombre_dispositivo", etNombre.getText().toString()).apply();
            Intent i = new Intent(this, BtleReceiver.class);
            i.setAction("com.user.anypair.TEST_POPUP");
            // Simulamos datos de batería para la prueba
            i.putExtra("android.bluetooth.device.extra.BATTERY_LEVEL", 85);
            i.putExtra("com.google.android.gms.nearby.discovery.extra.LEFT_BATTERY", 90);
            i.putExtra("com.google.android.gms.nearby.discovery.extra.RIGHT_BATTERY", 45);
            sendBroadcast(i);
        } else if (id == R.id.btn_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            if (selectedUri != null) {
                if (Build.VERSION.SDK_INT >= 19) {
                    getContentResolver().takePersistableUriPermission(selectedUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                prefs.edit().putString("imagen_uri", selectedUri.toString()).apply();
                imgPreview.setImageURI(selectedUri);
            }
        }
    }
}
