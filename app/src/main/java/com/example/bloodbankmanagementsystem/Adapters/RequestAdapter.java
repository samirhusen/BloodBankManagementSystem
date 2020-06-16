package com.example.bloodbankmanagementsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbankmanagementsystem.HelperClass.UploadHelperClass;
import com.example.bloodbankmanagementsystem.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<UploadHelperClass> dataSet;
    private Context context;
    private OnItemClickListener mListner;

    public RequestAdapter(List<UploadHelperClass> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_layout, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        UploadHelperClass uploadCurrent = dataSet.get(position);
        holder.message.setText(dataSet.get(position).getMessage());
        Picasso.with(context)
                .load(dataSet.get(position).getImageUri())
                .placeholder(R.drawable.login)
                .fit()
                .into(holder.imageView);

        //instead fo glide will be i will be using Piccaso
        //Glide.with(context).load(dataSet.get(position).getImageUri()).into(holder.imageView);

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionChecker.checkSelfPermission(context, CALL_PHONE)
                        == PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + dataSet.get(position).getNumber()));
                    context.startActivity(intent);
                } else {
                    ((Activity) context).requestPermissions(new String[]{CALL_PHONE}, 401);
                }
            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        holder.message.getText().toString() + "\n\nContact: " + dataSet.get(position).getNumber());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey, could you help here");
                context.startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView message;
        ImageView imageView, callButton, shareButton;

        ViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.image);
            callButton = itemView.findViewById(R.id.call_button);
            shareButton = itemView.findViewById(R.id.share_button);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListner != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListner.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Click below to delete post");
            //menu.setHeaderTitle("You will never get this post back");
            MenuItem delete = menu.add(Menu.NONE,1,1,"Delete");

            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListner != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()) {
                        case 1:
                            mListner.onDeleteCLick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

        //void onWhatEverCLick(int position);

        void onDeleteCLick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListner = listener;
    }

}

