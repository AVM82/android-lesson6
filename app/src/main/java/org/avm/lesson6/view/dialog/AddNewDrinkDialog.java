package org.avm.lesson6.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.avm.lesson6.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class AddNewDrinkDialog extends DialogFragment {

    public interface DialogListener {
        void onClickYesButton(String nameOfDrink);
    }

    private Unbinder unbinder;
    private DialogListener dialogListener;

    @BindView(R.id.name_of_drink)
    TextInputEditText nameOfDrink;

    @OnClick({R.id.close_dialog, R.id.button_no})
    void onClickCloseDialog() {
        Timber.d("onClickCloseDialog() handler was called");
        dismiss();
    }

    @OnClick(R.id.button_yes)
    void onClickYesButton() {
        dialogListener.onClickYesButton(nameOfDrink.getText().toString());
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        return builder.setView(layoutInflater.inflate(R.layout.dialog_add_new_drink, null)).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("AddNewDrinkDialog onStart() handler was called");
        unbinder = ButterKnife.bind(this, getDialog());
    }

    public void setOnDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onDestroyView() {
        Timber.d("AddNewDrinkDialog onDestroyView() handler was called");
        super.onDestroyView();
        unbinder.unbind();

    }
}
