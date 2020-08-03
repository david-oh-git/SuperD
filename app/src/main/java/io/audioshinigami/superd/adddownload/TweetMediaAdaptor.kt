/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.audioshinigami.superd.adddownload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.data.TweetMedia
import io.audioshinigami.superd.databinding.TweetMediaItemBinding

class TweetMediaAdaptor(
    private val listener: TweetMediaItemClickAction
): ListAdapter<TweetMedia, TweetMediaAdaptor.TweetMediaViewHolder>(
    BitrateDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetMediaViewHolder = TweetMediaViewHolder.from(parent)

    override fun onBindViewHolder(holder: TweetMediaViewHolder, position: Int) {

        getItem(position)?.apply {
            holder.bind(this, listener)
        } ?: holder.clear()
    }

    class TweetMediaViewHolder(private val binding: TweetMediaItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(_tweetMedia: TweetMedia, listener: TweetMediaItemClickAction){
            with(binding){
                tweetMedia = _tweetMedia
                clickListener = listener
                executePendingBindings()
            }
        }

        fun clear(){
            binding.unbind()
        }

        companion object {

            fun from(parent: ViewGroup): TweetMediaViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TweetMediaItemBinding.inflate(layoutInflater, parent, false)

                return TweetMediaViewHolder(binding)
            }
        }

    }

    private class BitrateDiffCallBack: DiffUtil.ItemCallback<TweetMedia>(){

        override fun areItemsTheSame(oldItem: TweetMedia, newItem: TweetMedia): Boolean {
            return oldItem.bitrate == newItem.bitrate
        }

        override fun areContentsTheSame(oldItem: TweetMedia, newItem: TweetMedia): Boolean {
            return oldItem.url == newItem.url
        }
    }
}