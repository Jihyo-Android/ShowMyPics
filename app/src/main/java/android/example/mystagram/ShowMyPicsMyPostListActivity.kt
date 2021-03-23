package android.example.mystagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.activity_show_my_pics_info.*
import kotlinx.android.synthetic.main.activity_show_my_pics_my_post_list.*
import kotlinx.android.synthetic.main.activity_show_my_pics_my_post_list.my_list
import kotlinx.android.synthetic.main.activity_show_my_pics_my_post_list.upload
import kotlinx.android.synthetic.main.activity_show_my_pics_my_post_list.user_info
import kotlinx.android.synthetic.main.activity_show_my_pics_post_list.*
import kotlinx.android.synthetic.main.activity_show_my_pics_upload.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowMyPicsMyPostListActivity : AppCompatActivity() {

    lateinit var myPostRecyclerView: RecyclerView
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_my_pics_my_post_list)

        myPostRecyclerView = mypost_recyclerview
        glide = Glide.with(this@ShowMyPicsMyPostListActivity)
        createList()
        user_info.setOnClickListener {
            startActivity(Intent(this, ShowMyPicsInfo::class.java))
        }

        all_lis.setOnClickListener {
            startActivity(Intent(this, ShowMyPicsPostListActivity::class.java))
        }

        upload.setOnClickListener {
            startActivity(Intent(this, ShowMyPicsUploadActivity::class.java))
        }
    }

    fun createList() {
        (application as MasterApplication).service.getUserPostList().enqueue(
            object : Callback<ArrayList<Post>> {
                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    if (response.isSuccessful) {
                        val myPostList = response.body()
                        val adapter = MyPostAdapter(
                            myPostList!!,
                            LayoutInflater.from(this@ShowMyPicsMyPostListActivity),
                            glide
                        )
                        myPostRecyclerView.adapter = adapter
                        myPostRecyclerView.layoutManager = LinearLayoutManager(this@ShowMyPicsMyPostListActivity)
                    }
                }
            }
        )
    }

}


class MyPostAdapter(
    var postList: ArrayList<Post>,
    val inflater: LayoutInflater,
    val glide: RequestManager
) : RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView
        val postOwner: TextView
        val postContet: TextView

        init {
            postImage = itemView.findViewById(R.id.post_img)
            postOwner = itemView.findViewById(R.id.post_owner)
            postContet = itemView.findViewById(R.id.post_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.showmypics_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.postOwner.setText(postList.get(position).owner)
        holder.postContet.setText(postList.get(position).content)
        glide.load(postList.get(position).image).into(holder.postImage)
    }
}