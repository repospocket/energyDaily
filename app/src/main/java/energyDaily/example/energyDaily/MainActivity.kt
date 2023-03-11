package energyDaily.example.energyDaily

import energyDaily.example.energyDaily.databinding.ActivityMainBinding
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class MainActivity : AppCompatActivity() {


    private var  list1 =  arrayListOf<CalendarDay>()
    var datacolo = mutableMapOf<String, String>()
    val calendar = Calendar.getInstance()
    var dateNotes = mutableMapOf<String, String>()
    private var alarmMgr: AlarmManager? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        title = "energyDaily"
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        setContentView(binding.root)

        val min = Calendar.getInstance()
        min.set(2020 ,0,1)
        val max = Calendar.getInstance()
        max.set(2025 ,1,1)
        binding.calendarView.setMinimumDate(min)
        val calendarView = binding.calendarView
        calendarView.setMaximumDate(max)
        calendarView.setCalendarDayLayout(R.layout.cuscelll)
        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar = eventDay.calendar
                val timee = clickedDayCalendar.get(Calendar.YEAR).toString() + " " + clickedDayCalendar.get(Calendar.MONTH).toString() + " " + clickedDayCalendar.get(Calendar.DAY_OF_MONTH).toString()
                if (dateNotes[timee].isNullOrEmpty()){
                    val toast = Toast.makeText(applicationContext, "No note!", Toast.LENGTH_LONG)
                    toast.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        toast.cancel()
                    }, 800)

                }else{
                   // Toaster.makeLongToast(dateNotes.get(timee), 8000, baseContext);
                    val toast = Toast.makeText(applicationContext, dateNotes[timee], Toast.LENGTH_LONG)
                    toast.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        toast.cancel()
                    }, 800)

                }
            }

        })

        alarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent1 = Intent(this, NotificationReceiver::class.java)
        alarmIntent1.action = "jaa"

        val ala=    alarmIntent1.let { intent ->
            PendingIntent.getBroadcast(this, 0, intent,             PendingIntent.FLAG_IMMUTABLE)
        }

        // Set the alarm to start at approximately 2:00 p.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 21)
        }

        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            ala
        )
    }

    fun submitNote( @Suppress("UNUSED_PARAMETER") view: View) {
        val txt = binding.textnote.text.toString()
        val time = Calendar.getInstance()
        val year = time[Calendar.YEAR] // get the current year
        val month = time[Calendar.MONTH] // month...
        val day = time[Calendar.DAY_OF_MONTH] // current day in the month

        dateNotes["$year $month $day"] = txt

        saveDataa(dateNotes,"dataNotes")
        Toast.makeText(applicationContext, "            Note saved :)\n Tap on a day to show a note", Toast.LENGTH_SHORT).show()

        this.recreate()

    }

    fun tapcolor(view: View) {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR] // get the current year
        val month = cal[Calendar.MONTH] // month...
        val day = cal[Calendar.DAY_OF_MONTH] // current day in the month

        if(datacolo.containsKey("$year $month $day")){
            list1.removeAt(list1.size -1)
        }

        when (view.tag.toString()) {
            "green" -> list1.add(
                    CalendarDay(calendar).apply {
                        backgroundDrawable = ContextCompat.getDrawable(baseContext, android.R.color.holo_green_light) })
            "red" -> list1.add(
                    CalendarDay(calendar).apply {
                        backgroundDrawable = ContextCompat.getDrawable(baseContext, android.R.color.holo_red_light) })
            "blue" -> list1.add(
                    CalendarDay(calendar).apply {
                        backgroundDrawable = ContextCompat.getDrawable(baseContext, android.R.color.holo_blue_light) })
            else -> print("otherwise")
        }

        binding.calendarView.setCalendarDays(list1)


        val myDate = "$year $month $day"

        when (view.tag.toString()) {
            "green" -> datacolo[myDate] = "green"

            "red" -> datacolo[myDate] = "red"

            "blue" -> datacolo[myDate] = "blue"
            else -> print("otherwise")
        }
        saveDataa(datacolo,"task list")

   }

    private fun saveDataa( list: MutableMap<String, String>, sharedrStr: String) {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(list)
        editor.putString(sharedrStr, json)
        editor.apply()
    }
     private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        val json = sharedPreferences.getString("task list", null)
         val json2 = sharedPreferences.getString("dataNotes", null)

         val type = object : TypeToken<MutableMap<String, String>>() {}.type

         if(!json.isNullOrEmpty()){
           datacolo = Gson().fromJson(json, type)

         }
         if (!json2.isNullOrEmpty()){
             dateNotes= Gson().fromJson(json2, type)
         }

         var lolo =""

         if (datacolo.isNotEmpty()){

             for ((k, v) in datacolo) {
             lolo += "$k = $v"
             val str = k.split(" ")
             val yr = str[0].toInt()
             val mnth =  str[1].toInt()
             val dy  =  str[2].toInt()

             val calendarrrr = Calendar.getInstance()
             calendarrrr.set(yr,mnth,dy)

             when (v) {
                 "green" -> list1.add(
                         CalendarDay(calendarrrr).apply {
                             backgroundDrawable = ContextCompat.getDrawable(baseContext, android.R.color.holo_green_light) })
                 "red" -> list1.add(
                         CalendarDay(calendarrrr).apply {
                             backgroundDrawable = ContextCompat.getDrawable(baseContext, android.R.color.holo_red_light)  })
                 "blue" -> list1.add(
                         CalendarDay(calendarrrr).apply {
                             backgroundDrawable = ContextCompat.getDrawable(baseContext, android.R.color.holo_blue_light)  })
                 else -> print("otherwise")
             }
             }
             binding.calendarView.setCalendarDays(list1)

         }
         Log.d("loadDataa", lolo)
     }



}






