package com.example.kinoship.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinoship.R
import com.example.kinoship.databinding.MovieItemBinding
import com.example.kinoship.models.Result
import com.example.kinoship.utils.Constans
import com.example.kinoship.utils.getGenre
import com.example.kinoship.utils.getRating

class MovieAdapter(val context: Context,val onMovieListener:OnMovieListener):RecyclerView.Adapter<MovieAdapter.ViewHolderData>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderData {
        val view=LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false)
        return ViewHolderData(view)
    }

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    inner class ViewHolderData(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        private val binding=MovieItemBinding.bind(itemView)
        fun setData(result: Result)= with(binding){
            movieTitle.text=result.title
            val genre= getGenre(result.genre_ids)
            movieJanre.text=genre
            Glide.with(context).load(Constans.URL_IMAGE.plus(result.poster_path)).into(movieImage)
            ratingBar.rating= getRating(result.vote_average)
            movieDate.text=result.release_date
        }

        override fun onClick(v: View?) {
            onMovieListener.onMovieClick(differ.currentList[adapterPosition])
        }


    }
    private val diffUtil=object :DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.poster_path==newItem.poster_path
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffUtil)

    interface OnMovieListener{
        fun onMovieClick(movie: Result)
    }
}