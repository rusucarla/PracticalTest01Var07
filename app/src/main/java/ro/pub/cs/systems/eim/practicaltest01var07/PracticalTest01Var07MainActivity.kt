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
import android.content.Context


class PracticalTest01Var07MainActivity : AppCompatActivity() {

    private lateinit var edits: List<EditText>
    private lateinit var resultText: TextView
    private lateinit var setButton: Button
    private lateinit var receiver: BroadcastReceiver
    private lateinit var intentFilter: IntentFilter

    private var sum = 0
    private var product = 0

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val result = it.data?.getIntExtra("result", 0) ?: 0
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
        resultText = findViewById(R.id.resultText)

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

        // Start serviciu
        startService(Intent(this, PracticalTest01Var07Service::class.java))

        // Receiver pentru broadcast-uri
        intentFilter = IntentFilter("ro.pub.cs.systems.eim.practicaltest01var07.action")
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val n1 = intent?.getIntExtra("n1", 0)
                val n2 = intent?.getIntExtra("n2", 0)
                val n3 = intent?.getIntExtra("n3", 0)
                val n4 = intent?.getIntExtra("n4", 0)

                Log.d("Receiver", "Received randoms: $n1, $n2, $n3, $n4")

                // suprascrie câmpurile
                edits[0].setText(n1.toString())
                edits[1].setText(n2.toString())
                edits[2].setText(n3.toString())
                edits[3].setText(n4.toString())
            }
        }
//        receiver = PracticalTest01Var07BroadcastReceiver()
//        intentFilter = IntentFilter("ro.pub.cs.systems.eim.practicaltest01var07.action")
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
        stopService(Intent(this, PracticalTest01Var07Service::class.java))
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("sum", sum)
        outState.putInt("product", product)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        sum = savedInstanceState.getInt("sum")
        product = savedInstanceState.getInt("product")
        resultText.text = "Sum=$sum Product=$product"
    }
}
