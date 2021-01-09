package com.example.sqlitepracticeproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.items_row.view.*
import java.text.FieldPosition

class ItemAdapter(val context: Context, val items: ArrayList<EmpModelClass>) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    /**
     * Inflates the item views which is designed in xml layout file
     * create a new {@link ViewHolder} and initializes some private fields to be used byRecycler view
     */

    override fun OnCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,false
            )
        )
    }
}

/**
 * Binds each item in the ArrayList to a view
 *
 * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
 * an item.
 * This new ViewHolder should be constructed with a new View that can represent the items type.
 * Yu can either create a new View manually or inflate it from an XML
 *
 */

override fun onBindViewHolder(holder: ViewHolder, position: Int){
    val item = items.get(position)

    holder.tvName.text = item.name
    holder.tvEmail.text = item.email

    //updating the background color according to the even odd
    if(position % 2 == 0){
        holder.llMain.setBackgroundColor(
            ContextCompat.getColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGray
                )
            )
        ) else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }

        holder.ivEdit.setOnClickListner{ view->
            if(context is MainActivity){
                conetxt.updateRecordDialog(item)
            }
        }

        holder.ivDelete.setOnClickListner{
            if(context is MainActivity){
                context.deleteRecordAlertDialog(item)
            }
        }
    }
}

/**
 * Gets the number of items in the list
 */
    override fun getItemCound(): Int{
        return items.size
    }
/**
 * A ViewHolder describes an item view and metadata about its place within the RecyclerView
 */

class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
    //Holds the TextView that will ad each item to
    val llMain = view.llMain
    val tvName = view.tvName
    val tvEmail = view.tvEmail
    val ivEdit = view.ivEdit
    val ivDelete = view.ivDelete

}


