package com.music.catalog.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private Long id;
    private String title;
    private Integer releaseYear;
    private List<Artist> artists = new ArrayList<>();
    private List<String> coverImageUrls = new ArrayList<>(); // URLs prontas para o front
}