package demo

import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        if ( Environment.current == Environment.DEVELOPMENT ) {
            initStudents()
        }
    }

    static void initStudents() {
        Student.saveAll(
                new Student(name: 'Nirav', grade: 100),
                new Student(name: 'Jeff', grade: 95),
                new Student(name: 'Sergio', grade: 90),
        )
    }

    def destroy = {
    }
}
