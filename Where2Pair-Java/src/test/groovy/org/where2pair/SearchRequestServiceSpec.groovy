package org.where2pair

import spock.lang.Specification

import static org.where2pair.SearchRequestBuilder.aSearchRequest
import static org.where2pair.SearchRequestMatcher.equalTo

class SearchRequestServiceSpec extends Specification {

	static final CURRENT_TIME = new SimpleTime(12, 0)
	static final USER_LOCATION = new Coordinates(1.0, 0.1)
	TimeProvider timeProvider = Mock()
	SearchRequestService searchRequestService = new SearchRequestService(timeProvider)
	
	def "builds a search request for the given locations for open venues with wifi and seating"() {
		given:
		timeProvider.currentTime >> CURRENT_TIME
		def expectedSearchRequest = aSearchRequest().openFrom(CURRENT_TIME).near([USER_LOCATION]).withWifi().withSeating().build()
		
		when:
		def actualSearchRequest = searchRequestService.buildSearchRequest([USER_LOCATION])
		
		then:
		actualSearchRequest equalTo(expectedSearchRequest)
	}

}
