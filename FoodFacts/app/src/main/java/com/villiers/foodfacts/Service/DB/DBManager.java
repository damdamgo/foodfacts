package com.villiers.foodfacts.Service.DB;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.villiers.foodfacts.Service.Modele.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Manager pour la base de données de l'application (Singleton)
 * @author villiers
 */
public class DBManager {
    /**
     * base de données
     */
    private Database database = null;

    /**
     * Instance du singleton
     */
    private static final DBManager ourInstance = new DBManager();

    /**
     * Permet d'accéder à l'instance du singleton
     * @return l'instance du singleton
     */
    public static DBManager getInstance() {
        return ourInstance;
    }

    /**
     * Constructeur
     */
    private DBManager() {
    }

    /**
     * innitialise la base de données
     * @param context
     */
    public void initDB(Context context){
        AndroidContext androidContext = new AndroidContext(context);
        Manager manager = null;
        try {
            manager = new Manager(androidContext, Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            database = manager.getDatabase("products");
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        View productByDate = database.getView("productByDate");
        productByDate.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object productObj = document.get("product");
                int time = (int) document.get("time");
                emitter.emit(time, productObj);
            }
        }, "1");
        View productByBarcode = database.getView("productByBarcode");
        productByBarcode.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object productObj = document.get("product");
                Gson gson = new Gson();
                String jsonString = gson.toJson(productObj, Map.class);
                Product product = gson.fromJson(jsonString, Product.class);
                emitter.emit(product.getBarcodeNumber(), productObj);
            }
        }, "1");
    }

    /**
     * enregistrer un produit
     * @param product model du produit
     * @return produit avec son ID
     */
    public ProductID recordProduct(Product product){
        Long tsLong = System.currentTimeMillis()/1000;
        Document document = database.createDocument();
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("product", product);
        properties.put("time", tsLong);
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return new ProductID(document.getId(),product);
    }

    /**
     * recuperer un ensemble de produits par ordre décroissant de leurs dates d'insertion
     * @param skip indice de départ
     * @param limit indice d'arrivée
     * @return liste de produits
     */
    public List<ProductID> getProduct(int skip, int limit){
        Query query = database.getView("productByDate").createQuery();
        query.setDescending(true);
        query.setLimit(limit);
        query.setSkip(skip);
        QueryEnumerator result = null;
        List<ProductID> listProducts = new LinkedList<>();
        try {
            result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow qr = it.next();
                listProducts.add(getProductIDByRow(qr));
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return listProducts;
    }

    /**
     * Convertisseur de JSON en produitID
     * @param qr json
     * @return produitID
     */
    private ProductID getProductIDByRow(QueryRow qr){
        Gson gson = new Gson();
        String jsonString = gson.toJson(qr.getValue(), Map.class);
        Product product = gson.fromJson(jsonString, Product.class);
        return new ProductID(qr.getDocumentId(),product);
    }

    /**
     * Permet de récuperer un produit si celui-ci existe dans la base de données
     * @param BarcodeNumber identification du produit
     * @return produitID
     */
    public ProductID productExist(String BarcodeNumber){
        ProductID res = null;
        Query query = database.getView("productByBarcode").createQuery();
        query.setStartKey(BarcodeNumber);
        query.setEndKey(BarcodeNumber);
        try {
            QueryEnumerator result = query.run();
            if(result.getCount()==1){
                QueryRow qr = result.next();
                res = getProductIDByRow(qr);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * met à jour un produit
     * @param productID
     */
    public void updateProduct(ProductID productID) {
        Document doc = database.getDocument(productID.getID());
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.putAll(doc.getProperties());
        properties.put("product", productID.getProduct());
        try {
            doc.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
