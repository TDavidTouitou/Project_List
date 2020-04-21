package com.example.project_list;

import java.util.List;

public class RestPokemonResponse {

    private Integer count;
    private String next;
    private List<Pokemon> results;

    public Integer getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    List<Pokemon> getResults() {
        return results;
    }
}
