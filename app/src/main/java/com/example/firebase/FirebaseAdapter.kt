package com.example.firebase

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ItemListBinding
import com.google.firebase.firestore.FirebaseFirestore


typealias onClickDelete = (Budget) -> Unit


class FirebaseAdapter(private val listItem : List<Budget>, private val onClickDelete: onClickDelete) : RecyclerView.Adapter<FirebaseAdapter.ItemBudgetViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()
    private var updateId = ""
    private val budgetListLiveData : MutableLiveData<List<Budget>>
            by lazy {
                MutableLiveData<List<Budget>>()
            }
    private val budgetCollectionRef = firestore.collection("budgets")


    inner class ItemBudgetViewHolder(private val binding : ItemListBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(data: Budget){
                with(binding){
                    txtDeskripsi.text = data.description
                    txtNominal.text = data.nominal
                    txtTanggal.text = data.date

                    buttonUpdate.setOnClickListener{
                        val intent = Intent(binding.root.context, SecondActivity::class.java)
                            .apply {
                                putExtra("extraid", data.id)
                                putExtra("extratanggal", data.date)
                                putExtra("extradeskripsi", data.description)
                                putExtra("extranominal", data.nominal)
                                putExtra("extranama", data.nama)
                                putExtra("extraalamat", data.alamat)
                            }
                        binding.root.context.startActivity(intent)
                    }
                    buttonDelete.setOnClickListener {
                        val posisi = listItem[position]
                        deleteBudget(posisi)
                    }

                    itemView.setOnClickListener {
                        val intent = Intent(binding.root.context, ShowActivity::class.java)
                            .apply {
                                putExtra("extraid", data.id)
                                putExtra("extratanggal", data.date)
                                putExtra("extradeskripsi", data.description)
                                putExtra("extranominal", data.nominal)
                                putExtra("extranama", data.nama)
                                putExtra("extraalamat", data.alamat)
                            }
                        binding.root.context.startActivity(intent)

                    }


                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBudgetViewHolder {
            val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemBudgetViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return listItem.size
        }

        override fun onBindViewHolder(holder: ItemBudgetViewHolder, position: Int) {
            holder.bind(listItem[position])

        }

    private fun deleteBudget(budget: Budget){
        budgetCollectionRef.document(budget.id).delete()
            .addOnFailureListener{
                Log.d("MainActivity", "Error deleting budget : ",
                    it)
            }
    }

}