package com.feedsome.daq.test.route.component;

import lombok.Data;

import java.util.Collection;

@Data
public class JokeResponseDTO {

    private String type;

    private JokeResponseDTO.Value value;

    @Data
    static class Value {
        private long id;

        private String joke;

        private Collection<String> categories;
    }

}
