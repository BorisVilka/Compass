package com.boom.compass.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.boom.compass.WarningDialog
import com.boom.compass.databinding.FragmentHomeBinding
import kotlin.math.abs

class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private var magnetometer: Sensor? = null
    private var lastAccelerometer = FloatArray(3)
    private var lastMagnetometer = FloatArray(3)
    private var isAccelerometerSet = false
    private var isMagnetometerSet = false
    private var currentDegree = 0f
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        try {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!;
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!;
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if(magnetometer==null) {
            val dialog = WarningDialog()
            dialog.show(requireActivity().supportFragmentManager,"Warning")
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        try {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        } catch (e: Exception) {}
        try {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        } catch (e: Exception) {

        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor === accelerometer) {
            System.arraycopy(event!!.values, 0, lastAccelerometer, 0, event!!.values.size)
            isAccelerometerSet = true
        } else if (event!!.sensor === magnetometer) {
            System.arraycopy(event!!.values, 0, lastMagnetometer, 0, event!!.values.size)
            isMagnetometerSet = true
        }

        if (isAccelerometerSet && isMagnetometerSet) {
            val rotationMatrix = FloatArray(9)
            val success = SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                lastAccelerometer,
                lastMagnetometer
            )
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val azimuthInRadians = orientation[0]
                val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()
                rotateCompassImage(azimuthInDegrees)
            }
        }
    }
    var b = false
    var d = 1f
    var label = arrayListOf("S","SW","W","NW","N","NE","E","SE",)
    private fun rotateCompassImage(degrees: Float) {
        if(!b) {
            d = degrees
            b = true
        } else {
            if(abs(d-degrees) <=1) return
            d = degrees
        }
        val rotateAnimation = RotateAnimation(
            currentDegree,
            -degrees,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 250
        rotateAnimation.fillAfter = true
        binding.imageView4.startAnimation(rotateAnimation)
        val degrees1 = -degrees
        var ind = 0
        if(degrees1>15 && degrees1<75) ind = 1
        else  if(degrees1 in 75.0..105.0) ind = 2
        else  if(degrees1>105 && degrees1<145) ind = 3
        else  if(degrees1>145 || degrees1<145 && degrees1>0) ind = 4
        else  if(degrees1<-15 && degrees1>-75) ind = 5
        else  if(degrees1<=-75 && degrees1>=-105) ind = 6
        else ind = 7
        //if(ind!=0) ind = label.size-ind
        currentDegree = -degrees
        binding.textView.text = "${-degrees.toInt()}"
        binding.textView3.text = label[ind]
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}