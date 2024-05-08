package com.eluded.privacymanager.features.panickwipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.eluded.privacymanager.R
import com.eluded.privacymanager.databinding.FragmentTriggerSettingsBinding
import com.eluded.privacymanager.features.panickwipe.trigger.PanickTrigger


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PanickTriggerRecyclerAdapter(
    private val values: List<PanickTrigger>,
    private val context: Context
) : RecyclerView.Adapter<PanickTriggerRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTriggerSettingsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.settingbutton.text = item.name
        holder.settingbutton.setOnClickListener {
            var manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
             manager.beginTransaction().add(R.id.fragment, item.fragment).addToBackStack(item.name).commit()

        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentTriggerSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val settingbutton: TextView = binding.PanicTriggerSettingsButton

    }

}