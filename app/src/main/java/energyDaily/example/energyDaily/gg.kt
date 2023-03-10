package energyDaily.example.energyDaily

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class gg: AppCompatActivity() {

     fun saveData( day :String, co:String , context: Context) {

         val sharedPreferences1 = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val json1 = sharedPreferences1.getString("task list", null)
        val type = object : TypeToken<MutableMap<String, String>>() {}.type

         if(!json1.isNullOrEmpty()){
             val datacolo = Gson().fromJson<MutableMap<String, String>>(json1, type)
         }

          val datacolo = mutableMapOf<String, String>()

            datacolo.put(day,co)

        val sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(datacolo)
        Log.d("SaveDataa", json);
        editor.putString("task list", json)
        editor.apply()
    }


}