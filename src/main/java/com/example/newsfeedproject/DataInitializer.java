package com.example.newsfeedproject;

import com.example.basic.entity.Course;
import com.example.basic.entity.Student;
import com.example.basic.repository.CourseRepository;
import com.example.basic.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public DataInitializer(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 수업 3개 준비
        Course backend = new Course("backend");
        Course frontend = new Course("frontend");
        Course pm = new Course("pm");

        // 학생 6명 준비
        // - backend 수업에 속한 학생 목록
        Student gygim = new Student("gygim", "gygim@example.com", 10, backend);
        Student steve = new Student("steve", "steve@example.com", 11, backend);
        Student alice = new Student("alice", "alice@example.com", 12, backend);

        // - frontend 수업에 속한 학생 목록
        Student isac = new Student("isac", "isac@example.com", 13, frontend);
        Student michell = new Student("michelle", "michelle@example.com", 14, frontend);

        // - pm 수업에 속한 학생 목록
        Student ian = new Student("ian", "ian@example.com", 15, pm);

        // 수업 생성
        courseRepository.save(backend);
        courseRepository.save(frontend);
        courseRepository.save(pm);

        // 학생 생성
        studentRepository.save(gygim);
        studentRepository.save(steve);
        studentRepository.save(alice);
        studentRepository.save(isac);
        studentRepository.save(michell);
        studentRepository.save(ian);
    }
}