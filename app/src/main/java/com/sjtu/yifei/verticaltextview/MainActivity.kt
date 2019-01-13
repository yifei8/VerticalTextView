package com.sjtu.yifei.verticaltextview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sjtu.yifei.route.RouteService
import com.sjtu.yifei.route.Routerfit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        acb1.setOnClickListener {
            Routerfit.register(RouteService::class.java).openListActivity()
        }

    }

}
