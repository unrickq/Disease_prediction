package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.R;

import java.util.List;

public class ConsultationAdapter extends RecyclerView.Adapter<ConsultationAdapter.ViewHolder>{
    private final List<String> products;
//    private Context mContext;

    public ConsultationAdapter(List<String> products) {
        this.products = products;
//        this.mContext = context;
    }

    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent   The ViewGroup into which the new View will be added
     *                 after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */
    @Override
    public ConsultationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new ConsultationAdapter.ViewHolder(view);
    }

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder   The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(final ConsultationAdapter.ViewHolder holder, int position) {
        // Get current product
        String product = products.get(position);

        //Populate the view with data
        holder.bindTo(product);

    }

    /**
     * Required method for determining the size of the data set.
     *
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView productName;


        public ViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.consultation_name);

            view.setOnClickListener(this);
        }

        void bindTo(String product) {
            // Populate views with data
            // If product is on sale


            productName.setText(product);
            // load images of product

        }


        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

        }
    }
}
