package org.avm.lesson6.presenter;

import android.widget.ArrayAdapter;

import org.avm.lesson6.model.DrinkRealmObject;
import org.avm.lesson6.model.NotificationBroadcastReceiver;
import org.avm.lesson6.model.db.DataBaseRealm;
import org.avm.lesson6.model.db.IDataBaseManager;
import org.avm.lesson6.view.IMainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;
import timber.log.Timber;

public class MainPresenter implements IMainPresenter {

    private static final int FIRST_POSITION_INDEX = 0;

    private IMainActivity mainActivity;
    private IDataBaseManager dataBaseManager;

    public MainPresenter(IMainActivity mainActivity) {
        this.mainActivity = mainActivity;
        dataBaseManager = new DataBaseRealm();
    }

    @Override
    public void saveNewDrinkToBase(String drinkName) {
        dataBaseManager.saveNewDrink(drinkName);
        Timber.d("New drink was saved in database");
    }

    @Override
    public List<String> getListDrinks() {
        RealmResults<DrinkRealmObject> drinks = dataBaseManager.getAllDrinks();
        ArrayList<String> drinkNames = new ArrayList<>(drinks.size());
        for (int i = 0; i < drinks.size(); i++) {
            drinkNames.add(drinks.get(i).getName());
        }
        return drinkNames;
    }

    @Override
    public int getActiveDrinkPosition(ArrayAdapter<String> adapter) {
        RealmResults<DrinkRealmObject> activeDrinks = dataBaseManager.getActiveDrinks();
        return activeDrinks.size() > 0 ? adapter.getPosition(activeDrinks.get(FIRST_POSITION_INDEX)
                .getName()) : FIRST_POSITION_INDEX;
    }

    @Override
    public long setActiveDrink(String drinkName) {
        Timber.d("%s is now active", drinkName);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        dataBaseManager.setActiveDrink(drinkName, timeInMillis);
        return timeInMillis;
    }

    @Override
    public void startNotification() {
        NotificationBroadcastReceiver.scheduledNotification(mainActivity.getContext());
    }

    @Override
    public void stopNotification() {
        NotificationBroadcastReceiver.unscheduledNotification(mainActivity.getContext());
    }

    @Override
    public void closeDatabase() {
        dataBaseManager.close();
    }

    @Override
    public void disableAllActiveDrinks() {
        dataBaseManager.disableAllActiveDrinks();
        stopNotification();
        Timber.d("All active drinks were turned off.");
    }



}