package com.example.tourweatherreminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var timelineRecyclerAdapter: TimelineRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        resetAdapter()

        addFAB.setOnClickListener {
            addSchedule("Sunny", "유튜브", "2020-06 01:50", 28f, "집", 37.504182, 127.026738)
//            val intent = Intent(this, AddActivity::class.java)
//            startActivity(intent)
        }
    }

    fun resetAdapter() {
        timelineRecyclerAdapter = TimelineRecyclerAdapter()
        recycler_view.adapter = timelineRecyclerAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        //timelineRecyclerAdapter.clear()
        timelineRecyclerAdapter.addWeatherHeader(cityWeather)
        for (i in 0 until ScheduleList.size) {
            timelineRecyclerAdapter.addTimepoint(Timepoint())
            timelineRecyclerAdapter.addSchedule(ScheduleList[i])
        }
    }

    fun addSchedule(
        weather: String,
        title: String,
        date: String,
        temp: Float,
        place: String,
        lat: Double,
        long: Double
    ) {
        ScheduleList.add(Schedule(weather, title, date, temp, place, lat, long))
        Collections.sort(ScheduleList, object : Comparator<Schedule> {
            override fun compare(x: Schedule, y: Schedule) = x.date.compareTo(y.date)
        })
        resetAdapter()
    }

    // db와 연결해서 데이터 설정
    companion object FakeData {
        val cityWeather: CityWeather = CityWeather(
            "Warsaw",
            "Sunny", 21.5f, "5%", "56%", "25km/h"
        )


        val ScheduleList: ArrayList<Schedule> = arrayListOf(
            Schedule("Sunny", "영화", "2020-05-30 01:50", 24f, "강남역", 37.504182, 127.026738),
            Schedule("Windy", "건대입구", "2020-05-31", 22.2f, "강남역", 37.504182, 127.026738),
            Schedule("Rain fall", "성수역", "2020-05-31", 18.5f, "강남역", 37.504182, 127.026738),
            Schedule("Cloudy", "춘천", "2020-05-31", 18f, "강남역", 37.504182, 127.026738),
            Schedule("Clear sky", "강릉", "2020-06-02", 21.5f, "강남역", 37.504182, 127.026738),
            Schedule("Sunny", "양막창", "2020-06-03", 21.5f, "신논현역", 37.504182, 127.026738),
            Schedule(
                "Rain fall",
                "떡볶이",
                "2020-06-03",
                19.7f,
                "강남역",
                37.504182,
                127.026738,
                isLastItem = true
            )
        )

    }
}
