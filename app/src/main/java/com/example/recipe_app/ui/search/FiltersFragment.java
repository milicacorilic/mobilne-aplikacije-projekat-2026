package com.example.recipe_app.ui.search;

import android.os.Bundle;
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
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.util.DisplayNames;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FiltersFragment extends Fragment {

    private SearchViewModel viewModel;
    private FiltersViewModel filtersViewModel;
    private RecipeFilter draftFilter;

    private MaterialButton buttonShowRecipes;
    private TextView textGroupsSelected;
    private LinearLayout sectionsContainer;

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
            // Rebuild the UI to uncheck all boxes
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
        FilterSectionView<DishType> dishSection = new FilterSectionView<>(requireContext());
        DishType[] dishValues = DishType.values();
        String[] dishLabels = new String[dishValues.length];
        for (int i = 0; i < dishValues.length; i++) {
            dishLabels[i] = DisplayNames.dishType(dishValues[i]);
        }

        dishSection.setup(
                getString(R.string.filters_dish_type_title),
                getString(R.string.filters_dish_type_helper),
                dishValues,
                dishLabels,
                draftFilter.dishTypes,
                this::updateBottomBar
        );

        sectionsContainer.addView(dishSection);
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