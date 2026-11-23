package com.example.recipe_app.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recipe_app.R;

import java.util.Set;

public class FilterSectionView<T extends Enum<T>> extends LinearLayout {

    public interface OnSelectionChangedListener {
        void onSelectionChanged();
    }

    private Set<T> selectedValues;
    private OnSelectionChangedListener listener;

    public FilterSectionView(Context context) {
        super(context);
        init(context);
    }

    public FilterSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_filter_section, this, true);
    }

    public void setup(String title, String helper, T[] values, String[] labels, Set<T> currentSelection, OnSelectionChangedListener listener) {
        this.selectedValues = currentSelection;
        this.listener = listener;

        TextView textTitle = findViewById(R.id.textSectionTitle);
        TextView textHelper = findViewById(R.id.textSectionHelper);
        GridLayout grid = findViewById(R.id.gridOptions);

        textTitle.setText(title);
        textHelper.setText(helper);
        grid.removeAllViews();

        for (int i = 0; i < values.length; i++) {
            T value = values[i];
            String label = labels[i];

            FrameLayout card = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_filter_option, grid, false);
            CheckBox checkbox = card.findViewById(R.id.checkbox);
            TextView textLabel = card.findViewById(R.id.textLabel);

            textLabel.setText(label);

            // Set initial state
            boolean isChecked = selectedValues.contains(value);
            checkbox.setChecked(isChecked);
            card.setSelected(isChecked);

            card.setOnClickListener(v -> {
                boolean newCheckedState = !checkbox.isChecked();
                checkbox.setChecked(newCheckedState);
                card.setSelected(newCheckedState);

                if (newCheckedState) {
                    selectedValues.add(value);
                } else {
                    selectedValues.remove(value);
                }

                if (this.listener != null) {
                    this.listener.onSelectionChanged();
                }
            });

            grid.addView(card);
        }
    }
}