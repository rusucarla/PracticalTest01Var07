package ro.pub.cs.systems.eim.practicaltest01var07

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ro.pub.cs.systems.eim.practicaltest01var07.Constants.action

class PracticalTest01Var07MainActivity : AppCompatActivity() {

    private lateinit var edits: List<EditText>
    private lateinit var resultText: TextView
    private lateinit var setButton: Button

    private lateinit var randomButton: Button
    private lateinit var receiver: BroadcastReceiver
    private lateinit var intentFilter: IntentFilter

    private var sum = 0
    private var product = 0

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                // need to check both extras
                val resultS = it.data?.getIntExtra("resultS", 0) ?: 0
                val resultP = it.data?.getIntExtra("resultP", 0) ?: 0
                val result = if (resultS != 0) resultS.also{sum = resultS} else resultP.also{product = resultP}
                Toast.makeText(this, "Received result: $result", Toast.LENGTH_SHORT).show()
                resultText.text = "Result: $result"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test01_var07_main)

        edits = listOf(
            findViewById(R.id.edit1),
            findViewById(R.id.edit2),
            findViewById(R.id.edit3),
            findViewById(R.id.edit4)
        )

        setButton = findViewById(R.id.btnSet)
        randomButton = findViewById(R.id.btnRandom)
        resultText = findViewById(R.id.resultText)

        // random button for generating random number (<= 10)
        randomButton.setOnClickListener {
            for (edit in edits) {
                if (edit.text.toString().isEmpty() || edit.text.toString().toIntOrNull() == null) {
                    val randomValue = (0..10).random()
                    edit.setText(randomValue.toString())
                }
            }
        }

        // set for numbers and go to 2nd activity
        setButton.setOnClickListener {
            val values = edits.map { it.text.toString() }

            // validare numerica
            if (values.any { it.isEmpty() || it.toIntOrNull() == null }) {
                Toast.makeText(this, "Please enter 4 valid numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ints = values.map { it.toInt() }
            val intent = Intent(this, PracticalTest01Var07SecondaryActivity::class.java).apply {
                putExtra("n1", ints[0])
                putExtra("n2", ints[1])
                putExtra("n3", ints[2])
                putExtra("n4", ints[3])
            }
            launcher.launch(intent)
        }

        // Receiver pentru broadcast-uri
        intentFilter = IntentFilter(action)
        receiver = PracticalTest01Var07BroadcastReceiver(edits)

        // Start serviciu
        Log.d("MainActivity", "Starting service")
        startService(Intent(this, PracticalTest01Var07Service::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        Log.d("onDestroy", "Stopping service")
        stopService(Intent(this, PracticalTest01Var07Service::class.java))
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("onSaveInstanceState", "Saved: num=$sum product=$product")
        outState.putInt("sum", sum)
        outState.putInt("product", product)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        sum = savedInstanceState.getInt("sum")
        product = savedInstanceState.getInt("product")
        resultText.text = "Sum=$sum\nProduct=$product"
        Log.d("onRestoreInstanceState", "$resultText.text")
    }
}
