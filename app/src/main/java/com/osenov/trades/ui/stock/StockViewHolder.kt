package com.osenov.trades.ui.stock

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.osenov.trades.R
import com.osenov.trades.databinding.ItemStockBinding
import com.osenov.trades.domain.entity.StockSymbol
import androidx.core.content.ContextCompat
import com.osenov.trades.domain.entity.StockUI

class StockViewHolder(private val binding: ItemStockBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(stock: StockUI, position: Int) {
        with(binding) {
            textStockName.text = stock.displaySymbol
            textStockDescription.text = stock.description

            if (position % 2 == 0) {
                constraintLayoutStock.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.stock_background)
                )
            } else {
                constraintLayoutStock.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
            }

            stock.quote?.let {
                linearLayoutQuote.visibility = View.VISIBLE
                textError.visibility = View.GONE
                progressStockPrice.visibility = View.GONE

                textPrice.text = "$${it.currentPrice}"
                textPriceDayDelta.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.default_stock_color
                    )
                )
                if (it.percentChange > 0) {
                    textPriceDayDelta.text = "+$${it.change} (${it.percentChange}%)"
                    textPriceDayDelta.setTextColor(
                        ContextCompat.getColor(binding.root.context, R.color.stock_rise_color)
                    )
                } else if (it.percentChange < 0) {
                    textPriceDayDelta.text = "-$${it.change} (${it.percentChange}%)"
                    textPriceDayDelta.setTextColor(
                        ContextCompat.getColor(binding.root.context, R.color.stock_decrease_color)
                    )
                } else {
                    textPriceDayDelta.text = "$${it.change} (${it.percentChange}%)"
                }
            }
            if (stock.quote == null) {
                if (stock.quoteExceptionMessage != null) {
                    linearLayoutQuote.visibility = View.GONE
                    progressStockPrice.visibility = View.GONE
                    textError.visibility = View.VISIBLE
                    textError.text = stock.quoteExceptionMessage
                } else {
                    linearLayoutQuote.visibility = View.GONE
                    progressStockPrice.visibility = View.VISIBLE
                    textError.visibility = View.GONE
                }
            }

        }
    }

}