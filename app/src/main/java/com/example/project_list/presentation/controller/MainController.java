package com.example.project_list.presentation.controller;

import android.content.SharedPreferences;

import com.example.project_list.Constants;
import com.example.project_list.Singletons;
import com.example.project_list.presentation.model.Pokemon;
import com.example.project_list.presentation.model.RestPokemonResponse;
import com.example.project_list.presentation.view.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainController {

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private MainActivity view;

    public MainController(MainActivity mainActivity, Gson gson, SharedPreferences sharedPreferences){
        this.view = mainActivity;
        this.gson = gson;
        this.sharedPreferences = sharedPreferences;

    }

    public void onStart(){

        List<Pokemon> pokemonList = getDataFromCache();

        if(pokemonList != null /*&& !haveInternetConnection()*/){
            view.showList(pokemonList);
        }else{
            makeApiCall();
        }
    }

    /*private boolean haveInternetConnection(){
        //return true si connecté, return false dans le cas contraire
        NetworkInfo network = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (network==null || !network.isConnected()){
            return false;
        }else{
            return true;
        }
    }*/

    private void makeApiCall(){
        Call<RestPokemonResponse> call = Singletons.getPokeApi().getPokemonResponse();
        call.enqueue(new Callback<RestPokemonResponse>() {

            @Override
            public void onResponse(Call<RestPokemonResponse> call, Response<RestPokemonResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Pokemon> pokemonList = response.body().getResults();
                    view.showList(pokemonList);
                    saveList(pokemonList);
                }else{
                    view.showError();
                }
            }

            @Override
            public void onFailure(Call<RestPokemonResponse> call, Throwable t) {

                view.showError();
            }

        });
    }

    private void saveList(List<Pokemon> pokemonList){
        String jsonString = gson.toJson(pokemonList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_POKEMON_LIST, jsonString)
                .apply();
    }

    private List<Pokemon> getDataFromCache() {
        String jsonPokemon = sharedPreferences.getString("jsonPokemonList", null);

        if(jsonPokemon == null){
            return null;
        }else{
            Type listType = new TypeToken<List<Pokemon>>(){}.getType();
            return gson.fromJson(jsonPokemon, listType);
        }
    }

    public void onItemClick(Pokemon pokemon){
        view.navigateToDetails(pokemon);
    }
}
