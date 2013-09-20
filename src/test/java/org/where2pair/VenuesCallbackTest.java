package org.where2pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VenuesCallbackTest {

    @Mock VenuesResultAction venuesResultAction;

    @Test
    public void onSuccessInvokesActionSuccessCallback() {
        List<Venue> venues = newArrayList();
        VenuesCallback venuesCallback = new VenuesCallback(venuesResultAction);

        venuesCallback.success(venues, null);

        verify(venuesResultAction).apply(venues);
    }

}
