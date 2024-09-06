package com.example.schedule.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.schedule.R
import com.example.schedule.databinding.ActivityMainBinding
import com.example.schedule.presentation.days.schedule.DaysScheduleFragment
import com.example.schedule.presentation.search.SearchFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(R.layout.activity_main), NavigationBarView.OnItemSelectedListener {
    private val binding by viewBinding(ActivityMainBinding::bind, R.id.root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceMainContainer<DaysScheduleFragment>()
        binding.bottomNavigation.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_home -> {
                replaceMainContainer<DaysScheduleFragment>()
                true
            }

            R.id.menu_search -> {
                replaceMainContainer<SearchFragment>()
                true
            }

            else -> false
        }

    private inline fun <reified F : Fragment> replaceMainContainer() {
        supportFragmentManager.commit {
            replace<F>(R.id.container_main)
        }
    }
}