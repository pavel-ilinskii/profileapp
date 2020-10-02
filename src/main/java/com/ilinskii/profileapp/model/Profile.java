package com.ilinskii.profileapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "pa_profile")
@Getter
@Setter
public class Profile {

    public static final int SIZE_EMAIL = 255;

    public static final int SIZE_NAME = 63;

    public static final int MIN_AGE = 0;

    public static final int MAX_AGE = 127;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "s_email", length = SIZE_EMAIL)
    private String email;

    @Column(name = "s_name", length = SIZE_NAME)
    private String name;

    @Column(name = "i_age")
    private Integer age;

    @Column(name = "dt_created")
    private ZonedDateTime created;
}
