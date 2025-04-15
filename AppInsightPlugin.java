package com.example.appinsight;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.RequiresApi;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.File;
import java.util.Calendar;
import java.util.List;

@CapacitorPlugin(name = "AppInsight")
public class AppInsightPlugin extends Plugin {
    
    @PluginMethod
    public void getUsageStats(PluginCall call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JSObject ret = new JSObject();
            ret.put("stats", getUsageStatsData());
            call.resolve(ret);
        } else {
            call.reject("This feature requires Android 5.0 or above");
        }
    }
    
    @PluginMethod
    public void getStorageInfo(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("storage", getStorageData());
        call.resolve(ret);
    }
    
    @PluginMethod
    public void getBatteryInfo(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("battery", getBatteryData());
        call.resolve(ret);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private JSObject getUsageStatsData() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = calendar.getTimeInMillis();
        
        List<UsageStats> stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        
        JSObject usageData = new JSObject();
        for (UsageStats usage : stats) {
            if (usage.getTotalTimeInForeground() > 0) {
                JSObject appStats = new JSObject();
                appStats.put("packageName", usage.getPackageName());
                appStats.put("timeInForeground", usage.getTotalTimeInForeground());
                usageData.put(usage.getPackageName(), appStats);
            }
        }
        
        return usageData;
    }
    
    private JSObject getStorageData() {
        File path = Environment.getDataDirectory();
        long total = path.getTotalSpace();
        long free = path.getFreeSpace();
        long used = total - free;
        
        JSObject storageData = new JSObject();
        storageData.put("total", total);
        storageData.put("free", free);
        storageData.put("used", used);
        
        return storageData;
    }
    
    private JSObject getBatteryData() {
        BatteryManager bm = (BatteryManager) getContext().getSystemService(Context.BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        
        JSObject batteryData = new JSObject();
        batteryData.put("level", level);
        
        return batteryData;
    }
}
