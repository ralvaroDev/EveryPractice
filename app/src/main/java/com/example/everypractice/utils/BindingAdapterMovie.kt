package com.example.everypractice.ui.movies

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.bumptech.glide.Glide
import com.example.everypractice.R

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(imageView)
            .load(it)
            .into(imageView)
    }
}

@BindingAdapter("bindImage")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        imgView.load(it){
            error(R.drawable.ic_not_found)
        }
    }
}

@BindingAdapter("bindTitle")
fun bindTitle(txtView: TextView, title: String?){
    title?.let {
        txtView.text = title
    }
}

@BindingAdapter("bindgGenreIds")
fun bindgGenreIds(txtView: TextView, genId: List<Int>?){
    genId?.let {
        txtView.text=genId.toList().toString()
    }
}

/*@BindingAdapter("movieRequestStatus")
fun bindImageStatud(statusImageView: Layou, status: RequestMovieStatus?){

    when(status){
        RequestMovieStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.layout.layout_shimmer_content_loader_list_items)
        }

        RequestMovieStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_error_conection)
        }

        RequestMovieStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }

}*/
/*
@BindingAdapter("blurImage")
fun blurImage(imgView: ImageView, imgUrl: String?,context: Context){
    imgView.load(imgUrl){
        transformations(
            BlurTransformation(context, radius = 8f)
        )
            .build()
    }
}
*/
