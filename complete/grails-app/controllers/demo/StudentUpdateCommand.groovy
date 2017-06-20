package demo

import grails.validation.Validateable

class StudentUpdateCommand implements Validateable {
    Long id
    String name
    BigDecimal grade

    static constraints = {
        id nullable: false
        name nullable: false
        grade nullable: false, min: 0.0
    }
}
