package com.example.chatwise

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatwise.adapters.ProductListAdapter
import com.example.chatwise.databinding.ActivityProductListBinding

const val TAG = "ProductListActivity"

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpProductListRV()

        // Show progress bar while loading data
        binding.pBar.visibility = View.VISIBLE

        // Observe data from ViewModel
        viewModel.response.observe(this) { response ->
            if (response != null && response.isSuccessful && response.body() != null) {
                val products = response.body()?.products
                productListAdapter.productList = products ?: listOf()
                binding.pBar.visibility = View.GONE
            } else {
                Log.e(TAG, "Response not successful or null")
                Toast.makeText(this@ProductListActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                binding.pBar.visibility = View.GONE
            }
        }
    }

    private fun setUpProductListRV() = binding.productsRV.apply {
        productListAdapter = ProductListAdapter(this@ProductListActivity)
        adapter = productListAdapter
        layoutManager = LinearLayoutManager(this@ProductListActivity)
    }
}