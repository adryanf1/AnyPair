package com.user.anypair;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BtleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Context appContext = context.getApplicationContext();
        SharedPreferences prefs = appContext.getSharedPreferences("AnyPairConfig", Context.MODE_PRIVATE);

        if ("com.user.anypair.TEST_POPUP".equals(action) || BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            mostrarVentanaFlotante(appContext, prefs);
        }
    }

    private void mostrarVentanaFlotante(Context context, SharedPreferences prefs) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) return;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.BOTTOM;
        params.dimAmount = 0.5f;

        if (Build.VERSION.SDK_INT >= 31 && prefs.getBoolean("usar_blur", true)) {
            params.flags |= WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            params.setBlurBehindRadius(80);
        }

        View view = LayoutInflater.from(new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_DayNight)).inflate(R.layout.popup, null);
        
        // --- RESTAURACIÓN DE TEXTOS EDITABLES ---
        ((TextView) view.findViewById(R.id.tv_title)).setText(prefs.getString("nombre_dispositivo", "AnyPair"));
        ((TextView) view.findViewById(R.id.tv_subtitle)).setText(prefs.getString("subtitulo_popup", "Dispositivo conectado"));
        
        Button btnClose = (Button) view.findViewById(R.id.btn_close);
        btnClose.setText(prefs.getString("texto_boton", "Listo"));
        
        // --- FIX DE CONTRASTE ---
        btnClose.setTextColor(Color.parseColor("#1C1B1F")); // Forzamos texto oscuro legible

        String uri = prefs.getString("imagen_uri", "");
        if (!uri.isEmpty()) try { ((ImageView) view.findViewById(R.id.img_audifonos)).setImageURI(Uri.parse(uri)); } catch(Exception e){}

        View.OnClickListener closeAction = v -> { try { if(view.isAttachedToWindow()) wm.removeView(view); } catch(Exception e){} };
        btnClose.setOnClickListener(closeAction);
        view.findViewById(R.id.btn_x).setOnClickListener(closeAction);

        wm.addView(view, params);

        // Mantener la animación de entrada suave
        view.setTranslationY(1000f);
        view.animate().translationY(0f).setDuration(600).setInterpolator(new OvershootInterpolator(1.2f)).start();

        int t = prefs.getInt("tiempo_popup", 7000);
        if (t > 0) new Handler(Looper.getMainLooper()).postDelayed(() -> closeAction.onClick(null), t);
    }
}
