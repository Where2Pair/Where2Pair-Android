package org.where2pair.presentation

import static org.where2pair.Facility.BABY_CHANGING
import static org.where2pair.Facility.SEATING
import static org.where2pair.Facility.WIFI

import org.where2pair.SearchOptionsRepository
import org.where2pair.SimpleTime
import org.where2pair.TimeProvider

import spock.lang.Specification

class SearchOptionsPresentationModelSpec extends Specification {

	SearchOptionsRepository searchOptionsRepository = Mock()
	TimeProvider timeProvider = Mock()
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
	
	def "every increment of the time slider bars represents half an hour"() {
		given:
		timeProvider.getCurrentTime() >> new SimpleTime(12,0)
		
		when:
		def incrementCount = searchOptionsPresentationModel.getTimeSliderIncrementCount()
		
		then:
		incrementCount == 24
	}
	
	def "shows 'open from' label"() {
		
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
