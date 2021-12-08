package com.pds.chambitas.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.pds.chambitas.R
import kotlinx.android.synthetic.main.fragment_pedirservicio.*
import kotlinx.android.synthetic.main.fragment_pedirservicio.view.*

class PedirservicioFragment : Fragment() {

    private var serviceDetail: ServiceDetail = ServiceDetail(null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if (!bundle.isEmpty) {
                serviceDetail = bundle.getParcelable("serviceDetail")!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_pedirservicio, container, false)

        val nav = Navigation.createNavigateOnClickListener(R.id.action_pedirservicioFragment_to_elegirdestinoFragment)
        root.etxtIngresaDestino.setOnClickListener {
            nav.onClick(it)
        }


        root.btnConfirmarpedirservicio.setOnClickListener {
            if (etxtIngresaDestino.text.isNotEmpty() && etxtIngresaTrabajo.text.isNotEmpty()) {

                serviceDetail.service = etxtIngresaTrabajo.text.toString()
                val nav2 = Navigation.createNavigateOnClickListener(
                    R.id.action_pedirservicioFragment_to_confirmarservicioFragment,
                    bundleOf("serviceDetail" to serviceDetail)
                )

                nav2.onClick(it)
            } else {
                Toast.makeText(activity, "Complete los campos faltantes", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (serviceDetail.direction != null && serviceDetail.direction!!.isNotEmpty()) {
            etxtIngresaDestino.setText(serviceDetail.direction)
        }
    }
}