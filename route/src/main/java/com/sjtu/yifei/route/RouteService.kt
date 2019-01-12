package com.sjtu.yifei.route

import com.sjtu.yifei.annotation.Go

import com.sjtu.yifei.route.RoutePath.PATH_LISTACTIVITY

/**
 * [description]
 * author: yifei
 * created at 2019/1/12 下午2:30
 */
interface RouteService {

    @Go(PATH_LISTACTIVITY)
    fun openListActivity(): Boolean
}
