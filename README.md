# ImageIndicator

[![](https://jitpack.io/v/ydxlt/DownButton.svg)](https://jitpack.io/#ydxlt/DownButton)

一款多功能的炫酷的图片指示器，支持图片，颜色填充指示器:

Step 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ydxlt:ImageIndicator:1.0.0'
	}

## 演示效果

## 使用步骤

Step1：activity_main.xml

	<org.yzjt.library.indicator.ImagePagerIndicator
        android:id="@+id/image_indicator3"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        app:tab_select_color="#0ff"
        app:tab_unselect_color="#000"
        app:tab_visiable_count="3"
        app:tab_indicator="@color/tab_light_blue"
        app:indicator_height="3dp"
        app:indicator_width="20dp"
        app:image_radio_height="0.25"/>

Step2:Init ViewPager And bind ImageIndicator

	private fun attach(viewPager:ViewPager,tabIndicator:ImagePagerIndicator){
        tabIndicator.setTabTitles(titles,18f)
        tabIndicator.setViewPager(viewPager,0)
    }


## 属性说明

	<attr name="tab_select_color" format="color|reference"></attr>
	<attr name="tab_unselect_color" format="color|reference"></attr>
	<attr name="tab_indicator" format="reference|color"></attr>
	<attr name="tab_visiable_count" format="integer"></attr>
	<attr name="image_radio_height" format="float"></attr>
	<attr name="indicator_width" format="dimension"></attr>
	<attr name="indicator_height" format="dimension"></attr>

1. tab_select\_color：The color of tab indicator when select
2. tab_unselect\_color：The color of tab indicator when unselect
3. tab_indicator：The indicator，it can be a color or drawable
4. tab_visiable\_count：The Visible Count of tab,Exceeding will scroll
5. image_radio\_height:The max height radio of image indicator
6. indicator_width:The width of indicator
7. indicator_height:The height of indicator

## Other

- setIndicatorWidth
- setIndicatorHeight
	