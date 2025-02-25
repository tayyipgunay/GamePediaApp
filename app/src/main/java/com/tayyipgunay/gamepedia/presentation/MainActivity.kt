package com.tayyipgunay.gamepedia.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.tayyipgunay.gamepedia.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavController
import com.tayyipgunay.gamepedia.R

@AndroidEntryPoint
     class MainActivity : AppCompatActivity() {

         private lateinit var binding: ActivityMainBinding
            private lateinit var navController: NavController

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                // ViewBinding ile ActivityMainBinding'i bağla
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

                MenuKontrol()
            }


           fun MenuKontrol() {
               binding.bottomNavigationView.setOnItemSelectedListener { item ->
                   // NavController'ı al
                   navController = Navigation.findNavController(this, R.id.fragmentContainerView)

                   // Şu anki fragment'in ID'sini al
                   val currentDestinationId = navController.currentDestination?.id

                   when (item.itemId) {
                       R.id.menu_home -> {
                           println("Menü Home seçildi")
                           if (currentDestinationId != R.id.homeFragment) {
                               // Eğer mevcut fragment Home değilse Home'a git
                               navController.navigate(R.id.homeFragment)
                           }
                           true
                       }

                       R.id.menu_favorites -> {
                           println("Menü Favorites seçildi")
                           if (currentDestinationId != R.id.favoritesFragment) {
                               // Eğer mevcut fragment Favorites değilse Favorites'a git
                               navController.navigate(R.id.favoritesFragment)
                           }
                           true
                       }

                       else -> false
                   }
               }
           }


}






