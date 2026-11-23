package com.example.recipe_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel vm = new ViewModelProvider(this).get(HomeViewModel.class);

        // Pretraga
        MaterialCardView searchCard = view.findViewById(R.id.cardHomeSearch);
        searchCard.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("focus_search", true);

            NavHostFragment.findNavController(this).navigate(R.id.searchFragment, bundle);
        });

        // Kategorije -> pretraga
        ChipGroup chips = view.findViewById(R.id.chipGroup);
        for (int i = 0; i < chips.getChildCount(); i++) {
            View chip = chips.getChildAt(i);
            chip.setOnClickListener(v -> {
                String chipText = ((com.google.android.material.chip.Chip) chip).getText().toString();

                com.example.recipe_app.ui.search.SearchViewModel searchViewModel =
                        new androidx.lifecycle.ViewModelProvider(requireActivity()).get(com.example.recipe_app.ui.search.SearchViewModel.class);

                // Clear previous filters and apply the new one
                com.example.recipe_app.ui.search.RecipeFilter newFilter = new com.example.recipe_app.ui.search.RecipeFilter();

                if (chipText.equalsIgnoreCase("Salads")) {
                    newFilter.mealTypes.add(com.example.recipe_app.model.enums.MealType.SALAD);
                } else if (chipText.equalsIgnoreCase("Soups")) {
                    newFilter.dishTypes.add(com.example.recipe_app.model.enums.DishType.SOUP);
                } else if (chipText.equalsIgnoreCase("Deserts")) {
                    newFilter.mealTypes.add(com.example.recipe_app.model.enums.MealType.DESSERT);
                } else if (chipText.equalsIgnoreCase("Pizza")) {
                    newFilter.dishTypes.add(com.example.recipe_app.model.enums.DishType.PIZZA);
                }

                searchViewModel.setFilter(newFilter);
                searchViewModel.setQuery(""); // Clear text search

                // Navigate to Search tab
                NavHostFragment.findNavController(this).navigate(R.id.action_home_to_search);
            });
        }

        // FAB -> dodavanje recepta
        view.findViewById(R.id.fabAdd).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.addRecipeFragment));

        // Sekcije
        LinearLayout sections = view.findViewById(R.id.sectionsContainer);
        addSection(sections, R.string.sec_latest, R.string.sec_latest_sub, vm.getNewest());
        addSection(sections, R.string.sec_favorites, R.string.sec_favorites_sub, vm.getPopular());
    }

    private void addSection(LinearLayout container, @StringRes int titleRes,
                            @StringRes int subtitleRes, List<RecipeCard> items) {
        if (items == null || items.isEmpty()) return;

        View section = getLayoutInflater().inflate(R.layout.view_section, container, false);
        ((TextView) section.findViewById(R.id.tvTitle)).setText(titleRes);
        ((TextView) section.findViewById(R.id.tvSubtitle)).setText(subtitleRes);

        RecyclerView rv = section.findViewById(R.id.rvRecipes);
        rv.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new RecipeCardAdapter(items, recipe ->
                Toast.makeText(requireContext(), recipe.name, Toast.LENGTH_SHORT).show()));

        container.addView(section);
    }
}