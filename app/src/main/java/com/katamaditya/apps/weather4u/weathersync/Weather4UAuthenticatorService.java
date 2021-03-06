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
public class Weather4UAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private Weather4UAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Weather4UAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
