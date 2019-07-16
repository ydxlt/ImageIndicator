package org.yzjt.library.indicator

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import org.yzjt.library.R

/**
 * Created by LT on 2019/7/16.
 */
open class ImagePagerIndicator @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(mContext, attrs, defStyleAttr) {

    /**
     * 选中与没有选中的颜色
     */
    private var mHigh_light_color: Int = 0
    private var mNumal_color: Int = 0

    /**
     * tab标题的宽度（文字部分）
     */
    private var mTabWidth: Int = 0
    private var mPaint: Paint = Paint()

    /**
     * 指示器初始时的偏移量
     */
    private var mStartTranslateX: Int = 0

    /**
     * 指示器跟随移动的偏移量
     */
    private var mTranslateX: Int = 0

    /**
     * 可见tab标题的数量
     */
    private var mTabVisibleCount: Int = DEFAULT_TAB_VISIBLE_COUNT

    private lateinit var mViewPager: ViewPager
    private var mOnPageChangeListener: ViewPager.OnPageChangeListener? = null

    /**
     * 指示器图片
     */
    private var mImageBitmap: Bitmap

    /**
     * 指定图片指示器和容器的最大高度比率
     */
    private var mImageHeightRadio: Float = 0f


    /**
     * 指示器的宽高
     */
    private var mIndicatorHeight: Int = 0
    private var mIndicatorWidth: Int = 0

