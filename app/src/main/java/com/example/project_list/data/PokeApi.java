package com.example.project_list.data;

import com.example.project_list.presentation.model.RestPokemonResponse;

import retrofit2.Call;
import retrofit2.http.GET;


public interface PokeApi {
    @GET("/api/v2/pokemon?limit=151/")
    Call<RestPokemonResponse> getPokemonResponse();

}
