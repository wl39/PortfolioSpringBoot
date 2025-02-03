package com.wl39.portfolio.calendar;

import com.wl39.portfolio.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

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
}
