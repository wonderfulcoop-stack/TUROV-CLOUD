# Сборка Android APK через GitHub Actions

Так можно собрать APK вообще без Android Studio на твоём ПК.

## Как пользоваться

1. Создай новый репозиторий на GitHub.
2. Загрузи в него содержимое папки `turov-cloud-app-suite` целиком.
3. Открой вкладку **Actions**.
4. Выбери workflow **Build Android APK**.
5. Нажми **Run workflow**.
6. Дождись окончания сборки.
7. Открой завершённый запуск и скачай файл из блока **Artifacts**:

```text
TurovCloud-debug-apk
```

Внутри будет APK-файл примерно по пути:

```text
app-debug.apk
```

Его уже можно скинуть на Android-телефон и установить.

## Где поменять сайт

Ссылка на сайт находится здесь:

```text
android-webview/app/src/main/java/cloud/turov/app/MainActivity.java
```

Строка:

```java
private static final String APP_URL = "http://cloud-turov.duckdns.org/";
```

Если сайт будет на другом домене — меняй только эту ссылку.

## Важно

Это debug APK. Для личного теста и установки вручную — нормально.
Для публикации в Google Play позже лучше собрать release APK/AAB с подписью.
