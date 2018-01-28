package com.villiers.foodfacts.Utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.PermissionListener;
import com.villiers.foodfacts.R;

import java.util.HashMap;
import java.util.Map;


/**
 * Fonctions utiles pour le projet
 * @author villiers
 */
public class Utils {

    /**
     *convertisseur erreur et chaine de caractères
     */
    public static Map<Integer,Integer> ERROR_TEXT = new HashMap<>();
    static {
        ERROR_TEXT.put(AppConfiguration.ERROR_REQUEST, R.string.error_request);
        ERROR_TEXT.put(AppConfiguration.ERROR_CODENUMBER, R.string.error_barcode);
    }

    /**
     * Montrer un message à l'utilisateur
     * @param message
     * @param activity
     */
    public static void showMessage(String message,Activity activity){
        Toast.makeText(activity, message,
                Toast.LENGTH_LONG).show();
    }

    /**
     * demander une permission
     * @param permission
     * @param activity
     * @param permissionListener
     */
    public static void askPermission(String permission, Activity activity,PermissionListener permissionListener) {
        Dexter.withActivity(activity)
                .withPermission(permission)
                .withListener(permissionListener)
                .check();
    }

    /**
     * vérifier une permission
     * @param permission
     * @param activity
     * @return
     */
    public static boolean permissionHasBeenAccepted(String permission, Activity activity){
        return ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED;
    }
}
