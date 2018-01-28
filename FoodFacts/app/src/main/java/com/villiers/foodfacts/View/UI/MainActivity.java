package com.villiers.foodfacts.View.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.villiers.foodfacts.R;
import com.villiers.foodfacts.Service.DB.ProductID;
import com.villiers.foodfacts.View.Listener.ListenerAddProduct;
import com.villiers.foodfacts.ViewModel.ViewModelProduct;


/**
 * vue principale
 */
public class MainActivity extends AppCompatActivity {

    /**
     * vue en mode portrait
     */
    private View productShow;
    /**
     * modele
     */
    private ViewModelProduct model;
    /**
     * floating button
     */
    private FloatingActionButton addProduct;
    /**
     * listener
     */
    private ListenerAddProduct vmAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = ViewModelProviders.of(this).get(ViewModelProduct.class);
        model.setUp(getApplicationContext());

        setUpFloatButton(model);

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){

            setUpPortraitMode(model);
        }
    }

    /**
     * mis en place d'un observer si la device est en mode portrait
     * @param model
     */
    private void setUpPortraitMode(final ViewModelProduct model) {
        productShow = findViewById(R.id.containsShowProduct);
        model.getProduct().observe(this, new Observer<ProductID>() {
            @Override
            public void onChanged(@Nullable ProductID productID) {
                if(productID!=null){
                    productShow.setVisibility(View.VISIBLE);
                    addProduct.setVisibility(View.GONE);
                }
                else {
                    productShow.setVisibility(View.GONE);
                    addProduct.setVisibility(View.VISIBLE);
                }
            }
        });
        productShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setProductToShow(null);
            }
        });
    }

    /**
     * listener sur le floating button
     * @param model
     */
    private void setUpFloatButton(ViewModelProduct model) {
        addProduct = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddProduct);
        vmAdd = new ListenerAddProduct(model,this);
        addProduct.setOnClickListener(vmAdd);
    }

    /**
     * permet la gestion du retour en mode portrait
     */
    @Override
    public void onBackPressed() {
        if(productShow.getVisibility()==View.VISIBLE){
            model.setProductToShow(null);
        }
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(vmAdd.getD()!=null)vmAdd.getD().dismiss();
        super.onDestroy();
    }
}
