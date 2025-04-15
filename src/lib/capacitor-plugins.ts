import { Capacitor } from '@capacitor/core';

export async function getDeviceStats() {
  if (Capacitor.isNativePlatform()) {
    try {
      const { AppInsight } = Capacitor.Plugins;
      
      // Get usage statistics
      const usageStatsResponse = await AppInsight.getUsageStats();
      const usageStats = usageStatsResponse.stats;
      
      // Get storage information
      const storageResponse = await AppInsight.getStorageInfo();
      const storageInfo = storageResponse.storage;
      
      // Get battery information
      const batteryResponse = await AppInsight.getBatteryInfo();
      const batteryInfo = batteryResponse.battery;
      
      return {
        usageStats,
        storageInfo,
        batteryInfo
      };
    } catch (error) {
      console.error('Error getting device stats:', error);
      return null;
    }
  } else {
    // Return mock data when running in web browser
    return null;
  }
}
