package com.tp.recyclertree

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.tp.recyclertree.databinding.ActivityRecyclerTreeSampleBinding

class RecyclerTreeSampleActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecyclerTreeSampleBinding

    external fun stringFromJNI(): String



    companion object {
        // Used to load the 'samplenative' library on application startup.
        init {
            System.loadLibrary("recyclertree")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val r =  (stringFromJNI())


        Log.d("mvv12"," onCreate r : $r" )

        binding = ActivityRecyclerTreeSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_recycler_tree_sample)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_recycler_tree_sample)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}