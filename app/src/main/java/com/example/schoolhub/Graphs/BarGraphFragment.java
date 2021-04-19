package com.example.schoolhub.Graphs;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.schoolhub.R;
import com.example.schoolhub.ui.statistics.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class BarGraphFragment extends Fragment{

    AnyChartView anyChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_bar_graph, container, false);

        anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        for(int a=0;a<StatisticsFragment.ComparisonSchools.size();a++){
            float[] results = new float[1];
            Location.distanceBetween(
                    33.684422,
                    73.047882,
                    Double.valueOf(StatisticsFragment.ComparisonSchools.get(a).getSchoolCoordinates().getLatitude()),
                    Double.valueOf(StatisticsFragment.ComparisonSchools.get(a).getSchoolCoordinates().getLongitude()),
                    results);

            data.add(new ValueDataEntry(StatisticsFragment.ComparisonSchools.get(a).getSchoolName(), results[0]/1000));
        }
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("Distance from your current location");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Schools");
        cartesian.yAxis(0).title("Distance(KM)");
        anyChartView.setChart(cartesian);

        return root;
    }
}