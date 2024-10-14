// MedicineAdapter.java
package com.example.indegenousmedicine2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private List<Medicine> drugList;
    private Context context;

    public MedicineAdapter(Context context, List<Medicine> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drug, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = drugList.get(position);
        holder.textViewName.setText(medicine.getName());
        holder.textViewScientificName.setText(medicine.getScientificName());

        holder.imageViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, DrugDetailsActivity.class);
            intent.putExtra("DRUG_KEY", medicine.getKey());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewScientificName;
        ImageView imageViewDetails;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewScientificName = itemView.findViewById(R.id.textViewScientificName);
            imageViewDetails = itemView.findViewById(R.id.imageViewDetails);
        }
    }
}
