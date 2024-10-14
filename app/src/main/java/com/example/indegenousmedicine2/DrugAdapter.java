package com.example.indegenousmedicine2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {

    private Context context;
    private List<Drug> drugList;
    private OnItemClickListener listener;

    public DrugAdapter(Context context, List<Drug> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drug, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drug drug = drugList.get(position);
        holder.bind(drug);
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewScientificName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewScientificName = itemView.findViewById(R.id.textViewScientificName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        public void bind(Drug drug) {
//            textViewScientificName.setText(drug.getScientificName());
//            Log.d(TAG, "bind: binded");
        }
    }
}
