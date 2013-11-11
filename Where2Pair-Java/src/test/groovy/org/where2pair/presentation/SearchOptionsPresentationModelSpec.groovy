package org.where2pair.presentation

import spock.lang.Specification

class SearchOptionsPresentationModelSpec extends Specification {

	SearchOptionsPresentationModel searchOptionsPresentationModel = new SearchOptionsPresentationModel()
	
	def "shows monday-sunday"() {
		when:
		def days = searchOptionsPresentationModel.getDays()
		
		then:
		days == ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
	}
	
}
