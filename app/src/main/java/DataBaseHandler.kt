import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "localHosts"
val COL_URL = "url"

    class DataBaseHandler (var context: Context,factory: SQLiteDatabase.CursorFactory?) :
        SQLiteOpenHelper(context,DATABASE_NAME,factory,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val  createTable = "CREATE TABLE $TABLE_NAME ($COL_URL VARCHAR (256))";

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }


    fun insertData(localhost: Localhost) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_URL,localhost.url)
        var result = db.insert(TABLE_NAME,null,cv)
        db.close()
    }

    fun getAllName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

}