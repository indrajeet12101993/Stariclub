package app.striclub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.striclub.R
import app.striclub.callback.InterfaceCitySelectListner
import app.striclub.pojo.city.Result

class CustomAdapterForCity(private val cityList: MutableList<Result>, private val listner: InterfaceCitySelectListner) : RecyclerView.Adapter<CustomAdapterForCity.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapterForCity.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.state_list, parent, false)
        return ViewHolder(v)
    }


    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapterForCity.ViewHolder, position: Int) {
        holder.bindItems(cityList[position],listner)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cityList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(result: Result, listner: InterfaceCitySelectListner) {
            val textViewName = itemView.findViewById(R.id.tv_name) as TextView
            textViewName.text = result.name

            itemView.setOnClickListener {
                listner.onItemClickState(result)
            }

        }
    }
}