package grails.controller.testing

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class StudentControllerIntSpec extends Specification {

    def setup() {
        BootStrap.initStudents()
    }


    void "test json in URI to return students"() {
        when:
            RestBuilder rest = new RestBuilder()
            RestResponse resp = rest.get("http://localhost:${serverPort}/student.json") // <1> <2>

        then:
            resp.status == 200 // <3>
            resp.json.size() == 3
            resp.json.toString() ==
                    """[{"grade":100,"name":"Nirav","id":1},{"grade":95,"name":"Jeff","id":2},{"grade":90,"name":"Sergio","id":3}]"""
    }

    void "test calculateAvg grade end to end"() {
        when:
            RestBuilder rest = new RestBuilder()
            def resp = rest.get("http://localhost:${serverPort}/student/calculateAvgGrade") // <4>

        then:
            resp.status == 200
            resp.text == "Avg Grade is 95.0"
    }
}