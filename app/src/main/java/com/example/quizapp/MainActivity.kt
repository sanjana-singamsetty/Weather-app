package com.example.quizapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat=intent.getStringExtra("lat")
        val long=intent.getStringExtra("long")
        /*Toast.makeText(this,lat+" "+long,Toast.LENGTH_LONG).show()*/
        window.statusBarColor=Color.parseColor("#5E7092")

        getJsonData(lat,long)


    }
//
    private fun getJsonData(lat: String?, long: String?) {
        val aPI_KEY="119eaf89ce3dfb09d77b27328219e7f9"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${aPI_KEY}"

// Request a string response from the provided URL.
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener {
                    response ->
                setvalues(response)


            },
            Response.ErrorListener { Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        queue.add(jsonRequest)

    }
    private fun setvalues(response: JSONObject){
        cityname.text=response.getString("name")
        var lat = response.getJSONObject("coord").getString("lat")
        var long=response.getJSONObject("coord").getString("lon")
        coordinates.text="${lat} , ${long}"
        weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr=response.getJSONObject("main").getString("temp")
        tempr=((((tempr).toFloat()-273.15)).toInt()).toString()
        temp.text="${tempr}°C"


        var mintemp=response.getJSONObject("main").getString("temp_min")
        mintemp=((((mintemp).toFloat()-273.15)).toInt()).toString()
        min_temp.text=mintemp+"°C"
        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
        max_temp.text=maxtemp+"°C"
        pressure.text=response.getJSONObject("main").getString("pressure")
        humidity.text=response.getJSONObject("main").getString("humidity")+"%"
        wind.text=response.getJSONObject("wind").getString("speed")
        degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"
//        gust.text="Gust: "+response.getJSONObject("wind").getString("deg")+"°"
    }
}