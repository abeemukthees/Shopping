package com.abeemukthees.shopping

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.View.GONE
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.entities.Product
import com.abeemukthees.domain.statemachine.home.HomeAction
import com.abeemukthees.domain.statemachine.home.HomeState
import com.abeemukthees.shopping.base.BaseActivity
import com.abeemukthees.shopping.user.SignInActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.item_product.view.*
import org.jetbrains.anko.startActivity
import org.koin.android.architecture.ext.viewModel

class HomeActivity : BaseActivity() {

    private val homeViewModel by viewModel<HomeViewModel>()

    private val taskRecyclerViewAdapter by lazy { HomeRecyclerViewAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        recyclerView_home.adapter = taskRecyclerViewAdapter
        recyclerView_home.layoutManager = LinearLayoutManager(this)

        homeViewModel.state.observe(this, Observer {
            setupViews(it!!)
        })

        homeViewModel.input.accept(HomeAction.LoadInitialDataAction)

        addDisposable(RxRecyclerView.scrollStateChanges(recyclerView_home)
                .map { !recyclerView_home.canScrollVertically(it) }
                .filter { it }
                .map { HomeAction.LoadMoreDataAction }
                .subscribe(homeViewModel.input))

        addDisposable(RxView.clicks(fab).subscribe { startActivity<SignInActivity>() })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews(state: State) {

        when (state) {

            is HomeState.LoadingFirstTimeState -> {

                recyclerView_home.visibility = GONE
                progressBar.visibility = View.VISIBLE
            }

            is HomeState.ShowDataState -> {

                recyclerView_home.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

                taskRecyclerViewAdapter.loadItems(state.items, false)
            }

            is HomeState.ShowDataAndLoadMoreState -> {

                recyclerView_home.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

                taskRecyclerViewAdapter.loadItems(state.items, true)
                recyclerView_home.scrollToPosition(taskRecyclerViewAdapter.products.count()) // Scroll to the last item
            }

            is HomeState.ShowDataAndLoadMoreErrorState -> {

                recyclerView_home.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

                taskRecyclerViewAdapter.loadItems(state.items, false)

                showToastMessage("Error loading")
            }

            is HomeState.ErrorLoadingInitialDataState -> {

                recyclerView_home.visibility = View.GONE
                progressBar.visibility = View.GONE

                showToastMessage("Some real shit happened")
            }

            else -> Log.d(TAG, "Received state ${state::class.simpleName}")
        }

        invalidateOptionsMenu()
    }
}

class ProductRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title = itemView.text_title!!

}

class HomeRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = HomeRecyclerViewAdapter::class.simpleName

    private val VIEW_TYPE_REPO = 1
    private val VIEW_TYPE_LOADING_NEXT = 2

    var products = emptyList<Product>()
    private var showLoading = false

    fun loadItems(products: List<Product>, showLoading: Boolean) {
        //Log.d(HomeRecyclerViewAdapter::class.simpleName,"loadItems size = ${products.size}, showLoading = $showLoading")
        this.products = products
        this.showLoading = showLoading
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int =
            if (showLoading && position == products.size) // Its the last item and show loading
                VIEW_TYPE_LOADING_NEXT
            else
                VIEW_TYPE_REPO

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_REPO -> ProductRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false))
            VIEW_TYPE_LOADING_NEXT -> LoadingNextPageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_next, parent, false))
            else -> throw IllegalArgumentException("ViewType $viewType is unexpected")
        }
    }

    override fun getItemCount(): Int {
        return products.size + (if (showLoading) 1 else 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductRecyclerViewHolder) {
            val product = products[position]
            holder.title.text = product.name
        }

    }

    inner class LoadingNextPageViewHolder(v: View) : RecyclerView.ViewHolder(v)
}



