package com.sjtu.yifei.verticaltextview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vtv1.text = "fdaljfdkjfdaj对肌肤的开发建设独立空间发呆看风景啊来得及啊空间发呆咖啡看电视dafdafdadsf"

    }
}
