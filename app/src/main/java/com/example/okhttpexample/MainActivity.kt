package com.example.okhttpexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.example.okhttpexample.databinding.ActivityRepositoryDetailBinding
import com.example.okhttpexample.databinding.ItemRepositoryBinding
import com.example.okhttpexample.models.Repository
import com.example.okhttpexample.network.GitHubApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var gitHubApiInterface: GitHubApiInterface

    private lateinit var repoAdapter: SimpleItemRecyclerViewAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRepositoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.repositories_title)

        (application as OkHttpExampleApp).netComponent.inject(this)

        val recyclerView: RecyclerView = binding.repositoryList
        repoAdapter = SimpleItemRecyclerViewAdapter(mutableListOf<Repository>())
        recyclerView.adapter = repoAdapter

        val call: Call<ArrayList<Repository>> =
            gitHubApiInterface.getRepository("bblia")

        call.enqueue(object : Callback<ArrayList<Repository>> {
            override fun onResponse(
                call: Call<ArrayList<Repository>>,
                response: Response<ArrayList<Repository>>
            ) {
                repoAdapter.values.apply {
                    clear()
                    response.body()?.toCollection(this)
                }
                repoAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    class SimpleItemRecyclerViewAdapter(val values: MutableList<Repository>) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val binding = ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.title.text = item.name
            holder.contentView.text = item.description
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(binding: ItemRepositoryBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val title: TextView = binding.title
            val contentView: TextView = binding.content
        }

    }
}
