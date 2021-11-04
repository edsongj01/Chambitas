package com.pds.chambitas.ui.slideshow

import android.app.FragmentTransaction
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.pds.chambitas.R
import com.pds.chambitas.login.IngresaUsuarioActivity
import kotlinx.android.synthetic.main.fragment_slideshow.*


class SlideshowFragment : Fragment() {

    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.fragment_slideshow, container, false)
        imageView = root.findViewById(R.id.btnCambiocontra)
        imageView!!.setOnClickListener(View.OnClickListener {

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.commit {
                setReorderingAllowed(true)
                replace<CambiocontraFragment>(R.id.fragmentCambiocontra)
            }

            //Ocultar botones
            fragmentCambiocontra.setVisibility(View.VISIBLE)
            textView5.setVisibility(View.GONE)
            linearLayout.setVisibility(View.GONE)
            btnPerfilG.setVisibility(View.GONE)


//            Toast.makeText(
//                context,
//                "HOLAAAA",
//                Toast.LENGTH_LONG
//            ).show()
        })
        return root
    }

}