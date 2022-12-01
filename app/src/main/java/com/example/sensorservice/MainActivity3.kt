package com.example.sensorservice

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView

class MainActivity3 : AppCompatActivity(),SensorEventListener {
    lateinit var accelSensor : Sensor
    lateinit var sensorManager: SensorManager
    var x1:Float = 0.0f;var x2:Float = 0.0f;var y1:Float=0.0f;var y2:Float=0.0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                if (x1 < x2) {
                    val intent: Intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                } else {
                    val i: Intent = Intent(this, MainActivity4::class.java)
                    startActivity(i)
                }
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        val save: Button = findViewById(R.id.button)
        val stop : Button =findViewById(R.id.button3)

        save.setOnClickListener {
            val serviceIntent =Intent(this,BackgroundService3::class.java)
            startService(serviceIntent)
        }
        stop.setOnClickListener {
            val serviceIntent =Intent(this,BackgroundService3::class.java)
            stopService(serviceIntent)
        }
        //getting sensor services
        sensorManager= getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //registering listener the sensor
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accelSensor,SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type==Sensor.TYPE_ACCELEROMETER){
            val accelText: TextView =findViewById(R.id.textView)
            accelText.text = "Accel Value\n x = ${event!!.values[0]}\n" + "y = ${event.values[1]}\n"+"z = ${event.values[2]}\n"}

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}