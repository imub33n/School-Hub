package com.example.schoolhub.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.schoolhub.GraphAdapter;
import com.example.schoolhub.PagerAdapter;
import com.example.schoolhub.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel =
                ViewModelProviders.of(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        ViewPager vp = root.findViewById(R.id.viewPagerGraphs);
        GraphAdapter gA= new GraphAdapter(getFragmentManager());
        vp.setAdapter(gA);
        TabLayout tL=root.findViewById(R.id.toolbarGraphs);
        tL.setupWithViewPager(vp);

        //BarChart
//        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view);
//        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));
//        Cartesian cartesian = AnyChart.column();
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("School 1", 5000));
//        data.add(new ValueDataEntry("School 2", 13000));
//        data.add(new ValueDataEntry("School 3", 10000));
//        data.add(new ValueDataEntry("School 4", 25000));
//        Column column = cartesian.column(data);
//        column.tooltip()
//                .titleFormat("{%X}")
//                .position(Position.CENTER_BOTTOM)
//                .anchor(Anchor.CENTER_BOTTOM)
//                .offsetX(0d)
//                .offsetY(5d)
//                .format("{%Value}{groupsSeparator: }");
//        cartesian.animation(true);
//        //cartesian.title("Top 10 Cosmetic Products by Revenue");
//        cartesian.yScale().minimum(0d);
//        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//        cartesian.interactivity().hoverMode(HoverMode.BY_X);
//        cartesian.xAxis(0).title("Schools");
//        cartesian.yAxis(0).title("Monthly Fee");
//        anyChartView.setChart(cartesian);

        //Pie Chart
//        AnyChartView anyChartView2 = root.findViewById(R.id.any_chart_view2);
//        anyChartView2.setProgressBar(root.findViewById(R.id.progress_bar2));
//
//        Pie pie = AnyChart.pie();
//
//        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
//            @Override
//            public void onClick(Event event) {
//                Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        List<DataEntry> data2 = new ArrayList<>();
//        data2.add(new ValueDataEntry("School 1", 3.4));
//        data2.add(new ValueDataEntry("School 2", 2.7));
//        data2.add(new ValueDataEntry("School 3", 4.9));
//        data2.add(new ValueDataEntry("School 4", 2.3));
//        data2.add(new ValueDataEntry("School 5", 0));
//        pie.data(data2);
//        pie.title("School Ratting Graph");
//        pie.labels().position("outside");
//        pie.legend().title().enabled(true);
//        pie.legend().title()
//                .text("Schools")
//                .padding(0d, 0d, 10d, 0d);
//        pie.legend()
//                .position("center-bottom")
//                .itemsLayout(LegendLayout.HORIZONTAL)
//                .align(Align.CENTER);
//        anyChartView2.setChart(pie);

        return root;
    }
}