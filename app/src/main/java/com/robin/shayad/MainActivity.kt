package com.robin.shayad

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.robin.shayad.adapter.CatAdapter
import com.robin.shayad.databinding.ActivityMainBinding
import com.robin.shayad.databinding.DialogCatLayoutBinding
import com.robin.shayad.model.CategoryModel



class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var db : FirebaseFirestore
    val list = arrayListOf<CategoryModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backPress.setOnClickListener {
            onBackPressed()
        }
        setAdapter()

        //add category
        binding.addCat.setOnClickListener {
            val dialog = Dialog(this)
            val bindingDialog = DialogCatLayoutBinding.inflate(layoutInflater)
            dialog.setContentView(bindingDialog.root)
            dialog.show()
            bindingDialog.catDialogAddBtn.setOnClickListener {

                db = FirebaseFirestore.getInstance()
                val id = db.collection("Shayari").document().id.toString()
                //get data from user
                var data = bindingDialog.catDialogEdittext.text.toString()
                if (data != ""){
                    var model = CategoryModel(id,data)
                    //set data to firebase
                    db.collection("Shayari").document(id).set(model).addOnSuccessListener {
                        Toast.makeText(this, "Add successfully", Toast.LENGTH_SHORT).show()
                    }
                        .addOnCanceledListener {
                            Toast.makeText(this, "somethings wrong", Toast.LENGTH_SHORT).show()
                        }
                }else
                    Toast.makeText(this, "set the catagory first", Toast.LENGTH_SHORT).show()
                
                dialog.dismiss()
            }
        }


    }

    private fun setAdapter() {
        binding.allshayariRev.layoutManager = LinearLayoutManager(this)

        db = FirebaseFirestore.getInstance()
       db.collection("Shayari").addSnapshotListener(com.google.firebase.firestore.EventListener { value, error ->
           if (error==null){
               list.clear()
               val data = value?.toObjects(CategoryModel::class.java)
               list.addAll(data!!)
               binding.allshayariRev.adapter = CatAdapter(this,list)
           }
       })

    }
}