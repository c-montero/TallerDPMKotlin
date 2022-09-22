package com.cmontero.tallerdpmkotlin.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.PatternsCompat
import androidx.navigation.fragment.findNavController
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentLoginBinding

import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEntrar.setOnClickListener {
            validate()
        }

    }

    private fun validate() {
        val result = arrayOf(validateEmail(), validatePassword())
        if (false in result)
            return
        findNavController().navigate(R.id.nav_welcome)
    }

    private fun validateEmail(): Boolean {
        val email = binding.emailUsuario.text.toString()
        return if (email.isEmpty()) {
            binding.emailUsuario.error = "El campo no puede estar vacío"
            false
        }
        else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailUsuario.error = "Escriba un correo electrónico válido"
            false
        } else {
            binding.emailUsuario.error = null
            true
        }

    }

    private fun validatePassword(): Boolean {
        val password = binding.passwordUsuario.text.toString()
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +        //at least 1 lower case letter
                    "(?=.*[A-Z])" +        //at least 1 upper case letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$"
        )
        return when {
            password.isEmpty() -> {
                binding.passwordUsuario.error = "El campo no puede estar vacío"
                false
            }
            !passwordRegex.matcher(password).matches() -> {
                binding.passwordUsuario.error = "Password debe tener minimo 1 mayuscula, 1 minuscula, 1 numero, 1 caracter especial"
                false
            }
            else -> {
                binding.passwordUsuario.error = null
                true
            }
        }

    }

}