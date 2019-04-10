package app.striclub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.striclub.R
import app.striclub.pojo.stepAndIngriedients.Ingredient
import app.striclub.pojo.stepAndIngriedients.Step
import kotlinx.android.synthetic.main.ingridents_steps.view.*


class CustomAdapterSteps(val userSuggestions: List<Step>) : RecyclerView.Adapter<CustomAdapterSteps.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapterSteps.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ingridents_steps, parent, false)

        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapterSteps.ViewHolder, position: Int) {

        holder.bindItems(userSuggestions[position])



    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userSuggestions.size

    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(result: Step) {

            itemView. tv_ingredients.text = result.steps
            itemView. tv_count.text = result.step_start+"."

            itemView.setOnClickListener {


            }


        }
    }
}