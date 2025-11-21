package com.khetigaadi.weather;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.ActivityWeatherNewBinding;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.weather.model.ForecastDay;
import com.khetigaadi.weather.model.WeatherResp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class WeatherNewActivity extends BaseActivity {
    private WeatherResp weather;
    private DateAdapter mAdapter;
    @Inject
    SessionManager session;
    private WeatherViewModel viewModel;
    private ActivityWeatherNewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityWeatherNewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        ViewCompat.setOnApplyWindowInsetsListener(view, (v,insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weather = new Gson().fromJson(getIntent().getStringExtra("weather"), WeatherResp.class);
        if (weather != null) {
            initUI(weather);
        } else {
            String city = session.getUserLocation().getName();
            if (!city.equals("")) {
                getWeather(city);
            }
        }
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (weather != null) {
            binding.toolbar.setTitle(weather.getLocation().getName());
          //  binding.toolbar.setSubtitle(DateUtils.convertDateToDate(weather.getCurrent().getLastUpdated(),DateUtils.DateFormat.yyyy_MM_dd_HH_mm,DateUtils.DateFormat.dd_MM_yyyy));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd HH:mm");
            String dateInString = weather.getCurrent().getLastUpdated();
            SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy");
            try {
                Date date = formatter.parse(dateInString);
                System.out.println(date);
                assert date != null;
                //System.out.println(formatterOut.format(date));
                binding.toolbar.setSubtitle(formatterOut.format(date));
            }
            catch(ParseException e) {
                e.printStackTrace();
            }
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.news);
    }
    private void initUI(WeatherResp weather) {
        this.weather = weather;
        initToolbar();
        List<ForecastDay> myDatas = new ArrayList<>();
        myDatas.addAll(weather.getForecast().getForecastDay());
        myDatas.get(0).setSelected(true);
        binding.rvDate.setLayoutManager(new LinearLayoutManager(WeatherNewActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new DateAdapter(this, myDatas, binding.rvDate);
        binding.rvDate.setAdapter(mAdapter);
        setDataToUI(myDatas.get(0));
        mAdapter.setOnClickListener(position -> {
            for (int i = 0; i < myDatas.size(); i++) {
                myDatas.get(i).setSelected(false);
            }
            myDatas.get(position).setSelected(true);
            setDataToUI(myDatas.get(position));
            mAdapter.notifyDataSetChanged();
        });
        Glide.with(this)
                .load(R.drawable.loader_white)
                .into(binding.llProgressBar.ivLoader);
    }
    private void setDataToUI(ForecastDay forecast) {
        Glide.with(this)
                .load("https:" + forecast.getDay().getCondition().getIcon())
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image).
                into(binding.ivImage);
        binding.tvTemperature.setText(String.format("%s", forecast.getDay().getAvgtempC()));
        binding.tvConditionText.setText(forecast.getDay().getCondition().getText());
        binding.tvMaxTemperature.setText(String.format("%s", forecast.getDay().getMaxtempC()));
        binding.tvMinTemperature.setText(String.format("%s", forecast.getDay().getMintempC()));
        binding.clExtras.setVisibility(View.VISIBLE);
        binding.tvPrecipitation.setText(String.format("Precip\n%smm", forecast.getDay().getTotalprecipMm()));
        binding.tvWind.setText(String.format("Wind\n%skm/h", forecast.getDay().getMaxwindKph()));
        binding.tvHumidity.setText(String.format("Humidity\n%s%%", forecast.getDay().getAvghumidity()));
    }
    private void getWeather(String city) {
       try {
           viewModel.getWeather(city).observe(this, weatherRespStateData -> {
               Log.d("bvcxbcvbcvbc===", "" + weatherRespStateData.getStatus() + "==" + weatherRespStateData.getData());
               switch (weatherRespStateData.getStatus()) {
                   case LOADING:
                       binding.llProgressBar.llProgress.setVisibility(View.VISIBLE);
                   case SUCCESS:
                       try {
                           if (weatherRespStateData.getData() != null) {
                               initUI(weatherRespStateData.getData());
                           }
                           binding.llProgressBar.llProgress.setVisibility(View.GONE);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                       break;
                   case ERROR:
                       binding.llProgressBar.llProgress.setVisibility(View.GONE);
                       assert weatherRespStateData.getError() != null;
                       if (weatherRespStateData.getError().equalsIgnoreCase("HTTP 400 ")) {
                           binding.noWeather.getRoot().setVisibility(View.VISIBLE);
                       }
                       Toast.makeText(this, weatherRespStateData.getError(), Toast.LENGTH_SHORT).show();
                       break;
               }
           });
       }
       catch (Exception e) {
           e.printStackTrace();
       }

    }
}
