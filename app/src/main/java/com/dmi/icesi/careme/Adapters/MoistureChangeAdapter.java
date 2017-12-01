package com.dmi.icesi.careme.Adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmi.icesi.careme.Model.MoistureChange;
import com.dmi.icesi.careme.R;

import java.util.ArrayList;

/**
 * Created by Paula on 29/11/2017.
 */

public class MoistureChangeAdapter extends RecyclerView.Adapter<MoistureChangeAdapter.MoistureViewHolder> {

    ArrayList<MoistureChange> moistureChanges;

    public MoistureChangeAdapter(ArrayList<MoistureChange> moistureChanges) {
        this.moistureChanges = moistureChanges;
    }

    @Override
    public MoistureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moisurechange_list, null, false);
        return new MoistureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoistureViewHolder holder, int position) {

        int hum = moistureChanges.get(position).getMoisturePercentage();
        int idealHum = 26;
        if (hum < (idealHum/2)) {
            holder.ll_color.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e27c49")));
        } else  if (  hum >= (idealHum/2) && (idealHum) > hum) {
            holder.ll_color.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e5e148")));
        } else  if (  hum == idealHum) {
            holder.ll_color.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43e97b")));
        }else  if (  hum > (idealHum + 3) && hum < (idealHum + idealHum/2) ) {
            holder.ll_color.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3ebbcc")));
        }  else  if (  hum > (idealHum + idealHum/2) ) {
            holder.ll_color.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2b4d6d")));
        }
        holder.tv_moisture.setText("Humedad " + moistureChanges.get(position).getMoisturePercentage() +"%");
        holder.time.setText("Hoy, " + moistureChanges.get(position).getHour() + ":" +
                moistureChanges.get(position).getMinute());
    }

    public Object getItem(int position) {return moistureChanges.get(position);}

    public long getItemId(int position) { return  position;}

    @Override
    public int getItemCount() {
        return moistureChanges.size();
    }

    public class MoistureViewHolder extends RecyclerView.ViewHolder {

        TextView tv_moisture;
        TextView time;
        LinearLayout ll_color;

        public MoistureViewHolder(View itemView) {
            super(itemView);

            tv_moisture = (TextView) itemView.findViewById(R.id.tv_moisture);
            time = (TextView) itemView.findViewById(R.id.tv_time_moisture);
            ll_color = (LinearLayout) itemView.findViewById(R.id.ll_colorMoisture);

        }
    }
}
