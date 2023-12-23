
package com.example.stopwatchnoob

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import com.example.stopwatchnoob.databinding.ActivityMainBinding // Make sure to import the generated binding class

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var isRunning = false
    private var minutes: String? = "00.00.00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var lapsList = ArrayList<String>()
        var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lapsList)
        binding.listView.adapter = arrayAdapter
        binding.lap.setOnClickListener {
            if (isRunning) {
                lapsList.add(0, binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
                binding.listView.smoothScrollToPosition(0)
            }
        }

        binding.clock.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.dialogue)
            var numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 5
            dialog.findViewById<Button>(R.id.setTime).setOnClickListener {
                binding.clockTime.text =
                    dialog.findViewById<NumberPicker>(R.id.numberPicker).value.toString() + " min"
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.run.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                if (!minutes.equals("00.00.00")) {
                    var totalmin = minutes!!.toInt() * 60 * 1000L
                    var countDown = 1000L
                    binding.chronometer.base = SystemClock.elapsedRealtime() + totalmin
                    binding.chronometer.format = "%S %S"
                    binding.chronometer.onChronometerTickListener =
                        Chronometer.OnChronometerTickListener {
                            var elapsedtime =
                                SystemClock.elapsedRealtime() - binding.chronometer.base
                            if (elapsedtime >= totalmin) {
                                binding.chronometer.stop()
                                isRunning = false
                                binding.run.text = "Run"
                            }
                        }
                } else {
                    isRunning = true
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.run.text = "Stop"
                    binding.chronometer.start()
                }
            } else {
                binding.chronometer.stop()
                isRunning = false
                binding.run.text = "Run"
            }

        }
    }
}

