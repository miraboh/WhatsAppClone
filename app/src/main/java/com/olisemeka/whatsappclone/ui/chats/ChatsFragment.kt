package com.olisemeka.whatsappclone.ui.chats

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.olisemeka.whatsappclone.MainActivity
import com.olisemeka.whatsappclone.R
import com.olisemeka.whatsappclone.databinding.FragmentChatsBinding
import com.olisemeka.whatsappclone.datasource.DataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }
    private var chatMenuId: Int = R.menu.menu_chats

    @Inject
    lateinit var prefs: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val chatsRecyclerView = binding.recyclerviewChats

        val chatList = DataSource.loadChats()
        val adapter = ChatListAdapter(context, chatList)
        chatsRecyclerView.adapter = adapter

        binding.chatFab.setOnClickListener {
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_viewPager_to_newChatFragment)

        }
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
                it[preferencesKey<Int>("menu")] = chatMenuId
                Log.d("-------CHAT-------", "$chatMenuId")
            }
        }
    }
}