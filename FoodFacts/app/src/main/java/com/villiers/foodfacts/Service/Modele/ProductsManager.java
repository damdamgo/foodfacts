package com.villiers.foodfacts.Service.Modele;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.villiers.foodfacts.Service.API.OpenFoodFactsService;
import com.villiers.foodfacts.Service.API.ProductDeserializer;
import com.villiers.foodfacts.Service.DB.DBManager;
import com.villiers.foodfacts.Service.DB.ProductID;
import com.villiers.foodfacts.Utils.AppConfiguration;
import com.villiers.foodfacts.Utils.SaveProductImage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Classe qui gére l'ensemble des interactions possible (API, BDD, Modèle)
 * @author villiers
 */
public class ProductsManager {
    /**
     * API
     */
    private OpenFoodFactsService openFoodFactsService = null;
    /**
     * context
     */
    private Context context;

    /**
     * Constructeur
     * @param context
     */
    public ProductsManager(Context context){
        initService();
        DBManager.getInstance().initDB(context);
        this.context = context;
    }

    /**
     * initialisation de l'API
     */
    private void initService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfiguration.URL_SERVICE_OPENFOODFACTS)
                .addConverterFactory(ProductDeserializer.buildGsonConverter())
                .build();

        openFoodFactsService= retrofit.create(OpenFoodFactsService.class);
    }

    /**
     * Recuperer un produit depuis son code barre
     * @param barcodeNumber
     * @param callbackAskProduct
     */
    public void getProductBycodebarNumber(final String barcodeNumber, final CallbackAskProduct callbackAskProduct){
        ProductID productExist = DBManager.getInstance().productExist(barcodeNumber);
        if(productExist!=null){
            callbackAskProduct.productExist(productExist);
        }else{
           openFoodFactsService.productInformation(barcodeNumber).enqueue(new Callback<Product>() {
               @Override
               public void onResponse(Call<Product> call, Response<Product> response) {
                   Product product = response.body();

                   if(product==null)callbackAskProduct.error(AppConfiguration.ERROR_CODENUMBER);
                   else {
                       product.setBarcodeNumber(barcodeNumber);
                       ProductID proID = DBManager.getInstance().recordProduct(product);
                       callbackAskProduct.success(proID);
                       new ObserverImageSave(proID);
                       saveProductPicture(proID,context);
                   }
               }
               @Override
               public void onFailure(Call<Product> call, Throwable t) {
                   callbackAskProduct.error(AppConfiguration.ERROR_REQUEST);
               }
           });
       }
    }

    /**
     * sauvegarder l'image d'un produit
     * @param product
     * @param context
     */
    private void saveProductPicture(ProductID product,Context context){
        new SaveProductImage(context).execute(product);
    }

    /**
     * recuperer l'image d'un produit
     * @param product
     * @param context
     * @return
     */
    public Bitmap getProductPicture(Product product,Context context){
        Bitmap res = null;
        if(product.getImage()!=null && product.getImage().length()>0){
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("productPicture", Context.MODE_PRIVATE);
            try {
                File f=new File(directory, product.getImage());
                res = BitmapFactory.decodeStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * recuperer un ensemble de produits
     * @param skip
     * @param limit
     * @return
     */
    public List<ProductID> getProducts(int skip, int limit){
        return DBManager.getInstance().getProduct(skip,limit);
    }

    /**
     * recuperer l'image d'un produit
     * @param productID
     * @return
     */
    public Bitmap getBitmapByProductID(ProductID productID) {
        return getProductPicture(productID.getProduct(),context);
    }


    /**
     * permet d'observer la création de l'image et de sauvegarder dans la base de données la nom de l'image
     */
    private class ObserverImageSave implements Observer{
        private ProductID productID;

        public ObserverImageSave(ProductID productID) {
            this.productID = productID;
            productID.addObserver(this);
        }

        @Override
        public void update(java.util.Observable observable, Object o) {
            DBManager.getInstance().updateProduct(productID);
            removeMySelf();
        }

        private void removeMySelf(){
            productID.deleteObserver(this);
            productID=null;
        }
    }
}
