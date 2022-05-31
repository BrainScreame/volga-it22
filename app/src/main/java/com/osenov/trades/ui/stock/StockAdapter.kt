package com.osenov.trades.ui.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.osenov.trades.databinding.ItemStockBinding
import com.osenov.trades.domain.entity.StockSymbol
import com.osenov.trades.domain.entity.StockUI
import com.osenov.trades.domain.entity.Trade

class StockAdapter(private val onItemClicked: (StockUI) -> Unit) :
    RecyclerView.Adapter<StockViewHolder>() {

    private var stocks = ArrayList<StockUI>()

    fun updateItem(quoteResult: QuoteResult) {
        quoteResult.result.onSuccess {
            stocks[quoteResult.position].quote = quoteResult.result.getOrNull()
            notifyItemChanged(quoteResult.position)
        }
        quoteResult.result.onFailure {
            stocks[quoteResult.position].quoteExceptionMessage = quoteResult.result.exceptionOrNull()?.message
            notifyItemChanged(quoteResult.position)
        }

    }

    fun updateData(data: List<Trade>) {
        for (trade in data) {
            for (i in 0 until stocks.size) {
                if (trade.ticker == stocks[i].displaySymbol) {
                    stocks[i].trade = trade
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun setData(data: List<StockUI>) {
        stocks.clear()
        this.stocks.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder(
            ItemStockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stocks[position], position)
        holder.itemView.setOnClickListener {
            onItemClicked(stocks[position])
        }
    }

    override fun getItemCount(): Int {
        return stocks.size
    }
}