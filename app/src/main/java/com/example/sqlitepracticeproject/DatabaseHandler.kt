package com.example.sqlitepracticeproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler (context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

        companion object{
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "EmployeeDatabase"
            private const val TABLE_CONTACTS = "EmployeeTable"

            private const val KEY_ID = "_id" //primary key or unique key
            private const val KEY_NAME = "name"
            private const val KEY_EMAIL = "email"
        }

        override fun onCreate(db: SQLiteDatabase?){
            //creating table with fields
            val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+ KEY_EMAIL + " TEXT" + ")"
                    )
            db?.execSQL(CREATE_CONTACTS_TABLE)
        }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db!!.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTACTS)
        onCreate(db)
    }

///  function to insert data
    fun addEmployee(emp: EmpModelClass): Long{
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(KEY_NAME, emp.name)
    contentValues.put(KEY_EMAIL,emp.email)

    //Inserting Row
    val success = db.insert(TABLE_CONTACTS, null, contentValues)

    //2nd arguments is String containing nullColumnHack

    db.close()//closing database connection
    return success
    }

    //method to read data
    fun viewEmployee(): ArrayList<EmpModelClass>{
        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery  = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var name: String
        var email: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val emp = EmpModelClass(id = id, name = name,email = email)
                empList.add(emp)
            }while (cursor.moveToNext())
        }

        return empList
    }

    //function to update record

    fun updateEmployee(emp: EmpModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_NAME, emp.name)//class Name
        contentValues.put(KEY_EMAIL,emp.email)//class Email

        //updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "="+emp.id,null`)
        //2nd argument is String containing nullColumnHack

        db.close() //Closing database connection
        return success
    }


    //function to delete record
    fun deleteEmployee(emp: EmpModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)//EmpModelClass id
        //Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "="+emp.id,null)
        //2nd argument is string containg nullCloumnHack
        db.close()//cosing database connection
        return success
    }


}

