package org.where2pair.presentation

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.mockito.BDDMockito.given
import static org.where2pair.SearchRequestBuilder.aSearchRequest
import static org.where2pair.SearchRequestMatcher.equalTo

import org.where2pair.Coordinates

import spock.lang.Specification

class VenueFinderPresentationModelSpec extends Specification {

	LocationProvider locationProvider = Mock()
	UserLocationsObserver userLocationsObserver = Mock()
	VenueFinderPresentationModel venueFinderPresentationModel = new VenueFinderPresentationModel(locationProvider, userLocationsObserver)(locationProvider, userLocationsObserver)
	
	def "only search and search options buttons should be visible at first"() {
		when:
		def propertyVisible = venueFinderPresentationModel."${view}Visible"
		
		then:
		propertyVisible == visibilityExpectation
		
		where:
		view 					| visibilityExpectation
		'searchButton' 			| true
		'searchOptionsButton' 	| true
		'mapButton'				| false
		'listButton'			| false
		'loadingIcon'			| false
	}
	
	def "whilst searching for venues loading icon should be visible"() {
		when:
		venueFinderPresentationModel.searchButtonPressed()
		
		then:
		venueFinderPresentationModel."${view}Visible" == visibilityExpectation
		
		where:
		view 					| visibilityExpectation
		'searchButton' 			| false
		'searchOptionsButton' 	| false
		'mapButton'				| false
		'listButton'			| false
		'loadingIcon'			| true
	}
	
	def "when searching for venues completes, list button should be visible"() {
		when:
		venueFinderPresentationModel.notifyVenuesFound([])
		
		then:
		venueFinderPresentationModel."${view}Visible" == visibilityExpectation
		
		where:
		view 					| visibilityExpectation
		'searchButton' 			| false
		'searchOptionsButton' 	| false
		'mapButton'				| false
		'listButton'			| true
		'loadingIcon'			| false
	}

	def "current location should be visible as user location at first"() {
		given:
		def currentLocation = new Coordinates(1.0, 0.1)
		locationProvider.getCurrentLocation() >> currentLocation
		
		when:
		def userLocations = venueFinderPresentationModel.userLocations
		
		then:
		userLocations == [currentLocation]
	}
	
	def "when current location is unavailable then user locations should be empty at first"() {
		when:
		def userLocations = venueFinderPresentationModel.userLocations
		
		then:
		userLocations == []
	}
	
	def "notifies user locations observer when adding a new user location"() {
		given:
		def newLocation = new Coordinates(1.0,0.1)
		
		when:
		venueFinderPresentationModel.addUserLocation(newLocation)
		
		then:
		1 * userLocationsObserver.notifyLocationAdded(newLocation)
	}
	
	def "finds open venues near current location with wifi and seating when search button is pressed"() {
		
	}
	
//	@Test
//	public void findsOpenVenuesNearCurrentLocationWithWifiAndSeatingWhenSearchButtonIsPressed() {
//		given(timeProvider.getCurrentTime()).willReturn(CURRENT_TIME);
//		given(locationProvider.getCurrentLocation()).willReturn(CURRENT_LOCATION);
//		SearchRequest expectedSearchRequest = aSearchRequest().openFrom(CURRENT_TIME).near(CURRENT_LOCATION).withWifi().withSeating().build();
//		
//		venueFinderPresentationModel.pressSearchButton();
//		
//		assertThat(actualSearchRequest(), is(equalTo(expectedSearchRequest)));
//	}
}
