package com.example.recipe_app;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnGoToLogin).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_login));

        view.findViewById(R.id.btnGoToProfile).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_profile));

        // opens first recipe from FakeData (recipeId = "1")
        view.findViewById(R.id.btnGoToRecipe).setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("recipeId", "1");
            Navigation.findNavController(v)
                    .navigate(R.id.action_home_to_recipeDetails, args);
        });
    }
}