package com.robin.shayad.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.robin.shayad.AllShayariActivity
import com.robin.shayad.databinding.ActivityMainBinding

import com.robin.shayad.databinding.CatAdapterBinding
import com.robin.shayad.databinding.DialogCatLayoutBinding
import com.robin.shayad.model.CategoryModel

class CatAdapter(val context:Context, val list: ArrayList<CategoryModel>) : RecyclerView.Adapter<CatAdapter.ViewHolder>() {



    class ViewHolder(val binding: CatAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CatAdapterBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textCat.text = list[position].name.toString()
        holder.binding.root.setOnClickListener {
            var intent = Intent(context, AllShayariActivity::class.java)
            intent.putExtra("id",list[position].id)
            intent.putExtra("name",list[position].name)
            context.startActivity(intent)
        }

        //delete

        holder.binding.catDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setMessage("Are you sure")
            dialog.setPositiveButton("Yes",DialogInterface.OnClickListener { dialog, which ->
                val db = FirebaseFirestore.getInstance()
                db.collection("Shayari").document(list[position].id.toString()).delete()
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()

            })
            dialog.setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->  })

            // Create and show the AlertDialog
            dialog.create()
            dialog.show()
            
        }
    }
}


