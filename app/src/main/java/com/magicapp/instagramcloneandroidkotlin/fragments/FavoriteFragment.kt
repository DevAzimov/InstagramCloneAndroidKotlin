package com.magicapp.instagramcloneandroidkotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.magicapp.instagramcloneandroidkotlin.R
import com.magicapp.instagramcloneandroidkotlin.adapter.FavoriteAdapter
import com.magicapp.instagramcloneandroidkotlin.adapter.HomeAdapter
import com.magicapp.instagramcloneandroidkotlin.managers.AuthManager
import com.magicapp.instagramcloneandroidkotlin.managers.DatabaseManager
import com.magicapp.instagramcloneandroidkotlin.managers.DatabaseManager.deletePost
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBPostHandler
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBPostsHandler
import com.magicapp.instagramcloneandroidkotlin.model.Post
import com.magicapp.instagramcloneandroidkotlin.utills.DialogListener
import com.magicapp.instagramcloneandroidkotlin.utills.Utils

class FavoriteFragment : BaseFragment(){
    val TAG = FavoriteFragment::class.java.toString()
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_favorite, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)

        refreshAdapter(loadPost())
    }

    private fun refreshAdapter(items: ArrayList<Post>) {
        val adapter = FavoriteAdapter(this, items)
        recyclerView.adapter = adapter
    }

    fun loadLikedFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadLikedFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
            }

        })
    }



    private fun loadPost(): ArrayList<Post> {
        val items = ArrayList<Post>()
        items.add(Post("Here we go","https://images.unsplash.com/photo-1649590242138-2d56b3a48c5f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw5M3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("Here we go","https://images.unsplash.com/photo-1649590242138-2d56b3a48c5f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw5M3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("Here we go", "https://images.unsplash.com/photo-1649583693539-f36f908da137?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMDh8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("Here we go", "https://images.unsplash.com/photo-1649583693539-f36f908da137?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMDh8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("Here we go", "https://images.unsplash.com/photo-1649575207563-0314a0cd0398?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMjN8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("Here we go", "https://images.unsplash.com/photo-1649575207563-0314a0cd0398?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMjN8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("Here we go", "https://images.unsplash.com/photo-1649575207563-0314a0cd0398?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMjN8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        return items
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)

        loadLikedFeeds()
    }

    fun showDeleteDialog(post: Post) {
        Utils.dialogDouble(requireContext(), getString(R.string.str_delete_post), object :
            DialogListener {
            override fun onCallback(isChosen: Boolean) {
                if (isChosen) {
                    deletePost(post)
                }
            }

        })
    }

    private fun deletePost(post: Post) {
        DatabaseManager.deletePost(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                loadLikedFeeds()
            }

            override fun onError(e: Exception) {
            }
        })
    }

}