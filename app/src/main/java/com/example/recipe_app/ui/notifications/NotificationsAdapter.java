package com.example.recipe_app.ui.notifications;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private static final Object FOOTER_MARKER = new Object();

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    private final List<Object> items = new ArrayList<>();
    private final OnNotificationClickListener listener;

    public NotificationsAdapter(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    public void submitNotifications(List<Notification> notifications) {
        items.clear();
        int lastGroupRes = 0;
        for (Notification notification : notifications) {
            if (notification.getGroupRes() != lastGroupRes) {
                lastGroupRes = notification.getGroupRes();
                items.add(lastGroupRes);
            }
            items.add(notification);
        }
        if (!notifications.isEmpty()) {
            items.add(FOOTER_MARKER);
        }
        notifyDataSetChanged();
    }

    public Notification getNotificationAt(int position) {
        Object item = items.get(position);
        return item instanceof Notification ? (Notification) item : null;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Integer) {
            return VIEW_TYPE_HEADER;
        } else if (item == FOOTER_MARKER) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_notification_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_notification_footer, parent, false);
            return new FooterViewHolder(view);
        }
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            @StringRes int headerRes = (Integer) items.get(position);
            ((HeaderViewHolder) holder).bind(headerRes);
        } else if (holder instanceof NotificationViewHolder) {
            Notification notification = (Notification) items.get(position);
            ((NotificationViewHolder) holder).bind(notification, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtHeader;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.txtHeader);
        }

        void bind(@StringRes int headerRes) {
            txtHeader.setText(headerRes);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardNotification;
        private final ImageView imgIcon;
        private final TextView txtTitle;
        private final TextView txtMessage;
        private final TextView txtTime;
        private final View viewUnreadDot;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNotification = itemView.findViewById(R.id.cardNotification);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            viewUnreadDot = itemView.findViewById(R.id.viewUnreadDot);
        }

        void bind(Notification item, OnNotificationClickListener listener) {
            Context context = itemView.getContext();
            imgIcon.setImageResource(item.getIconRes());
            txtTitle.setText(item.getTitleRes());
            txtMessage.setText(item.getMessageRes());
            txtTime.setText(item.getTimeRes());

            if (item.isUnread()) {
                cardNotification.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.notification_card_unread));
                txtTitle.setTypeface(null, Typeface.BOLD);
                viewUnreadDot.setVisibility(View.VISIBLE);
            } else {
                cardNotification.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.notification_card_read));
                txtTitle.setTypeface(null, Typeface.NORMAL);
                viewUnreadDot.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNotificationClick(item);
                }
            });
        }
    }
}