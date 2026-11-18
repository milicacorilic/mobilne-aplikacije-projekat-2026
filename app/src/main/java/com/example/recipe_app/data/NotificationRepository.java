package com.example.recipe_app.data;

import com.example.recipe_app.R;
import com.example.recipe_app.ui.notifications.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {

    public List<Notification> getNotifications() {
        List<Notification> list = new ArrayList<>();

        list.add(new Notification(
                "1",
                R.drawable.ic_comment,
                R.string.notif_comment_title,
                R.string.notif_comment_message,
                R.string.notif_comment_time,
                R.string.notifications_group_today,
                true
        ));
        list.add(new Notification(
                "2",
                R.drawable.ic_favorite_border,
                R.string.notif_favorite_title,
                R.string.notif_favorite_message,
                R.string.notif_favorite_time,
                R.string.notifications_group_today,
                true
        ));
        list.add(new Notification(
                "3",
                R.drawable.ic_live,
                R.string.notif_live_title,
                R.string.notif_live_message,
                R.string.notif_live_time,
                R.string.notifications_group_today,
                false
        ));
        list.add(new Notification(
                "4",
                R.drawable.ic_rating,
                R.string.notif_rating_title,
                R.string.notif_rating_message,
                R.string.notif_rating_time,
                R.string.notifications_group_yesterday,
                false
        ));
        list.add(new Notification(
                "5",
                R.drawable.ic_suggestion,
                R.string.notif_suggestion_title,
                R.string.notif_suggestion_message,
                R.string.notif_suggestion_time,
                R.string.notifications_group_yesterday,
                false
        ));

        return list;
    }
}