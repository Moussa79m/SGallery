package com.moussa79m.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.videoFrameMillis
import com.moussa79m.domain.Model.MediaItem

data class SimpleGalleryItem(
    val isHeader: Boolean,
    val dateTitle: String = "",
    val media: MediaItem? = null
)

class GalleryMediaAdapter(private val onMediaClick: (MediaItem) -> Unit) :
    ListAdapter<SimpleGalleryItem, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        // نوع 0 = صورة، نوع 1 = عنوان تاريخ
        return if (getItem(position).isHeader) 1 else 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_gallery_header, parent, false)
            val layoutParms = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            view.layoutParams=layoutParms
            HeaderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_gallery_media, parent, false)
            MediaViewHolder(view)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        if (holder is HeaderViewHolder) {
            holder.textDate.text = item.dateTitle
        } else if (holder is MediaViewHolder && item.media != null) {
            holder.bind(item.media)
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textDate: TextView = view.findViewById(R.id.text_date)
    }

    inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageMedia: ImageView = itemView.findViewById(R.id.image_media)
        private val iconVideo: ImageView = itemView.findViewById(R.id.icon_video)
        private val overlaySelected: View = itemView.findViewById(R.id.overlay_selected)

        init {
            itemView.setOnClickListener {
                getItem(bindingAdapterPosition).media?.let { onMediaClick(it) }
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION)
//                    onMediaClick(getItem(position))
            }
        }

        fun bind(mediaItem: MediaItem) {
            iconVideo.visibility = if (mediaItem.isVideo) {
                View.VISIBLE
            } else {
                View.GONE
            }
            imageMedia.load(mediaItem.uri) {
                crossfade(true)
                size(300)
                if (mediaItem.isVideo) {
                    videoFrameMillis(0)
                }
            }

        }
    }

}

class DiffCallback : DiffUtil.ItemCallback<SimpleGalleryItem>() {
    override fun areItemsTheSame(
        oldItem: SimpleGalleryItem,
        newItem: SimpleGalleryItem
    ): Boolean {
        return if (!oldItem.isHeader && !newItem.isHeader)
            oldItem.media?.id == newItem.media?.id
        else oldItem.dateTitle == newItem.dateTitle
    }

    override fun areContentsTheSame(
        oldItem: SimpleGalleryItem,
        newItem: SimpleGalleryItem
    ): Boolean {
        return oldItem == newItem
    }

}