package com.olisemeka.whatsappclone.ui.status

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import com.olisemeka.whatsappclone.MainActivity
import com.olisemeka.whatsappclone.R
import com.olisemeka.whatsappclone.databinding.FragmentStatusBinding
import com.olisemeka.whatsappclone.datasource.DataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StatusFragment : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }
    private var statusMenuId: Int = R.menu.menu_status

    @Inject
    lateinit var prefs: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = StatusListAdapter(DataSource.loadStatuses())
        binding.recyclerviewStatuses.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            mainActivity.refreshMenu()
            prefs.edit {
                it[preferencesKey<Int>("menu")] = statusMenuId
                Log.d("-------STATUS-------", "$statusMenuId")
            }
        }
    }
}