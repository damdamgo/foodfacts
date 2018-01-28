package com.villiers.foodfacts.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;

import com.villiers.foodfacts.Service.DB.ProductID;
import com.villiers.foodfacts.Service.Modele.CallbackAskProduct;
import com.villiers.foodfacts.Service.Modele.ProductsManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import static com.villiers.foodfacts.Utils.AppConfiguration.NUMBER_PRODUCTS_LOADED;

/**
 * Vue modele de l'application
 */
public class ViewModelProduct extends ViewModel{
    /**
     * liste des produits
     */
    private MutableLiveData<List<ProductID>> listProducts = new MutableLiveData<>();
    /**
     * produit sélectionné
     */
    private MutableLiveData<ProductID> product = new MutableLiveData<>();
    /**
     * modele de l'application
     */
    private ProductsManager productsManager = null;
    /**
     * nombre de produits chargés
     */
    int loadedNumber = 0;
    /**
     * boolean pour connaitre si il y a d'autre produit à charger
     */
    private ObservableBoolean stillDataToLoad = new ObservableBoolean(true);

    /**
     * constructeur
     */
    public ViewModelProduct(){

    }

    /**
     * getter de la liste des produits
     * @return
     */
    public MutableLiveData<List<ProductID>> getListProducts() {
        return listProducts;
    }

    /**
     * set up du vue modele
     * @param context
     */
    public void setUp(Context context){
        if(productsManager==null){
            productsManager = new ProductsManager(context);
            listProducts.setValue(new LinkedList<ProductID>());
            product.setValue(null);
            loadMoreData();
        }
    }

    /**
     * chargement des produits
     */
    public void loadMoreData(){
        List<ProductID> moreData =  productsManager.getProducts(loadedNumber,NUMBER_PRODUCTS_LOADED);
        List<ProductID> existingData = listProducts.getValue();
        existingData.addAll(moreData);
        if(moreData.size()>0)listProducts.setValue(existingData);
        loadedNumber += NUMBER_PRODUCTS_LOADED;
        if(moreData.size()<NUMBER_PRODUCTS_LOADED)stillDataToLoad.set(false);
    }

    /**
     * setter du produit à afficher
     * @param productID
     */
    public void setProductToShow(ProductID productID){
        product.setValue(productID);
    }

    /**
     * insérer un nouveau produit
     * @param productID
     */
    public void setNewProduct(final ProductID productID)
    {
       List<ProductID> li = listProducts.getValue();
       li.add(0,productID);
       listProducts.setValue(li);
       setProductToShow(productID);
       new ObserverImage(productID);
        loadedNumber++;
    }

    /**
     * recuperer le produit sélectionné
     * @return
     */
    public MutableLiveData<ProductID> getProduct() {
        return product;
    }

    /**
     * Demander un produit par son code barre
     * @param barcodeNumber
     * @param callbackAskProduct
     */
    public void askProductBybarcode(final String barcodeNumber, final CallbackAskProduct callbackAskProduct){
        productsManager.getProductBycodebarNumber(barcodeNumber,callbackAskProduct);
    }

    /**
     * recuperer l'image du produit
     * @param productID
     * @return
     */
    public Bitmap getBitmapByProductID(ProductID productID) {
        return productsManager.getBitmapByProductID(productID);
    }

    /**
     * classe qui permet d'observer l'enregistrement de l'image pour pouvoir ensuite l'afficher
     */
    private class ObserverImage implements Observer{
        private ProductID productID;

        public ObserverImage(ProductID productID) {
            this.productID = productID;
            productID.addObserver(this);
        }

        @Override
        public void update(java.util.Observable observable, Object o) {
            if(product.getValue().getID().equals(productID.getID())){
                product.setValue(productID);
            }
            removeMySelf();
        }

        private void removeMySelf(){
            productID.deleteObserver(this);
            productID=null;
        }
    }

    public ObservableBoolean getStillDataToLoad() {
        return stillDataToLoad;
    }
}
