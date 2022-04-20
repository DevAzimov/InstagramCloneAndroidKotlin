package com.magicapp.instagramcloneandroidkotlin.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.magicapp.instagramcloneandroidkotlin.R
import com.magicapp.instagramcloneandroidkotlin.adapter.HomeAdapter
import com.magicapp.instagramcloneandroidkotlin.managers.AuthManager
import com.magicapp.instagramcloneandroidkotlin.managers.DatabaseManager
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBPostHandler
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBPostsHandler
import com.magicapp.instagramcloneandroidkotlin.model.Post
import com.magicapp.instagramcloneandroidkotlin.utills.DialogListener
import com.magicapp.instagramcloneandroidkotlin.utills.Utils
import java.lang.RuntimeException


class HomeFragment : BaseFragment(){
    val TAG = HomeFragment::class.java.toString()
    lateinit var recyclerView: RecyclerView
    private var listener: HomeListener? = null
    var feeds = ArrayList<Post>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && feeds.size > 0) {
            loadMyFeeds()
        }
    }

    /*
    *onAttach is for communication of Fragments
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is HomeListener) {
            context
        }else {
            throw RuntimeException("$context must implement UploadListener")
        }
    }

    /*
    *onDetach is for communication of Fragments
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)
        val iv_camera = view.findViewById<ImageView>(R.id.iv_camera)
        iv_camera.setOnClickListener {
            listener!!.scrollToUpload()
        }

        loadMyFeeds()
    }

    private fun loadMyFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                Log.d(TAG, posts.size.toString())
                feeds.clear()
                feeds.addAll(posts)
                refreshAdapter(feeds)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }

        })
    }


    private fun refreshAdapter(items: ArrayList<Post>) {
        val adapter = HomeAdapter(this, items)
        recyclerView.adapter = adapter
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)
    }

    fun showDeleteDialog(post: Post) {
        Utils.dialogDouble(requireContext(), getString(R.string.str_delete_post), object : DialogListener {
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
                loadMyFeeds()
            }

            override fun onError(e: Exception) {
            }
        })
    }


    interface HomeListener {
        fun scrollToUpload()
    }

}