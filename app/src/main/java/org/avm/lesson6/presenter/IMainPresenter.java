package org.avm.lesson6.presenter;

import android.widget.ArrayAdapter;

import java.util.List;

public interface IMainPresenter {

    void saveNewDrinkToBase(String drinkName);

    List<String> getListDrinks();

    int getActiveDrinkPosition(ArrayAdapter<String> adapter);

    void setActiveDrink(String drinkName);

}