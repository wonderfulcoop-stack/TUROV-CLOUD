const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('TurovCloudApp', {
  reload: () => ipcRenderer.invoke('reload-app'),
  platform: process.platform
});
