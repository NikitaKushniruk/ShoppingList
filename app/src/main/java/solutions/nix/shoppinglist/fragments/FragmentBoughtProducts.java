package solutions.nix.shoppinglist.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import domain.BoughtProductsRVAdaper;
import domain.BoughtProductsView;
import entity.Product;
import presentation.BoughtProductsPresenter;
import solutions.nix.shoppinglist.R;

public class FragmentBoughtProducts extends Fragment implements BoughtProductsView {
    View v;
    BoughtProductsPresenter boughtProductsPresenter;
    private BoughtProductsRVAdaper adapter;
    private LinearLayoutCompat bottomMenu;
    private ImageButton back;
    private ImageButton selectAll;
    private ImageButton move;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bought_products_fragment,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        boughtProductsPresenter = new BoughtProductsPresenter( );
        boughtProductsPresenter.onAttach(this);
        bottomMenu = getView().findViewById(R.id.bottom_menu);
        move = getView().findViewById(R.id.move);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boughtProductsPresenter.onMoveButtonClicked();
            }
        });
        back = getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boughtProductsPresenter.onClickBack();
            }
        });
        selectAll = getView().findViewById(R.id.select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boughtProductsPresenter.onClicSelectAll();
            }
        });
        move = getView().findViewById(R.id.move);
    }

    @Override
    public void onDestroyView() {
        boughtProductsPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void initProductsList(ArrayList<Product> arrayList) {
        RecyclerView recyclerView = getView().findViewById(R.id.bought_products_rv);
        adapter = new BoughtProductsRVAdaper(arrayList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void showBottomMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomMenu() {
        bottomMenu.setVisibility(View.INVISIBLE);
    }

    @Override
    public void notifyLongTouchModeEnabled(){
        boughtProductsPresenter.notifyLongTouchModeEnabled();
    }
    @Override
    public void notifyBackButtonClicked() {
        adapter.notifyBackButtonClicked();
    }

    @Override
    public void notifySetAllButtonClicked() {
        adapter.notifySetAllButtonClicked();
    }

    @Override
    public void notifyMoveButtonClicked() {
        adapter.notifyMoveButtonClicked();
    }

    @Override
    public void notifyLongTouchModeDisabled(){
        bottomMenu.setVisibility(View.INVISIBLE);
        boughtProductsPresenter.notifyLongTouchModeDisabled();
    }
}
