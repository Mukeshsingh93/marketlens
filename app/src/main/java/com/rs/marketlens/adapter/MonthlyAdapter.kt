package com.rs.marketlens.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rs.marketlens.R
import com.rs.marketlens.network.MontlyPlan

class MonthlyAdapter  : ArrayAdapter<MontlyPlan> {


    private var CustomSpinnerItems: List<MontlyPlan>

    constructor(context: Context, resource: Int, pickerItems: List<MontlyPlan>) : super(context, resource, pickerItems){this.CustomSpinnerItems = pickerItems}


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return CustomSpinnerView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return CustomSpinnerView(position, convertView, parent)
    }

    private fun CustomSpinnerView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = layoutInflater.inflate(R.layout.montly_spinner, parent, false)
        val textView = customView.findViewById(R.id.heighttext) as TextView
        textView.setText(CustomSpinnerItems[position].duration.toString())
        return customView
    }

}