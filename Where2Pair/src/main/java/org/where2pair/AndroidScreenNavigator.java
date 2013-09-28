package org.where2pair;

import android.content.Context;
import android.content.Intent;

import org.where2pair.presentation.Screen;
import org.where2pair.presentation.ScreenNavigator;

public class AndroidScreenNavigator implements ScreenNavigator {

    private Context context;

    public AndroidScreenNavigator(Context context) {
        this.context = context;
    }

    @Override
    public void navigateTo(Screen screen) {
        Intent intent = new Intent(context, VenuesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
