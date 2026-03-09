package com.user.anypair;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BtleReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        SharedPreferences prefs = context.getSharedPreferences("AnyPairConfig", Context.MODE_PRIVATE);
        
        if ("com.user.anypair.TEST_POPUP".equals(action) || BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            mostrarVentanaFlotante(context, prefs);
        }
    }

    private void mostrarVentanaFlotante(Context context, SharedPreferences prefs) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return;

        ContextThemeWrapper themeContext = new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_DayNight);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.BOTTOM; 
        params.dimAmount = 0.5f;
        params.windowAnimations = android.R.style.Animation_InputMethod;

        LayoutInflater inflater = LayoutInflater.from(themeContext);
        View popupView = inflater.inflate(R.layout.popup, null);

        // Aplicar personalización de imagen
        ImageView img = (ImageView) popupView.findViewById(R.id.img_audifonos);
        String imagenUri = prefs.getString("imagen_uri", "");
        if (!imagenUri.isEmpty()) {
            try { img.setImageURI(Uri.parse(imagenUri)); } catch (Exception e) {}
        }

        // Aplicar personalización de textos
        TextView tvTitle = (TextView) popupView.findViewById(R.id.tv_title);
        if (tvTitle != null) tvTitle.setText(prefs.getString("nombre_dispositivo", "GenericBuds"));

        TextView tvSubtitle = (TextView) popupView.findViewById(R.id.tv_subtitle);
        if (tvSubtitle != null) tvSubtitle.setText(prefs.getString("subtitulo_popup", "Dispositivo conectado"));

        Button btnMainClose = (Button) popupView.findViewById(R.id.btn_close);
        if (btnMainClose != null) btnMainClose.setText(prefs.getString("texto_boton", "Listo"));

        // Asignar los clics para cerrar
        CerrarClickListener listener = new CerrarClickListener(wm, popupView);
        if (btnMainClose != null) btnMainClose.setOnClickListener(listener);
        View btnX = popupView.findViewById(R.id.btn_x);
        if (btnX != null) btnX.setOnClickListener(listener);

        wm.addView(popupView, params);
        
        // Aplicar personalización de Tiempo
        int tiempoEspera = prefs.getInt("tiempo_popup", 7000);
        if (tiempoEspera > 0) { // Si es 0 (Infinito), ignoramos el temporizador
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new AutoCierreRunnable(wm, popupView), tiempoEspera);
        }
    }

    private static class CerrarClickListener implements View.OnClickListener {
        private final WindowManager wm; private final View view;
        public CerrarClickListener(WindowManager wm, View view) { this.wm = wm; this.view = view; }
        @Override public void onClick(View v) {
            try { if (view.isAttachedToWindow()) wm.removeView(view); } catch (Exception e) {}
        }
    }

    private static class AutoCierreRunnable implements Runnable {
        private final WindowManager wm; private final View view;
        public AutoCierreRunnable(WindowManager wm, View view) { this.wm = wm; this.view = view; }
        @Override public void run() {
            try { if (view.isAttachedToWindow()) wm.removeView(view); } catch (Exception e) {}
        }
    }
}
