package com.example.kinoship.ui.other

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.kinoship.R
import com.example.kinoship.adapters.PagerAdapter
import com.example.kinoship.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var currentPosition=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSplashScreen()
        addDots(0)

    }

    private fun addDots(position:Int)=with(binding){

        layoutDots.removeAllViews()
        val dots=Array(4){
            val dot=TextView(this@SplashActivity)
            dot.apply {
                text=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml("&#8226;",Html.FROM_HTML_MODE_LEGACY)
                }
                else{
                    Html.fromHtml("&#8226;")
                }
                textSize=35f
                setTextColor(ContextCompat.getColor(this@SplashActivity,R.color.grey))
            }
            layoutDots.addView(dot)

            return@Array dot
        }

        dots[position].setTextColor(ContextCompat.getColor(this@SplashActivity,R.color.black))

    }
    private fun setupSplashScreen() = with(binding){
        viewPager2.apply {
            val pagerAdapter=PagerAdapter(this@SplashActivity)
            adapter=pagerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    /*EMPTY*/
                }

                override fun onPageSelected(position: Int) {
                    addDots(position)
                    currentPosition=position
                    if(position==0){
                        bBack.visibility=View.GONE
                    }else if(position==1){
                        bBack.visibility=View.VISIBLE
                    }else if(position==2){
                        if(bNext.text.equals("Let's go"))
                            bNext.text="Next"
                    }
                    else{
                        if(bNext.text.equals("Next"))
                            bNext.text="Let's go"
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    /*EMPTY*/
                }

            })
        }

    }

    override fun onStart()=with(binding) {
        super.onStart()
        bNext.setOnClickListener {
            if(bNext.text.equals("Next"))
                viewPager2.currentItem=currentPosition+1
            else
                intentToMainActivity()
        }
        bBack.setOnClickListener {
            viewPager2.currentItem=currentPosition-1
        }
        bSkip.setOnClickListener {
            intentToMainActivity()
        }
    }

    private fun intentToMainActivity() = Intent(this,MainActivity::class.java).also {
        startActivity(it)
        finish()
    }

}