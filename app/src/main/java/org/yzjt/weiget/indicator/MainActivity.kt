package org.yzjt.weiget.indicator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import org.yzjt.library.indicator.ImagePagerIndicator

class MainActivity : AppCompatActivity() {

    private val titles = arrayOf("热点", "科技", "财经", "NBA", "电竞", "生活")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var viewPager:ViewPager = findViewById(R.id.view_pager)
        var image_indicator1:ImagePagerIndicator = findViewById(R.id.image_indicator1)
        var image_indicator2:ImagePagerIndicator = findViewById(R.id.image_indicator2)
        var image_indicator3:ImagePagerIndicator = findViewById(R.id.image_indicator3)
        var image_indicator4:ImagePagerIndicator = findViewById(R.id.image_indicator4)
        var image_indicator5:ImagePagerIndicator = findViewById(R.id.image_indicator5)
        viewPager.adapter = MyViewPagerAdapter()
        attach(viewPager,image_indicator1)
        attach(viewPager,image_indicator2)
        attach(viewPager,image_indicator3)
        attach(viewPager,image_indicator4)
        attach(viewPager,image_indicator5)
    }

    private fun attach(viewPager:ViewPager,tabIndicator:ImagePagerIndicator){
        tabIndicator.setTabTitles(titles,18f)
        tabIndicator.setViewPager(viewPager,0)
    }

    inner class MyViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {

        override fun getCount(): Int {
            return titles.size
        }

        override fun getItem(position: Int): Fragment {
            var bundle = Bundle()
            bundle.putString("title",titles[position])
            var textFragment = TextFragment()
            textFragment.arguments = bundle
            return textFragment
        }
    }
}
