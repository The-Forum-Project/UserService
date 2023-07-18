package com.bfs.hibernateprojectdemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
public class SimpleMessage implements Serializable{
    private String email;
    private String code;
}
