package org.avm.lesson6.model.db;

import org.avm.lesson6.model.DrinkRealmObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class DataBaseRealm implements IDataBaseManager {
    private Realm realm;


    public DataBaseRealm() {
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void saveNewDrink(String newDrinkName) {
        realm.executeTransaction(realm -> {
            DrinkRealmObject drinkRealmObject = realm.createObject(DrinkRealmObject.class);
            drinkRealmObject.setName(newDrinkName);
            drinkRealmObject.setActive(false);
            drinkRealmObject.setTimeLastStart(0);
        });
    }

    @Override
    public RealmResults<DrinkRealmObject> getAllDrinks() {
        return realm.where(DrinkRealmObject.class).findAll();
    }

    @Override
    public void setActiveDrink(String drinkName, long timeInMillis) {
        DrinkRealmObject drink = findDrinkByName(drinkName);
        realm.executeTransaction(realm -> {
            drink.setActive(true);
            drink.setTimeLastStart(timeInMillis);
        });
    }

    @Override
    public void disableAllActiveDrinks() {
        final RealmResults<DrinkRealmObject> activeDrinks = getActiveDrinks();
        realm.executeTransaction(realm -> {
            for (DrinkRealmObject drink : activeDrinks) {
                drink.setActive(false);
                drink.setTimeLastStart(0);
            }
        });
    }

    @Override
    public void close() {
        realm.close();
    }

    @Override
    public long getActiveDrinkTime(String nameDrink) {
        DrinkRealmObject drinkRealmObject = realm.where(DrinkRealmObject.class)
                .equalTo("active", true)
                .findFirst();
        return drinkRealmObject != null ? drinkRealmObject.getTimeLastStart() : 0;
    }

    private DrinkRealmObject findDrinkByName(String drinkName) {
        return realm.where(DrinkRealmObject.class)
                .equalTo("name", drinkName)
                .findFirst();
    }

    @Override
    public RealmResults<DrinkRealmObject> getActiveDrinks() {
        return realm.where(DrinkRealmObject.class)
                .equalTo("active", true)
                .findAll();
    }
}
