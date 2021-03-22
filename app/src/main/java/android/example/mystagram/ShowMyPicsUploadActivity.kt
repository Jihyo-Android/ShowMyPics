package android.example.mystagram

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_show_my_pics_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ShowMyPicsUploadActivity : AppCompatActivity() {

    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_my_pics_upload)

        view_pictures.setOnClickListener {
            getPicture()
        }
        upload_post.setOnClickListener {
            uploadPost()
        }

        all_list.setOnClickListener {
            startActivity(Intent(this, ShowMyPicsPostListActivity::class.java))
        }
        my_list.setOnClickListener {
            startActivity(Intent(this, ShowMyPicsMyPostListActivity::class.java))
        }
        user_info.setOnClickListener {
            startActivity(Intent(this, ShowMyPicsInfo::class.java))
        }
    }

    fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK) // 앱이 이미지를 관리하지 않고 기기가 관리한다
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // 외부에서 이미지 가져오기
        intent.setType("image/*")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val uri: Uri = data!!.data!!
            filePath = getImageFilePath(uri)
        }
    }

    fun getImageFilePath(contentUri: Uri): String {
        var columnIndex = 0
        val projection = arrayOf(MediaStore.Images.Media.DATA) // 이미지만 걸러내기
        val cursor = contentResolver.query(contentUri, projection, null, null, null)

        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }

    fun uploadPost() {
        val file = File(filePath)
        val fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("image", file.name, fileRequestBody)
        val content = RequestBody.create(MediaType.parse("text/plain"), getContent())

        (application as MasterApplication).service.uploadPost(
            part, content
        ).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {

            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    finish()
                    startActivity(Intent(this@ShowMyPicsUploadActivity, ShowMyPicsMyPostListActivity::class.java))
                }
            }
        })
    }

    fun getContent(): String {
        return content_input.text.toString()
    }
}