package com.example.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var cityRv: androidx.recyclerview.widget.RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityRv = findViewById(R.id.rv_main)

        AppExcutors.diskIO().execute {
            loadCityFromAssets()
        }
    }

    private fun loadCityFromAssets() {
        val ins = assets.open("city.json")
        try {
            val buffer = ByteArray(ins.available())
            ins.read(buffer)
            ins.close()
            val cityJson = String(buffer, Charset.forName("UTF-8"))
            val jsonArray = JSONArray(cityJson)

            val originDataList = ArrayList<City>()

            for (i in 0 until jsonArray.length()) {
                val itemObj = jsonArray.getJSONObject(i)
                originDataList.add(City().apply { name = itemObj.opt("name") as String })
            }
            val characterParser = CharacterParser.getInstance()
            val pinyinComparator = PinyinComparator()
            originDataList.forEach {
                if (TextUtils.equals("", it.name)) {
                    it.sortLetter = "#"
                } else {
                    val pinyin = characterParser.getSelling(it.name)
                    val sortString = pinyin.substring(0, 1).toUpperCase()

                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]".toRegex())) {
                        it.sortLetter = sortString.toUpperCase()
                    } else {
                        it.sortLetter = "#"
                    }
                }
            }

            Collections.sort(originDataList, pinyinComparator)

            AppExcutors.mainThread().execute {
                cityRv.addItemDecoration(StickyItemDecoration(this).also {
                    it.setDividerTextCallback { position ->
                        originDataList[position].sortLetter
                    }
                })

                cityRv.adapter = CityAdapter(originDataList)
            }
        } catch (e: IOException) {

        } catch (jsonException: JSONException) {

        }
    }
}
