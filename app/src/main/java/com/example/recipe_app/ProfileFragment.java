package com.example.recipe_app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.model.FakeData;
import com.example.recipe_app.model.Recipe;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applyStatusBarInset(view);
        fillFakeProfileData(view);
        setupRecipesList(view);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> toast("TODO: menu"));

        MaterialButton btnEdit = view.findViewById(R.id.btnEditProfile);
        btnEdit.setOnClickListener(v -> toast("TODO: edit profile"));

        FloatingActionButton fab = view.findViewById(R.id.fabAddRecipe);
        fab.setOnClickListener(v -> toast("TODO: new recipe"));
    }

    private void applyStatusBarInset(View root) {
        View topBar = root.findViewById(R.id.topBar);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            topBar.setPadding(0, bars.top, 0, 0);
            return insets;
        });
    }

    private void fillFakeProfileData(View v) {
        setText(v, R.id.tvProfileName, "Pera Peric");
        setText(v, R.id.tvProfileUsername, "@peraperic");
        setText(v, R.id.tvBio, "Home cook, comfort-food lover. Sharing simple recipes that bring everyone to the table.");
        setText(v, R.id.tvAddedCount, "3");
        setText(v, R.id.tvSavedCount, "3");

        setText(v, R.id.tvFullName, "Pera Peric");
        setText(v, R.id.tvUsername, "@peraperic");
        setText(v, R.id.tvEmail, "pera.peric@gmail.com");
        setText(v, R.id.tvPhone, "+381 62 7430755");
        setText(v, R.id.tvLocation, "Novi Sad, Serbia");
        setText(v, R.id.tvMemberSince, "November 2026");
        setText(v, R.id.tvDiet, "No restrictions");
    }

    private void setupRecipesList(View v) {
        List<Recipe> recipes = FakeData.getRecipes();

        setText(v, R.id.tvRecipesCountLabel, recipes.size() + " recipes");

        RecyclerView rv = v.findViewById(R.id.rvRecipes);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(new ProfileRecipeAdapter(recipes,
                new ProfileRecipeAdapter.OnRecipeActionListener() {
                    @Override
                    public void onEditClick(Recipe recipe) {
                        toast("TODO: edit recipe " + recipe.Name);
                    }

                    @Override
                    public void onRecipeClick(Recipe recipe) {
                        Bundle args = new Bundle();
                        args.putInt("recipeId", recipe.Id);
                        Navigation.findNavController(v).navigate(R.id.action_profile_to_recipeDetails, args);
                    }
                }));
    }

    private void setText(View root, int id, String text) {
        ((TextView) root.findViewById(id)).setText(text);
    }

    private void toast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}