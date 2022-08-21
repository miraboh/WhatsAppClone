package com.olisemeka.whatsappclone.ui.calls

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.olisemeka.whatsappclone.MainActivity
import com.olisemeka.whatsappclone.R
import com.olisemeka.whatsappclone.databinding.FragmentCallsBinding
import com.olisemeka.whatsappclone.datasource.DataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CallsFragment : Fragment() {
    private var _binding: FragmentCallsBinding? = null
    private val binding get() = _binding!!

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    private var callsMenuId: Int = R.menu.menu_calls

    @Inject
    lateinit var prefs: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CallListAdapter(requireContext(), DataSource.loadCalls())
        binding.recyclerviewCalls.adapter = adapter

        binding.callFab.setOnClickListener {
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_viewPager_to_newCallFragment)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            mainActivity.refreshMenu()
            prefs.edit {
                it[preferencesKey<Int>("menu")] = callsMenuId
                Log.d("-------CALLS-------", "$callsMenuId")
            }
        }
    }
}