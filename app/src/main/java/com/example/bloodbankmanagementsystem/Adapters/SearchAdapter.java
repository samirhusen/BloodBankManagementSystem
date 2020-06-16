package com.example.bloodbankmanagementsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbankmanagementsystem.R;

import java.util.ArrayList;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    ArrayList<String> namelist;
    ArrayList<String> citylist;
    ArrayList<String> bloodgrouplist;
    ArrayList<String> phonenumberlist;

    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView name, city, blood_group, number;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            city = itemView.findViewById(R.id.city);
            blood_group = itemView.findViewById(R.id.blood_group);
            number = itemView.findViewById(R.id.number);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> namelist,
                         ArrayList<String> citylist, ArrayList<String> bloodgrouplist, ArrayList<String> phonenumberlist) {
        this.context = context;
        this.namelist = namelist;
        this.citylist = citylist;
        this.bloodgrouplist = bloodgrouplist;
        this.phonenumberlist = phonenumberlist;
    }


    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder( SearchViewHolder holder, int position) {

        holder.name.setText(namelist.get(position));
        holder.city.setText(citylist.get(position));
        holder.blood_group.setText(bloodgrouplist.get(position));
        holder.number.setText(phonenumberlist.get(position));

        holder.city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "City Name Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return citylist.size();
    }
}