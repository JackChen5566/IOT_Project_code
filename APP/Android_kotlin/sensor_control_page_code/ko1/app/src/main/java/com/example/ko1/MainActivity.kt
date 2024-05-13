package com.example.ko1

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.faizkhan.mjpegviewer.MjpegView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private var tmp = "192.168.58.57"
    private lateinit var buttonServo : Button
    private lateinit var buttonPump : Button
    private lateinit var buttonInput : Button
    private lateinit var buttonRenew: Button
    private lateinit var buttonLight : Button
    private lateinit var textResult: TextView
    private lateinit var textEdit: EditText
    private var view: MjpegView? = null
    private lateinit var temperatureTextView: TextView
    private lateinit var phTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textResult = findViewById(R.id.textView_result)
        textEdit = findViewById(R.id.editText)
        buttonServo = findViewById(R.id.button_servo)
        buttonPump = findViewById(R.id.button_waterpump)
        buttonInput = findViewById(R.id.button_input)
        buttonLight = findViewById(R.id.button_led)
        buttonRenew = findViewById(R.id.button_renew)
        temperatureTextView = findViewById(R.id.temperatureTextView)
        phTextView = findViewById(R.id.phTextView)

        val MUrl = "http://$tmp:8080/?action=stream"
        val timer = Timer()

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                fetchSensor()
                Log.d("APPtimer","fetchSensor")
            }
        }, 0L, 60000L)


        view = findViewById(R.id.mjpegid)
        view!!.isAdjustHeight = true
        view!!.mode1 = MjpegView.MODE_FIT_WIDTH
        view!!.setUrl(MUrl)
        view!!.isRecycleBitmap1 = true
        view!!.startStream()
        view!!.setOnClickListener(null)
        //stop stream
        //view!!.stopStream();

        buttonInput.setOnClickListener {
            tmp = textEdit.text.toString()
            textResult.text = tmp
        }

        buttonServo.setOnClickListener {
            val url = "http://$tmp:8000/control_servo"
            HttpRequestTask1().execute(url)
        }
        buttonPump.setOnClickListener {
            val url = "http://$tmp:8000/WaterPump"
            HttpRequestTask1().execute(url)
        }
        buttonLight.setOnClickListener {
            val url = "http://$tmp:8000/1"
            HttpRequestTask1().execute(url)
        }
        buttonRenew.setOnClickListener {
            fetchSensor()
        }
        fetchSensor()
    }

    private fun fetchSensor() {
        val tempurl = "http://$tmp:8000/temperature_get/"
        val phurl = "http://$tmp:8000/ph_get/"
        HttpRequestTask().execute(tempurl, phurl)
    }

    private inner class HttpRequestTask : AsyncTask<String, Void, JSONObject>() {
        override fun doInBackground(vararg params: String): JSONObject? {
            val tempUrl = URL(params[0])
            val phUrl = URL(params[1])
            val tempResponse = makeRequest(tempUrl)
            val temperature = tempResponse?.optDouble("tem") ?: Double.NaN
            val phResponse = makeRequest(phUrl)
            val ph = phResponse?.optDouble("ph") ?: Double.NaN

            return JSONObject().apply {
                put("temperature", temperature)
                put("ph", ph)
            }
        }

        override fun onPostExecute(result: JSONObject?) {
            result?.let {
                val serverResponse = it.toString()
                val temperature = it.optDouble("temperature")
                val ph = it.optDouble("ph")
                if (temperature > 25)
                {
                    Log.d("temp","Temp too high")
                    Toast.makeText(applicationContext,
                        "Temp too high", Toast.LENGTH_SHORT).show()
                }
                else if (temperature < 15)
                {
                    Log.d("temp","Temp too low")
                    Toast.makeText(applicationContext,
                        "Temp too low", Toast.LENGTH_SHORT).show()
                }
                if (ph > 8)
                {
                    Log.d("ph","ph too high")
                    Toast.makeText(applicationContext,
                        "ph too high", Toast.LENGTH_SHORT).show()
                }
                else if (ph < 5)
                {
                    Log.d("ph","ph too low")
                    Toast.makeText(applicationContext,
                        "ph too low", Toast.LENGTH_SHORT).show()
                }
                temperatureTextView.text = "Temperature: $temperature"
                phTextView.text = "PH: $ph"
            }
        }
    }

    private fun makeRequest(url: URL): JSONObject? {
        var urlConnection: HttpURLConnection? = null
        try {
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"

            val responseCode = urlConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                return JSONObject(response.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
        }
        return null
    }
    private inner class HttpRequestTask1 : AsyncTask<String, Void, JSONObject>() {

        override fun doInBackground(vararg params: String): JSONObject? {
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(params[0])
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    return JSONObject(response.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
            return null
        }

        override fun onPostExecute(result: JSONObject?) {

            result?.let {
                val serverResponse = it.toString()

            }
        }
    }

}