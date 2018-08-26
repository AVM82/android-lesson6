package org.avm.lesson6.presenter;

import android.widget.ArrayAdapter;

import org.avm.lesson6.model.DrinkRealmObject;
import org.avm.lesson6.model.NotificationBroadcastReceiver;
import org.avm.lesson6.view.IMainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;
import timber.log.Timber;

public class MainPresenter implements IMainPresenter {

    private static final int FIRST_POSITION_INDEX = 0;
    private IMainActivity mainActivity;

    public MainPresenter(IMainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    //TODO create DataBaseManager
    @Override
    public void saveNewDrinkToBase(String drinkName) {
        mainActivity.getRealm().executeTransaction(realm -> {
            DrinkRealmObject drinkRealmObject = realm.createObject(DrinkRealmObject.class);
            drinkRealmObject.setName(drinkName);
            drinkRealmObject.setActive(false);
            drinkRealmObject.setTimeLastStart(0);
        });
    }

    @Override
    public List<String> getListDrinks() {
        final RealmResults<DrinkRealmObject> drinks = mainActivity.getRealm()
                .where(DrinkRealmObject.class)
                .findAll();
        ArrayList<String> drinkNames = new ArrayList<>(drinks.size());
        for (int i = 0; i < drinks.size(); i++) {
            drinkNames.add(drinks.get(i).getName());
        }
        return drinkNames;
    }

    @Override
    public int getActiveDrinkPosition(ArrayAdapter<String> adapter) {
        final RealmResults<DrinkRealmObject> activeDrinks = getActiveDrinks();
        return activeDrinks.size() > 0 ? adapter.getPosition(activeDrinks.get(FIRST_POSITION_INDEX)
                .getName()) : FIRST_POSITION_INDEX;
    }

    @Override
    public long setActiveDrink(String drinkName) {
        DrinkRealmObject drink = findDrinkByName(drinkName);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        mainActivity.getRealm().executeTransaction(realm -> {
            drink.setActive(true);
            drink.setTimeLastStart(timeInMillis);
        });
        Timber.d("%s is now active", drinkName);
        return timeInMillis;
    }

    @Override
    public void startNotification(int frequencyMinutes) {
        NotificationBroadcastReceiver.scheduledNotification(mainActivity.getContext(), frequencyMinutes);
        Timber.d("A notification has been successfully installed");
    }

    @Override
    public void stopNotification() {
        NotificationBroadcastReceiver.unscheduledNotification(mainActivity.getContext());
        Timber.d("A notification has been successfully stopped");
    }

    @Override
    public void disableAllActiveDrinks() {
        final RealmResults<DrinkRealmObject> activeDrinks = getActiveDrinks();
        mainActivity.getRealm().executeTransaction(realm -> {
            for (DrinkRealmObject drink: activeDrinks) {
                drink.setActive(false);
            }
        });
        Timber.d("All active drinks were turned off.");
    }

    private RealmResults<DrinkRealmObject> getActiveDrinks() {
        return mainActivity.getRealm()
                .where(DrinkRealmObject.class)
                .equalTo("active", true)
                .findAll();
    }

    private DrinkRealmObject findDrinkByName(String drinkName) {
        return mainActivity.getRealm()
                .where(DrinkRealmObject.class)
                .equalTo("name", drinkName)
                .findFirst();
    }
}