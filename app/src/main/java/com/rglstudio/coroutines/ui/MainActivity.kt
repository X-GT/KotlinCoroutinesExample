package com.rglstudio.coroutines.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rglstudio.coroutines.R
import com.rglstudio.coroutines.adapter.RvAdapter
import com.rglstudio.coroutines.viewmodel.AlbumViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var albumViewModel: AlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        loadData()
        observeData()

        swipeRefresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun initViewModel() {
        albumViewModel = this.run {
            ViewModelProviders.of(this,
                AlbumViewModel.FACTORY(this))
                .get(AlbumViewModel::class.java)
        }
    }

    private fun loadData() {
        albumViewModel.getAlbum()
    }

    private fun observeData() {
        albumViewModel.responData.observe(this, Observer {
                t -> t?.let {
                    rvItem.apply {
                        layoutManager = GridLayoutManager(this@MainActivity,
                            2, RecyclerView.VERTICAL, false)
                        adapter = RvAdapter(it)
                    }
                }
        })

        albumViewModel.errorInfo.observe(this, Observer {
                t -> t?.let {
                    Toast.makeText(this, t, Toast.LENGTH_LONG).show()
                }
        })

        albumViewModel.loading.observe(this, Observer {
                t -> t?.let {
                    if (t){
                        swipeRefresh.isRefreshing = true
                        rvItem.visibility = View.GONE
                    }
                    else{
                        swipeRefresh.isRefreshing = false
                        rvItem.visibility = View.VISIBLE
                    }
                }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        albumViewModel.cancelRequests()
    }
}
