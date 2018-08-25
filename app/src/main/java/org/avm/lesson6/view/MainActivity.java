package org.avm.lesson6.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.avm.lesson6.R;
import org.avm.lesson6.presenter.IMainPresenter;
import org.avm.lesson6.presenter.MainPresenter;
import org.avm.lesson6.view.dialog.AddNewDrinkDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private static final int MESSAGE_FREQUENCY_MINUTES = 30;
    public static final int MILLISECOND = 1000;
    public static final int SECOND_IN_MINUTE = 60;

    private IMainPresenter mainPresenter;
    private Realm realm;
    private ArrayAdapter<String> adapter;

    @BindView(R.id.drink_list_spinner)
    Spinner drinkListSpinner;
    @BindView(R.id.tvClock)
    TextView tvClock;

    @OnClick(R.id.start_button)
    void onStartButtonClick() {
        String drinkName = drinkListSpinner.getSelectedItem().toString();
        mainPresenter.disableAllActiveDrinks();
        long activationTime = mainPresenter.setActiveDrink(drinkName);
        updateTimer(activationTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainPresenter = new MainPresenter(this);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                mainPresenter.getListDrinks());
        drinkListSpinner.setAdapter(adapter);
        int activeDrinkPosition = mainPresenter.getActiveDrinkPosition(adapter);
        drinkListSpinner.setSelection(activeDrinkPosition);
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
    public Realm getRealm() {
        return realm;
    }

    @Override
    public void updateTimer(long timeInMillis) {
        long nextAlert = getNextAlert(timeInMillis);
        long currentTime = getCurrentTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
        tvClock.setText(simpleDateFormat.format(nextAlert - currentTime));
    }

    private long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private long getNextAlert(long timeInMillis) {
        return timeInMillis + (MESSAGE_FREQUENCY_MINUTES * SECOND_IN_MINUTE * MILLISECOND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
