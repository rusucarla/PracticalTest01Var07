package ro.pub.cs.systems.eim.practicaltest01var07

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PracticalTest01Var07SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test01_var07_secondary)

        val n1 = intent.getIntExtra("n1", 0)
        val n2 = intent.getIntExtra("n2", 0)
        val n3 = intent.getIntExtra("n3", 0)
        val n4 = intent.getIntExtra("n4", 0)

        findViewById<TextView>(R.id.text1).text = n1.toString()
        findViewById<TextView>(R.id.text2).text = n2.toString()
        findViewById<TextView>(R.id.text3).text = n3.toString()
        findViewById<TextView>(R.id.text4).text = n4.toString()

        findViewById<Button>(R.id.btnSum).setOnClickListener {
            val result = n1 + n2 + n3 + n4
            setResult(RESULT_OK, Intent().putExtra("resultS", result))
            finish()
        }

        findViewById<Button>(R.id.btnProduct).setOnClickListener {
            val result = n1 * n2 * n3 * n4
            setResult(RESULT_OK, Intent().putExtra("resultP", result))
            finish()
        }
    }
}
