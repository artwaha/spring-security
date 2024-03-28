package com.artwaha.springraphql.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookRequest {
    private String title;
    private String publisher;
    private Long authorId;
}
