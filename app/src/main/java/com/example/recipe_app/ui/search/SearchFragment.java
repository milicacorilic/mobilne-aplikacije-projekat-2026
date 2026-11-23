package com.example.recipe_app.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.MealType;
import com.example.recipe_app.model.enums.PrepMethod;
import com.example.recipe_app.util.DisplayNames;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class SearchFragment extends Fragment {

    private SearchRecipeAdapter adapter;
    private SearchViewModel viewModel;
    private TextView textListTitle;
    private TextView textRecipeCount;
    private TextView textActiveCount;
    private LinearLayout activeFiltersContainer;
    private ChipGroup chipGroupActiveFilters;
    private MaterialButton buttonFilters;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        textRecipeCount = view.findViewById(R.id.textRecipeCount);
        textListTitle = view.findViewById(R.id.textListTitle);
        textActiveCount = view.findViewById(R.id.textActiveCount);
        activeFiltersContainer = view.findViewById(R.id.activeFiltersContainer);
        chipGroupActiveFilters = view.findViewById(R.id.chipGroupActiveFilters);
        buttonFilters = view.findViewById(R.id.buttonFilters);
        TextInputEditText searchInput = view.findViewById(R.id.searchInput);

        adapter = new SearchRecipeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.buttonFilters).setOnClickListener(v -> {
            if (Navigation.findNavController(v).getCurrentDestination().getId() == R.id.searchFragment) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                }
                searchInput.clearFocus();
                Navigation.findNavController(v).navigate(R.id.action_search_to_filters);
            }
        });

        view.findViewById(R.id.buttonClearAllSearch).setOnClickListener(v -> {
            viewModel.setFilter(new RecipeFilter());
        });

        viewModel.getFilteredRecipes().observe(getViewLifecycleOwner(), recipes -> {
            adapter.setRecipes(recipes);
            textRecipeCount.setText(getResources().getQuantityString(
                    R.plurals.recipe_count, recipes.size(), recipes.size()));
        });

        viewModel.getQuery().observe(getViewLifecycleOwner(), query -> {
            updateListTitle();
            if (!searchInput.getText().toString().equals(query)) {
                searchInput.setText(query);
                searchInput.setSelection(searchInput.getText().length());
            }
        });

        viewModel.getFilter().observe(getViewLifecycleOwner(), filter -> {
            updateListTitle();
            updateActiveFiltersUI(filter);
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                viewModel.setQuery(s.toString());
            }
        });

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                }
                searchInput.clearFocus();
                return true;
            }
            return false;
        });

        if (getArguments() != null && getArguments().getBoolean("focus_search", false)) {
            searchInput.post(() -> {
                searchInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    }

    private void updateListTitle() {
        String query = viewModel.getQuery().getValue();
        RecipeFilter filter = viewModel.getFilter().getValue();

        boolean isFiltering = (query != null && !query.trim().isEmpty())
                || (filter != null && filter.activeGroupCount() > 0);

        if (isFiltering) {
            textListTitle.setText(R.string.matching_recipes_title);
        } else {
            textListTitle.setText(R.string.all_recipes_title);
        }
    }

    private void updateActiveFiltersUI(RecipeFilter filter) {
        int activeGroups = filter.activeGroupCount();

        if (activeGroups > 0) {
            buttonFilters.setText(getString(R.string.filters_button_active, activeGroups));
        } else {
            buttonFilters.setText(R.string.filters_button);
        }

        if (activeGroups > 0) {
            activeFiltersContainer.setVisibility(View.VISIBLE);
            textActiveCount.setText(getResources().getQuantityString(
                    R.plurals.filters_applied_groups, activeGroups, activeGroups));
            buildChips(filter);
        } else {
            activeFiltersContainer.setVisibility(View.GONE);
            chipGroupActiveFilters.removeAllViews();
        }
    }

    private void buildChips(RecipeFilter filter) {
        chipGroupActiveFilters.removeAllViews();

        for (DishType d : filter.dishTypes) {
            addChip(DisplayNames.dishType(d), () -> {
                RecipeFilter copy = filter.copy();
                copy.dishTypes.remove(d);
                viewModel.setFilter(copy);
            });
        }

        for (DietType d : filter.dietTypes) {
            addChip(DisplayNames.dietType(d), () -> {
                RecipeFilter copy = filter.copy();
                copy.dietTypes.remove(d);
                viewModel.setFilter(copy);
            });
        }

        for (MealType m : filter.mealTypes) {
            addChip(DisplayNames.mealType(m), () -> {
                RecipeFilter copy = filter.copy();
                copy.mealTypes.remove(m);
                viewModel.setFilter(copy);
            });
        }

        for (PrepMethod m : filter.prepMethods) {
            addChip(DisplayNames.prepMethod(m), () -> {
                RecipeFilter copy = filter.copy();
                copy.prepMethods.remove(m);
                viewModel.setFilter(copy);
            });
        }

        for (Allergen a : filter.excludedAllergens) {
            addChip(DisplayNames.allergen(a), () -> {
                RecipeFilter copy = filter.copy();
                copy.excludedAllergens.remove(a);
                viewModel.setFilter(copy);
            });
        }

        if (filter.isTimeActive()) {
            String timeLabel = DisplayNames.formatTimeRange(filter.minMinutes, filter.maxMinutes);
            addChip(timeLabel, () -> {
                RecipeFilter copy = filter.copy();
                copy.minMinutes = RecipeFilter.DEFAULT_MIN_TIME;
                copy.maxMinutes = RecipeFilter.DEFAULT_MAX_TIME;
                viewModel.setFilter(copy);
            });
        }
    }

    private void addChip(String label, Runnable onRemove) {
        Chip chip = new Chip(requireContext(), null, com.google.android.material.R.attr.chipStyle);
        chip.setChipBackgroundColorResource(R.color.dust);
        chip.setTextColor(requireContext().getColor(R.color.ink));
        chip.setCloseIconTint(requireContext().getColorStateList(R.color.ink));

        chip.setText(label);
        chip.setCloseIconVisible(true);
        chip.setContentDescription(getString(R.string.remove_filter, label));
        chip.setOnCloseIconClickListener(v -> onRemove.run());

        chipGroupActiveFilters.addView(chip);
    }
}