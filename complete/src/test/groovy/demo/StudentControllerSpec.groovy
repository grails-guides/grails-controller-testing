// tag::specBeginning[]
package demo

import demo.BootStrap
import demo.Student
import demo.StudentService
import grails.test.mixin.*
import spock.lang.*

@TestFor(StudentController)
@Mock(Student)
class StudentControllerSpec extends Specification {

// end::specBeginning[]

    def setup() {
        controller.studentService = Mock(StudentService)
    }

    // tag::populateParams[]
    def populateValidParams(params) {
        assert params != null

        params["name"] = 'Nirav'
        params["grade"] = 100
    }

    // end::populateParams[]

    // tag::testIndex[]
    void "Test the index action returns the correct model"() {
        given:
            BootStrap.initStudents() // <1>

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            model.studentList  // <2>
            model.studentCount == 3
    }

    // end::testIndex[]

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.student!= null
    }

    // tag::testSave[]
    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST' // <3>
            def student = new Student()
            student.validate()
            controller.save(student)

        then:"The create view is rendered again with the correct model"
            model.student!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            student = new Student(params)

            controller.save(student)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/student/show/1'
            controller.flash.message != null
            Student.count() == 1
    }

    // end::testSave[]

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def student = new Student(params)
            controller.show(student)

        then:"A model is populated containing the domain instance"
            model.student == student
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def student = new Student(params)
            controller.edit(student)

        then:"A model is populated containing the domain instance"
            model.student == student
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/student/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def student = new Student()
            student.validate()
            controller.update(student)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.student == student

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            student = new Student(params).save(flush: true)
            controller.update(student)

        then:"A redirect is issued to the show action"
            student != null
            response.redirectedUrl == "/student/show/$student.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/student/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def student = new Student(params).save(flush: true)

        then:"It exists"
            Student.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(student)

        then:"The instance is deleted"
            Student.count() == 0
            response.redirectedUrl == '/student/index'
            flash.message != null
    }

    void "Test controller correctly calls service method"() {
        when: "controller is invoked to calculate grades"
            controller.calculateAvgGrade()

        then: "verify the service was called and response received"
            response.text == "Avg Grade is 100"
            1 * controller.studentService.calculateAvgGrade() >> 100
    }

    void "test update with param inputs to a command object" () {
        when: "student is saved initially"
            request.method = 'PUT'
            def student = new Student(name: "Niraaav_misspelled", grade: 100)
            student.save()
            params.id = student.id
            params.name = 'Nirav'
            controller.update()

        then:
            model.student.name == "Nirav"
            model.student.grade == 100
    }

    void "test save with json request - domain conversion"() {

        when: "json request is sent with domain conversion"
            request.method = 'POST'
            request.json = new Student(name: "Nirav", grade: 87)
            controller.save()

        then:
            Student.count() == 1
    }

    void "test save with json request - raw json"() {
        when: "json request is sent with domain conversion"
            request.method = 'POST'
            request.json = '{"name":"Rina","grade":85}'
            controller.save()

        then:
            Student.count() == 1
    }
// tag::specEnding[]
}
// end::specEnding[]
