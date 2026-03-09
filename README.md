<div align="center">

# AnyPair 🎧

<img width="128" height="128" alt="AnyPair Logo" src="https://github.com/user-attachments/assets/06fc2d5b-1036-4d33-8ef9-26b18303e8a7" />




AnyPair es una utilidad ligera y personalizable que detecta cuando tus audífonos o dispositivos Bluetooth se conectan a tu teléfono y despliega una animación flotante elegante (Pop-up) en la parte inferior de la pantalla.


<img width="216" height="428" alt="Screenshot_20260309-170217" src="https://github.com/user-attachments/assets/e52b4a37-f1e0-443e-a50a-28aecb64e769" />
...
<img width="216" height="428" alt="Screenshot_20260309-170319" src="https://github.com/user-attachments/assets/c0bc8c45-7aff-4be7-aabf-b18d31bcb9f3" />

### **Características Principales**

**Universal:** Funciona con cualquier dispositivo Bluetooth, no requiere hardware con chips propietarios.
  
**Personalizable:** Permite cambiar el nombre del dispositivo, el mensaje de estado y la imagen que aparece en la animación (soporta PNGs transparentes).
  
**Diseño Moderno:** Interfaz pulida con bordes redondeados, soporte nativo para Modo Oscuro/Claro y diseño basado en Material You.
  
**Ligera:** Sin servicios persistentes que drenen la batería de tu dispositivo en segundo plano.

---

## 🛠️ ¿Cómo se hizo?

**Tecnología:** Escrito completamente en **Java puro**.

**Entorno:** Desarrollado sin depender de entornos pesados como Android Studio. Fue compilado de forma nativa "a mano" utilizando scripts de Bash (`aapt2`, `javac`, `d8`) directamente desde la terminal en un entorno Debian, demostrando que es posible crear software moderno y de calidad en equipos con recursos limitados (como procesadores Celeron y 4GB de RAM).

**Inteligencia Artificial:** Co-creado con la asistencia de **Google Gemini**, utilizando ingeniería de prompts para resolver bugs complejos de compilación (como incompatibilidades del compilador D8 con bytecode moderno) y diseñar interfaces UI/UX eficientes mediante XML.

---

## ⚠️ Sobre las alertas de Google Play Protect

Al instalar el archivo `.apk` descargado desde este repositorio, es muy probable que Google Play Protect muestre una advertencia de seguridad en color rojo indicando "App no segura" o "Desarrollador desconocido".

**¿Por qué sucede esto?**

1. **Firma local:** La aplicación está firmada con un certificado de desarrollador propio, no con un certificado comercial validado por Google.
2. **Permisos de superposición:** AnyPair requiere el permiso `SYSTEM_ALERT_WINDOW` (Mostrar sobre otras apps) para dibujar la animación fuera de la aplicación.

**Solución:**
El código es 100% libre, abierto y seguro. Para instalarla, simplemente presiona **"Más detalles"** en la alerta de Play Protect y selecciona **"Instalar de todas formas"**.

---
*Desarrollado con ☕, terminal y mucho código.*

</div>
