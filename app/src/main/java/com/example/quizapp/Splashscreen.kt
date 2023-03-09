package com.example.quizapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


class Splashscreen : AppCompatActivity() {
    private lateinit var mmfusedlocation: FusedLocationProviderClient
    private  var myrequestcode: Int= 1010
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        mmfusedlocation = LocationServices.getFusedLocationProviderClient(this)
        getlastlocation()
        window.statusBarColor= Color.parseColor("#5E7092")

    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    private fun getlastlocation() {
        if(checkpermision()){

            if(locationenable()){
                mmfusedlocation.lastLocation.addOnCompleteListener{
                        task->
                    var location: Location?=task.result
                    if(location==null){
                        newlocation()
                    }
                    else{
                        Handler(Looper.getMainLooper()).postDelayed({//to display after sometime
                            var intent = Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },2000)
                    }
                }
            }else{
                Toast.makeText(this,"Please turn on ur Gps lOCATION",Toast.LENGTH_LONG).show()
            }
        }
        else{
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newlocation() {
        var locationRequest= com.google.android.gms.location.LocationRequest()
        locationRequest.priority=/*LocationRequest.*/Priority.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1
        mmfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        mmfusedlocation.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())


    }
    private val locationCallback=object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location? =p0.lastLocation
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun locationenable(): Boolean {

var locationmanager=getSystemService(Context.LOCATION_SERVICE)as LocationManager
        return locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION
            ,android.Manifest.permission.ACCESS_FINE_LOCATION),myrequestcode)
    }

    private fun checkpermision(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==myrequestcode)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getlastlocation()
            }
        }
    }
    }
