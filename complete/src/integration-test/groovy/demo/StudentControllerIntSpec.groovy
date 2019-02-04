package demo

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class StudentControllerIntSpec extends Specification {

    @Shared HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(baseUrl.toURL())
    }

    def setup() {
        Student.saveAll(
                new Student(name: 'Nirav', grade: 100),
                new Student(name: 'Jeff', grade: 95),
                new Student(name: 'Sergio', grade: 90),
        )
    }

    def 'test json in URI to return students'() {
        when:
        HttpResponse<Map> resp = client.toBlocking().exchange(HttpRequest.GET("/student.json"), Map) // <1> <2>

        then:
        resp.status == HttpStatus.OK // <3>
        resp.json.size() == 3
        resp.json.find { it.grade == 100 && it.name == 'Nirav' }
        resp.json.find { it.grade == 95 &&  it.name == 'Jeff' }
        resp.json.find { it.grade == 90 &&  it.name == 'Sergio' }
    }
}
