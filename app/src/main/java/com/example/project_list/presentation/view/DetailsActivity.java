package com.example.project_list.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_list.R;
import com.example.project_list.Singletons;
import com.example.project_list.presentation.model.Pokemon;

public class DetailsActivity extends AppCompatActivity {

    private TextView txtDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        txtDetails = findViewById(R.id.details_txt);
        Intent intent = getIntent();
        String pokemonJson = intent.getStringExtra("pokemonKey");
        Pokemon pokemon = Singletons.getGson().fromJson(pokemonJson, Pokemon.class);
        ShowDetails(pokemon);
    }

    private void ShowDetails(Pokemon pokemon) {
        txtDetails.setText(pokemon.getName());
    }
}
