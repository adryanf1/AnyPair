package com.user.anypair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

public class OnboardingActivity extends Activity implements View.OnClickListener {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        prefs = getSharedPreferences("AnyPairConfig", Context.MODE_PRIVATE);
        
        // ¡La validación maestra! Si ya entró antes, saltar directo al Main
        if (prefs.getBoolean("onboarding_completado", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        findViewById(R.id.btn_permiso_overlay).setOnClickListener(this);
        findViewById(R.id.btn_comenzar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_permiso_overlay) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else if (id == R.id.btn_comenzar) {
            // Guardamos que ya pasó el tutorial
            prefs.edit().putBoolean("onboarding_completado", true).apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
