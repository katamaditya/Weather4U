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

package com.katamaditya.apps.weather4u;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.katamaditya.apps.weather4u.weathersync.Weather4USyncAdapter;

/**
 * Created by VeeraSivaSriAditya on 6/28/2016.
 */
public class MainActivity extends AppCompatActivity implements WeatherForecastFragment.Callback {
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mIsTwoPane;
    private String mUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserLocation = WeatherUtil.getPreferredLocation(this);
        if (findViewById(R.id.weather_detail_container) != null) {
            mIsTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mIsTwoPane = false;
            getSupportActionBar().setElevation(8f);
        }
        WeatherForecastFragment forecastFragment = ((WeatherForecastFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_weather_forecast));
        forecastFragment.setUseTodayLayout(!mIsTwoPane);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle();
        Weather4USyncAdapter.initializeSyncAdapter(this);
        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_mob_application_id));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = WeatherUtil.getPreferredLocation(this);
        if (location != null && !location.equals(mUserLocation)) {
            WeatherForecastFragment ff = (WeatherForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_weather_forecast);
            if (null != ff) {
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if (null != df) {
                df.onLocationChanged(location);
            }
            mUserLocation = location;
        }
        setTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mIsTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }

    public void setTitle() {
        String title = WeatherUtil.getPreferredLocation(this);
        if (title != null) {
            title = title.trim();
            title = title.substring(0, 1).toUpperCase() + title.substring(1);
            if (title.contains(",")) {
                String temp[] = title.split(",");
                Character c = temp[0].charAt(0);
                c = Character.toUpperCase(c);
                temp[0] = temp[0].replaceFirst(temp[0].substring(0, 1), c.toString());
                temp[1] = temp[1].toUpperCase();
                title = temp[0] + ", " + temp[1];
            }
            setTitle(getString(R.string.app_name) + " - " + title);
        }
    }
}
