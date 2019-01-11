package com.sjtu.yifei.verticaltextview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vtv1.text = "fdaljfdkjfdaj对肌肤的开发建设独立空间发呆看风景啊来得及啊空间发呆咖啡看电视dafdafdadsf"
        vtv2.text = "又用到了倒计时，好尴尬，不知道大家每次遇到倒计时后的时候都是怎么做的。我想吧，这东西要写真的很简单，起一个线程不停的–就搞定的事情。"
        vtv3.text = "fdaljfdkjfdaj对肌肤的开发建设独立空间发呆看风景啊来得及啊空间发呆咖啡看电视dafdafdadsf"

    }
}
