package org.where2pair;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class VenuesCallback implements Callback<List<Venue>> {
    private final VenuesResultAction venuesResultAction;

    public VenuesCallback(VenuesResultAction venuesResultAction) {
        this.venuesResultAction = venuesResultAction;
    }

    @Override
    public void success(List<Venue> venues, Response response) {
        venuesResultAction.apply(venues);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
