package com.example.chatwise

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatwise.adapters.ProductListAdapter
import com.example.chatwise.databinding.ActivityProductListBinding
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

const val TAG = "ProductListActivity"
class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productListAdapter: ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpProductListRV()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                binding.pBar.visibility = View.VISIBLE

                val response = try {
                    RetrofitInstance.api.getProducts()
                } catch (e: IOException) {
                    Log.e(TAG, "IOException", e)
                    return@repeatOnLifecycle
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException", e)
                    return@repeatOnLifecycle
                }
                if(response.isSuccessful && response.body() != null){
                    val products = response.body()?.products
                    (binding.productsRV.adapter as ProductListAdapter).productList = products ?: listOf()
                } else {
                    Log.e(TAG, "Response not successful")
                    Toast.makeText(this@ProductListActivity, "Response not successful", Toast.LENGTH_SHORT).show()
                }
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