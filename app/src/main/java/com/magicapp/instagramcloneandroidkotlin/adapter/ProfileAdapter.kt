package com.magicapp.instagramcloneandroidkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewFragment
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.magicapp.instagramcloneandroidkotlin.R
import com.magicapp.instagramcloneandroidkotlin.fragments.HomeFragment
import com.magicapp.instagramcloneandroidkotlin.fragments.ProfileFragment
import com.magicapp.instagramcloneandroidkotlin.model.Post
import com.magicapp.instagramcloneandroidkotlin.utills.Utils

class ProfileAdapter(var fragment: ProfileFragment, var items: ArrayList<Post>): BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_profile, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = items[position]
        if (holder is PostViewHolder){
            var iv_post = holder.iv_post
            setViewHeight(iv_post)
            Glide.with(fragment).load(post.postImg).into(iv_post)
        }
    }


    class PostViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var iv_post: ShapeableImageView = view.findViewById(R.id.iv_post)
//        var tv_fullname: TextView = view.findViewById(R.id.tv_fullname)
//        var tv_caption: TextView = view.findViewById(R.id.tv_caption)

    }

    /*
    *Set ShapeableImageView height as a half of screen width
     */
    private fun setViewHeight(view: View) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(fragment.requireActivity().application).width / 2
        view.layoutParams = params
    }

}