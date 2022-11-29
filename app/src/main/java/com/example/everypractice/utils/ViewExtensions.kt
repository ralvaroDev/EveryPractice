package com.example.everypractice.helpers.extensions

import android.annotation.SuppressLint
import android.view.View

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

@SuppressLint("SwitchIntDef")
fun View.alternateGoneVisible(){
    when (this.visibility) {
        View.VISIBLE -> this.gone()
        View.GONE -> this.visible()
    }
}