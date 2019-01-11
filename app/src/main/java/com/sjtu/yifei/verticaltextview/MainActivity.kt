package com.sjtu.yifei.verticaltextview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sAdapter = CourseItemAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sAdapter
        }
        sAdapter.addDatas(getList())
    }

    private fun getList(): ArrayList<String> {
        val homeDataList: ArrayList<String> = ArrayList();
        homeDataList.add("在Android studio 的Terminal终端输入以下指令，可以查看")
        homeDataList.add("我们都知道自定义view绘制当中最重要的有三个方法，onMeasure onLayout onDraw")
        homeDataList.add("view绘制过程中为了确认view大小，会多次重复调用onMeasure。这跟view绘制机制有关，有兴趣可以去官网了解How Android Draws Views")
        homeDataList.add("onWindowFocusChanged是判断view是否获取焦点，参数hasWindowFocus 对应返回true 和false 可以用该方法判断view进出后台")
        homeDataList.add("onDetachedFromWindow当activity销毁之后，view会从window上抽离，此时view销毁。")
        homeDataList.add("onAttachedToWindow是将view绑定到activity所在window，附加到window后，程序开始进行所有view的绘制。")
        homeDataList.add("view的绘制发生在activity onResume之后，确切来说是在onAttachedToWindow之后")
        homeDataList.add("当从xml中加载完成后，调用onFinishInflate 方法，这时view完成初始准备环节。")
        homeDataList.add("如图所示，一开始view在xml中加载，调用构造函数CustomView(Context contextt, AttributeSet attrs)")
        homeDataList.add("监测绘")
        homeDataList.add("定义一个自定义CustomView，在xml中加载，打印MainActivity和view各个生命周期方法，对比如图显示")
        homeDataList.add("中国是世界四大文明古国之一，有五千年的文明史，文化传统和历史积淀渊源流长。单就民俗一项，就五花八门、千奇百怪。")
        homeDataList.add("其中有些是古人长期历史经验的总结，是对不利或不可掌控因素的趋利避害，包含了朴素的科学道理，作为民族的优良传统我们有必要继承和发展。")
        homeDataList.add("有的则纯粹是封建迷信或无稽之谈，可以算做一种“陋习”")
        homeDataList.add("我们大可以不以为然，相信随着时代的发展这些“陋习”迟早会湮灭于无形。比如所谓“正月剃头死舅舅”就是一例。")
        homeDataList.add("不少80后和90后在小时候听家里人说正月不能剪头发，因为长辈告诉他们“正月理发会死舅舅”。")
        homeDataList.add("至于为什么有这种说法？大人们往往也不知其详")
        homeDataList.add("只是“老辈传下来就是这样的！”、“少废话，照着做就是了！”等这类搪塞式的回答。")
        homeDataList.add("少数喜欢刨根问底的“熊孩子”问多了或许还遭到训斥。")
        homeDataList.add("日常生活中也时常有因谨守传统的舅舅碰到“叛逆”的外甥正月里理了发而发生争执、闹得不愉快的情况")
        homeDataList.add("个别的甚至导致甥舅动手。")
        return homeDataList
    }

    class CourseItemAdapter : RecyclerView.Adapter<VHCourse>() {
        private var homeDataList: ArrayList<String> = ArrayList()

        fun addDatas(data: ArrayList<String>) {
            homeDataList.clear()
            homeDataList.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHCourse =
            VHCourse.create(parent)

        override fun getItemCount() = homeDataList.size

        override fun onBindViewHolder(holder: VHCourse, position: Int) {
            holder.bind(homeDataList[position])
        }

    }

    class VHCourse(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: String) {
            itemView.course_title.text = item
            itemView.title.text = item
        }

        companion object {
            fun create(parent: ViewGroup): VHCourse {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
                return VHCourse(view)
            }
        }
    }
}
