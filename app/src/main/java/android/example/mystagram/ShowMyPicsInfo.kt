package android.example.mystagram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_show_my_pics_info.*

class ShowMyPicsInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_my_pics_info)

        all_list.setOnClickListener { startActivity(Intent(this, ShowMyPicsPostListActivity::class.java)) }
        my_list.setOnClickListener { startActivity(Intent(this, ShowMyPicsMyPostListActivity::class.java)) }
        upload.setOnClickListener { startActivity(Intent(this, ShowMyPicsUploadActivity::class.java)) }

        logout.setOnClickListener {
            val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("login_sp", "null")
            editor.commit()
            (application as MasterApplication).createRetrofit()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}