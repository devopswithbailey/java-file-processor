package com.file.javafileprocessor;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book implements Serializable {
    private String author;
    private String title;
}
