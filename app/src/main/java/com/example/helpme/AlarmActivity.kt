package com.example.helpme

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.helpme.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAlarmBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private var isAlarm = false

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(TIME_PICKER_REPEAT_TAG, Context.MODE_PRIVATE)

        binding.btnAlarm.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_Alarm -> {
                if (isAlarm){
                    alarmReceiver.cancelAlarm(this)
                    isAlarm = false
                    checkAlarm(false)
                } else {
                    alarmReceiver.setRepeatingAlarm(this,AlarmReceiver.TYPE_REPEATING,"Alarm Reminder")
                    isAlarm = true
                    checkAlarm(true)
                }
            }
        }
    }
    private fun checkAlarm(check: Boolean){
        if (check) {
            binding.btnAlarm.text = "On Alarm"
        } else {
            binding.btnAlarm.text = "Off Alarm"
        }
    }
}