package com.example.sensorservice

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import baseSettings
import com.amplifyframework.core.Amplify
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity4:baseSettings(),SensorEventListener {
    lateinit var sensorManager: SensorManager
    lateinit var magnoSensor : Sensor
    lateinit var textView: TextView
    val decimalFormat=DecimalFormat("#.##")

    var x1:Float = 0.0f;var x2:Float = 0.0f;var y1:Float=0.0f;var y2:Float=0.0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                x1 = event.x
                //y1 = event.y
            }
            MotionEvent.ACTION_UP->{
                x2=event.x
               // y2=event.y
                if (x1<x2){
                    val intent: Intent = Intent(this,MainActivity3::class.java)
                    startActivity(intent)
                }
                else{
//                    val i: Intent = Intent(this,MainActivity::class.java)
//                    startActivity(i)
                }
            }
        }
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val save: Button = findViewById(R.id.button4)
        val stop: Button =findViewById(R.id.button5)

        save.setOnClickListener {
            val serviceIntent =Intent(this,BackgroundService4::class.java)
            startService(serviceIntent)
        }
        stop.setOnClickListener {
            val serviceIntent =Intent(this,BackgroundService4::class.java)
            stopService(serviceIntent)
            uploadFile()
        }
        magnoSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(this, magnoSensor, SensorManager.SENSOR_DELAY_NORMAL)
        textView=findViewById(R.id.mag_max)
        textView.text="Maximum range : ${magnoSensor.maximumRange.toString()}"

        mChart=findViewById(R.id.lineChart4)
        commonSettings(Sensor.TYPE_MAGNETIC_FIELD)
        mChart.description.text="Magnetometer Data Visualization"
        mChart.setDrawBorders(true)
        startPlot()
        decimalFormat.roundingMode= RoundingMode.DOWN
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0?.sensor?.type==Sensor.TYPE_MAGNETIC_FIELD){
            val magnetoText: TextView =findViewById(R.id.textView2)
            magnetoText.text = "Magno Value\n x = ${decimalFormat.format(p0!!.values[0])} \n\n" + "y = ${decimalFormat.format(p0!!.values[1])}\n\n"+"z = ${decimalFormat.format(p0!!.values[2])}"
            if (plotData){
                addEntry(p0)
                plotData=false}

    }
    }

    override fun onDestroy() {
        thread?.interrupt()
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private fun uploadFile() {
        val exampleFile = File(applicationContext.externalCacheDir, "Magno.json")
        Amplify.Storage.uploadFile("Magnetometer", exampleFile,
            { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
            { Log.e("MyAmplifyApp", "Upload failed", it) }
        )
    }
}