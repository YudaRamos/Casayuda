package com.afundacionfp.casayuda.screens.login;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.afundacionfp.casayuda.R;

//LoginActivity_AboutDialog

public class LA_AboutDialog extends DialogFragment {

    public static LA_AboutDialog newInstance(String title) {
        LA_AboutDialog aboutDialog = new LA_AboutDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        aboutDialog.setArguments(args);
        return aboutDialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_dialog, container);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
    }

}



