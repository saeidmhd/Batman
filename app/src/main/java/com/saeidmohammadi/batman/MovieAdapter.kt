package com.saeidmohammadi.batman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class MovieAdapter(private val onItemClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var movies: List<Movie> = listOf()

    fun setData(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { onItemClick(movie) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = movie.Title
            itemView.findViewById<TextView>(R.id.yearTextView).text = movie.Year
            itemView.findViewById<TextView>(R.id.typeTextView).text = movie.Type

            // ImageView برای نمایش پوستر فیلم
            val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)

            // آدرس پوستر فیلم
            val posterUrl = movie.Poster

            // تنظیمات بارگذاری Glide
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_foreground) // تصویر پیش‌فرض برای نمایش تا زمانی که تصویر فیلم بارگذاری نشده است
                .error(R.drawable.ic_launcher_background) // تصویر نشان داده می‌شود در صورتی که بارگذاری تصویر با مشکل مواجه شود

            // بارگذاری تصویر با استفاده از Glide
            Glide.with(itemView.context)
                .load(posterUrl)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade()) // افکت انتقال تصویر
                .into(posterImageView)
        }
    }
}
