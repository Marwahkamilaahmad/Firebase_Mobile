package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var updateId = ""
    private val budgetListLiveData : MutableLiveData<List<Budget>>
    by lazy {
         MutableLiveData<List<Budget>>()
    }
    private val budgetCollectionRef = firestore.collection("budgets")



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
//            btnAdd.setOnClickListener {
//                val nominal = edtNominal.text.toString()
//                val desc = edtDesc.text.toString()
//                val tanggal = edtDate.text.toString()
//
//                val newBudget = Budget(nominal = nominal, description = desc,
//                    date = tanggal)
//                addBudget(newBudget)
//            }

            btnCreate.setOnClickListener{
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)
            }

//            btnUpdate.setOnClickListener {
//                if (updateId != "") {
//                    val nominal = edtNominal.text.toString()
//                    val desc = edtDesc.text.toString()
//                    val date = edtDate.text.toString()
//
//                    val updateBudget = Budget(
//                        nominal = nominal, description = desc,
//                        date = date
//                    )
//                    updateBudget(updateBudget)
//                    updateId = ""
//                    setEmptyField()
//                }
//            }

//
//            listView.setOnItemClickListener{
//                    adapterView, _, i, _ ->
//                val item = adapterView.adapter.getItem(i) as Budget
//                updateId = item.id
//                edtNominal.setText(item.nominal)
//                edtDesc.setText(item.description)
//                edtDate.setText(item.date)
//            }

            listView.onItemLongClickListener = AdapterView.OnItemLongClickListener(){
                    adapterView, _, i, _ ->
                val item = adapterView.adapter.getItem(i) as Budget
                deleteBudget(item)
                true
            }
        }
        observeBudgets()
        getAllBudget()
        }

    private fun getAllBudget(){
        budgetCollectionRef.addSnapshotListener{ snapshots, error ->
            if(error != null){
                Log.d("MainActivity", "error", error)
                return@addSnapshotListener
            }
            val budgets = arrayListOf<Budget>()
            snapshots?.forEach{
                documentReference ->
                budgets.add(
                    Budget(documentReference.id,
                        documentReference.get("nominal").toString(),
                        documentReference.get("description").toString(),
                        documentReference.get("date").toString(),
                        documentReference.get("nama").toString(),
                        documentReference.get("alamat").toString(),
                        )
                )
            }
            if (budgets != null){
                budgetListLiveData.postValue(budgets)
            }
        }
        observeBudgets()
    }

    private fun observeBudgets() {
//        budgetListLiveData.observe(this){
//            budgets ->
//            val adapter = ArrayAdapter(this,
//                android.R.layout.simple_list_item_1,budgets.toMutableList() )
//            binding.listView.adapter = adapter
//        }

        budgetListLiveData.observe(this){
                budgets ->
            val adapter = FirebaseAdapter(budgets){
                budget->
                deleteBudget(budget)
        }
            binding.rvRoom.adapter = adapter
            binding.rvRoom.apply {
                layoutManager = LinearLayoutManager(context)
            }
        }

    }

    private fun addBudget(budget: Budget){
        budgetCollectionRef.add(budget).addOnFailureListener{
            Log.d("MainActivity", "Error adding budget : ",
                it)
        }
    }

    private fun updateBudget(budget: Budget){
        budgetCollectionRef.document(updateId).set(budget)
            .addOnFailureListener{
                Log.d("MainActivity", "Error updating budget : ",
                    it)
            }
    }

    private fun deleteBudget(budget: Budget){
        budgetCollectionRef.document(updateId).delete()
            .addOnFailureListener{
                Log.d("MainActivity", "Error deleting budget : ",
                    it)
            }
    }
//    private fun setEmptyField(){
//        with(binding){
//            edtNominal.setText("")
//            edtDesc.setText("")
//            edtDate.setText("")
//        }
//    }






}