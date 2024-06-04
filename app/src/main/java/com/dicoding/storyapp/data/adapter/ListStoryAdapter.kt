package com.dicoding.storyapp.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.paging.entity.StoryEntity
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.databinding.ItemStoryBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

//class ListStoryAdapter(private val context: Context, ):
//    PagingDataAdapter<StoryEntity, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
//
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val stories = getItem(position)
////        holder.bind(stories, context)
////        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(position)) }
//
//        if (stories != null) {
//            holder.bind(stories, context)
//            holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(stories) }
//        }
//    }
//
//    class MyViewHolder(private val binding: ItemStoryBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(stories: StoryEntity, context: Context) {
//            binding.tvItemName.text = stories.name
//            binding.description.text = stories.description
//            binding.dateTextView.text = formatRelativeTime(stories.createdAt, context)
//
//            Glide.with(itemView.context)
//                .load(stories.photoUrl)
//                .fitCenter()
//                .override(Target.SIZE_ORIGINAL)
//                .skipMemoryCache(true)
//                .into(binding.ivItemPhoto)
//        }
//
//        private fun formatRelativeTime(iso8601DateTime: String?, context: Context): String {
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
//                timeZone = TimeZone.getTimeZone("UTC")
//            }
//
//            return try {
//                val dateCreated = inputFormat.parse(iso8601DateTime) ?: return context.getString(R.string.invalid_date)
//
//                val calendar = Calendar.getInstance().apply {
//                    time = dateCreated
//                    timeZone = TimeZone.getTimeZone("Asia/Jakarta")
//                }
//
//                val dateIndonesia = calendar.time
//                val now = Date()
//                val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(now.time - dateIndonesia.time)
//
//                when {
//                    diffInMinutes < 1 -> context.getString(R.string.just_now)
//                    diffInMinutes < 60 -> context.getString(R.string.minutes_ago, diffInMinutes)
//                    diffInMinutes < 1440 -> context.getString(R.string.hours_ago, TimeUnit.MINUTES.toHours(diffInMinutes))
//                    else -> SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale.getDefault()).format(dateIndonesia)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                context.getString(R.string.invalid_date)
//            }
//        }
//    }
//
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(data: ListStoryItem)
//    }
//
//    companion object {
//
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
//            override fun areItemsTheSame(
//                oldItem: StoryEntity,
//                newItem: StoryEntity
//            ): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(
//                oldItem: StoryEntity,
//                newItem: StoryEntity
//            ): Boolean {
//                return newItem == oldItem
//            }
//        }
//    }
//}

class ListStoryAdapter(private val context: Context) :
    PagingDataAdapter<StoryEntity, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stories = getItem(position)
        if (stories != null) {
            holder.bind(stories, context)
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(stories) }
        }
    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryEntity, context: Context) {
            binding.tvItemName.text = story.name
            binding.description.text = story.description
            binding.dateTextView.text = formatRelativeTime(story.createdAt, context)

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .fitCenter()
                .override(Target.SIZE_ORIGINAL)
                .skipMemoryCache(true)
                .into(binding.ivItemPhoto)
        }

        private fun formatRelativeTime(iso8601DateTime: String?, context: Context): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            return try {
                val dateCreated = inputFormat.parse(iso8601DateTime) ?: return context.getString(R.string.invalid_date)

                val calendar = Calendar.getInstance().apply {
                    time = dateCreated
                    timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                }

                val dateIndonesia = calendar.time
                val now = Date()
                val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(now.time - dateIndonesia.time)

                when {
                    diffInMinutes < 1 -> context.getString(R.string.just_now)
                    diffInMinutes < 60 -> context.getString(R.string.minutes_ago, diffInMinutes)
                    diffInMinutes < 1440 -> context.getString(R.string.hours_ago, TimeUnit.MINUTES.toHours(diffInMinutes))
                    else -> SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale.getDefault()).format(dateIndonesia)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                context.getString(R.string.invalid_date)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryEntity)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id // Assuming StoryEntity has an id field
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
