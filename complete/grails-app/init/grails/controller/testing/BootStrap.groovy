package grails.controller.testing

import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        if ( Environment.current == Environment.DEVELOPMENT ) {
            initStudents()
        }
    }

    static void initStudents() {
        List<Map<String, Object>> students = [
                [name: 'Nirav', grade: 100],
                [name: 'Jeff', grade: 95],
                [name: 'Sergio', grade: 90]
        ]

        for ( Map<String, Object> s : students ) {
            new Student(name: s.name, grade: s.grade).save(flush: true)
        }
    }

    def destroy = {
    }
}
