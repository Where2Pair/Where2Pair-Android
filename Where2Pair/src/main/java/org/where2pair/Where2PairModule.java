package org.where2pair;

import android.content.Context;
import android.os.Vibrator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.where2pair.presentation.DeviceVibrator;
import org.where2pair.presentation.LocationProvider;
import org.where2pair.presentation.TimeProvider;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.rest.RetrofitVenueFinder;
import org.where2pair.rest.RetrofitVenueServiceAdapterFactory;

public class Where2PairModule extends AbstractModule {

    private Context applicationContext;

    public Where2PairModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides @Singleton
    public VenueFinderPresentationModel getVenueFinderPresentationModel(RetrofitVenueServiceAdapterFactory venueServiceAdapterFactory, LocationProvider locationProvider,
                                                                        TimeProvider timeProvider, DeviceVibrator deviceVibrator) {
        VenueFinder venueFinder = new RetrofitVenueFinder(venueServiceAdapterFactory.createRetrofitVenueService());

        return new VenueFinderPresentationModel(venueFinder, locationProvider, timeProvider, deviceVibrator);
    }

    @Provides @Singleton
    public LocationProvider getLocationProvider() {
        return new AndroidLocationProvider(applicationContext);
    }

    @Provides @Singleton
    public RetrofitVenueServiceAdapterFactory getVenueServiceAdapterFactory() {
        return new RetrofitVenueServiceAdapterFactory("http://where2pair.herokuapp.com");
    }

    @Provides @Singleton
    public DeviceVibrator getDeviceVibrator() {
        return new AndroidDeviceVibrator((Vibrator)applicationContext.getSystemService(Context.VIBRATOR_SERVICE));
    }

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(AndroidTimeProvider.class);
    }
}
