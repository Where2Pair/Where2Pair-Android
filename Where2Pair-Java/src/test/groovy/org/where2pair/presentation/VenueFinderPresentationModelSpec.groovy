package org.where2pair.presentation

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.mockito.BDDMockito.given
import static org.where2pair.SearchRequestBuilder.aSearchRequest
import static org.where2pair.SearchRequestMatcher.equalTo
import static org.where2pair.TestUtils.sampleVenuesWithDistances
import static org.where2pair.TestUtils.sampleVenues
import static spock.util.matcher.HamcrestSupport.that

import org.where2pair.Coordinates
import org.where2pair.SimpleTime
import org.where2pair.VenueFinder

import spock.lang.Specification
import spock.lang.Unroll

class VenueFinderPresentationModelSpec extends Specification {

	def "when current location is established, adds current location to user locations and notifies observers"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		
		then:
		venueFinderPresentationModel.getUserLocations() == [CURRENT_LOCATION]
		1 * userLocationsObserver.notifyUserLocationAddedAndZoomCamera(CURRENT_LOCATION)
	}
	
	def "when current location is established but user locations have already been manually added, ignores"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.mapLongPressed(NEW_LOCATION)
		
		when:
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		
		then:
		venueFinderPresentationModel.getUserLocations() == [NEW_LOCATION]
		0 * userLocationsObserver.notifyUserLocationAdded(CURRENT_LOCATION)
	}
	
	def "when there are no user locations or venues to display, then map should focus on London with wide zoom"() {
		given:
		initializePresentationModel()
		
		when:
		def mapViewportBounds = venueFinderPresentationModel.getMapViewportBounds()
		
		then:
		mapViewportBounds == [VenueFinderPresentationModel.LONDON]
	}
	
	def "when there are user locations but no venues to display, then map should focus on user locations with wide zoom"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		
		when:
		def mapViewportBounds = venueFinderPresentationModel.getMapViewportBounds()
		
		then:
		mapViewportBounds == [CURRENT_LOCATION]
	}
	
	def "when there are user locations and venues to display, then map should focus on user locations and nearest 5 venues with close zoom"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		venueFinderPresentationModel.venues = sampleVenuesWithDistances()
		
		when:
		def mapViewportBounds = venueFinderPresentationModel.getMapViewportBounds()
		
		then:
		mapViewportBounds == [CURRENT_LOCATION] + sampleVenuesWithDistances()[0..4].collect{ it.venue.location }
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
		'resetButton'			| false
		'loadingIcon'			| false
	}
	
	@Unroll
	def "when searching for venues, view: #view should be visible: #visibilityExpectation"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		
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
		'resetButton'			| false
		'loadingIcon'			| true
	}
	
	@Unroll
	def "when searching for venues completes, view: #view should be visible: #visibilityExpectation"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFound([])
		
		then:
		venueFinderPresentationModel."${view}Visible" == visibilityExpectation
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFindingFailed("Network error")
		
		then:
		venueFinderPresentationModel."${view}Visible" == visibilityExpectation
		
		where:
		view 					| visibilityExpectation
		'searchButton' 			| false
		'searchOptionsButton' 	| false
		'mapButton'				| false
		'listButton'			| true
		'resetButton'			| true
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
		'resetButton'			| true
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
		'resetButton'			| true
		'loadingIcon'			| false
	}
	
	@Unroll
	def "when reset button pressed, view: #view should be visible: #visibilityExpectation"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFound([])
		venueFinderPresentationModel.resetButtonPressed()
		
		then:
		venueFinderPresentationModel."${view}Visible" == visibilityExpectation
		
		where:
		view 					| visibilityExpectation
		'searchButton' 			| true
		'searchOptionsButton' 	| true
		'mapButton'				| false
		'listButton'			| false
		'resetButton'			| false
		'loadingIcon'			| false
	}
	
	def "current location is visible as user location"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
				
		when:
		def userLocations = venueFinderPresentationModel.userLocations
		
		then:
		userLocations == [CURRENT_LOCATION]
	}
	
	def "when current location is unavailable then user locations are empty"() {
		given:
		initializePresentationModel()
		
		when:
		def userLocations = venueFinderPresentationModel.userLocations
		
		then:
		userLocations == []
	}
	
	def "when the map is long-pressed a new location is added and the device vibrates"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.mapLongPressed(NEW_LOCATION)
		
		then:
		1 * userLocationsObserver.notifyUserLocationAdded(NEW_LOCATION)
		venueFinderPresentationModel.getUserLocations() == [NEW_LOCATION]
		1 * deviceVibrator.vibrate(100)
	}
	
	def "when a user location is pressed before searching for venues, location is removed"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.mapLongPressed(NEW_LOCATION)
		
		when:
		venueFinderPresentationModel.userLocationPressed(NEW_LOCATION)
		
		then:
		1 * userLocationsObserver.notifyUserLocationRemoved(NEW_LOCATION)
		venueFinderPresentationModel.userLocations == []
	}
	
	def "when a user location is pressed whilst searching for venues, does nothing"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.mapLongPressed(NEW_LOCATION)
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.userLocationPressed(NEW_LOCATION)
		
		then:
		0 * userLocationsObserver.notifyUserLocationRemoved(NEW_LOCATION)
		venueFinderPresentationModel.userLocations == [NEW_LOCATION]
	}
	
	def "when a user location is pressed whilst viewing venue search results, does nothing"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.mapLongPressed(NEW_LOCATION)
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFound([])
		venueFinderPresentationModel.userLocationPressed(NEW_LOCATION)
		
		then:
		0 * userLocationsObserver.notifyUserLocationRemoved(NEW_LOCATION)
		venueFinderPresentationModel.userLocations == [NEW_LOCATION]
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
		0 * deviceVibrator.vibrate(_)
	}
	
	def "when venues are updated sets venues and notifies venues observer"() {
		given:
		def venues = sampleVenuesWithDistances()
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.notifyVenuesFound(venues)
		
		then:
		1 * venuesObserver.notifyVenuesUpdated()
		venueFinderPresentationModel.venues == venues
	}
	
	def "when search button is pressed finds open venues near selected locations with wifi and seating"() {
		given:
		timeProvider.getCurrentTime() >> CURRENT_TIME
		initializePresentationModel()
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		venueFinderPresentationModel.mapLongPressed(NEW_LOCATION)
		def expectedSearchRequest = aSearchRequest()
			.openFrom(CURRENT_TIME)
			.near([CURRENT_LOCATION, NEW_LOCATION])
			.withWifi().withSeating().build()
		
		when:
		venueFinderPresentationModel.searchButtonPressed()
		
		then:
		1 * venueFinder.findVenues({ equalTo(expectedSearchRequest).matches(it) }, _)
	}
	
	def "when search button is pressed but no user locations have been specified, does nothing"() {
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
	
	def "when map button is pressed, delegates to transition handler"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.mapButtonPressed()
		
		then:
		1 * venuesViewTransitioner.showMap()
	}
	
	def "when list button is pressed, delegates to transition handler"() {
		given:
		initializePresentationModel()
		
		when:
		venueFinderPresentationModel.listButtonPressed()
		
		then:
		1 * venuesViewTransitioner.showList()
	}
	
	def "when pressing reset button all user supplied locations and venues are cleared"() {
		given:
		initializePresentationModel()
		venueFinderPresentationModel.notifyCurrentLocationEstablished(CURRENT_LOCATION)
		
		when:
		venueFinderPresentationModel.mapLongPressed(NEW_LOCATION)
		venueFinderPresentationModel.searchButtonPressed()
		venueFinderPresentationModel.notifyVenuesFound(sampleVenues())
		venueFinderPresentationModel.resetButtonPressed()
		
		then:
		venueFinderPresentationModel.venues == []
		venueFinderPresentationModel.userLocations == [CURRENT_LOCATION]
		!venueFinderPresentationModel.viewingVenueSearchResults
		1 * venuesViewTransitioner.resetDisplay()
	}

	def initializePresentationModel() {
		venueFinderPresentationModel = new VenueFinderPresentationModel(
				venueFinder, locationProvider, timeProvider, deviceVibrator)
		venueFinderPresentationModel.userLocationsObserver = userLocationsObserver
		venueFinderPresentationModel.venuesObserver = venuesObserver
		venueFinderPresentationModel.venuesViewTransitioner = venuesViewTransitioner
	}
	
	static final CURRENT_TIME = new SimpleTime(12, 30);
	static final CURRENT_LOCATION = new Coordinates(1.0, 0.1);
	static final NEW_LOCATION = new Coordinates(2.0, 0.2)
	VenueFinder venueFinder = Mock()
	LocationProvider locationProvider = Mock()
	TimeProvider timeProvider = Mock()
	DeviceVibrator deviceVibrator = Mock()
	VenuesViewTransitioner venuesViewTransitioner = Mock()
	UserLocationsObserver userLocationsObserver = Mock()
	VenuesObserver venuesObserver = Mock()
	VenueFinderPresentationModel venueFinderPresentationModel
}
