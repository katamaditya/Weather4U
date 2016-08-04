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

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by VeeraSivaSriAditya on 6/28/2016.
 */
public class WeatherForecastAdapter extends CursorAdapter {


    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    public WeatherForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_forecast_today;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_forecast;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                // Get weather icon
                int artResourceForWeatherCondition = WeatherUtil.getArtResourceForWeatherCondition(
                        cursor.getInt(WeatherForecastFragment.COL_WEATHER_CONDITION_ID));
                viewHolder.iconView.setImageResource(artResourceForWeatherCondition);
                LinearLayout list_Item_Forecast_Today = (LinearLayout) view.findViewById(R.id.list_item_forecast_today);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String changeColorKey = context.getString(R.string.pref_enable_color_change_key);
                boolean changeColor = prefs.getBoolean(changeColorKey,
                        Boolean.parseBoolean(context.getString(R.string.pref_enable_color_change_default)));
                if (changeColor) {
                    switch (artResourceForWeatherCondition) {
                        case R.drawable.art_storm: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_stormandrain);
                            break;
                        }
                        case R.drawable.art_rain: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_stormandrain);
                            break;
                        }
                        case R.drawable.art_light_rain: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_snowandlightrain);
                            break;
                        }
                        case R.drawable.art_snow: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_snowandlightrain);
                            break;
                        }
                        case R.drawable.art_fog: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_fogandcloudy);
                            break;
                        }
                        case R.drawable.art_clear: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_clear);
                            break;
                        }
                        case R.drawable.art_light_clouds: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_lightclouds);
                            break;
                        }
                        case R.drawable.art_clouds: {
                            list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector_fogandcloudy);
                            break;
                        }
                    }
                } else {
                    list_Item_Forecast_Today.setBackgroundResource(R.drawable.today_touch_selector);
                }
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                // Get weather icon
                viewHolder.iconView.setImageResource(WeatherUtil.getIconResourceForWeatherCondition(
                        cursor.getInt(WeatherForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
        }

        // Read date from cursor
        long dateInMillis = cursor.getLong(WeatherForecastFragment.COL_WEATHER_DATE);
        // Find TextView and set formatted date on it
        viewHolder.dateView.setText(WeatherUtil.getFriendlyDayString(context, dateInMillis));

        // Read weather forecast from cursor
        String description = cursor.getString(WeatherForecastFragment.COL_WEATHER_DESC);
        // Find TextView and set weather forecast on it
        viewHolder.descriptionView.setText(description);

        // For accessibility, add a content description to the icon field
        viewHolder.iconView.setContentDescription(description);

        // Read user preference for metric or imperial temperature units
        boolean isMetric = WeatherUtil.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(WeatherForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.highTempView.setText(WeatherUtil.formatTemperature(context, high));

        // Read low temperature from cursor
        double low = cursor.getDouble(WeatherForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.lowTempView.setText(WeatherUtil.formatTemperature(context, low));
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }
}
