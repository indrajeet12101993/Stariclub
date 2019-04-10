package app.striclub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.striclub.R
import app.striclub.pojo.searchsuggestion.Result
import app.striclub.pojo.stepAndIngriedients.Ingredient
import kotlinx.android.synthetic.main.ingridents.view.*

class CustomIngredientsAdapter(val userSuggestions: List<Ingredient>) : RecyclerView.Adapter<CustomIngredientsAdapter.ViewHolder>()  {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomIngredientsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ingridents, parent, false)

        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomIngredientsAdapter.ViewHolder, position: Int) {

        holder.bindItems(userSuggestions[position])



    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userSuggestions.size

    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(result: Ingredient) {

            itemView. tv_ingredients.text = result.ingredients

            itemView.setOnClickListener {


            }


        }
    }
}