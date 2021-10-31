package com.mindorks.framework.logapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mindorks.framework.logapp.databinding.FragmentLoginBinding
import java.math.BigInteger
import java.security.MessageDigest

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.logInButton.setOnClickListener{
            if(validFields()) {
                binding.textView.text = hash(binding.passwordEditController.text.toString())
                Toast.makeText(context, "Log in was enabled", Toast.LENGTH_SHORT).show()
            }
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

    private fun validFields(): Boolean{
        var valid = true
        if (binding.userNameEditController.text.isNullOrEmpty()){
            binding.userNameEdit.error = "Введите имя пользователя"
            valid = false
        }
        else {
            binding.userNameEdit.error = null
        }
        if (binding.passwordEditController.text.isNullOrEmpty()){
            binding.passwordEdit.error = "Введите пароль"
            valid = false
        }
        else {
            binding.passwordEdit.error = null
        }
        return valid
    }

}