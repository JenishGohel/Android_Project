package com.jg.scientificcalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter
        extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    public interface OnHistoryClickListener {
        void onHistoryClick(HistoryItem item);
    }

    private final ArrayList<HistoryItem> list;
    private final OnHistoryClickListener listener;

    public HistoryAdapter(ArrayList<HistoryItem> list,
                          OnHistoryClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        HistoryItem item = list.get(position);
        holder.expr.setText(item.expression);
        holder.res.setText(item.result);

        holder.itemView.setOnClickListener(v ->
                listener.onHistoryClick(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView expr, res;

        ViewHolder(View itemView) {
            super(itemView);
            expr = itemView.findViewById(R.id.tvExpr);
            res = itemView.findViewById(R.id.tvResult);
        }
    }
}
