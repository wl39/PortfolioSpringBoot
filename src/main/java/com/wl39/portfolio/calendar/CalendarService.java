package com.wl39.portfolio.calendar;

import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.assignment.AssignmentRepository;
import com.wl39.portfolio.assignment.AssignmentService;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentService;
import com.wl39.portfolio.submission.Submission;
import com.wl39.portfolio.submission.SubmissionRepository;
import com.wl39.portfolio.submission.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final StudentService studentService;

    public void assignNewQuestion(Student student, LocalDate date) {
        Calendar calendar = this.calendarRepository.findByStudent_NameAndYearAndMonthAndDay(student.getName(), date.getYear(), date.getMonthValue(), date.getDayOfMonth()).orElse(new Calendar(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth(), student, 0L,0L,0L
        ));
        calendar.setUnsolved(calendar.getUnsolved() + 1);

        calendarRepository.save(calendar);
    }

    public void assignNewQuestions(Student student, LocalDate date, int counts) {
        Calendar calendar = this.calendarRepository.findByStudent_NameAndYearAndMonthAndDay(student.getName(), date.getYear(), date.getMonthValue(), date.getDayOfMonth()).orElse(new Calendar(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth(), student, 0L,0L,0L
        ));

        calendar.setUnsolved(calendar.getUnsolved() + counts);

        calendarRepository.save(calendar);
    }

    public void submitAnswer(Student student, LocalDate date, boolean isSAQ) {
        Calendar calendar = this.calendarRepository.findByStudent_NameAndYearAndMonthAndDay(student.getName(), date.getYear(), date.getMonthValue(), date.getDayOfMonth()).orElseThrow();

        if (isSAQ) {
            calendar.setToMark(calendar.getToMark() + 1);
        }

        calendar.setUnsolved(calendar.getUnsolved() - 1);
        calendar.setSolved(calendar.getSolved() + 1);

        calendarRepository.save(calendar);
    }

    public void submitAnswers(Student student, LocalDate date, int ms, int ss) {
        Calendar calendar = this.calendarRepository.findByStudent_NameAndYearAndMonthAndDay(student.getName(), date.getYear(), date.getMonthValue(), date.getDayOfMonth()).orElseThrow();

        calendar.setToMark(calendar.getToMark() + ss);


        calendar.setUnsolved(calendar.getUnsolved() - (ms + ss));
        calendar.setSolved(calendar.getSolved() + (ms + ss));

        calendarRepository.save(calendar);
    }

    public List<Calendar> findByYearMonthAndStudent(int year, int month, String name) {
        return calendarRepository.findByYearAndMonthAndStudent_Name(year, month, name);
    }

    public void markSAQQuestion(int year, int month, int day, String name) {
        Calendar calendar =  calendarRepository.findByStudent_NameAndYearAndMonthAndDay(name, year, month, day).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "calendar not found")
        );
        calendar.setToMark(calendar.getToMark() - 1);

        this.calendarRepository.save(calendar);
    }

    @Transactional
    public int reloadCalendars(String name) {
        Student student = studentService.get(name);

        List<Assignment> assignments = assignmentRepository.findByStudentName(name);
        List<Submission> submissions = submissionRepository.findByStudentName(name);

        Map<LocalDate, Long> toMark = new HashMap<>();
        Map<LocalDate, Long> solved = new HashMap<>();
        Map<LocalDate, Long> unsolved = new HashMap<>();

        for (Assignment assignment : assignments) {
            LocalDate date = assignment.getTargetDate().toLocalDate();
            unsolved.merge(date, 1L, Long::sum);
        }

        for (Submission submission : submissions) {
            LocalDate date = submission.getTargetDate().toLocalDate();
            if (submission.getMarked() == 0) {
                toMark.merge(date, 1L, Long::sum);
            } else {
                solved.merge(date, 1L, Long::sum);
            }
        }

        // 날짜 키를 통합 (toMark + solved + unsolved)
        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(toMark.keySet());
        allDates.addAll(solved.keySet());
        allDates.addAll(unsolved.keySet());

        List<Calendar> calendars = new ArrayList<>();

        calendarRepository.deleteByStudent_Name(name);

        for (LocalDate date : allDates) {
            Calendar calendar = new Calendar();
            calendar.setYear(date.getYear());
            calendar.setMonth(date.getMonthValue());
            calendar.setDay(date.getDayOfMonth());

            calendar.setToMark(toMark.getOrDefault(date, 0L));
            calendar.setSolved(solved.getOrDefault(date, 0L));
            calendar.setUnsolved(unsolved.getOrDefault(date, 0L));
            calendar.setStudent(student);

            calendars.add(calendar);
        }

        return calendarRepository.saveAll(calendars).size();
    }
}
