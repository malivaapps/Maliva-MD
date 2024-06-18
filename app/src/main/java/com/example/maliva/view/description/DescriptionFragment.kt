package com.example.maliva.view.description

import android.content.Intent
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.maliva.R

class DescriptionFragment : Fragment() {

    companion object {
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_LINK = "link"
        private const val ARG_FASILITAS = "Fasilitas"
        private const val ARG_AKSESBILITAS = "Aksesibilitas"

        fun newInstance(description: String?, link: String?, fasilitas: String?, aksesibilitas: String?): DescriptionFragment {
            val fragment = DescriptionFragment()
            val args = Bundle()
            args.putString(ARG_DESCRIPTION, description)
            args.putString(ARG_LINK, link)
            args.putString(ARG_FASILITAS, fasilitas)
            args.putString(ARG_AKSESBILITAS, aksesibilitas)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val fasilitasTextView: TextView = view.findViewById(R.id.fasilitasTextView)
        val aksesibilitasTextView: TextView = view.findViewById(R.id.aksebilitasTextView)

        val button: Button = view.findViewById(R.id.mapButton)

        val description = arguments?.getString(ARG_DESCRIPTION)
        val link = arguments?.getString(ARG_LINK)
        val fasilitas = arguments?.getString(ARG_FASILITAS)
        val aksesibilitas = arguments?.getString(ARG_AKSESBILITAS)

        descriptionTextView.text = description
        fasilitasTextView.text = fasilitas
        aksesibilitasTextView.text = aksesibilitas
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            descriptionTextView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }
    }
}
