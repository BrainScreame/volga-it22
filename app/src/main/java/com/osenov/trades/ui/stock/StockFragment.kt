package com.osenov.trades.ui.stock

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.osenov.trades.R
import com.osenov.trades.databinding.FragmentStockBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.random.Random


@AndroidEntryPoint
class StockFragment : Fragment() {

    private val viewModel: StockViewModel by viewModels()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStockBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        StockAdapter {
            Toast.makeText(requireContext(), it.displaySymbol, Toast.LENGTH_LONG).show()
        }
    }

    private val scrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateItems()
                }
            }
        }
    }

    private fun updateItems() {
        val linearLayout =
            binding.recyclerViewStocks.layoutManager as LinearLayoutManager
        val start = linearLayout.findFirstCompletelyVisibleItemPosition()
        val end = linearLayout.findLastCompletelyVisibleItemPosition()
        viewModel.updatePrices(start - 2, end + 2)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addSearchListeners()


        //viewModel.getStocks()
        binding.recyclerViewStocks.adapter = adapter
        binding.recyclerViewStocks.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if(position != 0) {
                    outRect.top = resources.getDimensionPixelOffset(R.dimen.margin_top_recycler)
                }
            }
        })
        binding.recyclerViewStocks.addOnScrollListener(scrollListener)

        lifecycleScope.launchWhenStarted {
            viewModel.stocks.collect {
                if(it.isNotEmpty()) {
                    adapter.setData(it)
                    binding.progressBarStocks.visibility = View.GONE
                    binding.recyclerViewStocks.visibility = View.VISIBLE
                    viewModel.updatePrices(0, 10)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.trades.collect {
                adapter.updateData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.quote.collect {
                adapter.updateItem(it)
            }
        }
    }

    private fun addSearchListeners() {
        binding.searchAction.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchAction.imageClose.visibility = View.VISIBLE
                binding.searchAction.imageSearch.setImageResource(R.drawable.ic_back)
                binding.searchAction.editTextSearch.setText("")
            } else {
                binding.searchAction.imageClose.visibility = View.GONE
                binding.searchAction.imageSearch.setImageResource(R.drawable.ic_search)
                binding.searchAction.editTextSearch.setText(resources.getString(R.string.search_text))
            }
        }

        binding.searchAction.imageSearch.setOnClickListener {
            if (binding.searchAction.imageSearch.drawable.constantState == ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_back
                )?.constantState
            ) {
                binding.searchAction.editTextSearch.clearFocus()
                requireActivity().currentFocus?.let { view ->
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }

        binding.searchAction.imageClose.setOnClickListener {
            binding.searchAction.editTextSearch.setText("")
        }
        binding.searchAction.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != viewModel.query.value){
                    viewModel.setQuery(s.toString())
                    binding.progressBarStocks.visibility = View.VISIBLE
                    binding.recyclerViewStocks.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

}