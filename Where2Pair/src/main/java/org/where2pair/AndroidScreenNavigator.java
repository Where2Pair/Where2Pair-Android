package org.where2pair;

import android.content.Context;
import android.content.Intent;

import org.where2pair.presentation.Screen;
import org.where2pair.presentation.ScreenNavigator;

import static org.where2pair.presentation.Screen.VENUES_VIEW;

public class AndroidScreenNavigator implements ScreenNavigator {

    private Context context;

    public AndroidScreenNavigator(Context context) {
        this.context = context;
    }

    @Override
    public void navigateTo(Screen screen) {
        Intent intent;
        if (screen == VENUES_VIEW)
            intent = new Intent(context, VenuesActivity.class);
        else
            intent = new Intent(context, LocationsActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
