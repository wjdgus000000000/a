package com.dongguk.cse.naemansan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User courseUser;

    @Column(name="title",unique = true)
    private String title;

    @Column(name="created_date")
    private Timestamp createdDate;

    @Column(name="introduction")
    private String introduction;

    @Column(name="start_location_name")
    private String startLocationName;

    @Column(name="start_location", columnDefinition = "POINT")
    private Point startLocation;

    @Column(name="locations", columnDefinition = "MULTIPOINT")
    private MultiPoint locations;

    @Column(name="distance")
    private double distance;

    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private boolean status;

    // ------------------------------------------------------------

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseTag> courseTags = new ArrayList<>();

    @OneToMany(mappedBy = "likeCourse", fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "commentCourse", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<UsingCourse> usingCourses = new ArrayList<>();

    @Builder
    public Course(User courseUser, String title, String introduction,
                  String startLocationName, Point startLocation, MultiPoint locations, double distance, boolean status) {
        this.courseUser = courseUser;
        this.title = title;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.introduction = introduction;
        this.startLocationName = startLocationName;
        this.startLocation = startLocation;
        this.locations = locations;
        this.distance = distance;
        this.status = status;
    }

    public void updateCourse(String title, String introduction) {
        setTitle(title);
        setIntroduction(introduction);
    }
}
