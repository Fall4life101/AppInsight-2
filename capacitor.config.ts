import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.example.appinsight',
  appName: 'AppInsight',
  webDir: 'dist',
  server: {
    androidScheme: 'https'
  }
};

export default config;
