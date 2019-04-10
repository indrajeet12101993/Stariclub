package app.striclub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.customadapterforsearchsuggestions.view.*
import app.striclub.R
import app.striclub.callback.InterfaceSearchAgainSuggestions
import app.striclub.pojo.searchsuggestion.Result

class CustomSearchAgainAdapter(val userSuggestions: MutableList<Result>, private val listner: InterfaceSearchAgainSuggestions) : RecyclerView.Adapter<CustomSearchAgainAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomSearchAgainAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.customadapterforsearchsuggestions, parent, false)

        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomSearchAgainAdapter.ViewHolder, position: Int) {

        holder.bindItems(userSuggestions[position],listner)



    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userSuggestions.size

    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(result: Result, listner: InterfaceSearchAgainSuggestions) {

            itemView.  tv_name.text = result.name
            itemView.setOnClickListener {

                listner.searchname(result.name)
            }


        }
    }
}