package com.example.quetzalli.ui.views

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentCountdownBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CountdownFragment : Fragment() {

    private lateinit var binding: FragmentCountdownBinding
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountdownBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        navController = findNavController()
        Glide.with(binding.root).asGif()
            .load(R.drawable.icons8_clock)
            .into(binding.imgTimer)

        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                if (secondsRemaining > 0) {
                    binding.tvCountdown.text = secondsRemaining.toString()
                } else {
                    binding.tvCountdown.text = getText(R.string.right_now)
                }
            }

            override fun onFinish() {
                val completedTest = arguments?.getString("completedTest")
                Log.d("completedTest", completedTest.toString())
                when (completedTest) {
                    "testmemory" -> navController.navigate(R.id.action_countdown_to_ubicacionTest)
                    "testubicacion" -> navController.navigate(R.id.action_countdown_to_calculoTest)
                    "testcalculation" -> {
                        // Todas las pruebas se han completado, muestra un AlertDialog
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Pruebas completadas")
                            .setMessage("Has respondido todas las pruebas por hoy. Continúa el día de mañana.")
                            .setPositiveButton("OK") { dialog, _ ->
                                navController.navigate(R.id.sesion)
                            }
                            .show()
                    }

                    else -> navController.navigate(R.id.action_countdown_to_memoryTest)
                }
            }
        }
        countDownTimer.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }
}
