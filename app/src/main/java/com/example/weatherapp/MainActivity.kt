package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//1b4791f1172067cdd3a31aa7966db7c4

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val txt=binding.searchView


      txt.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
          override fun onQueryTextSubmit(query: String?): Boolean {
              if (query != null) {
                  featchWeatherData(query)
              }
              return true
          }

          override fun onQueryTextChange(newText: String?): Boolean {

              return true
          }
      })
    }
    fun featchWeatherData(cityName:String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppInterface::class.java)

        val response = retrofit.getWeatherData(cityName,"1b4791f1172067cdd3a31aa7966db7c4","metric")
        response.enqueue(object :Callback<WeatherData>{
            override fun onResponse(p0: Call<WeatherData>, p1: Response<WeatherData>) {
                val response = p1.body()
                if(p1.isSuccessful&&response!=null){
                    val temp = response.main.temp
                    binding.temperatureView.text = "$temp °C"
                    binding.humpertxt.text = "${response.main.humidity}"
                    binding.windtxt.text = "${response.wind.speed}"

                    binding.sunsettxt.text = "${response.sys.sunset}"
                    binding.sunrisetxt.text = "${response.sys.sunrise}"
                    binding.seatxt.text = "${response.main.sea_level}"
                    binding.maxTempView.text = "max temp: ${response.main.temp_max}"
                    binding.minTempView.text ="min temp: ${response.main.temp_min}"
                    binding.weathetTxt.text = "${response.weather.firstOrNull()?.main?:"unkown" }"
                    binding.conditiontxt.text = "${response.weather.firstOrNull()?.main?:"unkown" }"
                    binding.dayNametxt.text= dayName()
                    binding.citytxt.text = "${cityName}"
                    binding.dateTxt.text = date()
                    changeImagesAccordingToWeather("${response.weather.firstOrNull()?.main?:"unkown" }")
                }
            }

            override fun onFailure(p0: Call<WeatherData>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun changeImagesAccordingToWeather(condition: String) {
        when(condition){
            "Clear sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Partly Clouds","Clouds","Overcast","Mist","Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain","Moderate Rain","Showers","Heavy Rain","Drizzle"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light snow","Moderate snow","Heavy snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

        }
    }

    fun dayName():String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
    fun date():String{
        val sdf = SimpleDateFormat("dd MM YYYY", Locale.getDefault())
        return sdf.format(Date())
    }
}