package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebase.databinding.ActivityShowBinding

class ShowActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras

        with(binding) {
            txtDeskripsi.setText(data?.getString("extradeskripsi"))
            txtTanggal.setText(data?.getString("extratanggal"))
            txtNominal.setText(data?.getString("extranominal"))
            txtNama.setText(data?.getString("extranama"))
            txtAlamat.setText(data?.getString("extraalamat"))

        }



    }
}