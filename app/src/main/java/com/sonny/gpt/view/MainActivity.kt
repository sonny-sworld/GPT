package com.sonny.gpt.view

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.nohjunh.test.R
import com.sonny.gpt.adapter.ContentAdapter
import com.sonny.gpt.database.entity.ContentEntity
import com.nohjunh.test.databinding.ActivityMainBinding
import com.sonny.gpt.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var branch : Int = 1
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private var contentDataList = ArrayList<ContentEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* coil GIF  */
        val imageLoader = this.let {
            ImageLoader.Builder(it)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
        }
        Coil.setImageLoader(imageLoader)
        binding.loading.visibility = View.INVISIBLE
        binding.loading.load(R.drawable.loading3)

        viewModel.getContentData()

        viewModel.contentList.observe(this, Observer {
            contentDataList.clear()
            for (entity in it) {
                contentDataList.add(entity)
            }
            setContentListRV(branch)
        })

        viewModel.deleteCheck.observe(this, Observer {
            if (it == true) {
                viewModel.getContentData()
                branch = 1
            }
        })

        viewModel.gptInsertCheck.observe(this, Observer {
            if (it == true) {
                viewModel.getContentData()
                binding.loading.visibility = View.INVISIBLE
            }
            branch = 2
        })

        binding.sendBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE

            viewModel.postResponse(binding.EDView.text.toString())
            viewModel.insertContent(binding.EDView.text.toString(), 2) // 1: Gpt, 2: User
            binding.EDView.setText("")
            branch = 2
            viewModel.getContentData()
        }

    }

    private fun setContentListRV(branch : Int) {
        val contentAdapter = ContentAdapter(contentDataList)
        binding.RVContainer.adapter = contentAdapter
        binding.RVContainer.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        //binding.RVContainer.scrollToPosition(contentDataList.size-1)
        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            binding.SVContainer.fullScroll(ScrollView.FOCUS_DOWN);
            if (branch != 1) {
                binding.EDView.requestFocus()
            }
        }
        // onClick
        contentAdapter.delChatLayoutClick = object : ContentAdapter.DelChatLayoutClick {
            override fun onLongClick(view : View, position: Int) {
                Timber.tag("data list").e("${contentDataList[position].id}")
                // alertDialog
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Would you like to remove the data?")
                    .setMessage("Once removed, the data would be recovered")
                    .setPositiveButton("YES"
                    ) { _, _ ->
                        viewModel.deleteSelectedContent(contentDataList[position].id)
                    }.setNegativeButton("NO"
                    ) { _, _ -> }
                builder.show()
            }
        }
    }
}