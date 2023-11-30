package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firebase.databinding.ActivitySecondBinding
import com.google.firebase.firestore.FirebaseFirestore

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private var updateId = ""
    private val firestore = FirebaseFirestore.getInstance()
    private val budgetListLiveData: MutableLiveData<List<Budget>>
            by lazy {
                MutableLiveData<List<Budget>>()
            }
    private val budgetCollectionRef = firestore.collection("budgets")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySecondBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val data = intent.extras

        with(binding) {
            editDesc.setText(data?.getString("extradeskripsi"))
            editDate.setText(data?.getString("extratanggal"))
            editNominal.setText(data?.getString("extranominal"))
            editPemesan.setText(data?.getString("extranama"))
            editAlamat.setText(data?.getString("extraalamat"))

            btnSave.setOnClickListener {
                val nominal = editNominal.text.toString()
                val desc = editDesc.text.toString()
                val tanggal = editDate.text.toString()
                val nama = editPemesan.text.toString()
                val alamat = editAlamat.text.toString()

                val newBudget = Budget(
                    nominal = nominal, description = desc,
                    date = tanggal, nama = nama, alamat = alamat
                )
                addBudget(newBudget)

                val intentToHome = Intent(this@SecondActivity, MainActivity::class.java)
                startActivity(intentToHome)
                finish()
            }

            buttonEdit.setOnClickListener {

                val nominal = editNominal.getText().toString()
                val desc = editDesc.getText().toString()
                val date = editDate.getText().toString()
                val alamat = editAlamat.getText().toString()
                val nama = editPemesan.getText().toString()
                updateId = data?.getString("extraid").toString()?:""

                val updateBudgets = Budget(
                    nominal = nominal, description = desc,
                    date = date, id = updateId, nama = nama, alamat = alamat
                )
                updateBudget(updateBudgets)

                val intentToHomeA = Intent(this@SecondActivity, MainActivity::class.java)
                startActivity(intentToHomeA)
                finish()
            }
        }
    }

    private fun updateBudget(budget: Budget) {
        budgetCollectionRef.document(budget.id).set(budget)
            .addOnFailureListener {
                Log.d(
                    "MainActivity", "Error updating budget : ",
                    it
                )
            }
    }

    private fun addBudget(budget: Budget) {
        budgetCollectionRef.add(budget).addOnFailureListener {
            Log.d(
                "MainActivity", "Error adding budget : ",
                it
            )
        }


    }
}
