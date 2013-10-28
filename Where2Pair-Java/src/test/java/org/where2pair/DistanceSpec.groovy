package org.where2pair

import spock.lang.Specification
import static org.where2pair.DistanceUnit.KM
import static org.where2pair.DistanceUnit.MILES

class DistanceSpec extends Specification {

	def "converts distance to human readable form"() {
		given:
		Distance distance1 = new Distance(0.15575645746, KM)
		Distance distance2 = new Distance(0.82974773422, MILES)
		
		when:
		def str1 = distance1.toHumanReadableString()
		def str2 = distance2.toHumanReadableString()
		
		then:
		str1 == '0.16km'
		str2 == '0.83m'
	}
	
}
