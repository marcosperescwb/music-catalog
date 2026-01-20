package com.music.catalog.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Regional {
    private Long id;
    private Long originalId; // ID da API externa
    private String name;
    private boolean active;
}