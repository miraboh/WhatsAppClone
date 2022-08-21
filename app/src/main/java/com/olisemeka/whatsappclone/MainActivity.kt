package com.olisemeka.whatsappclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayout
import com.olisemeka.whatsappclone.databinding.ActivityMainBinding
import com.olisemeka.whatsappclone.ui.calls.CallsFragment
import com.olisemeka.whatsappclone.ui.calls.NewCallFragment
import com.olisemeka.whatsappclone.ui.chats.ChatsFragment
import com.olisemeka.whatsappclone.ui.chats.NewChatFragment
import com.olisemeka.whatsappclone.ui.status.StatusFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private var optionMenu: Int? = null

    @Inject
    lateinit var prefs: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        //setSupportActionBar(toolbar)

        lifecycleScope.launch {
            prefs.data.collectLatest {
                if (it[preferencesKey<Int>("menu")] != null) {
                    optionMenu = it[preferencesKey("menu")]!!
                } else {
                    optionMenu = R.menu.toolbar_menu
                }
            }
        }

        Log.d("-------MAINACTIVITY-------", "$optionMenu")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(optionMenu!!, menu)
        return true
    }

    suspend fun refreshMenu(){
        setSupportActionBar(toolbar)
    }

}