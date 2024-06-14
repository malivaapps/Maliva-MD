// DescriptionFragment.kt
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

        fun newInstance(description: String?, link: String?): DescriptionFragment {
            val fragment = DescriptionFragment()
            val args = Bundle()
            args.putString(ARG_DESCRIPTION, description)
            args.putString(ARG_LINK, link)
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
        val button: Button = view.findViewById(R.id.mapButton)

        val description = arguments?.getString(ARG_DESCRIPTION)
        val link = arguments?.getString(ARG_LINK)

        descriptionTextView.text = description
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            descriptionTextView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }
    }
}