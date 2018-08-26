package org.avm.lesson6.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.avm.lesson6.R;
import org.avm.lesson6.service.NotificationBroadcastReceiver;
import org.avm.lesson6.Util;
import org.avm.lesson6.presenter.IMainPresenter;
import org.avm.lesson6.presenter.MainPresenter;
import org.avm.lesson6.view.dialog.AddNewDrinkDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private IMainPresenter mainPresenter;
    private ArrayAdapter<String> adapter;

    @BindView(R.id.drink_list_spinner)
    Spinner drinkListSpinner;

    @BindView(R.id.tvClock)
    TextView tvClock;

    @BindView(R.id.start_button)
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainPresenter = new MainPresenter(this);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                mainPresenter.getListDrinks());
        drinkListSpinner.setAdapter(adapter);
        int activeDrinkPosition = mainPresenter.getActiveDrinkPosition(adapter);
        drinkListSpinner.setSelection(activeDrinkPosition);
        if(!adapter.isEmpty()) {
            setTimer(drinkListSpinner.getSelectedItem().toString());
            startButton.setEnabled(true);
        }
    }

    @OnClick(R.id.start_button)
    void onStartButtonClick() {
        Timber.d("Start button was clicked");
        String drinkName = drinkListSpinner.getSelectedItem().toString();
        mainPresenter.disableAllActiveDrinks();
        long activationTime = mainPresenter.setActiveDrink(drinkName);
        mainPresenter.startNotification();
        updateTimer(activationTime);
    }

    @OnClick(R.id.stop_button)
    void onStopButtonClick() {
        Timber.d("Stop button was clicked");
        mainPresenter.stopNotification();
        resetTimer();

    }

    private void setTimer(String nameDrink) {
        long lastTimeActive = mainPresenter.getLastTimeNotification(nameDrink);
        if (lastTimeActive == 0) {
            resetTimer();
        } else {
            updateTimer(lastTimeActive);
        }
    }

    private void resetTimer() {
        Timber.d("Reset timer");
        tvClock.setText(getString(R.string.def_value_clock));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_drink_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("Show dialog to add new drink");
        AddNewDrinkDialog addNewDrinkDialog = new AddNewDrinkDialog();
        addNewDrinkDialog.setOnDialogListener(nameOfDrink -> {
            mainPresenter.saveNewDrinkToBase(nameOfDrink);
            adapter.add(nameOfDrink);
            adapter.notifyDataSetChanged();
        });
        addNewDrinkDialog.show(getSupportFragmentManager(), "addNewDrink");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTimer(long timeInMillis) {
        long nextAlert = getNextAlert(timeInMillis);
        long currentTime = getCurrentTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
        tvClock.setText(simpleDateFormat.format(nextAlert - currentTime));
//        Clock clock = new Clock();
        Timber.d("Timer on view was updated");
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    private long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private long getNextAlert(long timeInMillis) {
        return timeInMillis + Util.convertMinToMillis(NotificationBroadcastReceiver.MESSAGE_FREQUENCY_MINUTES);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.closeDatabase();
    }

}
