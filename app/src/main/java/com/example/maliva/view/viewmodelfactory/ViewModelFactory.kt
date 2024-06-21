package com.example.maliva.view.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.maliva.data.di.Injection
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.view.filter.FilterViewModel
import com.example.maliva.view.gallery.GalleryViewModel
import com.example.maliva.view.home.HomeViewModel
import com.example.maliva.view.login.LoginViewModel
import com.example.maliva.view.planner.PlannerViewModel
import com.example.maliva.view.profile.ProfileViewModel
import com.example.maliva.view.profilelogin.ProfileLoginViewModel
import com.example.maliva.view.register.RegisterViewModel
import com.example.maliva.view.review.ReviewsViewModel
import com.example.maliva.view.search.SearchViewModel
import com.example.maliva.view.trip.TripViewModel
import com.example.maliva.view.tripresult.TripResultViewModel

class ViewModelFactory private constructor(
    private val destinationRepository: DestinationRepository,
    private val loginPreferences: LoginPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(ProfileLoginViewModel::class.java) -> {
                ProfileLoginViewModel(destinationRepository, loginPreferences) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(FilterViewModel::class.java) -> {
                FilterViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(ReviewsViewModel::class.java) -> {
                ReviewsViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(GalleryViewModel::class.java) -> {
                GalleryViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(PlannerViewModel::class.java) -> {
                PlannerViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(TripResultViewModel::class.java) -> {
                TripResultViewModel(destinationRepository) as T
            }
            modelClass.isAssignableFrom(TripViewModel::class.java) -> {
                TripViewModel(destinationRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                val repository = Injection.provideRepository(context)
                val loginPreferences = Injection.provideLoginPreferences(context)
                INSTANCE ?: ViewModelFactory(repository, loginPreferences).also {
                    INSTANCE = it
                }
            }
        }
    }
}