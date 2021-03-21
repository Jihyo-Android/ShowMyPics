package android.example.mystagram

import java.io.Serializable

class Post(
    val owner: String? = null,
    val content: String? = null,
    var image: String? = null
): Serializable