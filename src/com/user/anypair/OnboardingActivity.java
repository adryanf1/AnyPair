package com.user.anypair;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class OnboardingActivity extends Activity implements View.OnClickListener {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) getActionBar().hide();
        prefs = getSharedPreferences("AnyPairConfig", Context.MODE_PRIVATE);
        
        if (prefs.getBoolean("onboarding_completado", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        findViewById(R.id.btn_permiso_overlay).setOnClickListener(this);
        findViewById(R.id.btn_comenzar).setOnClickListener(this);

        // LA MAGIA: Pedir permiso de Bluetooth en tiempo de ejecución (Android 12+)
        if (Build.VERSION.SDK_INT >= 31) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 101);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_permiso_overlay) {
            startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, android.net.Uri.parse("package:" + getPackageName())));
        } else if (id == R.id.btn_comenzar) {
            prefs.edit().putBoolean("onboarding_completado", true).apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
