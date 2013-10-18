package org.where2pair.presentation

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.mockito.BDDMockito.given
import static org.where2pair.SearchRequestBuilder.aSearchRequest
import static org.where2pair.SearchRequestMatcher.equalTo
import static org.where2pair.TestUtils.sampleVenues
import static spock.util.matcher.HamcrestSupport.that

import org.where2pair.Coordinates
import org.where2pair.SimpleTime
import org.where2pair.VenueFinder

import spock.lang.Specification
import spock.lang.Unroll

class VenueFinderPresentationModelSpec extends Specification {

	def "when there are no user locations or venues to display, then there are no markers to render on the map"() {
		given:
		initializePresentationModel()
		
		when:
		if (hasUserLocations) venueFinderPresentationModel.mapLongPressed(CURRENT_LOCATION)
		if (hasVenues) venueFinderPresentationModel.setVenues(sampleVenues())
		
		then:
		venueFinderPresentationModel.hasMapMarkersToDisplay() == expectsHasMapMarkersToDisplay
		
		where:
		hasUserLocations 	| hasVenues | expectsHasMapMarkersToDisplay
		false				| false		| false
		false				| true 		| true
		true				| false		| true
	}
	
	@Unroll
	def "at first, view: #view should be visible: #visibilityExpectation"() {
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
	
	@Unroll
	def "whilst searching for venues, view: #view should be visible: #visibilityExpectation"() {
		given:
		locationProvider.getCurrentLocation() >> CURRENT_LOCATION
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
	
	@Unroll
	def "when searching for venues completes, view: #view should be visible: #visibilityExpectation"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
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
	
	@Unroll
	def "when map button pressed, view: #view should be visible: #visibilityExpectation"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFound([])
		venueFinderPresentationModel.listButtonPressed()
		venueFinderPresentationModel.mapButtonPressed()
		
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

	@Unroll
	def "when list button pressed, view: #view should be visible: #visibilityExpectation"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFound([])
		venueFinderPresentationModel.listButtonPressed()
		
		then:
		venueFinderPresentationModel."${view}Visible" == visibilityExpectation
		
		where:
		view 					| visibilityExpectation
		'searchButton' 			| false
		'searchOptionsButton' 	| false
		'mapButton'				| true
		'listButton'			| false
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
	
	def "when the map is long-pressed a new location is added"() {
		given:
		def newLocation = new Coordinates(1.0, 0.1)
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.mapLongPressed(newLocation)
		
		then:
		1 * userLocationsObserver.notifyUserLocationAdded(newLocation)
		venueFinderPresentationModel.getUserLocations() == [newLocation]
	}
	
	def "when viewing venue search results then long-presses on map do nothing"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFound([])
		venueFinderPresentationModel.mapLongPressed(CURRENT_LOCATION)
		
		then:
		0 * userLocationsObserver.notifyUserLocationAdded(CURRENT_LOCATION)
		venueFinderPresentationModel.getUserLocations() == []
	}
	
	def "when venues are updated sets venues and notifies venues observer"() {
		given:
		def venues = sampleVenues()
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.notifyVenuesFound(venues)
		
		then:
		1 * venuesObserver.notifyVenuesUpdated()
		venueFinderPresentationModel.venues == venues
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
		venueFinderPresentationModel.searchButtonVisible
		venueFinderPresentationModel.searchOptionsButtonVisible
		!venueFinderPresentationModel.loadingIconVisible
	}
	
	def "when map button is pressed, delegate to transition handler"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.mapButtonPressed()
		
		then:
		1 * venuesViewTransitioner.showMap()
	}
	
	def "when list button is pressed, delegate to transition handler"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.listButtonPressed()
		
		then:
		1 * venuesViewTransitioner.showList()
	}
	
	def initializePresentationModel() {
		venueFinderPresentationModel = new VenueFinderPresentationModel(
				venueFinder, locationProvider, timeProvider)
		venueFinderPresentationModel.userLocationsObserver = userLocationsObserver
		venueFinderPresentationModel.venuesObserver = venuesObserver
		venueFinderPresentationModel.venuesViewTransitioner = venuesViewTransitioner
	}
	
	static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
	static final Coordinates CURRENT_LOCATION = new Coordinates(1.0, 0.1);
	VenueFinder venueFinder = Mock()
	LocationProvider locationProvider = Mock()
	TimeProvider timeProvider = Mock()
	VenuesViewTransitioner venuesViewTransitioner = Mock()
	UserLocationsObserver userLocationsObserver = Mock()
	VenuesObserver venuesObserver = Mock()
	VenueFinderPresentationModel venueFinderPresentationModel
}
