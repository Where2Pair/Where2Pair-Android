package org.where2pair.presentation

import org.where2pair.SearchOptionsRepository

import spock.lang.Specification

import static org.where2pair.Facility.WIFI
import static org.where2pair.Facility.SEATING
import static org.where2pair.Facility.BABY_CHANGING

class SearchOptionsPresentationModelSpec extends Specification {

	SearchOptionsRepository searchOptionsRepository = Mock()
	SearchOptionsPresentationModel searchOptionsPresentationModel = new SearchOptionsPresentationModel(
		searchOptionsRepository)
	
	def "shows monday-sunday"() {
		when:
		def days = searchOptionsPresentationModel.getDays()
		
		then:
		days == ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
	}
	
	def "shows all facilities"() {
		when:
		def facilities = searchOptionsPresentationModel.getFacilities()
		
		then:
		facilities == ["Wifi", "Seating", "Baby changing"]
	}
	
	def "loads selected facilities from search options repository"() {
		given:
		searchOptionsRepository.getSelectedFacilities() >> selectedFacilities
		
		when:
		def selectedFacilityPositions = searchOptionsPresentationModel.getSelectedFacilityPositions()
		
		then:
		selectedFacilityPositions == expectedSelectedFacilityPositions
		
		where:
		selectedFacilities 	| expectedSelectedFacilityPositions
		[WIFI, SEATING]		| [0, 1]
		[]					| []
		[BABY_CHANGING]		| [2]
	}
	
	def "updates search options repository when selecting facilities"() {
		when:
		searchOptionsPresentationModel.setSelectedFacilityPositions(selectedFacilityPositions)
		
		then:
		searchOptionsRepository.setSelectedFacilities(expectedSelectedFacilities)
		
		where:
		selectedFacilityPositions 	| expectedSelectedFacilities
		[0, 1]		        		| [WIFI, SEATING]
		[]					        | []
		[2]	        				| [BABY_CHANGING]	
	}
}
