/*
 * Copyright (c) 2016. Veera Siva Sri Aditya Katam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.katamaditya.apps.weather4u.weathersync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by VeeraSivaSriAditya on 6/28/2016.
 */
public class Weather4USyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static Weather4USyncAdapter sWeather4USyncAdapter = null;

    @Override
    public void onCreate() {
        //Log.d("Weather4USyncService", "onCreate - Weather4USyncService");
        synchronized (sSyncAdapterLock) {
            if (sWeather4USyncAdapter == null) {
                sWeather4USyncAdapter = new Weather4USyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sWeather4USyncAdapter.getSyncAdapterBinder();
    }
}
