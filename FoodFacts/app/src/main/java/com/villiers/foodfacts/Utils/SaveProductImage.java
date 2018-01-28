package com.villiers.foodfacts.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.villiers.foodfacts.Service.DB.ProductID;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


/**
 * sauvegarde de l'image d'un produit
 */
public class SaveProductImage  extends AsyncTask<ProductID, Void, ProductID> {

    /**
     * context
     */
    private final Context context;

    /**
     * Constructeur
     * @param context
     */
    public SaveProductImage(Context context){
        this.context = context;
    }

    @Override
    protected ProductID doInBackground(ProductID... products) {
        ProductID product = products[0];
        try {
            URL url = new URL(product.getProduct().getImageURL());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("productPicture", Context.MODE_PRIVATE);
            String nameFile = product.getID()+".png";
            File mypath=new File(directory,nameFile);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                product.setImagePath(nameFile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    protected void onPostExecute(ProductID productID) {
        productID.notifyObservers();
    }
}
