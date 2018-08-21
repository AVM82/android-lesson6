package org.avm.lesson6.presenter;

import org.avm.lesson6.model.DrinkRealmObject;
import org.avm.lesson6.view.IMainActivity;

import io.realm.Realm;

public class MainPresenter implements IMainPresenter {

    private IMainActivity mainActivity;

    public MainPresenter(IMainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void saveNewDrinkToBase(String drinkName) {
        Realm realm = mainActivity.getRealm();
        realm.beginTransaction();
        DrinkRealmObject drinkRealmObject = realm.createObject(DrinkRealmObject.class);
        drinkRealmObject.setName(drinkName);
        drinkRealmObject.setActive(false);
        drinkRealmObject.setTimeLastStart(0);
        realm.commitTransaction();
    }
}