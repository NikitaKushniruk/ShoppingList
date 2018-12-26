package presentation;

import android.support.annotation.NonNull;

import domain.MainView;

public class MainPresenter {
    private  MainView mainView;
    public void onAttach(MainView mainView){
        this.mainView = mainView;
    }
    public void onDetach(){
        mainView = null;
    }

}
