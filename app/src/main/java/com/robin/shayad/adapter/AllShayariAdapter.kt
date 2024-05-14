package com.robin.shayad.adapter


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import com.robin.shayad.databinding.AllShayariAdapterBinding
import com.robin.shayad.databinding.DialogShayariLayoutBinding
import com.robin.shayad.databinding.DialogShayariUpdateLayoutBinding
import com.robin.shayad.model.CategoryModel


class AllShayariAdapter(
    val context: Context,
    val list: ArrayList<CategoryModel>,
    val id: String?
): RecyclerView.Adapter<AllShayariAdapter.ViewHolder>() {
    val db = FirebaseFirestore.getInstance()


    class ViewHolder(val binding: AllShayariAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(AllShayariAdapterBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textAll.text = list[position].name.toString()
        holder.binding.root.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Take Action")
                .setMessage("Choice your Action")
                .setPositiveButton("Update",DialogInterface.OnClickListener { dialog, which ->
                    val dailog = Dialog(context)
                    val bindingCard = DialogShayariUpdateLayoutBinding.inflate(LayoutInflater.from(context))
                    dailog.setContentView(bindingCard.root)
                    //set shayari to editText
                    val shayariData = list[position].name
                    bindingCard.catDialogEdittext.setText(shayariData)
                    bindingCard.catDialogUpdateBtn.setOnClickListener {
                        val shayareData = bindingCard.catDialogEdittext.text.toString()
                        val uid = list[position].id.toString()
                        if(shayareData != ""){
                            val data = CategoryModel(uid,shayareData)
                            //set data to firebase
                            db.collection("Shayari").document(id.toString()).collection("All").document(uid).set(data).addOnCompleteListener {
                                Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show()

                            }
                        }else
                            Toast.makeText(context, "first write shayari", Toast.LENGTH_SHORT).show()

                        dailog.dismiss()

                    }
                    dailog.show()
                })
                .setNegativeButton("Delete",DialogInterface.OnClickListener { dialog, which ->
                    db.collection("Shayari").document(id.toString()).collection("All").document(list[position].id.toString()).delete().addOnCompleteListener {
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    }
                }).create().show()



        }

}

}