#!/bin/bash

SDK="$HOME/AndroidSdk"
BUILD_TOOLS="$SDK/build-tools/34.0.0"
PLATFORM="$SDK/platforms/android-34/android.jar"

echo "🧹 Limpiando compilaciones anteriores..."
rm -rf obj/* bin/* src/com/user/anypair/R.java
mkdir -p obj bin

echo "🎨 1. Compilando recursos XML con AAPT2..."
$BUILD_TOOLS/aapt2 compile --dir res/ -o bin/compiled_res.flata
$BUILD_TOOLS/aapt2 link -o bin/app-unaligned.apk \
    -I $PLATFORM \
    --manifest AndroidManifest.xml \
    --java src \
    bin/compiled_res.flata

echo "☕ 2. Compilando Java (Forzando compatibilidad estricta Java 8)..."
# Reemplazamos --release 11 por las directivas estrictas de Java 8
javac -d obj -classpath $PLATFORM -sourcepath src -source 8 -target 8 -Xlint:-options $(find src -name "*.java")

echo "⚙️ 3. Convirtiendo a formato Dalvik (D8)..."
$BUILD_TOOLS/d8 --lib $PLATFORM --release --output bin/ $(find obj -name "*.class")

echo "📦 4. Empaquetando el APK..."
cd bin
zip -qj app-unaligned.apk classes.dex
cd ..

echo "📏 5. Alineando APK (Zipalign)..."
zipalign -p -f 4 bin/app-unaligned.apk bin/AnyPair-release.apk

echo "✍️ 6. Firmando el APK..."
if [ ! -f mykey.keystore ]; then
    keytool -genkeypair -validity 10000 -dname "CN=AnyPair, O=OpenSource, C=EC" \
        -keystore mykey.keystore -storepass password -keypass password -alias mykey -keyalg RSA -keysize 2048
fi
$BUILD_TOOLS/apksigner sign --ks mykey.keystore --ks-pass pass:password bin/AnyPair-release.apk

echo "✅ ¡Compilación exitosa! Tu APK está en: bin/AnyPair-release.apk"
