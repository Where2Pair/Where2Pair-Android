package org.where2pair;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.where2pair.presentation.LocationProvider;
import org.where2pair.presentation.ScreenNavigator;
import org.where2pair.presentation.TimeProvider;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesViewerPresentationModel;
import org.where2pair.rest.RetrofitVenueRequester;
import org.where2pair.rest.RetrofitVenueService;
import org.where2pair.rest.RetrofitVenueServiceAdapterFactory;

import retrofit.RestAdapter;

public class Where2PairModule extends AbstractModule {

    private Context applicationContext;

    public Where2PairModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides @Singleton
    public VenueFinderPresentationModel getVenueFinderPresentationModel(TimeProvider timeProvider, LocationProvider locationProvider,
                                                                        VenuesViewerPresentationModel venuesViewerPresentationModel,
                                                                        ScreenNavigator screenNavigator,
                                                                        RetrofitVenueServiceAdapterFactory venueServiceAdapterFactory) {

        VenueFinder venueFinder = new RetrofitVenueRequester(venueServiceAdapterFactory.createRetrofitVenueService());

        return new VenueFinderPresentationModel(venueFinder, timeProvider, locationProvider, venuesViewerPresentationModel, screenNavigator);
    }

    @Provides @Singleton
    public VenuesViewerPresentationModel getVenuesViewerPresentationModel() {
        return new VenuesViewerPresentationModel();
    }

    @Provides
    public ScreenNavigator getScreenNavigator() {
        return new AndroidScreenNavigator(applicationContext);
    }

    @Provides
    public LocationProvider getLocationProvider() {
        return new AndroidLocationProvider(applicationContext);
    }

    @Provides
    public RetrofitVenueServiceAdapterFactory getVenueServiceAdapterFactory() {
        return new RetrofitVenueServiceAdapterFactory("http://where2pair.herokuapp.com");
    }

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(AndroidTimeProvider.class);
    }
}
