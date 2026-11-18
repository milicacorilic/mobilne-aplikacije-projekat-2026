package com.example.recipe_app.util;

import android.content.Context;
import android.widget.ImageView;

import com.example.recipe_app.R;

public class UiUtils {
    public static void loadImage(ImageView iv, String imageName) {
        Context ctx = iv.getContext();
        int id = 0;
        if (imageName != null) {
            id = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        }
        iv.setImageResource(id != 0 ? id : R.drawable.placeholder_food);
    }
}
