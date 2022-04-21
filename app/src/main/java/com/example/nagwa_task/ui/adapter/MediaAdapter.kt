package com.example.nagwa_task.ui.adapter

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nagwa_task.databinding.MediaItemBinding
import com.example.nagwa_task.model.ResponseItem


class MediaAdapter(val list: ArrayList<ResponseItem>, val Click: (Int, ResponseItem) -> Unit) :
    RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    inner class MediaViewHolder(val binding: MediaItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    var lastloaded: Int = 0
    lateinit var dm: DownloadManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding =
            MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        dm = parent.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return MediaViewHolder(binding)
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            media = item
            if (item.type.equals("VIDEO")) {

                videotitleTxt.text = item.name
                layoutVideo.visibility = View.VISIBLE
                pdfviewLayout.visibility = View.GONE

            } else if (item.type.equals("PDF")) {
                val pdfuri = Uri.parse(item.url)
                pdfView.fromUri(pdfuri)
                pdfTitle.text = item.name
                layoutVideo.visibility = View.GONE
                pdfviewLayout.visibility = View.VISIBLE
            }
            downloadVideo.setOnClickListener {
                Click(position, item)
                var request =
                    DownloadManager.Request(Uri.parse(item.url)).setTitle(item.name + ".mp4")
                        .setDescription("${item.name} is downloading......")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setAllowedOverMetered(true).setVisibleInDownloadsUi(false)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "fileName.mp4"
                )
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                val mydownloaded = dm.enqueue(request)
                downloadVideo.visibility = View.GONE
                request.setDestinationInExternalFilesDir(
                    holder.itemView.context.applicationContext,
                    "/file",
                    "Question1.mp4"
                )
                var isDownloading: Boolean = true
                Thread {
                    var totalBytesDownloaded: Int
                    var totalBytes: Int
                    while (isDownloading) {
                        val downloadQuery = DownloadManager.Query()
                        downloadQuery.setFilterById(mydownloaded)
                        var cursor: Cursor = dm.query(downloadQuery)
                        cursor.moveToFirst()
                        totalBytesDownloaded = cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                        )

                        totalBytes = cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                        )
                        Log.d("testDownload", totalBytesDownloaded.toString() + " " + totalBytes)
                        val downloadStatus =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL
                            || downloadStatus == DownloadManager.STATUS_FAILED
                        ) {
                            (holder.itemView.context as AppCompatActivity).runOnUiThread(Runnable {
                                progressdownloadVideo.visibility = View.GONE
                                progresstxt.visibility = View.GONE
                                downloadcompleteVideo.visibility = View.VISIBLE
                            })
                            isDownloading = false
                            break
                        }

                        if (totalBytes > 0) {
                            val downloadProgress =
                                (totalBytesDownloaded.toDouble() / totalBytes.toDouble() * 100f).toInt()

                            (holder.itemView.context as AppCompatActivity).runOnUiThread(Runnable {
                                progressdownloadVideo.visibility = View.VISIBLE
                                progressdownloadVideo.progress = downloadProgress
                                progresstxt.text = downloadProgress.toString() + "%"
                                Log.d("testDownload", "downloadProgress: $downloadProgress")
                            })
                        }

                        cursor.close()
                    }
                }.start()


            }
            downloadPdf.setOnClickListener {
                Click(position, item)
                var request =
                    DownloadManager.Request(Uri.parse(item.url)).setTitle(item.name + ".pdf")
                        .setDescription("${item.name} is downloading......")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setAllowedOverMetered(true).setVisibleInDownloadsUi(false)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "fileName.pdf"
                )
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                val mydownloaded = dm.enqueue(request)
                downloadPdf.visibility = View.GONE
                request.setDestinationInExternalFilesDir(
                    holder.itemView.context.applicationContext,
                    "/file",
                    "Question2.pdf"
                )
                var isDownloading: Boolean = true
                Thread {
                    var totalBytesDownloaded: Int
                    var totalBytes: Int
                    while (isDownloading) {
                        val downloadQuery = DownloadManager.Query()
                        downloadQuery.setFilterById(mydownloaded)
                        dm.query(downloadQuery)?.use { cursor ->
                            cursor.moveToFirst()
                            totalBytesDownloaded = cursor.getInt(
                                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                            )

                            totalBytes = cursor.getInt(
                                cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                            )
                            Log.d("testDownload",
                                totalBytesDownloaded.toString() + " " + totalBytes)

                            if (totalBytes > 0) {
                                val downloadProgress =
                                    (totalBytesDownloaded.toDouble() / totalBytes.toDouble() * 100f).toInt()

                                (holder.itemView.context as AppCompatActivity).runOnUiThread {
                                    progressdownloadPdf.visibility = View.VISIBLE
                                    progressdownloadPdf.progress = downloadProgress
                                    progresstxtPdf.text = downloadProgress.toString() + "%"
                                    Log.d("testDownload", "downloadProgress: $downloadProgress")
                                }
                            }

                            val downloadStatus =
                                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL
                                || downloadStatus == DownloadManager.STATUS_FAILED
                            ) {
                                (holder.itemView.context as AppCompatActivity).runOnUiThread {
                                    progressdownloadPdf.visibility = View.GONE
                                    progresstxtPdf.visibility = View.GONE
                                    downloadcompletePdf.visibility = View.VISIBLE
                                }
                                isDownloading = false
                            }
                        }
                    }
                }.start()


            }

            var br = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    //                   if(id==mydownloaded){
//                       Toast.makeText(holder.itemView.context,"${item.name}is downloading complete...",Toast.LENGTH_LONG).show()
//
//                   }
                }

            }
            holder.itemView.context.registerReceiver(
                br,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )


            playvideobtn.setOnClickListener {
                val videouri = Uri.parse(item.url)
                video.setVideoURI(videouri)
                Click(position, item)
                video.start()
                playvideobtn.visibility = View.GONE
                progressbar.visibility = View.VISIBLE
                video.setOnCompletionListener {
                    playvideobtn.visibility = View.VISIBLE
                }
                video.setOnPreparedListener {
                    it.setOnVideoSizeChangedListener { mp, width, height ->
                        progressbar.visibility = View.GONE
                    }
                }
                video.setOnErrorListener { mp, what, extra ->
                    progressbar.visibility = View.GONE
                    playvideobtn.visibility = View.VISIBLE
                    false
                }


            }
        }


    }

    fun setData(list: ArrayList<ResponseItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    override fun getItemCount() = list.size

}

