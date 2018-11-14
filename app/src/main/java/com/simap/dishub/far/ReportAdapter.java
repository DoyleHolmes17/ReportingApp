package com.simap.dishub.far;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Denny on 11/15/2016.
 */

public class ReportAdapter extends ArrayAdapter<Listreport> {
    List<Listreport> reportList;
    Context context;
    private LayoutInflater mInflater;
    ReportHolder holder;

    // Constructors
    public ReportAdapter(Context context, List<Listreport> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        reportList = objects;
    }

    @Override
    public Listreport getItem(int position) {
        return reportList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final ViewHolder vh;
        View view=convertView;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_laporan, parent, false);
            holder=new ReportHolder();
            holder.textalamatInfra=(TextView) view.findViewById(R.id.textareaInfra);
            holder.textnoteInfra=(TextView) view.findViewById(R.id.textnoteInfra);
            holder.textalamatInfra=(TextView) view.findViewById(R.id.textalamatInfra);
            holder.textreportStatus=(TextView) view.findViewById(R.id.textstatusReport);

            view.setTag(holder);
            //view.setTag(vh);
        } else {
            //vh = (ViewHolder) convertView.getTag();
            holder=(ReportHolder) view.getTag();
        }

        Listreport item = getItem(position);

        holder.textareaInfra.setText(item.getAreaInfra());
        holder.textnoteInfra.setText(item.getNoteInfra());
        holder.textalamatInfra.setText(item.getAlamatInfra());
        holder.textreportStatus.setText(item.getReportStatus());

        return view;
    }

    static class ReportHolder{
        TextView textareaInfra;
        TextView textnoteInfra;
        TextView textalamatInfra;
        TextView textreportStatus;
    }

    /*private static class ViewHolder {
        public final TextView textareaInfra;
        public final TextView textnoteInfra;
        public final TextView textalamatInfra;
        public final TextView textreportStatus;
        private ViewHolder(TextView textareaInfra, TextView textnoteInfra, TextView textalamatInfra, TextView textreportStatus) {
            this.textareaInfra = textareaInfra;
            this.textnoteInfra = textnoteInfra;
            this.textalamatInfra = textalamatInfra;
            this.textreportStatus = textreportStatus;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView textareaInfra = (TextView) rootView.findViewById(R.id.textareaInfra);
            TextView textnoteInfra = (TextView) rootView.findViewById(R.id.textnoteInfra);
            TextView textalamatInfra = (TextView) rootView.findViewById(R.id.textalamatInfra);
            TextView textreportStatus = (TextView) rootView.findViewById(R.id.textstatusReport);
            return new ViewHolder(rootView, textareaInfra, textnoteInfra, textalamatInfra, textreportStatus);
        }
    }*/
}
