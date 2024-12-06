package com.example.financemanagerapp.operations;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.financemanagerapp.R;
import com.example.financemanagerapp.database.AppDatabase;
import com.example.financemanagerapp.database.Operation;
import com.example.financemanagerapp.database.OperationDao;
import com.example.financemanagerapp.database.OperationWithDetails;
import com.example.financemanagerapp.period_selecting.Period;
import com.example.financemanagerapp.period_selecting.PeriodViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_FILTER = 2;
    private static final int REQUEST_CODE_EDIT_OPERATION = 3;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static AppDatabase appDatabase;
    private static OperationDao operationDao;
    private PeriodViewModel periodViewModel;
    OperationsFilter filter;

    private Button filterButton;
    private Button addOperationButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OperationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OperationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OperationsFragment newInstance(String param1, String param2) {
        OperationsFragment fragment = new OperationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        appDatabase = AppDatabase.getDatabase(requireContext());
        operationDao = appDatabase.operationDao();
        periodViewModel = new ViewModelProvider(requireActivity()).get(PeriodViewModel.class);

        filter = new OperationsFilter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations, container, false);

        filterButton = view.findViewById(R.id.filters_button);
        filterButton.setOnClickListener(v -> openFilterActivity());

        addOperationButton = view.findViewById(R.id.add_button);
        addOperationButton.setOnClickListener(v -> openAddOperationActivity());

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateOperationsList(periodViewModel.getPeriod().getValue(), filter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILTER && resultCode == RESULT_OK) {
            if (data != null) {
                filter = (OperationsFilter) data.getSerializableExtra("filter");
            }
        }

        if (requestCode == REQUEST_CODE_EDIT_OPERATION && resultCode == RESULT_OK) {
            // Получение и обработка обновленной операции
            if (data != null) {
                Operation updatedOperation = (Operation) data.getSerializableExtra("updated_operation");
                // Обновите ваш список операций, например, вызвав метод для запроса вновь
            }
        }

        updateOperationsList(periodViewModel.getPeriod().getValue(), filter);
    }


    private void updateOperationsList(Period period, OperationsFilter filter){

        final List<OperationWithDetails> operations = new ArrayList<>();

        executorService.execute(() -> {

            // Получение данных из database
            operations.addAll(operationDao.getAllOperationsWithDetails(
                    period.getMinDate(), period.getMaxDate(), filter.getCategoryName(),
                    filter.getMinValue(), filter.getMaxValue()));

            // Обновление UI на главном потоке
            getActivity().runOnUiThread(() -> {

                RecyclerView recyclerView = getView().findViewById(R.id.operations_view);
                // Создание адаптера с обработчиком нажатий
                OperationWithDetailsAdapter adapter = new OperationWithDetailsAdapter(requireContext(), operations, operation -> {
                    // Обработка нажатой операции
                    openEditOperationActivity(operation);
                });
                recyclerView.setAdapter(adapter);

            });
        });
    }

    public void openFilterActivity() {
        Intent intent = new Intent(getActivity(), FilterActivity.class);
        intent.putExtra("filter", filter); // Передача объекта фильтра
        startActivityForResult(intent, REQUEST_CODE_FILTER);
    }

    private void openEditOperationActivity(OperationWithDetails operation) {
        Intent intent = new Intent(getActivity(), EditOperationActivity.class);
        intent.putExtra("activityGoal", "EDIT");
        intent.putExtra("operation", operation);
        startActivityForResult(intent, REQUEST_CODE_EDIT_OPERATION);
    }

    private void openAddOperationActivity() {
        Intent intent = new Intent(getActivity(), EditOperationActivity.class);
        intent.putExtra("activityGoal", "ADD");
        startActivityForResult(intent, REQUEST_CODE_EDIT_OPERATION);
    }

//    private void openAddOperationActivity(String activityGoal, OperationWithDetails operation)
}