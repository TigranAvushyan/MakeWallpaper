package com.example.myapplication;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WallpaperService extends Service {
    public WallpaperService() {
    }

    static boolean isChanging = true;
    static int wpItem = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            Typeface typeface;
            public void run() {
                while(isChanging){
                    try {
                        Log.d("_service","alive" + wpItem);
                        typeface = Typeface.createFromAsset(getAssets(), MainActivity.Wallpaper_ARRAY.get(wpItem).getFontFamily());
                        myWallpaperManager.setBitmap(MainActivity.Wallpaper_ARRAY.get(wpItem).createBtimapWallpaper(typeface));
                        if(wpItem == MainActivity.Wallpaper_ARRAY.size() - 1) wpItem = 0; else{wpItem++;}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.d("_service","die");
        isChanging = false;
        super.onDestroy();
    }


}
