package com.example.recipe_app;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordFragment extends Fragment {

    private static final String KEY_LINK_SENT = "link_sent";

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private View formLayout;
    private View successLayout;
    private TextView tvSuccessBody;

    private boolean linkSent = false;

    public ForgotPasswordFragment() {
        super(R.layout.fragment_forgot_password);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilEmail = view.findViewById(R.id.tilEmail);
        etEmail = view.findViewById(R.id.etEmail);
        formLayout = view.findViewById(R.id.formLayout);
        successLayout = view.findViewById(R.id.successLayout);
        tvSuccessBody = view.findViewById(R.id.tvSuccessBody);

        MaterialButton btnSendLink = view.findViewById(R.id.btnSendLink);
        btnSendLink.setOnClickListener(v -> sendResetLink());

        etEmail.setOnEditorActionListener((tv, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendResetLink();
                return true;
            }
            return false;
        });

        // remove error when user stops typing
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilEmail.setError(null);
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        view.findViewById(R.id.tvBackToLogin).setOnClickListener(this::goBackToLogin);
        view.findViewById(R.id.btnBackToLogin).setOnClickListener(this::goBackToLogin);

        view.findViewById(R.id.tvResend).setOnClickListener(v -> {
            // TODO: call backend for reset link resend
            Toast.makeText(requireContext(), R.string.reset_link_resent, Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.tvUseDifferentEmail).setOnClickListener(v -> showForm());

        // after screen rotation
        if (savedInstanceState != null && savedInstanceState.getBoolean(KEY_LINK_SENT, false)) {
            showSuccess(getEmail());
        }
    }

    private void sendResetLink() {
        String email = getEmail();

        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.error_required));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            return;
        }

        hideKeyboard();

        // send email with password reset
        showSuccess(email);
    }

    private void showSuccess(String email) {
        linkSent = true;
        tvSuccessBody.setText(getString(R.string.forgot_success_body, email));
        formLayout.setVisibility(View.GONE);
        successLayout.setVisibility(View.VISIBLE);
    }

    private void showForm() {
        linkSent = false;
        successLayout.setVisibility(View.GONE);
        formLayout.setVisibility(View.VISIBLE);
        etEmail.requestFocus();
    }

    private String getEmail() {
        return etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
    }

    private void goBackToLogin(View v) {
        Navigation.findNavController(v).navigateUp();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LINK_SENT, linkSent);
    }
}