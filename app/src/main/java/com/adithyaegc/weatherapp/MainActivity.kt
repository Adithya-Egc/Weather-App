package com.adithyaegc.weatherapp

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.adithyaegc.weatherapp.databinding.ActivityMainBinding
import com.adithyaegc.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        viewModel.weatherResponse.observe(this) { result ->

            if (result.code() == 404) {
                hideViews()
                Toast.makeText(this, "Use proper city name", Toast.LENGTH_SHORT).show()
            } else if (result.message().toString().contains("city not found")) {
                Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show()
            } else if (result.body() != null) {
                showViews()
                binding.resultLocation.text = result.body()?.name
                val windSpeed = result.body()?.wind?.speed.toString()
                binding.resultWind.text = "$windSpeed mph"
                val temperature = result.body()?.main?.temp.toString()
                binding.resultTemp.text = "$temperature °C"
                binding.resultHumidity.text = result.body()?.main?.humidity.toString()
                val resultDate = dateConverter(result.body()!!.dt)
                binding.resultDate.text = resultDate
                binding.resultCountry.text = result.body()?.sys?.country
            } else {
                Toast.makeText(this, "Weather report not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val search = menu?.findItem(R.id.menu_search)
        searchView = (search?.actionView as? SearchView)!!
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            viewModel.getWeatherData(query)
            hideKeyboard()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?) = true

    private fun dateConverter(date: Int): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.toLong()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day/$month/$year"
    }

    private fun hideViews() {
        binding.resultHumidity.visibility = View.INVISIBLE
        binding.resultLocation.visibility = View.INVISIBLE
        binding.resultDate.visibility = View.INVISIBLE
        binding.resultCountry.visibility = View.INVISIBLE
        binding.resultTemp.visibility = View.INVISIBLE
        binding.resultWind.visibility = View.INVISIBLE
    }

    private fun showViews() {
        binding.resultHumidity.visibility = View.VISIBLE
        binding.resultLocation.visibility = View.VISIBLE
        binding.resultDate.visibility = View.VISIBLE
        binding.resultCountry.visibility = View.VISIBLE
        binding.resultTemp.visibility = View.VISIBLE
        binding.resultWind.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
    }
}