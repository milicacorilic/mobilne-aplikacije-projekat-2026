package com.example.recipe_app.ui.notifications;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class Notification {

    private final String id;
    @DrawableRes private final int iconRes;
    @StringRes private final int titleRes;
    @StringRes private final int messageRes;
    @StringRes private final int timeRes;
    @StringRes private final int groupRes;
    private boolean unread;

    public Notification(
            String id,
            @DrawableRes int iconRes,
            @StringRes int titleRes,
            @StringRes int messageRes,
            @StringRes int timeRes,
            @StringRes int groupRes,
            boolean unread
    ) {
        this.id = id;
        this.iconRes = iconRes;
        this.titleRes = titleRes;
        this.messageRes = messageRes;
        this.timeRes = timeRes;
        this.groupRes = groupRes;
        this.unread = unread;
    }

    public String getId()      { return id; }
    public int getIconRes()    { return iconRes; }
    public int getTitleRes()   { return titleRes; }
    public int getMessageRes() { return messageRes; }
    public int getTimeRes()    { return timeRes; }
    public int getGroupRes()   { return groupRes; }
    public boolean isUnread()  { return unread; }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}