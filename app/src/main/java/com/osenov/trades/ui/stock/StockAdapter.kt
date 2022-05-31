package com.osenov.trades.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.osenov.trades.databinding.ItemStockBinding
import com.osenov.trades.domain.entity.Quote
import com.osenov.trades.domain.entity.StockUI
import com.osenov.trades.domain.entity.Trade

class StockAdapter(private val onItemClicked: (StockUI) -> Unit) :
    RecyclerView.Adapter<StockViewHolder>() {
    companion object {
        const val QUOTE = "QUOTE"
        const val QUOTE_EXCEPTION_MESSAGE = "QUOTE_EXCEPTION_MESSAGE"
        const val TRADE = "TRADE"
    }

    private var stocks = ArrayList<StockUI>()

    fun updateItem(quoteResult: QuoteResult) {
        val diffPayLoad = Bundle()
        quoteResult.result.onSuccess {
            diffPayLoad.putParcelable(QUOTE, it)
            stocks[quoteResult.position].quote = it
        }
        quoteResult.result.onFailure {
            diffPayLoad.putString(QUOTE_EXCEPTION_MESSAGE, it.message)
            stocks[quoteResult.position].quoteExceptionMessage = it.message
        }
        notifyItemChanged(quoteResult.position, diffPayLoad)
    }

    fun updatePrice(tradeResult: TradeResult) {
        val diffPayLoad = Bundle()
        stocks[tradeResult.position].trade = tradeResult.trade
        diffPayLoad.putParcelable(TRADE, tradeResult.trade)
        notifyItemChanged(tradeResult.position, diffPayLoad)
    }

    fun setData(data: List<StockUI>) {
        stocks.clear()
        this.stocks.addAll(data)

        //TODO add DiffUtil
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

    override fun onBindViewHolder(
        holder: StockViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val bundle = payloads[0] as Bundle
            val message = bundle.getString(QUOTE_EXCEPTION_MESSAGE)
            val quote: Quote? = bundle.getParcelable(QUOTE)
            val trade: Trade? = bundle.getParcelable(TRADE)

            message?.let {
                holder.bindExceptionMessage(it)
            }
            quote?.let {
                holder.bindQuote(it)
            }
            trade?.let {
                holder.bindTrade(it)
            }
        }
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