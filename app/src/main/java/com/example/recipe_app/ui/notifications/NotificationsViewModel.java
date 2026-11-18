package com.example.recipe_app.ui.notifications;

import androidx.lifecycle.ViewModel;

import com.example.recipe_app.data.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private final NotificationRepository repo = new NotificationRepository();
    private final List<Notification> notifications = new ArrayList<>(repo.getNotifications());

    public List<Notification> getAll() {
        return new ArrayList<>(notifications);
    }

    public List<Notification> getUnread() {
        List<Notification> result = new ArrayList<>();
        for (Notification item : notifications) {
            if (item.isUnread()) {
                result.add(item);
            }
        }
        return result;
    }

    public int getUnreadCount() {
        int count = 0;
        for (Notification item : notifications) {
            if (item.isUnread()) {
                count++;
            }
        }
        return count;
    }

    public void markAsRead(Notification target) {
        for (Notification item : notifications) {
            if (item.getId().equals(target.getId())) {
                item.setUnread(false);
                break;
            }
        }
    }

    public void dismiss(Notification target) {
        notifications.removeIf(item -> item.getId().equals(target.getId()));
    }
}