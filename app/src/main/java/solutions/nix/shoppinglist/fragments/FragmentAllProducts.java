package solutions.nix.shoppinglist.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import dagger.Module;
import dagger.Provides;
import domain.AllProductsView;
import domain.AllProductsRVAdaper;
import entity.Product;
import presentation.AllProductsPresenter;
import solutions.nix.shoppinglist.R;

import static android.app.Activity.RESULT_OK;

public class FragmentAllProducts extends Fragment implements AllProductsView {

    private ImageView imageView;
    private AllProductsPresenter allProductsPresenter;
    private AllProductsRVAdaper adapter;
    private Bitmap photoBitmap;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 2;
    private static final int RESULT_LOAD_IMAGE = 3;
    private AlertDialog alertDialog;
    private LinearLayoutCompat bottomMenu;
    private ImageButton back;
    private ImageButton selectAll;
    private ImageButton move;

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all_products_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        allProductsPresenter = new AllProductsPresenter();
        allProductsPresenter.onAttach(this);
        bottomMenu = getView().findViewById(R.id.bottom_menu);
        move = getView().findViewById(R.id.move);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsPresenter.onMoveButtonClicked();
            }
        });
        back = getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsPresenter.onClickBack();
            }
        });
        selectAll = getView().findViewById(R.id.select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsPresenter.onClicSelectAll();
            }
        });
        move = getView().findViewById(R.id.move);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsPresenter.onClickFab();
            }
        });
    }
    @Override
    public void creatAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add new item");
        final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_item_layout, null);

        final EditText nameInput = (EditText) viewInflated.findViewById(R.id.name);
        final EditText descriptionInput = (EditText) viewInflated.findViewById(R.id.description);
        final EditText priceInput = (EditText) viewInflated.findViewById(R.id.price);
        Button cameraButton = (Button) viewInflated.findViewById(R.id.cameraButton);
        Button galleryButton = (Button) viewInflated.findViewById(R.id.galleryButton);
        imageView = (ImageView) viewInflated.findViewById(R.id.imageView);
        photoBitmap = null;

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               allProductsPresenter.onClickCameraButton();
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               allProductsPresenter.onClickGalleryButton();
            }
        });

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel,null);
        alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String price = priceInput.getText().toString();
                Bitmap photo = photoBitmap;

                allProductsPresenter.checkProduct(name, description, price, photo);
                }
            });
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void creatEmptyLineAlertToast() {
        Snackbar.make(Objects.requireNonNull(getView()), getResources().getString(R.string.fill_all), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void creatEmptyBitmapAlertToast() {
        Snackbar.make(Objects.requireNonNull(getView()), getResources().getString(R.string.choose_photo), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    @Override
    public void dismissAlertDialog(){
        alertDialog.dismiss();
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
    public void onDestroyView() {
        allProductsPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void initProductsList(ArrayList<Product> arrayList) {
        RecyclerView recyclerView = getView().findViewById(R.id.all_products_rv);
        adapter = new AllProductsRVAdaper(arrayList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    @Override
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void openImage() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(getContext(), R.string.access_denied, Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                } else {
                    Toast.makeText(getContext(), R.string.access_denied, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photoBitmap = Bitmap.createScaledBitmap((Bitmap) extras.get("data"), 150, 150, false);
            imageView.setImageBitmap(photoBitmap);
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap extras = null;
            try {
                extras = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            photoBitmap = Bitmap.createScaledBitmap((Bitmap) extras, 150, 150, false);
            imageView.setImageBitmap(photoBitmap);
        }
    }
    @Override
    public void notifyLongTouchModeEnabled(){
        allProductsPresenter.notifyLongTouchModeEnabled();
    }

    @Override
    public void hideFloatingButton() {
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFloatingButton() {
        fab.setVisibility(View.VISIBLE);
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
        allProductsPresenter.notifyLongTouchModeDisabled();
    }
}
