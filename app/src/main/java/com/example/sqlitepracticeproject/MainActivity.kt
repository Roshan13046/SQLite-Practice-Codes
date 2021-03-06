package com.example.sqlitepracticeproject

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        btnAdd.setOnClickListener{
            addRecord(view)
        }

        setupListofDataIntoRecyclerView()

    }

    //method for saving records in database

    fun addRecord(view: View){
        val name = etName.text.toString()
        val email = etEmailId.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(!name.isEmpty() && !email.isEmpty()){
            val status = databaseHandler.addEmployee(EmpModelClass(0,name,email))
            if(status > -1){
                Toast.makeText(applicationContext,"Record Saved", Toast.LENGTH_SHORT)
                etName.text.clear()
                etEmailId.text.clear()

                setupListofDataIntoRecyclerView()
            }
        }else{
            Toast.makeText(
                applicationContext, "Name or Email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }



    //function is used to show the list of inserted data

    private fun setupListofDataIntoRecyclerView(){
        if(getItemsList().size > 0){
            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            //Set the LayoutManager that this RecyclerView will use
            rvItemsList.LayoutManager = LinearLayoutManager(this)
            //Adapter class is initialized and list is passed in the param
            val itemAdapter = ItemAdapter(this,getItemsList())
            //adapter instance is set to the recyclerview to inflate the item
            rvItemsList.adapter = itemAdapter
        }else{
            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }


    //fun is used to get the Items List which is added in the database
    private  fun getItemsList(): ArrayList<EmpModelClass>{
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of the databaseHandler class to read the data
        return databaseHandler.viewEmployee()
    }

    //fun update record dialog

    fun updateRecordDialog(empModelClass: EmpModelClass){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        /*
        Set the screen content from a layout resource.
        The resource will be inflated,adding all top-level views to the screen
         */

        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(empModelClass.name)
        updateDialog.etUpdateName.setText(empModelClass.email)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener{
            val name = updateDialog.etUpdateName.text.toString()
            val email = updateDialog.etUpdateEmailId.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !email.isEmpty()) {
                val status =
                    databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })

        //Start the dialog and display it on screen.
        updateDialog.show()
    }


//      fun is used to show the Alert Dialog.

    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${empModelClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }


}
