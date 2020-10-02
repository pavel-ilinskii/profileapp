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
@Table(name = "pa_error_log")
@Getter
@Setter
public class ErrorLog {

    public static final int SIZE_MSG = 512;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "s_msg", length = SIZE_MSG)
    private String msg;

    @Column(name = "dt_created")
    private ZonedDateTime created;
}
