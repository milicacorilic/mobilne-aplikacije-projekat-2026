package com.example.recipe_app.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipe_app.R;
import com.example.recipe_app.data.RecipeRepository;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.MealType;
import com.example.recipe_app.model.enums.PrepMethod;
import com.example.recipe_app.util.DisplayNames;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FiltersFragment extends Fragment {

    private SearchViewModel viewModel;
    private FiltersViewModel filtersViewModel;
    private RecipeFilter draftFilter;

    private MaterialButton buttonShowRecipes;
    private TextView textGroupsSelected;
    private LinearLayout sectionsContainer;
    private TextView textTimeRangeLabel;

    public FiltersFragment() {
        super(R.layout.fragment_filters);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        filtersViewModel = new ViewModelProvider(this).get(FiltersViewModel.class);

        if (filtersViewModel.draft == null) {
            RecipeFilter appliedFilter = viewModel.getFilter().getValue();
            filtersViewModel.draft = appliedFilter != null ? appliedFilter.copy() : new RecipeFilter();
        }
        draftFilter = filtersViewModel.draft;

        buttonShowRecipes = view.findViewById(R.id.buttonShowRecipes);
        textGroupsSelected = view.findViewById(R.id.textGroupsSelected);
        sectionsContainer = view.findViewById(R.id.sectionsContainer);

        view.findViewById(R.id.buttonClose).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        view.findViewById(R.id.buttonClearAll).setOnClickListener(v -> {
            draftFilter.clear();
            sectionsContainer.removeAllViews();
            buildSections();
            updateBottomBar();
        });

        buttonShowRecipes.setOnClickListener(v -> {
            viewModel.setFilter(draftFilter);
            NavHostFragment.findNavController(this).popBackStack();
        });

        buildSections();
        updateBottomBar();
    }

    private void buildSections() {
        // 1. Dish Type
        DishType[] dishValues = DishType.values();
        String[] dishLabels = new String[dishValues.length];
        for (int i = 0; i < dishValues.length; i++) {
            dishLabels[i] = DisplayNames.dishType(dishValues[i]);
        }
        addFilterSection(
                getString(R.string.filters_dish_type_title),
                getString(R.string.filters_dish_type_helper),
                dishValues, dishLabels, draftFilter.dishTypes);

        // 2. Diet Type (Excluding REGULAR)
        List<DietType> dietList = new ArrayList<>();
        for (DietType d : DietType.values()) {
            if (d != DietType.REGULAR) dietList.add(d);
        }
        DietType[] dietValues = dietList.toArray(new DietType[0]);
        String[] dietLabels = new String[dietValues.length];
        for (int i = 0; i < dietValues.length; i++) {
            dietLabels[i] = DisplayNames.dietType(dietValues[i]);
        }
        addFilterSection(
                getString(R.string.filters_diet_title),
                getString(R.string.filters_diet_helper),
                dietValues, dietLabels, draftFilter.dietTypes);

        // 3. Meal
        MealType[] mealValues = MealType.values();
        String[] mealLabels = new String[mealValues.length];
        for (int i = 0; i < mealValues.length; i++) {
            mealLabels[i] = DisplayNames.mealType(mealValues[i]);
        }
        addFilterSection(
                getString(R.string.filters_meal_title),
                getString(R.string.filters_meal_helper),
                mealValues, mealLabels, draftFilter.mealTypes);

        // 4. Prep Time (Inserted after Meal)
        addTimeSection();

        // 5. Prep Method
        PrepMethod[] methodValues = PrepMethod.values();
        String[] methodLabels = new String[methodValues.length];
        for (int i = 0; i < methodValues.length; i++) {
            methodLabels[i] = DisplayNames.prepMethod(methodValues[i]);
        }
        addFilterSection(
                getString(R.string.filters_method_title),
                getString(R.string.filters_method_helper),
                methodValues, methodLabels, draftFilter.prepMethods);

        // 6. Allergens
        Allergen[] allergenValues = Allergen.values();
        String[] allergenLabels = new String[allergenValues.length];
        for (int i = 0; i < allergenValues.length; i++) {
            allergenLabels[i] = DisplayNames.allergen(allergenValues[i]);
        }
        addFilterSection(
                getString(R.string.filters_allergens_title),
                getString(R.string.filters_allergens_helper),
                allergenValues, allergenLabels, draftFilter.excludedAllergens);
    }

    private <T extends Enum<T>> void addFilterSection(
            String title, String helper, T[] values, String[] labels, Set<T> currentSelection) {

        FilterSectionView<T> section = new FilterSectionView<>(requireContext());
        section.setup(title, helper, values, labels, currentSelection, this::updateBottomBar);
        sectionsContainer.addView(section);
    }

    private void addTimeSection() {
        View timeView = LayoutInflater.from(requireContext()).inflate(R.layout.view_filter_time_section, sectionsContainer, false);

        RangeSlider slider = timeView.findViewById(R.id.rangeSlider);
        textTimeRangeLabel = timeView.findViewById(R.id.textTimeRangeLabel);

        // Format the bubble labels on the thumbs
        slider.setLabelFormatter(v -> DisplayNames.time(Math.round(v)));

        // Set initial values from the draft
        List<Float> values = new ArrayList<>();
        values.add((float) draftFilter.minMinutes);
        values.add((float) draftFilter.maxMinutes);
        slider.setValues(values);

        updateTimeLabel(draftFilter.minMinutes, draftFilter.maxMinutes);

        slider.addOnChangeListener((rangeSlider, value, fromUser) -> {
            List<Float> currentValues = rangeSlider.getValues();
            int min = Math.round(currentValues.get(0));
            int max = Math.round(currentValues.get(1));

            draftFilter.minMinutes = min;
            draftFilter.maxMinutes = max;

            updateTimeLabel(min, max);
            updateBottomBar();
        });

        sectionsContainer.addView(timeView);
    }

    private void updateTimeLabel(int min, int max) {
        if (min == RecipeFilter.DEFAULT_MIN_TIME && max == RecipeFilter.DEFAULT_MAX_TIME) {
            textTimeRangeLabel.setText(R.string.filters_time_any);
        } else {
            String minStr = DisplayNames.time(min);
            // Show "2 hr+" if the upper limit is at the default maximum
            String maxStr = (max == RecipeFilter.DEFAULT_MAX_TIME) ? "2 hr+" : DisplayNames.time(max);
            textTimeRangeLabel.setText(minStr + " – " + maxStr);
        }
    }

    private void updateBottomBar() {
        int groups = draftFilter.activeGroupCount();
        textGroupsSelected.setText(getResources().getQuantityString(
                R.plurals.filters_groups_selected, groups, groups));

        List<Recipe> filteredList = RecipeFilterEngine.apply(
                RecipeRepository.getInstance().getAllRecipes(),
                viewModel.getQuery().getValue(),
                draftFilter);

        int count = filteredList.size();
        buttonShowRecipes.setText(getResources().getQuantityString(
                R.plurals.filters_show_button, count, count));
    }
}