package com.sonny.gpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sonny.gpt.database.entity.ContentEntity
import com.nohjunh.test.databinding.GptContentItemBinding
import com.nohjunh.test.databinding.UserContentItemBinding

class ContentAdapter(private val dataSet: List<ContentEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val Gpt = 1
        const val User = 2
    }

    interface DelChatLayoutClick {
        fun onLongClick(view: View, position: Int)
    }

    var delChatLayoutClick: DelChatLayoutClick? = null

    inner class ViewHolderGPT(val binding: GptContentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ViewHolderUser(val binding: UserContentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Gpt) {
            val binding =
                GptContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderGPT(binding)
        } else {
            val binding =
                UserContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderUser(binding)
        }
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].gptOrUser == 1) { // Gpt
            Gpt
        } else {
            User
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Gpt) {
            (holder as ViewHolderGPT).binding.apply {
                setData(rvItemTV, holdingId, chatLayout, position)
            }
        } else {
            (holder as ViewHolderUser).binding.apply {
                setData(rvItemTV, holdingId, chatLayout, position)
            }
        }
    }

    private fun setData(
        rvItemTV: TextView,
        holdingId: TextView,
        chatLayout: ConstraintLayout,
        position: Int
    ) {
        rvItemTV.text = dataSet[position].content
        holdingId.text = dataSet[position].id.toString()
        chatLayout.setOnLongClickListener { view ->
            delChatLayoutClick?.onLongClick(view, position)
            return@setOnLongClickListener true
        }
    }

}