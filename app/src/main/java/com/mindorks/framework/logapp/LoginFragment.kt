package com.mindorks.framework.logapp

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mindorks.framework.logapp.com.mindorks.framework.logapp.ClientUtil
import com.mindorks.framework.logapp.com.mindorks.framework.logapp.MVPContract
import com.mindorks.framework.logapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.*
import java.math.BigInteger
import java.security.MessageDigest

class LoginFragment : Fragment(), MVPContract.LoginView {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var job = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val mainScope = CoroutineScope(Dispatchers.Main + job)

    // TODO("Change to Server IP")
    private val address = "192.168.100.218"
    private val port = 9999
    private lateinit var client: ClientUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding.logInButton.setOnClickListener {
//            if (validFields()) {
            //binding.textView.text = hash(binding.passwordEditController.text.toString())
            mainScope.launch {
                ioScope.launch {
                    try {
                        client = ClientUtil(address, port, this@LoginFragment)
                        client.run(binding.userNameEditController.text.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.join()
                Toast.makeText(requireContext(), "toast on main thread", Toast.LENGTH_SHORT).show()
            }
            //Toast.makeText(context, "Log in was enabled", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    fun hash(str: String): String {
        //return str.hashCode()
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(str.toByteArray())).toString(16).padStart(32, '0')
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validFields(): Boolean {
        var valid = true
        if (binding.userNameEditController.text.isNullOrEmpty()) {
            binding.userNameEdit.error = "?????????????? ?????? ????????????????????????"
            valid = false
        } else {
            binding.userNameEdit.error = null
        }
        if (binding.passwordEditController.text.isNullOrEmpty()) {
            binding.passwordEdit.error = "?????????????? ????????????"
            valid = false
        } else {
            binding.passwordEdit.error = null
        }
        return valid
    }

    companion object {
        val SERVERPORT = 3003
        val SERVER_IP = "10.1.10.108"
    }

    override fun showName(name: String) {
        mainScope.launch {
            binding.textView.text = "?????? ?????????????? $name"
        }
    }
}