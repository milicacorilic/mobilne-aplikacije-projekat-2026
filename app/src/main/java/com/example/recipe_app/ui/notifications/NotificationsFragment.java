package com.example.recipe_app.ui.notifications;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel viewModel;
    private NotificationsAdapter adapter;

    private TextView txtUnreadCount;
    private MaterialButton btnFilterAll;
    private MaterialButton btnFilterUnread;
    private RecyclerView rvNotifications;
    private TextView txtEmptyState;

    private boolean showUnreadOnly = false;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        txtUnreadCount = view.findViewById(R.id.txtUnreadCount);
        MaterialButtonToggleGroup toggleFilter = view.findViewById(R.id.toggleFilter);
        btnFilterAll = view.findViewById(R.id.btnFilterAll);
        btnFilterUnread = view.findViewById(R.id.btnFilterUnread);
        rvNotifications = view.findViewById(R.id.rvNotifications);
        txtEmptyState = view.findViewById(R.id.txtEmptyState);

        adapter = new NotificationsAdapter(notification -> {
            if (notification.isUnread()) {
                viewModel.markAsRead(notification);
                refreshUI();
            }
        });

        rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvNotifications.setAdapter(adapter);

        attachSwipeToDismiss();

        toggleFilter.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            showUnreadOnly = (checkedId == R.id.btnFilterUnread);
            updateToggleStyles();
            refreshUI();
        });

        updateToggleStyles();
        refreshUI();
    }

    private void attachSwipeToDismiss() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public int getSwipeDirs(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder
            ) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION || adapter.getNotificationAt(position) == null) {
                    return 0; // Disable swipe on Today / Yesterday headers
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target
            ) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                Notification item = adapter.getNotificationAt(position);
                if (item != null) {
                    viewModel.dismiss(item);
                    refreshUI();
                }
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rvNotifications);
    }

    private void updateToggleStyles() {
        int activeBg = ContextCompat.getColor(requireContext(), R.color.burnt_sienna);
        int activeText = Color.WHITE;
        int inactiveText = ContextCompat.getColor(requireContext(), R.color.ink);

        if (showUnreadOnly) {
            btnFilterUnread.setBackgroundTintList(ColorStateList.valueOf(activeBg));
            btnFilterUnread.setTextColor(activeText);
            btnFilterAll.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btnFilterAll.setTextColor(inactiveText);
        } else {
            btnFilterAll.setBackgroundTintList(ColorStateList.valueOf(activeBg));
            btnFilterAll.setTextColor(activeText);
            btnFilterUnread.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btnFilterUnread.setTextColor(inactiveText);
        }
    }

    private void refreshUI() {
        int unreadCount = viewModel.getUnreadCount();
        txtUnreadCount.setText(getString(R.string.notifications_unread_count, unreadCount));

        List<Notification> currentList = showUnreadOnly ? viewModel.getUnread() : viewModel.getAll();
        adapter.submitNotifications(currentList);

        if (currentList.isEmpty()) {
            rvNotifications.setVisibility(View.GONE);
            txtEmptyState.setVisibility(View.VISIBLE);
            txtEmptyState.setText(showUnreadOnly
                    ? R.string.notifications_empty_unread
                    : R.string.notifications_empty);
        } else {
            rvNotifications.setVisibility(View.VISIBLE);
            txtEmptyState.setVisibility(View.GONE);
        }
    }
}