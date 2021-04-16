package com.example.schoolhub.Graphs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.schoolhub.R;
import com.example.schoolhub.ui.statistics.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class PieChartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_pie_chart, container, false);

        //Pie Chart
        AnyChartView anyChartView2 = root.findViewById(R.id.any_chart_view2);
        anyChartView2.setProgressBar(root.findViewById(R.id.progress_bar2));

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });
        List<DataEntry> data2 = new ArrayList<>();
        for(int i=0;i<StatisticsFragment.ComparisonSchools.size();i++){
            data2.add(new ValueDataEntry(StatisticsFragment.ComparisonSchools.get(i).getSchoolName(), 4.3+i));
        }
        pie.data(data2);
        pie.title("School Ratting Graph");
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Schools")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView2.setChart(pie);

        return root;
    }
}