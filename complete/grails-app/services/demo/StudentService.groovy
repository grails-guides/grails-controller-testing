package demo

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.util.logging.Slf4j

interface IStudentService {
    Student save(String name, BigDecimal grade)

    Student update(Serializable id, String name, BigDecimal grade)

    Student delete(Serializable id)

    int count()
}

@SuppressWarnings('AbstractClassWithoutAbstractMethod')
@Slf4j
@Service(Student)
abstract class StudentService implements IStudentService {

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

    @Transactional
    Student save(StudentSaveCommand cmd) {
        save(cmd.name, cmd.grade)
    }

    @Transactional
    Student update(StudentUpdateCommand cmd) {
        update(cmd.id, cmd.name, cmd.grade)
    }

}
