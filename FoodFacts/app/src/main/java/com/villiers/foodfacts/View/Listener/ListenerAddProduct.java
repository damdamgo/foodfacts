package com.villiers.foodfacts.View.Listener;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.villiers.foodfacts.R;
import com.villiers.foodfacts.Service.DB.ProductID;
import com.villiers.foodfacts.Service.Modele.CallbackAskProduct;
import com.villiers.foodfacts.Utils.Utils;
import com.villiers.foodfacts.ViewModel.ViewModelProduct;
import com.villiers.foodfacts.databinding.AddProductDialogBinding;


/**
 * Classe qui gère le dialog qui permet d'ajouter un produit
 * @author villiers
 */
public class ListenerAddProduct implements View.OnClickListener,CallbackAskProduct {
    /**
     * activity
     */
    private Activity activity;
    /**
     * modele
     */
    private ViewModelProduct vmProduct;
    /**
     * dialog
     */
    private Dialog d = null;
    /**
     * vue binding
     */
    private AddProductDialogBinding addProductDialogBinding;

    /**
     * constructeur
     * @param viewModelProduct
     * @param activity
     */
    public ListenerAddProduct(ViewModelProduct viewModelProduct, Activity activity){
        this.vmProduct = viewModelProduct;
        this.activity = activity;
    }

    /**
     * initialisation du dialog avec un binding sur la vue
     */
    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        addProductDialogBinding = DataBindingUtil.inflate(inflater, R.layout.add_product_dialog,null,false);
        addProductDialogBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductDialogBinding.setSearch(true);
                Log.w("click","dialog");
                EditText eT = (EditText) addProductDialogBinding.editTextBarcode;
                vmProduct.askProductBybarcode(eT.getText().toString(),ListenerAddProduct.this);
            }
        });
        builder.setView(addProductDialogBinding.getRoot());
        d = builder.create();
        addProductDialogBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.hide();
            }
        });
        d.setCancelable(false);
    }


    @Override
    public void onClick(View view) {
        initDialog();
        addProductDialogBinding.setSearch(false);
        d.show();
    }

    /**
     * erreur sur l'ajout d'un produit
     * @param code (code erreur)
     */
    @Override
    public void error(final int code) {
        Handler mainHandler = new Handler(activity.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                addProductDialogBinding.setSearch(false);
                addProductDialogBinding.setError(activity.getString(Utils.ERROR_TEXT.get(code)));
            }
        };
        mainHandler.post(myRunnable);
    }

    /**
     * Ajout réussi
     * @param product ProductID du nouveau produit
     */
    @Override
    public void success(ProductID product) {
        d.hide();
        vmProduct.setNewProduct(product);
    }

    /**
     * le produit existe déjà
     * @param product ProductID du produit déjà existant
     */
    @Override
    public void productExist(ProductID product) {
        d.hide();
        vmProduct.setProductToShow(product);
    }

    public Dialog getD() {
        return d;
    }
}
