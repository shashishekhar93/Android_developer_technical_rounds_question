package com.example.machineround

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*this task is about learning the working of looper and handler which is often use in google maps
* location services.
* marker of rider updates continuously using this mechenism.
* Here we are practicing a counter increment.*/

class T4Activity : ComponentActivity() {

    private lateinit var tvCounter: TextView
    private var workerThread: Thread? = null

    private val uiHandler = object : Handler(Looper.getMainLooper()) {
        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            val count = msg.arg1
            tvCounter.text = "msg from background = $count"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task4)

        tvCounter = findViewById(R.id.counterTextview)

        startBackGroundThread()


    }

    private fun startBackGroundThread() {
        workerThread = Thread {
            var counter = 0
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(1000)
                val message = uiHandler.obtainMessage().apply {
                    arg1 = counter
                }
                uiHandler.sendMessage(message)
                counter++
            }
        }
        workerThread?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        workerThread?.interrupt()
    }
}


/*Now we will do the same task using coroutines.
* Here we are practicing a counter increment.*/

class T4Activity2 : ComponentActivity() {
    private lateinit var tvCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task4)

        tvCounter = findViewById(R.id.counterTextview)
        startBackgroundWork()
    }

    private fun startBackgroundWork() {
        lifecycleScope.launch(Dispatchers.IO) {
            var counter = 1

            while (isActive) {
                delay(1000)

                withContext(Dispatchers.Main) {
                    tvCounter.text = "msg from bg = $counter"
                }
                counter++
            }
        }
    }
}

/*Now we will do the same task using Viewmodel and flow.
* Here we are practicing a counter increment.*/

//for that, first let's create a viewmodel class

class CounterViewModel : ViewModel() {
    private val _counterText = MutableStateFlow("Waiting...")
    val counterText: StateFlow<String> = _counterText.asStateFlow()

    init {
        startBackgroundWork()
    }

    private fun startBackgroundWork() {
        viewModelScope.launch(Dispatchers.IO) {
            var counter = 1
            while (isActive) {
                delay(1000)
                _counterText.value = "msg from Background= $counter"
                counter++
            }
        }
    }
}

class T4Activity3 : ComponentActivity() {
    private lateinit var tvCounter: TextView
    private val viewModel: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task4)

        tvCounter = findViewById(R.id.counterTextview)
        observeCounter()
    }

    private fun observeCounter() {
        lifecycleScope.launch {
            viewModel.counterText.collectLatest { text ->
                tvCounter.text = text
            }
        }
    }
}


class CounterViewModel2 : ViewModel() {
    private val _counterText = MutableLiveData("Waiting...")
    val counterText: LiveData<String> = _counterText

    init {
        startBackgroundWork()
    }

    private fun startBackgroundWork() {
        viewModelScope.launch(Dispatchers.IO) {
            var counter = 1
            while (isActive) {
                delay(1000)
                withContext(Dispatchers.Main){
                    _counterText.value = "msg from Background= $counter"
                }
                counter++
            }
        }
    }
}


/*Now we will do the same task using Viewmodel and Livedata.
* Here we are practicing a counter increment.*/

//for that, first let's create a viewmodel class
class T4Activity4 : ComponentActivity() {
    private lateinit var tvCounter: TextView
    private val viewModel: CounterViewModel2 by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task4)

        tvCounter = findViewById(R.id.counterTextview)
        observeCounter()
    }

    private fun observeCounter() {
        viewModel.counterText.observe(this, {
            tvCounter.text = it
        })
    }
}
