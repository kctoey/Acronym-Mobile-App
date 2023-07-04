package org.classapp.sqlitedb2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyHelper(context: Context): SQLiteOpenHelper(context,"ACDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) { //?คือค่าเป็นnullได้
        //สร้างตารางชื่อ ACTABLE มี 3 คอลัม ใช้ method .execSQLเพราะไม่มีคำสั่งใน SQLiteDatabase ให้ใช้
        db?.execSQL("CREATE TABLE ACTABLE(_ID integer primary key autoincrement,NAME TEXT,MEANING TEXT)")
        //เพิ่มข้อมูลไป3ตัวในตาราง ใส่NAME=www MEANING=world wide web โดยidจะ auto increate มองในรูปแบบ key,value
        db?.execSQL("INSERT INTO ACTABLE(NAME ,MEANING ) VALUES('www','World Wide Web')")
        db?.execSQL("INSERT INTO ACTABLE(NAME ,MEANING ) VALUES('GDG','Google Developer Group')")
        db?.execSQL("INSERT INTO ACTABLE(NAME ,MEANING ) VALUES('AVD','Android Virtual Device')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}