package DB

import Models.ImageModels
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    DbHelper {

    companion object {
        val DB_NAME = "img_db"
        val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "create table image_table(id integer primary key autoincrement not null , image_path text not null, image blob not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun insertImage(imageModels: ImageModels) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("image_path", imageModels.imagePath)
        contentValues.put("image", imageModels.image)
        database.insert("image_table", null, contentValues)
        database.close()
    }

    override fun getAllImages(): List<ImageModels> {
        val list = ArrayList<ImageModels>()
        val query = "select * from image_table"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()){
            do {
                val imageModels = ImageModels()
                imageModels.id = cursor.getInt(0)
                imageModels.imagePath = cursor.getString(1)
                imageModels.image = cursor.getBlob(2)
                list.add(imageModels)
            }while (cursor.moveToNext())
        }
        return list
    }
}