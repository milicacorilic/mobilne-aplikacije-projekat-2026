package com.example.recipe_app.ui.common;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

/** Privremeni ekran za sve delove koji jos nisu napravljeni. */
public class Placeholder extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(requireContext());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        NavDestination dest = NavHostFragment.findNavController(this).getCurrentDestination();
        tv.setText(dest != null ? dest.getLabel() : "");
        return tv;
    }
}