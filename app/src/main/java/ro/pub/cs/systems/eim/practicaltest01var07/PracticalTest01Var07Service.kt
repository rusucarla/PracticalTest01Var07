package ro.pub.cs.systems.eim.practicaltest01var07

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlin.random.Random
import ro.pub.cs.systems.eim.practicaltest01var07.Constants.action
class PracticalTest01Var07Service : Service() {
    private var running = false
    private lateinit var thread: Thread

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        running = true
        thread = Thread {
            while (running) {
                val randomNumbers = List(4) { Random.nextInt(0, 100) }
                val broadcast = Intent(action).apply {
                    putExtra("n1", randomNumbers[0])
                    putExtra("n2", randomNumbers[1])
                    putExtra("n3", randomNumbers[2])
                    putExtra("n4", randomNumbers[3])
                }
                sendBroadcast(broadcast)
                Log.d("Service", "Broadcast sent with values: $randomNumbers")
                Thread.sleep(10000)
            }
        }
        thread.start()
        return START_STICKY
    }

    override fun onDestroy() {
        running = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
