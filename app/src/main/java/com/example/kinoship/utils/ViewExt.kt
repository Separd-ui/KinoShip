package com.example.kinoship.utils

import android.view.View
import android.view.animation.Animation

fun View.startCustomAnim(animation: Animation, action:()->Unit){
    animation.setAnimationListener(object:Animation.AnimationListener{
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?){
            action()
        }

        override fun onAnimationRepeat(animation: Animation?)= Unit

    })
    this.startAnimation(animation)
}