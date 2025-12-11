package com.example.machineround.machinetask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.machineround.databinding.ActivityHomeBinding
import com.example.machineround.machinetask.fragments.AuditTrailTimeline
import com.example.machineround.machinetask.fragments.Documents
import com.example.machineround.machinetask.fragments.K_Y_C
import com.example.machineround.machinetask.fragments.LoanSummery
import com.example.machineround.machinetask.fragments.Profile
import com.example.machineround.machinetask.fragments.VehicleInspection
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


private const val NUM_TABS = 6

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return Profile()
            1 -> return Documents()
            2 -> return K_Y_C()
            3 -> return LoanSummery()
            4 -> return AuditTrailTimeline()
            5 -> return VehicleInspection()

        }
        return AuditTrailTimeline()
    }
}


class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var adapter: ViewPagerAdapter
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    val tabNames = arrayOf(
        "Profile",
        "Documents", "KYC", "Loan Summery", "Audit Trail Timeline", "Vehicle Inspection"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()


        viewPager.isUserInputEnabled = true
    }
}