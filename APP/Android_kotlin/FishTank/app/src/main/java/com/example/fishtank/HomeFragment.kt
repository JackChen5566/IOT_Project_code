package com.example.fishtank

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fishtank.databinding.FragmentHomeBinding
import com.faizkhan.mjpegviewer.MjpegView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    private var tmp = "192.168.59.57"
//    private lateinit var buttonServo : Button
//    private lateinit var buttonPump : Button
//    private lateinit var buttonInput : Button
//    private lateinit var buttonRenew: Button
//    private lateinit var buttonLight : Button
//    private lateinit var textResult: TextView
//    private lateinit var textEdit: EditText
//    private var view: MjpegView? = null
//    private lateinit var temperatureTextView: TextView
//    private lateinit var phTextView: TextView
    val MUrl = "http://$tmp:8080/?action=stream"
    val timer = Timer()

    var _binding:FragmentHomeBinding? =null
    private val  binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                fetchSensor()
                Log.d("APPtimer","fetchSensor")
            }
        }, 0L, 60000L)


//        view = findViewById(R.id.mjpegid)
        binding.mjpegid!!.isAdjustHeight = true
        binding.mjpegid!!.mode1 = MjpegView.MODE_FIT_WIDTH
        binding.mjpegid!!.setUrl(MUrl)
        binding.mjpegid!!.isRecycleBitmap1 = true
        binding.mjpegid!!.startStream()
        binding.mjpegid!!.setOnClickListener(null)
        //stop stream
        //view!!.stopStream();

        binding.buttonInput.setOnClickListener {
            tmp = binding.editText.text.toString()
            binding.textViewResult.text = tmp
        }

        binding.buttonServo.setOnClickListener {
            val url = "http://$tmp:8000/control_servo"
            HttpRequestTask1().execute(url)
        }
        binding.buttonWaterpump.setOnClickListener {
            val url = "http://$tmp:8000/WaterPump"
            HttpRequestTask1().execute(url)
        }
        binding.buttonLed.setOnClickListener {
            val url = "http://$tmp:8000/1"
            HttpRequestTask1().execute(url)
        }
        binding.buttonRenew.setOnClickListener {
            fetchSensor()
        }
        fetchSensor()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
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
                    Toast.makeText(activity,
                        "Temp too high", Toast.LENGTH_SHORT).show()
                }
                else if (temperature < 15)
                {
                    Log.d("temp","Temp too low")
                    Toast.makeText(activity,
                        "Temp too low", Toast.LENGTH_SHORT).show()
                }
                if (ph > 8)
                {
                    Log.d("ph","ph too high")
                    Toast.makeText(activity,
                        "ph too high", Toast.LENGTH_SHORT).show()
                }
                else if (ph < 5)
                {
                    Log.d("ph","ph too low")
                    Toast.makeText(activity,
                        "ph too low", Toast.LENGTH_SHORT).show()
                }
                binding.temperatureTextView.text = "Temperature: $temperature"
                binding.phTextView.text = "PH: $ph"
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

