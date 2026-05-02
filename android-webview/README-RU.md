# Turov Cloud Android

Это Android-приложение WebView. Оно открывает твой сайт Turov Cloud внутри приложения.

## 1. Настрой ссылку
Открой файл:

`app/src/main/java/cloud/turov/app/MainActivity.java`

И проверь строку:

```java
private static final String APP_URL = "http://cloud-turov.duckdns.org/";
```

Поставь свой реальный адрес сайта, если он отличается.

## 2. Как собрать APK через Android Studio
1. Открой Android Studio.
2. Нажми **Open**.
3. Выбери папку `android-webview`.
4. Дождись Gradle Sync.
5. Нажми **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
6. Готовый APK будет в `app/build/outputs/apk/debug/`.

## Важно про HTTP
Сейчас включено `android:usesCleartextTraffic="true"`, поэтому приложение откроет HTTP-сайт. Но для нормальной публикации лучше позже сделать HTTPS-домен.
