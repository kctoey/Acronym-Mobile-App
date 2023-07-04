package org.classapp.sqlitedb2

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.classapp.sqlitedb2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var db: SQLiteDatabase
    lateinit var rs: Cursor
    lateinit var adapter:SimpleCursorAdapter
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var helper = MyHelper(applicationContext)
         db = helper.readableDatabase
//select ข้อมูลจากsqlมาแสดง
                 rs = db.rawQuery("SELECT _ID as _id,name,meaning FROM ACTABLE ",null)


        //ดึงคอลัมโดยหาคีย์id nameกับmeaningมาใช้งาน โดยnameคือ item1 ทำmeaning คือ item2
         adapter = SimpleCursorAdapter(
            applicationContext,
            android.R.layout.simple_expandable_list_item_2,
            rs,
            arrayOf("NAME", "MEANING"),
            intArrayOf(android.R.id.text1, android.R.id.text2), 0)
//เอาข้อมูลที่ดึงมาแสดงเป็นlist
        binding.listView.adapter = adapter
        //คลิกในlistviewได้
        registerForContextMenu(binding.listView)
//butun first ดักจัก onclick จะให้ทำงานในปีกกา
        binding.btFirst.setOnClickListener {
            if (rs.moveToFirst()) {
                //ย้ายมาแสดงในสิ่งที่เราbinding ตามidกล่องข้อความ
                binding.edName.setText(rs.getString(1))
                binding.edMeaning.setText(rs.getString(2))

            } else {
                Toast.makeText(applicationContext, "No data found", Toast.LENGTH_LONG).show()
            }
        }
        binding.btNext.setOnClickListener {
            if (rs.moveToNext()) {
                binding.edName.setText(rs.getString(1))
                binding.edMeaning.setText(rs.getString(2))
            } else if (rs.moveToLast()) {
                binding.edName.setText(rs.getString(1))
                binding.edMeaning.setText(rs.getString(2))
            }
        }
        binding.btPrev.setOnClickListener {
            if (rs.moveToPrevious()) {
                binding.edName.setText(rs.getString(1))
                binding.edMeaning.setText(rs.getString(2))
            } else if (rs.moveToNext()) {
                binding.edName.setText(rs.getString(1))
                binding.edMeaning.setText(rs.getString(2))
            }
        }
        binding.btLast.setOnClickListener {
            if (rs.moveToLast()) {
                binding.edName.setText(rs.getString(1))
                binding.edMeaning.setText(rs.getString(2))
            } else {
                Toast.makeText(applicationContext, "No data found", Toast.LENGTH_LONG).show()
            }

        }
        binding.btInsert.setOnClickListener {
            //สร้าง contentvalue เพื่อส่งข้อมูลไปที่กb
            var cv = ContentValues()
            //cv.put(KEY,VALUE)
            cv.put("NAME", binding.edName.text.toString())
            // cv.put(COLUMN_NAME,VALUE)
            cv.put("MEANING", binding.edMeaning.text.toString())
            //db.insert(TABLE,null_column_hack,content_values) เพิ่มข้อมูลในตาราง
            db.insert("ACTABLE", null, cv)
            //Show alert dialog
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Add a new record")
            ad.setMessage("Record inserted successfully!")
            //คลิกแล้วเคลียค่าใน2กล่องให้เป็นค่าว่าง
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                binding.edName.setText("")
                binding.edMeaning.setText("")
                binding.edName.requestFocus()
            })
            ad.show()


        }
        binding.btUpdate.setOnClickListener {
            //ContentValues จะเก็บเป็น key,values
            var cv = ContentValues()
            cv.put("NAME", binding.edName.text.toString())
            cv.put("MEANING", binding.edMeaning.text.toString())
            db.update("ACTABLE", cv, "_id=?", arrayOf(rs.getString(0)))
            rs = db.rawQuery("SELECT * FROM ACTABLE", null)

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Update record")
            ad.setMessage("Record updated Successfully")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                if (rs.moveToFirst()) {
                    binding.edName.setText(rs.getString(1))
                    binding.edMeaning.setText(rs.getString(2))
                }
            })
            ad.show()
        }
        binding.btDelete.setOnClickListener {
            db.delete("ACTABLE", "_id=?", arrayOf(rs.getString(0)))
            //ค้นหาข้อมูล
            rs = db.rawQuery("SELECT * FROM ACTABLE", null)

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Delete record")
            ad.setMessage("Record deleted successfully")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                //ลบแล้วจะmoveไปfirst
                if (rs.moveToFirst()) {
                    binding.edName.setText(rs.getString(1))
                    binding.edMeaning.setText(rs.getString(2))
                } else {
                    binding.edName.setText("No data found")
                    binding.edMeaning.setText("No data found!")
                }
            })
            ad.show()
        }

        binding.btClear.setOnClickListener {
            binding.edName.setText("")
            binding.edMeaning.setText("")
            //ดึงfocusdลับมาอยู่ที่กล่อง
            binding.edName.requestFocus()
        }
        binding.btViewall.setOnClickListener {
            //adapter จะ refreshข้อมูใหม่แล้วค่อยโชว์ขึ้นมา
            adapter.notifyDataSetChanged()

            binding.searchView.visibility = View.VISIBLE
            binding.listView.visibility = View.VISIBLE

            binding.searchView.isIconified = false
            //${rs.count}จำนวนrollที่มีอยู่ทั้งหมด
            binding.searchView.queryHint = "Search Among ${rs.count} records"

//            adapter.notifyDataSetChanged()
//
//            //View All Data
//            binding.searchView.isIconified = true
//            binding.searchView.queryHint = "Search Among ${rs.count} records"
//
//
//            binding.listView.adapter = adapter



        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
//เมื่อมีการใส่textเข้ามา จะเก็บไว้ใน p0=text และไปค้นหาข้อมูลตัวนั้นมาแสดง
            override fun onQueryTextChange(p0: String?): Boolean {
                var rs = db.rawQuery("SELECT _ID as _id,name,meaning FROM ACTABLE WHERE NAME LIKE '%${p0}%' OR MEANING LIKE '%${p0}%'",null)
                adapter.changeCursor(rs)
                return false
            }

        }


        )

    }

    //เวลากดแช่จะpop menu ขึ้นมา ให้ลบตัวนั้น
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v:View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ){
        super.onCreateContextMenu(menu,v,menuInfo)
        menu?.add(101,11,1,"DELETE")
        menu?.setHeaderTitle("Removing Data")
    }

    override fun onContextItemSelected(item: MenuItem):Boolean {
        if(item.itemId == 11) {
            db.delete("ACTABLE", "_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            binding.searchView.queryHint = "Search Among ${rs.count} record"
        }
return  super.onContextItemSelected(item)


    }
}



