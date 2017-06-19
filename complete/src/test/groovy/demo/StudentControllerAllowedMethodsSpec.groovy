package demo

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED
import static javax.servlet.http.HttpServletResponse.SC_OK

@TestFor(StudentController)
class StudentControllerAllowedMethodsSpec extends Specification {

    @Unroll
    def "test TestController.index does not accept #method requests"(String method) {
        when:
        request.method = method
        controller.save()

        then:
        response.status == SC_METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'GET', 'PUT']
    }

    def "test TestController.index accepts POST requests"() {
        when:
        request.method = 'POST'
        controller.save()

        then:
        response.status == SC_OK
    }
}
