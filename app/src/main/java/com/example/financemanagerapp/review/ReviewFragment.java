package com.example.financemanagerapp.review;

import static com.example.financemanagerapp.utils.NumberFormatter.roundNumber;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financemanagerapp.R;
import com.example.financemanagerapp.database.AppDatabase;
import com.example.financemanagerapp.database.CategoryDao;
import com.example.financemanagerapp.database.CategorySummary;
import com.example.financemanagerapp.database.OperationDao;
import com.example.financemanagerapp.period_selecting.Period;
import com.example.financemanagerapp.period_selecting.PeriodViewModel;
import com.example.financemanagerapp.utils.NumberFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static AppDatabase appDatabase;
    private static OperationDao operationDao;
    private static CategoryDao categoryDao;
    private PeriodViewModel periodViewModel;


    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = AppDatabase.getDatabase(requireContext());
        operationDao = appDatabase.operationDao();
        categoryDao = appDatabase.categoryDao();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        periodViewModel = new ViewModelProvider(requireActivity()).get(PeriodViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        updateFinanceReview(preparePeriod());
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFinanceReview(periodViewModel.getPeriod().getValue());
    }


    @SuppressLint("SetTextI18n")
    private void updateFinanceReview(Period period){

        final List<CategorySummary> categorySummaries = new ArrayList<>();

        executorService.execute(() -> {

            double allTimeTotalIncome = operationDao.getTotalIncome(period.getZeroDate(), period.getEndDate());
            double allTimeTotalExpense = operationDao.getTotalExpense(period.getZeroDate(), period.getEndDate());
            double allTimeBalance = allTimeTotalIncome - allTimeTotalExpense;

            double currentTotalIncome = operationDao.getTotalIncome(period.getMinDate(), period.getMaxDate());
            double currentTotalExpense = operationDao.getTotalExpense(period.getMinDate(), period.getMaxDate());
            double currentRest = currentTotalIncome - currentTotalExpense;

            double meanExpensePerDate = operationDao.getAverageExpense(period.getMinDate(), period.getMaxDate(), "date");
            double meanExpensePerWeek = operationDao.getAverageExpense(period.getMinDate(), period.getMaxDate(), "week");
            double meanExpensePerMonth = operationDao.getAverageExpense(period.getMinDate(), period.getMaxDate(), "month");

//            Log.i("BD", String.valueOf((int) meanExpence));


            // Получение данных из database
            categorySummaries.addAll(categoryDao.getCategorySummaries(
                    period.getMinDate(), period.getMaxDate(), 0.00));

            // Обновление UI на главном потоке
            getActivity().runOnUiThread(() -> {

                TextView balanceTextView = getView().findViewById(R.id.balance_text_view);
                TextView incomeTextView = getView().findViewById(R.id.income_text_view);
                TextView expenseTextView = getView().findViewById(R.id.expense_text_view);
                TextView restTextView = getView().findViewById(R.id.rest_text_view);

                balanceTextView.setText("Баланс:\n" + NumberFormatter.formatKPI((int) allTimeBalance));
                incomeTextView.setText("Доход:\n" + NumberFormatter.formatKPI((int) currentTotalIncome));
                expenseTextView.setText("Расход:\n" + NumberFormatter.formatKPI((int) currentTotalExpense));
                restTextView.setText("Остаток за период:\n" + NumberFormatter.formatKPI((int) currentRest));


                TextView averageDayTextView = getView().findViewById(R.id.average_day_text_view);
                TextView averageWeekTextView = getView().findViewById(R.id.average_week_text_view);
                TextView averageMonthTextView = getView().findViewById(R.id.average_month_text_view);

                averageDayTextView.setText("В день:\n" + roundNumber((int) meanExpensePerDate));
                averageWeekTextView.setText("В неделю:\n" + roundNumber((int) meanExpensePerWeek));
                averageMonthTextView.setText("В месяц:\n" + roundNumber((int) meanExpensePerMonth));


                RecyclerView recyclerView = getView().findViewById(R.id.categories_summary_view);
                CategorySummaryAdapter adapter = new CategorySummaryAdapter(requireContext(), categorySummaries);
                recyclerView.setAdapter(adapter);

            });
        });
    }
}