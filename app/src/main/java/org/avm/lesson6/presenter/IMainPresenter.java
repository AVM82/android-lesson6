package org.avm.lesson6.presenter;

import android.app.AlarmManager;
import android.widget.ArrayAdapter;

import java.util.List;

public interface IMainPresenter {

    void saveNewDrinkToBase(String drinkName);

    List<String> getListDrinks();

    int getActiveDrinkPosition(ArrayAdapter<String> adapter);

    /**
     * Changes the status all of drinks to the inactive
     */
    void disableAllActiveDrinks();

    /**
     *Changes the status of the drink to the active
     *
     * @param drinkName name of drink which will be set as active
     * @return activation time
     */
    long setActiveDrink(String drinkName);

    void startNotification(int frequencyMinutes);

    void stopNotification();
}