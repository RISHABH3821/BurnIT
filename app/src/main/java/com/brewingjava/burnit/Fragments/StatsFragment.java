package com.brewingjava.burnit.Fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brewingjava.burnit.Constants.StringConstants;
import com.brewingjava.burnit.DataModels.ChartDataModel;
import com.brewingjava.burnit.DataModels.HighestRep;
import com.brewingjava.burnit.DataModels.TodayRep;
import com.brewingjava.burnit.R;
import com.brewingjava.burnit.Util.API;
import com.brewingjava.burnit.Util.API_PROVIDER;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatsFragment extends Fragment {


    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    private TextView squatReps, pushupReps;
    private SeekBar squatSeekBar, pushUpSeekBar;
    private int squatCount, pushupCount;
    private LineChart chart;
    private TextView tipsText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        squatReps = view.findViewById(R.id.squat_reps);
        pushupReps = view.findViewById(R.id.pushup_reps);
        squatSeekBar = view.findViewById(R.id.squat_seekbar);
        pushUpSeekBar = view.findViewById(R.id.pushup_seekbar);
        tipsText = view.findViewById(R.id.tip);
        squatSeekBar.setEnabled(false);
        pushUpSeekBar.setEnabled(false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        getReps(StringConstants.squats, email, squatReps);
        getReps(StringConstants.pushUps, email, pushupReps);
        setupChart(view, email);
        getTip(email, tipsText);
    }


    private void getChartData(String email) {
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Entry> squatValues = new ArrayList<>();
        ArrayList<Entry> pushupValues = new ArrayList<>();

        Call<List<ChartDataModel>> call = API_PROVIDER.api.getChartData(email, StringConstants.squats);
        Call<List<ChartDataModel>> call1 = API_PROVIDER.api.getChartData(email, StringConstants.pushUps);

        call.enqueue(new Callback<List<ChartDataModel>>() {
            @Override
            public void onResponse(Call<List<ChartDataModel>> call, Response<List<ChartDataModel>> response) {
                if (response.isSuccessful()) {
                    int j = 0;
                    for (int i = response.body().size() - 1; i >= 0; i--) {
                        ChartDataModel dataModel = response.body().get(i);
                        String date = dataModel.getTimestamp().substring(0, 10);
                        dates.add(date);
                        squatValues.add(new Entry(j, Float.parseFloat(dataModel.getTotal()), ContextCompat.getDrawable(getContext(), R.drawable.ic_fire)));
                        ++j;
                    }

                    call1.enqueue(new Callback<List<ChartDataModel>>() {
                        @Override
                        public void onResponse(Call<List<ChartDataModel>> call, Response<List<ChartDataModel>> response1) {
                            if (response1.isSuccessful()) {
                                int k = 0;
                                for (int i = response1.body().size() - 1; i >= 0; i--) {
                                    ChartDataModel dataModel = response1.body().get(i);
                                    pushupValues.add(new Entry(k, Float.parseFloat(dataModel.getTotal()), ContextCompat.getDrawable(getContext(), R.drawable.ic_fire)));
                                    ++k;
                                }
                                setData(squatValues, pushupValues, dates);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ChartDataModel>> call, Throwable t) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<ChartDataModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupChart(View view, String email) {
        // // Chart Style // //
        chart = view.findViewById(R.id.linechart);

        // background color
        chart.setBackgroundColor(Color.WHITE);

        // disable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // set listeners
//        chart.setOnChartValueSelectedListener(getContext());
        chart.setDrawGridBackground(false);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(false);
        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            xAxis.disableGridDashedLine();
            xAxis.disableAxisLineDashedLine();
            // vertical grid lines
//            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.disableGridDashedLine();
            yAxis.disableAxisLineDashedLine();
//            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(50f);
            yAxis.setAxisMinimum(0f);
        }
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        chart.setDrawGridBackground(false);
getChartData(email);
    }


    private void setData(ArrayList<Entry> values, ArrayList<Entry> values2, ArrayList<String> dates) {
        Log.d("chartdata", new Gson().toJson(dates));

        LineDataSet set1;
        LineDataSet set2;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set2.setValues(values2);
            set2.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Squats");
            set2 = new LineDataSet(values2, "Pushups");


            set1.setDrawIcons(false);
            set2.setDrawIcons(false);

            set1.setDrawValues(false);
            set2.setDrawValues(false);

            set1.setColor(getContext().getColor(R.color.squat_pink));
            set1.setCircleColor(getContext().getColor(R.color.squat_pink));

            set2.setColor(getContext().getColor(R.color.pushup_pink));
            set2.setCircleColor(getContext().getColor(R.color.pushup_pink));


            // line thickness and point size
            set1.setLineWidth(4f);
            set1.setCircleRadius(7f);

            // line thickness and point size
            set2.setLineWidth(4f);
            set2.setCircleRadius(7f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);
            set2.setDrawCircleHole(false);

            set1.setDrawFilled(false);
            set2.setDrawFilled(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            set2.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets
            dataSets.add(set2); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);
            // set data
            chart.setData(data);
        }
        chart.getXAxis().setValueFormatter(new ClaimsXAxisValueFormatter(dates));
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.invalidate();
    }


    public static class ClaimsXAxisValueFormatter extends ValueFormatter {

        List<String> datesList;

        public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
            this.datesList = arrayOfDates;
        }


        @Override
        public String getAxisLabel(float value, AxisBase axis) {
/*
Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
*/
            Integer position = Math.round(value);
            SimpleDateFormat sdf = new SimpleDateFormat("EE");

            if (value > 1 && value < 2) {
                position = 0;
            } else if (value > 2 && value < 3) {
                position = 1;
            } else if (value > 3 && value < 4) {
                position = 2;
            } else if (value > 4 && value <= 5) {
                position = 3;
            }
            if (position < datesList.size())
                return sdf.format(new Date((getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));
            return "";
        }
    }

    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }


    private void getReps(String type, String email, TextView repText) {
        Call<List<TodayRep>> call = API_PROVIDER.api.getTodaysReps(email, type);
        call.enqueue(new Callback<List<TodayRep>>() {
            @Override
            public void onResponse(Call<List<TodayRep>> call, Response<List<TodayRep>> response) {
                if (response.isSuccessful()) {
                    Log.d("squatsreps", new Gson().toJson(response));
                    if (response.body() != null && response.body().size() > 0) {
                        repText.setText(String.format("%s reps", response.body().get(0).getSum()));
                        if (type.equals(StringConstants.squats)) {
                            squatCount = Integer.parseInt(response.body().get(0).getSum());
                            getHighestReps(type, email, squatSeekBar);
                        } else {
                            pushupCount = Integer.parseInt(response.body().get(0).getSum());
                            getHighestReps(type, email, pushUpSeekBar);
                        }
                    } else {
                        repText.setText("0");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TodayRep>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getHighestReps(String type, String email, SeekBar seekBar) {
        Call<List<HighestRep>> call = API_PROVIDER.api.getHighestReps(email, type);
        call.enqueue(new Callback<List<HighestRep>>() {
            @Override
            public void onResponse(Call<List<HighestRep>> call, Response<List<HighestRep>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        int total = Integer.parseInt(response.body().get(0).getTotal());
                        if(total==0){
                            total = 1;
                        }
                        if (type.equals(StringConstants.squats)) {
                            seekBar.setProgress((int) ((squatCount / total) * 100));
                        } else {
                            seekBar.setProgress((int) ((pushupCount / total) * 100));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HighestRep>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTip(String email, TextView textView) {
        Call<String> call = API_PROVIDER.api.getPerformanceTip(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    textView.setText(response.body());
                } else {
                    Toast.makeText(getContext(), "couldn't fetch tip", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "couldn't fetch tip", Toast.LENGTH_SHORT).show();
            }
        });
    }


}