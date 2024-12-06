package com.example.financemanagerapp.review;

import static com.example.financemanagerapp.utils.NumberFormatter.roundNumber;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanagerapp.R;
import com.example.financemanagerapp.database.CategorySummary;
import java.util.List;

public class CategorySummaryAdapter extends RecyclerView.Adapter<CategorySummaryAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<CategorySummary> categorySummaries;

    private Context context;
    Resources res;
    String packageName;

    CategorySummaryAdapter(Context context, List<CategorySummary> categorySummaries) {
        this.categorySummaries = categorySummaries;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.res = context.getResources();
        this.packageName = context.getPackageName();
    }

    @Override
    public CategorySummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.category_review_list_item1, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CategorySummaryAdapter.ViewHolder holder, int position) {
        CategorySummary categorySummary = categorySummaries.get(position);

//        int colorId = res.getIdentifier(categorySummary.categoryColor, "color", packageName);
        int iconId = res.getIdentifier(categorySummary.categoryIcon, "drawable", packageName);

        holder.imageView.setImageResource(iconId);

        holder.nameView.setText(categorySummary.getCategoryName());
        holder.nameView.setTextColor(Color.parseColor(categorySummary.getCategoryColor()));

        holder.totalValueView.setText(roundNumber((int) categorySummary.getCatTotalValue()));
        holder.totalValueView.setTextColor(Color.parseColor(categorySummary.getCategoryColor()));

        holder.percantageView.setText(String.valueOf((int) (categorySummary.getPercentage() * 100)) + " %");

        holder.categoryProgressBar.setProgress((int)( categorySummary.getPercentage() * 100));
    }

    @Override
    public int getItemCount() {
        return categorySummaries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView,  totalValueView, percantageView;
        final ProgressBar categoryProgressBar;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.cat_image);
            nameView = view.findViewById(R.id.category_name);
            totalValueView = view.findViewById(R.id.category_total);
            percantageView = view.findViewById(R.id.category_percent);
            categoryProgressBar = view.findViewById(R.id.progress_bar);
        }
    }
}
