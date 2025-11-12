package ro.pub.cs.systems.eim.practicaltest01var07

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.TextView

class PracticalTest01Var07BroadcastReceiver(private val edits: List<EditText>?) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val n1 = intent?.getIntExtra("n1", 0)
        val n2 = intent?.getIntExtra("n2", 0)
        val n3 = intent?.getIntExtra("n3", 0)
        val n4 = intent?.getIntExtra("n4", 0)

        Log.d("BroadcastReceiver", "Received randoms: $n1, $n2, $n3, $n4")

        // suprascriu valorile in campurile EditText => UI
        edits?.get(0)?.setText(n1.toString())
        edits?.get(1)?.setText(n2.toString())
        edits?.get(2)?.setText(n3.toString())
        edits?.get(3)?.setText(n4.toString())
    }
}
