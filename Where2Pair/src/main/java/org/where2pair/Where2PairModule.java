package org.where2pair;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import org.where2pair.presentation.LocationProvider;
import org.where2pair.presentation.ScreenNavigator;
import org.where2pair.presentation.TimeProvider;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesViewerPresentationModel;
import org.where2pair.rest.RetrofitVenueRequester;
import org.where2pair.rest.RetrofitVenueService;
import retrofit.RestAdapter;

public class Where2PairModule extends AbstractModule {

    private Context applicationContext;

    public Where2PairModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    public VenueFinderPresentationModel getVenueFinderPresentationModel(TimeProvider timeProvider, LocationProvider locationProvider,
                                                                        VenuesViewerPresentationModel venuesViewerPresentationModel, ScreenNavigator screenNavigator) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer("http://where2pair.herokuapp.com")
                .build();

        VenueFinder venueFinder = new RetrofitVenueRequester(restAdapter.create(RetrofitVenueService.class));

        return new VenueFinderPresentationModel(venueFinder, timeProvider, locationProvider, venuesViewerPresentationModel, screenNavigator);
    }

    @Provides
    public VenuesViewerPresentationModel getVenuesViewerPresentationModel() {
        return new VenuesViewerPresentationModel();
    }

    @Provides
    public ScreenNavigator getScreenNavigator() {
        return new AndroidScreenNavigator(applicationContext);
    }

    @Override
    protected void configure() {
        bind(LocationProvider.class).to(AndroidLocationProvider.class);
        bind(TimeProvider.class).to(AndroidTimeProvider.class);
    }
}
