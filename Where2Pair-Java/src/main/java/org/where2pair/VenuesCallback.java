package org.where2pair;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class VenuesCallback implements Callback<List<VenueWithDistances>> {
    private final VenuesResultAction venuesResultAction;

    public VenuesCallback(VenuesResultAction venuesResultAction) {
        this.venuesResultAction = venuesResultAction;
    }

    @Override
    public void success(List<VenueWithDistances> venues, Response response) {
        venuesResultAction.apply(venues);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        venuesResultAction.failed("Network Error");
    }
}
