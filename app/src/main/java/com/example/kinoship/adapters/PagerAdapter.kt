package com.example.kinoship.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.kinoship.R
import com.example.kinoship.databinding.PagerItemBinding

class PagerAdapter(
    private val context: Context
) :PagerAdapter(){

    private val images= arrayOf(R.drawable.popcorn,R.drawable.group,R.drawable.collection,R.drawable.fast)
    private val primaryText= arrayOf("Spend time with your friends",
    "A lot of good comments","An enormous collection of movies","Fast and comfortable navigating")
    private val secondaryText= arrayOf("Find an interesting movie and call your friends to spend time together.",
    "Every day we achieve a lot of beautiful comments about our application.",
    "You can find every kind of movie you like,because we provide thousands of movies all over the world.",
    "Thanks to comfortable and beautiful design,you can very fast navigate through our application")

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(context).inflate(R.layout.pager_item,container,false)
        val binding=PagerItemBinding.bind(view)

        binding.pagerImage.setImageResource(images[position])
        binding.pagerMessage.text=secondaryText[position]
        binding.pagerTitle.text=primaryText[position]

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}