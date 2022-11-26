package com.example.everypractice.prinoptions.search

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.everypractice.R
import com.example.everypractice.prinoptions.search.data.LastSearch
import com.example.everypractice.prinoptions.search.recycler.TimeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@BindingAdapter("timestamp", "scope")
fun liveText(textView: TextView?, timestamp: LastSearch, scope: CoroutineScope) {

    scope.launch {
            do  {
                Log.d("ccc", "view: ${textView} ")
                val currentTime = System.currentTimeMillis()
                val timeShown = getTimeFromTimestamp(currentTime - timestamp.timestamp).number
                textView?.text = timeShown.toString()
                Log.d("ddd", "getText:${timestamp.lastSearch} -- ${textView?.text} ")
                Log.d("ddd", "------------------------------- ")
                delay(1000L)
                if (timestamp==null){
                    break
                }
            } while (textView!=null)
        }

}

//falta implementar
/*@BindingAdapter("image","image_source","context")
fun blurImage(imageView: ImageView,idSource:Drawable,context:Context){

    Glide.with(context).asBitmap().load(idSource).into(object : CustomTarget<Bitmap>(2,2){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            imageView.setImageDrawable(BitmapDrawable(resource))
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            imageView.setImageDrawable(null)
        }
    }
    )
}*/

private fun getTimeFromTimestamp(unformattedTimestamp: Long) : TimeHelper {
    val seconds = unformattedTimestamp / 1000
    val minutes = seconds.toInt() / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = weeks / 4
    val years = months / 12
    val dumpList = listOf(
        TimeHelper(years, "Years"),
        TimeHelper(months, "Months"),
        TimeHelper(weeks, "Weeks"),
        TimeHelper(days, "Days"),
        TimeHelper(hours, "Hours"),
        TimeHelper(minutes, "Min"),
        TimeHelper(seconds.toInt(), "Sec")
    )
    Log.d("ccc", "list of times: $dumpList")
    try {
        return run loop@{
            dumpList.forEach { element ->
                if (element.number != 0) {
                    Log.d("ccc", "return $element")
                    return@loop element
                }
            }
        } as TimeHelper
    } catch (e: Exception) {
        Log.d("ccc", "error cast to TimeHelper: ${e.message}")
        return TimeHelper(1, "Sec")
    }
}