const { app, BrowserWindow, shell, Menu, ipcMain } = require('electron');
const path = require('path');

// Меняешь тут ссылку на свой домен сайта.
// Для локального теста можно запустить так:
// set TUROV_CLOUD_URL=http://localhost:3000 && npm start
const APP_URL = process.env.TUROV_CLOUD_URL || 'http://cloud-turov.duckdns.org/';

let mainWindow;

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 820,
    minWidth: 390,
    minHeight: 640,
    title: 'Turov Cloud',
    backgroundColor: '#050505',
    autoHideMenuBar: true,
    icon: path.join(__dirname, '..', 'assets', 'logo.png'),
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: true,
      webSecurity: true
    }
  });

  Menu.setApplicationMenu(null);

  mainWindow.loadURL(APP_URL);

  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    const internal = url.startsWith(APP_URL);
    if (!internal) shell.openExternal(url);
    return { action: internal ? 'allow' : 'deny' };
  });

  mainWindow.webContents.on('will-navigate', (event, url) => {
    const isSameHost = new URL(url).host === new URL(APP_URL).host;
    const isPayment = /yoomoney\.ru|yandex\.ru/.test(url);
    if (!isSameHost && !isPayment) {
      event.preventDefault();
      shell.openExternal(url);
    }
  });

  mainWindow.webContents.on('did-fail-load', () => {
    mainWindow.loadFile(path.join(__dirname, 'offline.html'));
  });
}

app.whenReady().then(createWindow);

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) createWindow();
});

ipcMain.handle('reload-app', async () => {
  if (mainWindow) await mainWindow.loadURL(APP_URL);
});
