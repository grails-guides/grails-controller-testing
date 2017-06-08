package grails.controller.testing

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Student {
    String name
    BigDecimal grade

    String toString() {
        name
    }

    static constraints = {
    }
}
