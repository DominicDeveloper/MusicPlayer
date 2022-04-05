package com.dominic.musicplayer2022
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.dominic.musicplayer2022.Adapter.RvAdapter
import com.dominic.musicplayer2022.Models.Music
import kotlinx.android.synthetic.main.fragment_list.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_list, container, false)
              val musicList: MutableList<Music> = context?.musicFiles()!!
              MyData.list = musicList as ArrayList
              val adapter = RvAdapter(requireContext(),musicList, object : RvAdapter.RvItemClick {
                  override fun onClick(music: Music, position: Int) {
                      findNavController().navigate(R.id.media, bundleOf("music" to music, "position" to position))
                  }
              })
              root.my_music_recycle_view.adapter= adapter

        return root
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(root.context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            val musicList: MutableList<Music> = context?.musicFiles()!!
            MyData.list = musicList as ArrayList
            val adapter = RvAdapter(requireContext(),musicList, object : RvAdapter.RvItemClick {
                override fun onClick(music: Music, position: Int) {
                    findNavController().navigate(R.id.media, bundleOf("music" to music, "position" to position))
                }
            })
            root.my_music_recycle_view.adapter = adapter
        }
    }
    private fun checkPermission(permission:String):Boolean{
        val checkPm:Int = ContextCompat.checkSelfPermission(requireContext(),permission)
        return (checkPm == PackageManager.PERMISSION_GRANTED)
    }



    @SuppressLint("Range", "InlinedApi")
    fun Context.musicFiles(): MutableList<Music> {

        val list: MutableList<Music> = mutableListOf()


        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"


        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"


        val cursor: Cursor? = this.contentResolver.query(
            uri, // Uri
            null, // Projection
            selection, // Selection
            null, // Selection arguments
            sortOrder // Sort order
        )

        // If query result is not empty
        if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val imageId: Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
            val authorId: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)


            do {
                val audioId: Long = cursor.getLong(id)
                val audioTitle: String = cursor.getString(title)
                var imagePath: String = ""
                if (imageId != -1) {
                    imagePath = cursor.getString(imageId)
                }
                    
                val musicPath: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val artist = cursor.getString(authorId)


                list.add(Music(audioId, audioTitle, imagePath, musicPath, artist))
            } while (cursor.moveToNext())
        }


        return list
    }




    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

object MyData{
    var list:ArrayList<Music> = ArrayList()
}