    init {
        // 初始化画笔
        mPaint.isAntiAlias = true
        mPaint.pathEffect = CornerPathEffect(3f) // 设置画笔平滑圆角，不然看起来尖锐
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.BLUE
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImagePagerIndicator)
        // 获取自定义属性
        mHigh_light_color = typedArray.getColor(R.styleable.ImagePagerIndicator_tab_select_color, Color.RED)
        mNumal_color = typedArray.getColor(R.styleable.ImagePagerIndicator_tab_unselect_color, Color.GRAY)
        mTabVisibleCount =
            typedArray.getInteger(R.styleable.ImagePagerIndicator_tab_visiable_count, DEFAULT_TAB_VISIBLE_COUNT)
        mImageHeightRadio =
            typedArray.getFloat(R.styleable.ImagePagerIndicator_image_radio_height, DEFAULT_IMAGE_HEIGHT_RADIO)
        mIndicatorWidth =
            typedArray.getDimension(
                R.styleable.ImagePagerIndicator_indicator_width,
                0f
            ).toInt()
        mIndicatorHeight =
            typedArray.getDimension(
                R.styleable.ImagePagerIndicator_indicator_height,
                dp2px(mContext, DEFAULT_INDICATOR_HEIGHT).toFloat()
            ).toInt()
        val drawable = typedArray.getDrawable(R.styleable.ImagePagerIndicator_tab_indicator)
        if (mTabVisibleCount < 1) {
            mTabVisibleCount = 1 // 指定最小可见数量为1
        }
        // Drawable转Bitmap
        when (drawable) {
            is BitmapDrawable -> {
                Log.i(TAG,"is BitmapDrawable")
                mImageBitmap = drawable.bitmap
                mIndicatorHeight = mImageBitmap.height
                mIndicatorWidth = mImageBitmap.width
            }
            is ColorDrawable -> {
                Log.i(TAG,"is ColorDrawable")
                if(mIndicatorWidth == 0){
                    mIndicatorWidth = WIDTH_MATCH_TAB
                }
                mImageBitmap = Bitmap.createBitmap(
                    mIndicatorWidth, mIndicatorHeight,
                    Bitmap.Config.ARGB_8888
                )
                mImageBitmap.eraseColor(drawable.color)//填充颜色
            }
            else -> {
                Log.i(TAG,"un know")
                if(mIndicatorWidth == 0) {
                    mIndicatorWidth = WIDTH_MATCH_TAB
                }
                mImageBitmap = Bitmap.createBitmap(
                    mIndicatorWidth, mIndicatorHeight,
                    Bitmap.Config.ARGB_8888
                )
                mImageBitmap.eraseColor(mHigh_light_color)//填充颜色
            }
        }
        typedArray.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTabWidth = width / mTabVisibleCount
        if (mIndicatorWidth > mTabWidth || mIndicatorWidth == WIDTH_MATCH_TAB) {
            mIndicatorWidth = mTabWidth
        }
        Log.i(TAG,"mIndicatorWidth:$mIndicatorWidth")
        Log.i(TAG,"mTabWidth:$mTabWidth")
        val maxIndicatorHeight = (h * mImageHeightRadio).toInt()
        if (mIndicatorHeight > maxIndicatorHeight) {
            mIndicatorHeight = maxIndicatorHeight
        }
        Log.i(TAG, "mIndicatorHeight$mIndicatorHeight")
        // 计算指示器偏移量
        mStartTranslateX = mTabWidth / 2 - mIndicatorWidth / 2
        post { correctTabWidth() }
    }

    private fun scroll(position: Int, offset: Float) {
        // 实现指示器的滚动
        mTranslateX = (mTabWidth * (offset + position)).toInt()
        invalidate()
        // 实现容器联动
        Log.i(TAG, position.toString() + "%" + mTabVisibleCount + ":" + position % mTabVisibleCount)
        // 什么时候容器需要滚动？
        if (offset > 0 && childCount > mTabVisibleCount && position > mTabVisibleCount - 2) {
            this.scrollTo((position - mTabVisibleCount + 1) * mTabWidth + (offset * mTabWidth).toInt(), 0)
        }
    }

    fun setViewPager(viewPager: ViewPager, pos: Int) {
        mViewPager = viewPager
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener!!.onPageScrollStateChanged(state)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }
                Log.i("onPageScrolled():", "positionOffset:$positionOffset")
                this@ImagePagerIndicator.scroll(position, positionOffset)
            }

            override fun onPageSelected(position: Int) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener!!.onPageSelected(position)
                }
                resetTextColor()
                highlightTextColor(position)
            }
        })
        mViewPager.currentItem = pos
        resetTextColor()
        highlightTextColor(pos)
        setTabClickListener()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.save()
        // 平移画布
        canvas.translate((mStartTranslateX + mTranslateX).toFloat(), (height - mIndicatorHeight).toFloat())
        // 设置图片的裁剪区域，为null则不裁剪
        val src = Rect(0, 0, mIndicatorWidth, mIndicatorHeight)
        // 设置图片为画布中显示的区域，由于将画布平移了，这里和图片的裁剪区域一致
        val dest = Rect(0, 0, mIndicatorWidth, mIndicatorHeight)
        // 绘制图片
        canvas.drawBitmap(mImageBitmap, src, dest, mPaint)
        canvas.restore()
    }

    private fun setTabClickListener() {
        for (i in 0 until childCount) {
            getChildAt(i).setOnClickListener { mViewPager.currentItem = i }
        }
    }

    private fun highlightTextColor(position: Int) {
        var view = getChildAt(position)
        if (view is TextView) {
            view.setTextColor(mHigh_light_color)
        }
    }

    private fun resetTextColor() {
        for (i in 0 until childCount) {
            var view = getChildAt(i)
            if (view is TextView) {
                view.setTextColor(mNumal_color)
            }
        }
    }

    /**
     * 设置指示器的宽度
     */
    fun setIndicatorWidth(indicatorWidth: Int) {
        this.mIndicatorWidth = indicatorWidth
    }

    /**
     * 设置指示器的高度
     */
    fun setIndicatorHeight(indicatorHeight: Int) {
        this.mIndicatorHeight = indicatorHeight
    }

    /**
     * 代码设置tab的标题，及文字大小(单位为sp）
     * @param titles
     * @param textSize
     */
    fun setTabTitles(titles: Array<String>, textSize: Float) {
        if (titles.isEmpty()) {
            return
        }
        // 动态添加title，这里讲是的布局设置的tab标题全部移除
        removeAllViews()
        for (title in titles) {
            val textView = TextView(context)
            textView.setLines(1)
            textView.text = title
            textView.gravity = Gravity.CENTER
            textView.setTextColor(mNumal_color)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            val layoutParams = LayoutParams(mTabWidth, ViewGroup.LayoutParams.MATCH_PARENT)
            addView(textView, layoutParams)
        }
    }

    /**
     * 代码改变tab标题的布局宽度，防止布局中为不同的title设置不同的宽度
     */
    private fun correctTabWidth() {
        if (childCount == 0) {
            return
        }
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams as LayoutParams
            if (child is TextView) {
                child.gravity = Gravity.CENTER
            }
            layoutParams.width = mTabWidth
            child.layoutParams = layoutParams
        }
    }

    companion object {
        const val TAG = "ImagePagerIndicator"
        const val DEFAULT_IMAGE_HEIGHT_RADIO = 1 / 4f
        const val DEFAULT_TAB_VISIBLE_COUNT = 3
        const val WIDTH_MATCH_TAB = 2
        const val DEFAULT_INDICATOR_HEIGHT = 6f
        const val DEFAULT_INDICATOR_WIDTH = DEFAULT_INDICATOR_HEIGHT * 2f

        /**
         * dp转 px.
         * @param value the value
         * @return the int
         */
        fun dp2px(context: Context, value: Float): Int {
            val scale = context.resources.displayMetrics.densityDpi.toFloat()
            return (value * (scale / 160) + 0.5f).toInt()
        }
    }

}