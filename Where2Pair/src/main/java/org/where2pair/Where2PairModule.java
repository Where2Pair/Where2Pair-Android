package org.where2pair;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

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
                                                                        TimeProvider timeProvider) {
        VenueFinder venueFinder = new RetrofitVenueFinder(venueServiceAdapterFactory.createRetrofitVenueService());

        return new VenueFinderPresentationModel(venueFinder, locationProvider, timeProvider);
    }

    @Provides @Singleton
    public LocationProvider getLocationProvider() {
        return new AndroidLocationProvider(applicationContext);
    }

    @Provides @Singleton
    public RetrofitVenueServiceAdapterFactory getVenueServiceAdapterFactory() {
        return new RetrofitVenueServiceAdapterFactory("http://where2pair.herokuapp.com");
    }

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(AndroidTimeProvider.class);
    }
}
