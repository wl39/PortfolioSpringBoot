package com.wl39.portfolio.post;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Entity
@Table
public class Post {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String title;
    private String author;
    private LocalDateTime generatedDate;
    private LocalDateTime lastModifiedDate;
    private String paragraph;
    @Transient
    private Long minAgo;
    @Transient
    private Long hourAgo;
    @Transient
    private Long dayAgo;
    @Transient
    private Boolean isModified;

    public Post() {
    }

    public Post(Long id, String title, String author, LocalDateTime generatedDate, LocalDateTime lastModifiedDate, String paragraph) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.generatedDate = generatedDate;
        this.lastModifiedDate = lastModifiedDate;
        this.paragraph = paragraph;
    }

    public Post(String title, String author, LocalDateTime generatedDate, LocalDateTime lastModifiedDate, String paragraph) {
        this.title = title;
        this.author = author;
        this.generatedDate = generatedDate;
        this.lastModifiedDate = lastModifiedDate;
        this.paragraph = paragraph;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public Long getMinAgo() {
        return ChronoUnit.MINUTES.between(this.lastModifiedDate, LocalDateTime.now());
    }

    public void setMinAgo(Long minAgo) {
        this.minAgo = minAgo;
    }

    public Long getHourAgo() {
        return ChronoUnit.HOURS.between(this.lastModifiedDate, LocalDateTime.now());
    }

    public void setHourAgo(Long hourAgo) {
        this.hourAgo = hourAgo;
    }

    public Long getDayAgo() {
        return ChronoUnit.DAYS.between(this.lastModifiedDate, LocalDateTime.now());
    }

    public void setDayAgo(Long dayAgo) {
        this.dayAgo = dayAgo;
    }

    public Boolean getModifiedAfter() {
        return !this.lastModifiedDate.equals(this.generatedDate);
    }

    public void setModifiedAfter(Boolean modified) {
        isModified = modified;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", generatedDate=" + generatedDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", paragraph='" + paragraph + '\'' +
                ", minAgo=" + minAgo +
                ", hourAgo=" + hourAgo +
                ", dayAgo=" + dayAgo +
                ", isModified=" + isModified +
                '}';
    }
}
