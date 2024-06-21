package com.example.maliva.adapter.profil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.example.maliva.data.response.SignInResponse


class ProfileAdapter(private val signInResponse: SignInResponse) :
RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_profile_login, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(signInResponse)
    }

    override fun getItemCount(): Int {
        return 1 // karena Anda hanya ingin menampilkan satu item (satu user)
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        private val tvEmail: TextView = itemView.findViewById(R.id.tv_Email)

        fun bind(signInResponse: SignInResponse) {
            tvUsername.text = signInResponse.data?.username ?: "Username Not Found"
            tvEmail.text = signInResponse.data?.email ?: "Email Not Found"
        }
    }
}