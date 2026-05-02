# Turov Cloud Desktop

Это ПК-версия Turov Cloud через Electron. Она не хранит сайт внутри себя, а открывает твой рабочий сайт как полноценное приложение.

## 1. Настрой ссылку
Открой `src/main.js` и проверь строку:

```js
const APP_URL = process.env.TUROV_CLOUD_URL || 'http://cloud-turov.duckdns.org/';
```

Поставь туда реальный адрес сайта, если он другой.

## 2. Запуск для проверки
```bash
cd desktop-electron
npm install
npm start
```

## 3. Сборка EXE
```bash
npm run build:win
```

Готовый установщик появится в папке `dist`.

## 4. Portable версия
```bash
npm run build:portable
```
