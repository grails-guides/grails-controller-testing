package demo

import grails.transaction.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
@Transactional
class StudentService {

    @Transactional(readOnly = true)
    BigDecimal calculateAvgGrade() {
        Student.where { }.projections {
            avg('grade')
        }.get() as BigDecimal
    }

    @Transactional(readOnly = true)
    List<Student> list(GrailsParameterMap params) {
        Student.list(params)
    }

    @Transactional(readOnly = true)
    Student create(GrailsParameterMap params) {
        new Student(params)
    }

    @Transactional(readOnly = true)
    Student read(Long id) {
        Student.read(id)
    }

    int count() {
        Student.count() as int
    }

    Student save(StudentSaveCommand cmd, boolean flush = false) {
        def student = new Student(name: cmd.name, grade: cmd.grade)
        if ( !student.save(flush: flush) ) {
            log.error("could not save student: ${student.errors.toString()}")
        }
        student
    }

    Student update(StudentUpdateCommand cmd, boolean flush = false) {
        def student = Student.get(cmd.id)
        if ( !student ) {
            return student
        }
        student.with {
            name = cmd.name
            grade = cmd.grade
        }
        if ( !student.save(flush: flush) ) {
            log.error("could not update student: ${student.errors.toString()}")
        }
        student
    }

    /**
     * @return false if the user was not found
     */
    boolean delete(Long id, boolean flush = false) {
        Student student = Student.get(id)
        if ( !student ) {
            return false
        }
        student.delete(flush: flush)
        true
    }
}
