package com.example.recipe_app.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.google.android.material.textfield.TextInputEditText;

public class SearchFragment extends Fragment {

    private SearchRecipeAdapter adapter;
    private SearchViewModel viewModel;
    private TextView textListTitle;
    private TextView textRecipeCount;

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
        TextInputEditText searchInput = view.findViewById(R.id.searchInput);

        adapter = new SearchRecipeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Observe filtered results
        viewModel.getFilteredRecipes().observe(getViewLifecycleOwner(), recipes -> {
            adapter.setRecipes(recipes);
            textRecipeCount.setText(getResources().getQuantityString(
                    R.plurals.recipe_count, recipes.size(), recipes.size()));
        });

        viewModel.getQuery().observe(getViewLifecycleOwner(), query -> {
            updateListTitle();
            // Restore the text if navigating back to the screen
            if (!searchInput.getText().toString().equals(query)) {
                searchInput.setText(query);
                searchInput.setSelection(searchInput.getText().length());
            }
        });

        viewModel.getFilter().observe(getViewLifecycleOwner(), filter -> updateListTitle());

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                viewModel.setQuery(s.toString());
            }
        });

        // Handle the keyboard "Search" action to hide the keyboard
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

        // NEW: Check if we arrived here from the Home screen search bar
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
}