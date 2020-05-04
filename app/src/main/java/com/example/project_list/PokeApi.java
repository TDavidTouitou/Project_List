package com.example.project_list;

import retrofit2.Call;
import retrofit2.http.GET;


public interface PokeApi {
    @GET("/api/v2/pokemon?limit=151/")
    Call<RestPokemonResponse> getPokemonResponse();

}
