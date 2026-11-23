package com.example.recipe_app;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.recipe_app.ui.search.SearchViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

        bottomNav.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.searchFragment) {
                searchViewModel.clearAll();
            }
            goTo(item.getItemId());
        });

        // Bocni meni (Teammate's Drawer)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        navView.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            goTo(item.getItemId());
            return true;
        });

        OnBackPressedCallback closeDrawerOnBack = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                drawer.closeDrawer(GravityCompat.START);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, closeDrawerOnBack);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                closeDrawerOnBack.setEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                closeDrawerOnBack.setEnabled(false);
            }
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

            // Centralized Search Reset (When navigating to other tabs)
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