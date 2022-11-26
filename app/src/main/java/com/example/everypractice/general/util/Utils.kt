package com.example.everypractice.general.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.everypractice.R
import com.example.everypractice.prinoptions.search.recycler.TimeHelper
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

/**
 * Truncate long text with a preference for word boundaries and without trailing punctuation.
 */
fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append("...")
    }
    return builder.toString()
}

/**
 * Function that obtain a bitmap from url
 * */
suspend fun getBitmap(url: String, context: Context): Bitmap {
    val loading = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .build()
    return try {
        val result = (loading.execute(request) as SuccessResult).drawable
        (result as BitmapDrawable).bitmap
    }catch (e: Exception){
        Timber.d("Error al obtener el bitmap, se coloco uno por defecto")
        getBitmapFromDraw(context)
    }

}

/**
 * Function that obtain a bitmap from Drawable
 * */
fun getBitmapFromDraw(context: Context): Bitmap {
    return BitmapFactory.decodeResource(context.resources, R.drawable.collection_background_path_sample)
}

/**
 * Function that modify the visibility
 */
fun Button.alternateVisibility(){
    when(visibility){
        View.VISIBLE -> visibility = View.GONE
        View.GONE -> visibility = View.VISIBLE
        View.INVISIBLE -> {
        }
    }
}

fun showSnackBar(
    view: View,
    message: String,
    anchorView: View? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionBtnText: String = "NA",
    action: () -> Unit = {}
) {
    Snackbar.make(view, message, duration).apply {
        this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        if (null != anchorView) this.anchorView = anchorView
        if ("NA" != actionBtnText) setAction(actionBtnText) { action.invoke() }
        this.show()
    }
}


/**
 *  Function that return the same list to Strings Genres
 */
fun List<Int>.getTransformationIdsToGenres(): List<String> {
    return map {
        when(it){
            28 -> "Action"
            12 -> "Adventure"
            16 -> "Animation"
            35 -> "Comedy"
            80 -> "Crime"
            99 -> "Documentary"
            18 -> "Drama"
            10751 -> "Family"
            14 -> "Fantasy"
            36 -> "History"
            27 -> "Horror"
            10402 -> "Music"
            9648 -> "Mystery"
            10749 -> "Romance"
            878 -> "Science Fiction"
            10770 -> "TV Movie"
            53 -> "Thriller"
            10752 -> "War"
            37 -> "Western"
            else -> "No genre"
        }
    }
}

fun EditText.onSearch(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (this.text.toString().trim().isNotEmpty()) {
                callback.invoke()
            }
            return@setOnEditorActionListener true
        } else false
    }
}

fun getTimeToText(runtime: Int) : String {
    val minutes = runtime%60
    val hours = (runtime-minutes)/60

    return "${hours}h ${minutes}m"
}

