package com.puneetkaur.movies.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.puneetkaur.movies.R
import com.puneetkaur.movies.network.model.api.model.Result
import com.puneetkaur.movies.utils.Constants

class MovieAdapter(private var movies: List<Result>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewPoster)
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            return ViewHolder(view)
        }

        private var onItemClick: ((Result) -> Unit)? = null
        fun setItemClickListener(listener: (Result) -> Unit) {
            onItemClick = listener
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val movie = movies[position]
            Glide.with(holder.itemView.context)
                .load(Constants.paster_image_path + movie.poster_path)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                onItemClick?.let {
                    it(movie)
                }
            }
        }

        override fun getItemCount(): Int = movies.size

        fun submitList(newItems: List<Result>) {
            movies = newItems
            notifyDataSetChanged()
        }
    }

