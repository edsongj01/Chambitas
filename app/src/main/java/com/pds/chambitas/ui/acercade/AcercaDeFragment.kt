package com.pds.chambitas.ui.acercade

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pds.chambitas.R
import androidx.lifecycle.Observer
import com.pds.chambitas.databinding.FragmentAcercaDeBinding
import com.pds.chambitas.databinding.FragmentHomeBinding
import com.pds.chambitas.ui.home.HomeViewModel

class AcercaDeFragment : Fragment() {

    private lateinit var acercaDeViewModel: AcercaDeViewModel
    private var _binding: FragmentAcercaDeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        acercaDeViewModel =
            ViewModelProvider(this).get(AcercaDeViewModel::class.java)

        _binding = FragmentAcercaDeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAcercade
        acercaDeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}