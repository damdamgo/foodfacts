package com.villiers.foodfacts.View.Adapter;


import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.villiers.foodfacts.R;
import com.villiers.foodfacts.Service.DB.ProductID;

import java.util.LinkedList;
import java.util.List;


/**
 * adapteur de la liste des produits
 * @author villiers
 */
public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {

    /**
     * liste des produits
     */
    private List<ProductID>  listProducts = null;
    /**
     * callback
     */
    private ClickOnItem clickOnItem;

    /**
     * constructeur
     * @param clickOnItem
     */
    public ListProductsAdapter(ClickOnItem clickOnItem){
        this.clickOnItem = clickOnItem;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_list_products, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductID productID = listProducts.get(position);
        holder.textView.setText(productID.product.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnItem.itemSelected(productID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProducts == null ? 0 : listProducts.size();
    }

    /**
     * Mettre à jour la liste des données
     * @param productIDS
     */
    public void updateData(final List<ProductID> productIDS) {
        if(productIDS!=null && productIDS.size()>0){
            if(listProducts==null){
                listProducts = new LinkedList<>();
            }
            listProducts.clear();
            listProducts.addAll(productIDS);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textViewNameProduct);
        }
    }

    /**
     * interface pour le clique sur un produit
     */
    public interface ClickOnItem{
        void itemSelected(ProductID productID);
    }
}
