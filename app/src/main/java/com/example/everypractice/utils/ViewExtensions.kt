package com.example.everypractice.helpers.extensions

import android.annotation.SuppressLint
import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

@SuppressLint("SwitchIntDef")
fun View.alternateGoneVisible(){
    when (this.visibility) {
        View.VISIBLE -> this.gone()
        View.GONE -> this.visible()
    }
}