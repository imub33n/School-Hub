package com.example.schoolhub.Graphs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.schoolhub.R;
import com.example.schoolhub.ui.statistics.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class LineGraphFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_line_graph, container, false);

        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");

        cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        //distance,fee,rating
        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));



        Set set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Line series1 = cartesian.line(series1Mapping);
        series1.name(StatisticsFragment.ComparisonSchools.get(0).getSchoolName());
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Line series2 = cartesian.line(series2Mapping);
        series2.name(StatisticsFragment.ComparisonSchools.get(1).getSchoolName());
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        if(StatisticsFragment.ComparisonSchools.size()>=3) {
            Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
            Line series3 = cartesian.line(series3Mapping);
            series3.name(StatisticsFragment.ComparisonSchools.get(2).getSchoolName());
            series3.hovered().markers().enabled(true);
            series3.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series3.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);
        }
        if(StatisticsFragment.ComparisonSchools.size()>=4){
            Mapping series4Mapping = set.mapAs("{ x: 'x', value: 'value4' }");
            Line series4 = cartesian.line(series4Mapping);
            series4.name(StatisticsFragment.ComparisonSchools.get(3).getSchoolName());
            series4.hovered().markers().enabled(true);
            series4.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series4.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);
        }
        if(StatisticsFragment.ComparisonSchools.size()==5) {
            Mapping series5Mapping = set.mapAs("{ x: 'x', value: 'value5' }");
            Line series5 = cartesian.line(series5Mapping);
            series5.name(StatisticsFragment.ComparisonSchools.get(4).getSchoolName());
            series5.hovered().markers().enabled(true);
            series5.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series5.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);
        }
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartesian);
        return root;
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4, Number value5) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
            setValue("value5", value5);
        }
        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
        }
        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }
}