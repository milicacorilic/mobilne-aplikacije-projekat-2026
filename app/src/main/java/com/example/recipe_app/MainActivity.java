package com.example.recipe_app;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.recipe_app.ui.search.SearchViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = host.getNavController();

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        NavigationUI.setupWithNavController(bottomNav, navController);

        bottomNav.setOnItemSelectedListener(item -> {
            goTo(item.getItemId());
            return true;
        });
        bottomNav.setOnItemReselectedListener(item -> goTo(item.getItemId()));

        // Hamburger
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.top_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                goTo(item.getItemId());
                return true;
            });
            popup.show();
        });

        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            int destId = destination.getId();

            // Manage toolbar titles
            if (destId == R.id.homeFragment) {
                toolbar.setTitle(R.string.toolbar_title);
            } else {
                toolbar.setTitle(destination.getLabel());
            }

            // Hide/show bars on filters screen
            boolean isFilters = destId == R.id.filtersFragment;
            findViewById(R.id.appBar).setVisibility(isFilters ? View.GONE : View.VISIBLE);
            findViewById(R.id.bottom_nav).setVisibility(isFilters ? View.GONE : View.VISIBLE);

            // Search reset
            if (destId != R.id.searchFragment && destId != R.id.filtersFragment) {
                searchViewModel.clearAll();
            }
        });
    }

    private void goTo(int destinationId) {
        if (navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() == destinationId) {
            return;
        }
        if (!navController.popBackStack(destinationId, false)) {
            navController.navigate(destinationId);
        }
    }
}