package com.exemple.oussama.utils;

import com.exemple.oussama.model.dto.AppDTO;

public class AppUtils {

    public static AppDTO createAppDto(String name, String version, String author) {
        return AppDTO.builder()
            .name(name)
            .version(version)
            .author(author)
            .build();
    }
}
