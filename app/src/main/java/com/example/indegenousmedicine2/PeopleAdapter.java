package com.example.indegenousmedicine2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private List<String> peopleList;

    public PeopleAdapter(List<String> peopleList) {
        this.peopleList = peopleList;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        String personName = peopleList.get(position);
        holder.textViewName.setText(personName);
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    static class PeopleViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
