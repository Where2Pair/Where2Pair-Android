package org.where2pair.presentation

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.mockito.BDDMockito.given
import static org.where2pair.SearchRequestBuilder.aSearchRequest
import static org.where2pair.SearchRequestMatcher.equalTo
import static org.where2pair.TestUtils.sampleVenues
import static spock.util.matcher.HamcrestSupport.that

import org.where2pair.Coordinates
import org.where2pair.SearchRequestMatcher
import org.where2pair.SimpleTime
import org.where2pair.VenueFinder

import spock.lang.Specification

class VenueFinderPresentationModelSpec extends Specification {

	def "only search and search options buttons should be visible at first"() {
		when:
		initializePresentationModel()
		
		then:
		venueFinderPresentationModel."${view}Visible" == visibilityExpectation
		
		where:
		view 					| visibilityExpectation
		'searchButton' 			| true
		'searchOptionsButton' 	| true
		'mapButton'				| false
		'listButton'			| false
		'loadingIcon'			| false
	}
	
	def "whilst searching for venues loading icon should be visible"() {
		given:
		initializePresentationModel()
		
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
		given:
		initializePresentationModel()
		
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
		locationProvider.getCurrentLocation() >> CURRENT_LOCATION
		initializePresentationModel()
		
		when:
		def userLocations = venueFinderPresentationModel.userLocations
		
		then:
		userLocations == [CURRENT_LOCATION]
	}
	
	def "when current location is unavailable then user locations should be empty at first"() {
		given:
		initializePresentationModel()
		
		when:
		def userLocations = venueFinderPresentationModel.userLocations
		
		then:
		userLocations == []
	}
	
	def "when a new user location is added notifies user locations observer"() {
		given:
		def newLocation = new Coordinates(1.0, 0.1)
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.addUserLocation(newLocation)
		
		then:
		1 * userLocationsObserver.notifyLocationAdded(newLocation)
	}
	
	def "when a new user location is added then should return new location in locations"() {
		given:
		def newLocation = new Coordinates(2.0, 0.2)
		def anotherNewLocation = new Coordinates(3.0, 0.3)
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.addUserLocation(newLocation)
		
		then:
		venueFinderPresentationModel.getUserLocations().size() == 1
		venueFinderPresentationModel.getUserLocations().contains(newLocation)
		
		when:
		venueFinderPresentationModel.addUserLocation(anotherNewLocation)
		
		then:
		venueFinderPresentationModel.getUserLocations().size() == 2
		venueFinderPresentationModel.getUserLocations().contains(anotherNewLocation)
	}
	
	def "when venues are updated notifies venues observer"() {
		given:
		def venues = sampleVenues()
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.notifyVenuesFound(venues)
		
		then:
		1 * venuesObserver.notifyVenuesUpdated()
	}
	
	def "when search button is pressed finds open venues near current location with wifi and seating"() {
		given:
		timeProvider.getCurrentTime() >> CURRENT_TIME
		locationProvider.getCurrentLocation() >> CURRENT_LOCATION
		def expectedSearchRequest = aSearchRequest().openFrom(CURRENT_TIME).near(CURRENT_LOCATION).withWifi().withSeating().build()
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		
		then:
		1 * venueFinder.findVenues({ equalTo(expectedSearchRequest).matches(it) }, _)
	}
	
	def "given no user locations, when search button is pressed do nothing"() {
		given:
		timeProvider.getCurrentTime() >> CURRENT_TIME
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		
		then:
		0 * venueFinder.findVenues(_, _)
	}
	
	def initializePresentationModel() {
		venueFinderPresentationModel = new VenueFinderPresentationModel(
				venueFinder, locationProvider, timeProvider, userLocationsObserver, venuesObserver)
	}
	
	static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
	static final Coordinates CURRENT_LOCATION = new Coordinates(1.0, 0.1);
	VenueFinder venueFinder = Mock()
	LocationProvider locationProvider = Mock()
	TimeProvider timeProvider = Mock()
	UserLocationsObserver userLocationsObserver = Mock()
	VenuesObserver venuesObserver = Mock()
	VenueFinderPresentationModel venueFinderPresentationModel
}