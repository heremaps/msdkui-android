package com.here.msdkuidev.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.here.msdkuidev.R
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("Registered")
open class BaseListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun setUpList(list : List<String>, secondLine: Boolean = false,  listener: LandingScreenAdapter.Listener) {
        with(landing_list) {
            layoutManager = android.support.v7.widget.LinearLayoutManager(
                this@BaseListActivity,
                android.support.v7.widget.LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        val adapter = LandingScreenAdapter(list,secondLine, this@BaseListActivity)
        adapter.itemListener = listener
        landing_list.adapter = adapter
    }

    class LandingScreenAdapter(private val items: List<String>, private val secondLine: Boolean = false,
                               private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

        var itemListener: Listener? = null

        override fun getItemCount(): Int {
            return items.size
        }

        // Inflates the item views
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowItem = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
            rowItem.setOnClickListener { itemListener?.onItemClicked(rowItem) }
            return ViewHolder(rowItem)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                text1.text = items[position]
                with(text2) {
                    if(secondLine) {
                       visibility = View.VISIBLE
                        text = if(position % 2 == 0) context.getString(R.string.default_value) else context.getString(R.string.fix_size)
                    } else {
                        visibility = View.GONE
                    }
                }
            }
        }

        /**
         * Listener to be notified when item is clicked.
         */
        interface Listener {

            /**
             * Callback to indicate a row has been clicked.
             */
            fun onItemClicked(view: View)
        }
    }

    /**
     * ViewHolder to hold data for a row.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text1 = view.findViewById<TextView>(android.R.id.text1)!!
        val text2 = view.findViewById<TextView>(android.R.id.text2)!!
    }
}