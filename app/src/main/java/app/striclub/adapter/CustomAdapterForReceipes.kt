package app.striclub.adapter

import app.striclub.R
import app.striclub.callback.InterfaceSelectCatogory
import app.striclub.pojo.CheckFordataDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recepieslist.view.*
import android.widget.CompoundButton



class CustomAdapterForReceipes(private val cityList: MutableList<CheckFordataDialog>, private val listner: InterfaceSelectCatogory) : RecyclerView.Adapter<CustomAdapterForReceipes.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapterForReceipes.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recepieslist, parent, false)
        return ViewHolder(v)
    }


    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapterForReceipes.ViewHolder, position: Int) {
        holder.bindItems(cityList[position],listner)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cityList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(result: CheckFordataDialog, listner: InterfaceSelectCatogory) {


            val textViewName = itemView.tv_name

            textViewName.text = result.tittle

            if(result.ischeck){
                itemView.tv_name.isChecked=true
            }
            if(result.tittle.equals("Time")){
                itemView.tv_name.visibility=View.INVISIBLE
                itemView.tv_catogory.visibility=View.VISIBLE
                itemView.tv_catogory.text = result.tittle


            }
            if(result.tittle.equals("Cusines")){
                itemView.tv_name.visibility=View.INVISIBLE
                itemView.tv_catogory.visibility=View.VISIBLE
                itemView.tv_catogory.text = result.tittle


            }
            itemView.tv_name.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                if(isChecked){
                    listner.selectRecepies(result.tittle)
                    result.ischeck=true

                }



            }
            )

            itemView.setOnClickListener {

            }

        }
    }
}