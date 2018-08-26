package org.avm.lesson6.model.db;

import org.avm.lesson6.model.DrinkRealmObject;

import io.realm.RealmResults;

public interface IDataBaseManager {

    void saveNewDrink(String newDrinkName);

    RealmResults<DrinkRealmObject> getAllDrinks();

    RealmResults<DrinkRealmObject> getActiveDrinks();

    void setActiveDrink(String drinkName, long timeInMillis);

    void disableAllActiveDrinks();

}
