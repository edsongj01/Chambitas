package com.pds.chambitas.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pds.chambitas.MainActivity
import com.pds.chambitas.R
import com.pds.chambitas.databinding.FragmentGalleryBinding
import com.pds.chambitas.splash.SplashActivity
import com.pds.chambitas.ui.slideshow.SlideshowFragment
import kotlinx.android.synthetic.main.fragment_cambiocontra.view.*

class GalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root:View = inflater.inflate(R.layout.fragment_gallery, container, false)



        return root
    }
}