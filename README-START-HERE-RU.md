# Turov Cloud App Suite

Я добавил к твоему Turov Cloud две обёртки:

- `desktop-electron` — ПК-приложение для Windows через Electron.
- `android-webview` — Android-приложение через WebView.
- `site` — твой сайт из архива, без `node_modules`, чтобы зависимости ставились свежо.

## Как это работает
Приложения не копируют всю серверную часть внутрь устройства. Они открывают твой уже работающий сайт внутри отдельного приложения. Это самый правильный вариант для Turov Cloud, потому что файлы, аккаунты, премиум, SQLite и оплата должны жить на сервере, а не на телефоне пользователя.

## Главное, что нужно проверить
В обоих приложениях сейчас стоит адрес:

`http://cloud-turov.duckdns.org/`

Если твой реальный адрес другой, замени его:

- ПК: `desktop-electron/src/main.js`
- Android: `android-webview/app/src/main/java/cloud/turov/app/MainActivity.java`

## ПК / Windows
```bash
cd desktop-electron
npm install
npm start
```

Собрать установщик:

```bash
npm run build:win
```

## Android
Открываешь папку `android-webview` в Android Studio и собираешь APK через:

`Build > Build Bundle(s) / APK(s) > Build APK(s)`

## Сайт
Если нужно запустить сайт локально:

```bash
cd site
npm install
npm start
```

Потом для проверки приложения на локальном сайте можно временно поставить URL:

`http://localhost:3000/`

На Android локальный `localhost` будет означать сам телефон, поэтому для телефона нужен домен сервера или IP компьютера в локальной сети.

---

## Android без Android Studio: сборка через GitHub

Я добавил готовый GitHub Actions workflow:

```text
.github/workflows/android-apk.yml
```

Как собрать:

1. Залей папку `turov-cloud-app-suite` в GitHub-репозиторий.
2. Открой **Actions**.
3. Запусти **Build Android APK**.
4. После сборки скачай artifact **TurovCloud-debug-apk**.

Подробная инструкция лежит здесь:

```text
android-webview/README-GITHUB-BUILD-RU.md
```
