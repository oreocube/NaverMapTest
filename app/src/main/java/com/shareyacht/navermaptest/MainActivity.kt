package com.shareyacht.navermaptest

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.shareyacht.navermaptest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    lateinit var mBinding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private val points: MutableList<LatLng> = mutableListOf()
    private val path = PathOverlay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val fm: FragmentManager = supportFragmentManager
        var mapFragment: MapFragment? = fm.findFragmentById(R.id.map) as MapFragment
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance()
            fm.beginTransaction().add(R.id.map, mapFragment).commit()
        }
        mapFragment!!.getMapAsync(this)

        initPath()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.setOnMapClickListener { point, coord ->
            val marker = Marker()
            marker.position = LatLng(coord.latitude, coord.longitude)
            marker.map = naverMap
            marker.onClickListener = this
            points.add(coord)

            updatePath()
        }

    }

    override fun onClick(overlay: Overlay): Boolean {
        if (overlay is Marker) {
            overlay.map = null
            points.remove(overlay.position)
            updatePath()
            return true
        }
        return false
    }

    private fun initPath() {
        path.outlineWidth = 3
        path.patternImage = OverlayImage.fromResource(R.drawable.ic_path_pattern)
        path.patternInterval = 10
        path.outlineColor = Color.BLUE
        path.color = Color.BLUE
    }

    private fun updatePath() {
        if (points.size < 2) {
            hideButton()
            path.map = null
        } else {
            showButton()
            path.coords = points
            path.map = naverMap
        }
    }

    private fun hideButton() {
        mBinding.submitButton.isEnabled = false
    }

    private fun showButton() {
        mBinding.submitButton.isEnabled = true
    }

    fun makeJson(view: View) {
        val data = mutableListOf<Point>()
        for (i in 0 until points.size) {
            data.add(Point(i, points[i].latitude.toString(), points[i].longitude.toString()))
        }

        val pointsList = Points(data)
        val jsonString = Gson().toJson(pointsList)

        Log.d("태그", jsonString)
    }
}