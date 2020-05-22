package com.example.project_list.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project_list.R;
import com.example.project_list.data.PokeApi;
import com.example.project_list.presentation.model.Pokemon;
import com.example.project_list.presentation.model.RestPokemonResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://pokeapi.co";


    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private boolean haveInternetConnection(){
        //return true si connecté, return false dans le cas contraire
        NetworkInfo network = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (network==null || !network.isConnected()){
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //showList();
        sharedPreferences = getSharedPreferences("application_Pokedex", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();

        List<Pokemon> pokemonList = getDataFromCache();

        if(pokemonList != null && haveInternetConnection() != true){
            showList(pokemonList);
        }else{
            makeApiCall();
        }
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

    private void showList(List<Pokemon> pokemonList) {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        /*List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
        }*/
        // define an adapter
        mAdapter = new ListAdapter(pokemonList);
        recyclerView.setAdapter(mAdapter);
    }


    private void makeApiCall(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PokeApi pokeApi = retrofit.create(PokeApi.class);

        Call<RestPokemonResponse> call = pokeApi.getPokemonResponse();
        call.enqueue(new Callback<RestPokemonResponse>() {

            @Override
            public void onResponse(Call<RestPokemonResponse> call, Response<RestPokemonResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Pokemon> pokemonList = response.body().getResults();
                    showList(pokemonList);
                    saveList(pokemonList);
                }else{
                    showError();
                }
            }

            @Override
            public void onFailure(Call<RestPokemonResponse> call, Throwable t) {
                showError();
            }

        });
    }

    private void saveList(List<Pokemon> pokemonList){
        String jsonString = gson.toJson(pokemonList);
        sharedPreferences
                .edit()
                .putString("jsonPokemonList", jsonString)
                .apply();
        Toast.makeText(this, "List Saved", Toast.LENGTH_SHORT).show();

    }

    private void showError() {
        Toast.makeText(this, "API Error", Toast.LENGTH_SHORT).show();
    }
}
