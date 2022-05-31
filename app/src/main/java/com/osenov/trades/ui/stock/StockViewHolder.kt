package com.osenov.trades.ui.stock

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.osenov.trades.R
import com.osenov.trades.databinding.ItemStockBinding
import androidx.core.content.ContextCompat
import com.osenov.trades.domain.entity.Quote
import com.osenov.trades.domain.entity.StockUI
import com.osenov.trades.domain.entity.Trade

class StockViewHolder(private val binding: ItemStockBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(stock: StockUI, position: Int) {
        with(binding) {
            progressStockPrice.visibility = View.VISIBLE
            linearLayoutQuote.visibility = View.GONE
            textError.visibility = View.GONE

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
        }
        stock.quote?.let {
            bindQuote(it)
        }
        stock.quoteExceptionMessage?.let {
            bindExceptionMessage(it)
        }
    }

    fun bindExceptionMessage(message: String) {
        with(binding) {
            linearLayoutQuote.visibility = View.GONE
            progressStockPrice.visibility = View.GONE
            textError.visibility = View.VISIBLE
            textError.text = message
        }
    }

    fun bindQuote(quote: Quote) {
        with(binding) {
            linearLayoutQuote.visibility = View.VISIBLE
            textError.visibility = View.GONE
            progressStockPrice.visibility = View.GONE

            textPrice.text = root.resources.getString(R.string.stock_price, quote.currentPrice)
            textPriceDayDelta.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.default_stock_color
                )
            )
            if (quote.percentChange > 0) {
                textPriceDayDelta.text = String.format(
                    root.resources.getString(R.string.stock_price_change_rise),
                    quote.change,
                    quote.percentChange
                )

                textPriceDayDelta.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.stock_rise_color)
                )
            } else if (quote.percentChange < 0) {
                textPriceDayDelta.text = String.format(
                    root.resources.getString(R.string.stock_price_change_decrease),
                    quote.change,
                    quote.percentChange
                )

                textPriceDayDelta.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.stock_decrease_color)
                )
            } else {
                textPriceDayDelta.text = String.format(
                    root.resources.getString(R.string.stock_price_change),
                    quote.change,
                    quote.percentChange
                )
            }
        }
    }

    fun bindTrade(trade: Trade) {
        with(binding) {
            linearLayoutQuote.visibility = View.VISIBLE
            textError.visibility = View.GONE
            progressStockPrice.visibility = View.GONE

            textPrice.text = root.resources.getString(R.string.stock_price, trade.lastPrice)

            //TODO add info price changed
        }
    }

}