package com.robin.shayad

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.robin.shayad.adapter.AllShayariAdapter
import com.robin.shayad.databinding.ActivityAllShayariBinding
import com.robin.shayad.databinding.DialogShayariLayoutBinding
import com.robin.shayad.model.CategoryModel


class AllShayariActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllShayariBinding
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllShayariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        setAdapter(id,name)
        binding.shayariStatus.text = name.toString()
        binding.backPress.setOnClickListener {
            onBackPressed()
        }
        binding.addShayari.setOnClickListener {
            addShayari(id.toString())
        }

    }

    private fun setAdapter(id: String?, name: String?) {
        val list = arrayListOf<CategoryModel>()
        db = FirebaseFirestore.getInstance()
        db.collection("Shayari").document(id.toString()).collection("All").addSnapshotListener { value, error ->
            list.clear()
            val data = value?.toObjects(CategoryModel::class.java)
            list.addAll(data!!)
            binding.allshayariRev.layoutManager = LinearLayoutManager(this)
            binding.allshayariRev.adapter = AllShayariAdapter(this,list,id)
        }
    }
    private fun addShayari(id:String){
        val dailog = Dialog(this)
        val bindingCard = DialogShayariLayoutBinding.inflate(LayoutInflater.from(this))
        dailog.setContentView(bindingCard.root)
        bindingCard.catDialogAddBtn.setOnClickListener {
            val shayareData = bindingCard.catDialogEdittext.text.toString()
            val uid = db.collection("Shayari").document().id
            if(shayareData != ""){
                val data = CategoryModel(uid,shayareData)
                //set data to firebase
                db.collection("Shayari").document(id).collection("All").document(uid).set(data).addOnCompleteListener {
                    Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show()

                }
            }else
                Toast.makeText(this, "first write shayari", Toast.LENGTH_SHORT).show()

            dailog.dismiss()

        }
        dailog.show()
    }
}