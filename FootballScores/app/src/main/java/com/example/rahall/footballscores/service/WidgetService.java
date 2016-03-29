package com.example.rahall.footballscores.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.rahall.footballscores.WidgetDataProvider;

/**
 * Created by rahall4405 on 3/26/16.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
