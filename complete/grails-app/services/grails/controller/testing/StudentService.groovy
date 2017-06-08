package grails.controller.testing

import grails.transaction.Transactional
import groovy.transform.CompileStatic

@CompileStatic
@Transactional(readOnly = true)
class StudentService {

    BigDecimal calculateAvgGrade() {
        Student.where {}.projections {
            avg('grade')
        }.get() as BigDecimal
    }
}

