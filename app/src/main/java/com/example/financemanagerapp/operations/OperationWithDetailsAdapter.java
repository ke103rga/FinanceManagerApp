package com.example.financemanagerapp.operations;

import static com.example.financemanagerapp.utils.NumberFormatter.roundNumber;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanagerapp.R;
import com.example.financemanagerapp.database.CategorySummary;
import com.example.financemanagerapp.database.OperationWithDetails;
import com.example.financemanagerapp.review.CategorySummaryAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OperationWithDetailsAdapter extends RecyclerView.Adapter<OperationWithDetailsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private List<OperationWithDetails> operationList;
    private OnItemClickListener listener;

    private Context context;
    Resources res;
    String packageName;

    public interface OnItemClickListener {
        void onItemClick(OperationWithDetails operation);
    }

    public OperationWithDetailsAdapter(Context context, List<OperationWithDetails> operationList, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.operationList = operationList;
        this.listener = listener;
        this.context = context;
        this.res = context.getResources();
        this.packageName = context.getPackageName();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.operation_review_list_item, parent, false);
        return new OperationWithDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OperationWithDetails operation = operationList.get(position);

//        int colorId = res.getIdentifier(operation.getOperationTypeColor(), "color", packageName);
//        int colorId = res.getIdentifier("black", "color", packageName);

        // Форматирование даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        holder.operationDate.setText(dateFormat.format(operation.getOperationDate()));

        holder.categoryName.setText(operation.getCategoryName());
        holder.categoryName.setTextColor(Color.parseColor(operation.getOperationTypeColor()));

        holder.operationComment.setText(operation.getOperationComment());

        holder.operationValue.setText(roundNumber(operation.getOperationValue()));
        holder.operationValue.setTextColor(Color.parseColor(operation.getOperationTypeColor()));

        // Установка обработчика нажатия
        holder.itemView.setOnClickListener(v -> listener.onItemClick(operation)); // Вызываем обработчик нажатия
    }

    @Override
    public int getItemCount() {
        return operationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView operationDate;
        public TextView categoryName;
        public TextView operationComment;
        public TextView operationValue;

        public ViewHolder(View itemView) {
            super(itemView);
            operationDate = itemView.findViewById(R.id.operation_date);
            categoryName = itemView.findViewById(R.id.category_name);
            operationComment = itemView.findViewById(R.id.operation_comment);
            operationValue = itemView.findViewById(R.id.operation_value);
        }
    }
}
