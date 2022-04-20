package com.magicapp.instagramcloneandroidkotlin.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.magicapp.instagramcloneandroidkotlin.R
import com.magicapp.instagramcloneandroidkotlin.adapter.SearchAdapter
import com.magicapp.instagramcloneandroidkotlin.managers.AuthManager
import com.magicapp.instagramcloneandroidkotlin.managers.DatabaseManager
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBFollowHandler
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBUserHandler
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBUsersHandler
import com.magicapp.instagramcloneandroidkotlin.model.User


class SearchFragment : BaseFragment() {
    val TAG = SearchFragment::class.java.simpleName
    lateinit var rv_search: RecyclerView
    var items = ArrayList<User>()
    var users = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        rv_search = view.findViewById(R.id.rv_search)
        rv_search.setLayoutManager(GridLayoutManager(activity, 1))
        val et_search = view.findViewById<EditText>(R.id.et_search)
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val keyword = s.toString().trim()
                usersByKeyword(keyword)
            }
        })
        loadUsers()
    }

    private fun refreshAdapter(items: ArrayList<User>) {
        val adapter = SearchAdapter(this, items)
        rv_search.adapter = adapter
    }

    fun usersByKeyword(keyword: String) {
        if (keyword.isEmpty())
            refreshAdapter(items)

        users.clear()
        for (user in items)
            if (user.fullname.toLowerCase().startsWith(keyword.toLowerCase()))
                users.add(user)

        refreshAdapter(users)
    }

    private fun loadUsers() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadUsers(object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                DatabaseManager.loadFollowing(uid, object : DBUsersHandler {
                    override fun onSuccess(following: ArrayList<User>) {
                        items.clear()
                        items.addAll(mergedUsers(uid,users, following))
                        refreshAdapter(items)
                    }
                    override fun onError(e: Exception) {
                    }
                })
            }
            override fun onError(e: Exception) {
            }
        })
    }

    fun followOrUnfollow(to: User) {
        val uid = AuthManager.currentUser()!!.uid
        if (!to.isFollowed) {
            followUser(uid, to)
        } else {
            unFollowUser(uid, to)
        }
    }



    fun followUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.followUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = true
                        DatabaseManager.storePostsToMyFeed(uid, to)
                    }
                    override fun onError(e: Exception) {
                    }
                })
            }
            override fun onError(e: Exception) {
            }
        })
    }

    fun unFollowUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.unFollowUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = false
                        DatabaseManager.removePostsToMyFeed(uid, to)
                    }
                    override fun onError(e: Exception) {
                    }
                })
            }
            override fun onError(e: Exception) {
            }
        })
    }

    private fun mergedUsers(uid: String, users: ArrayList<User>, following: ArrayList<User>) : ArrayList<User> {
        val items = ArrayList<User>()
        for (u in users) {
            val user = u
            for (f in following) {
                if (u.uid == f.uid) {
                    user.isFollowed = true
                    break
                }
            }
            if (uid != user.uid) {
                items.add(user)
            }
        }
        return items
    }

}