package com.suyee.mm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.suyee.mm.R;
import com.suyee.mm.model.DataUsage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{

    private Context context;
    private ArrayList<DataUsage> dataList;
    private LayoutInflater inflater;

    public DataAdapter(Context context, ArrayList<DataUsage> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(this.context);
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.inflater.inflate(R.layout.card_item, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        final DataUsage data = dataList.get(position);
        holder.year.setText(data.getYear());
        NumberFormat numFormat = new DecimalFormat();
        numFormat.setMinimumFractionDigits(4);
        numFormat.setMaximumFractionDigits(10);
        holder.volume.setText("Total: " + numFormat.format(data.getVolume()));

        if(data.getDownMessage() != null && data.getDownMessage().length() > 0){
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(android.R.drawable.ic_dialog_info);
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, data.getDownMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            holder.icon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView year, volume;
        private ImageButton icon;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.year);
            volume = itemView.findViewById(R.id.total);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
