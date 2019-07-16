package org.yzjt.weiget.indicator

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * Created by LT on 2019/7/16.
 */
class TextFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var textView = TextView(context)
        var title = arguments!!.getString("title")
        textView.gravity = Gravity.CENTER
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f)
        textView.text = title
        return textView
    }
}