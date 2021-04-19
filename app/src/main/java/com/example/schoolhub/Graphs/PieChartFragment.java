package com.example.schoolhub.Graphs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.ui.statistics.StatisticsFragment;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class PieChartFragment extends Fragment {
    float avg2;
    int count=0;
    DecimalFormat cdf= new DecimalFormat("0.0");

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
            for(int j=0;j<StatisticsFragment.allSchoolReviews.size();j++){
                if(Objects.equals(StatisticsFragment.ComparisonSchools.get(i).get_id(),StatisticsFragment.allSchoolReviews.get(j).getSchoolID())){
                    avg2+=StatisticsFragment.allSchoolReviews.get(j).getRating();
                    count++;
                }
            }
            avg2=avg2/count;

            data2.add(new ValueDataEntry(StatisticsFragment.ComparisonSchools.get(i).getSchoolName(), Float.valueOf(cdf.format(avg2))));
            avg2=0;
            count=0;
            if(i==StatisticsFragment.ComparisonSchools.size()-1){
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
            }
        }

        return root;
    }
}