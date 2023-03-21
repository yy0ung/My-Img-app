package com.example.my_image_app

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _repositoriesSearchRst = MutableLiveData<ArrayList<RstListDto>>()
    val repositories1 : MutableLiveData<ArrayList<RstListDto>>
        get() = _repositoriesSearchRst
    private val _repositoriesGetPref = MutableLiveData<ArrayList<SaveItemDto>>()
    val repositories3 : MutableLiveData<ArrayList<SaveItemDto>>
        get() = _repositoriesGetPref

    init {
        Log.d(TAG, "start: ")
    }
    
    private val imgAndVideo = ArrayList<RstListDto>()

    suspend fun searchRst(key : String, query : String, sort : String, page : Int, size : Int){
        viewModelScope.launch {
            repository.searchImg(key, query, sort, page, size).let { response ->
                if (response.isSuccessful && response.body()!=null) {
                    for (i in 0 until response.body()!!.documents.size){
                        imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                    }
                }
            }
            repository.searchVideo(key, query, sort, page).let { response ->
                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.documents.size){
                        imgAndVideo.add(RstListDto(response.body()!!.documents[i].thumbnail, response.body()!!.documents[i].datetime))
                    }
                }
            }
            imgAndVideo.sortWith(compareByDescending{ it.datetime })
            _repositoriesSearchRst.postValue(imgAndVideo)

        }
    }


    suspend fun getPref(key : String, default : String){
        viewModelScope.launch {
            repository.getPref(key, default).let { response->
                _repositoriesGetPref.postValue(response)
            }
        }
    }

    /*
    class MyViewModel : ViewModel() {
    private val apiService = Retrofit.Builder()
        .baseUrl("https://myapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private var isLoading = false
    private var hasMorePages = true

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    fun loadItems(page: Int) {
        isLoading = true

        viewModelScope.launch {
            try {
                val response = apiService.getItems(page)
                val newItems = response.items

                if (page == 1) {
                    _items.value = newItems
                } else {
                    val currentItems = _items.value ?: emptyList()
                    _items.value = currentItems + newItems
                }

                hasMorePages = (page < 3) // Assumes a maximum of 3 pages
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }
}


////fragment code
class MyFragment : Fragment() {
    private lateinit var myAdapter: MyAdapter
    private lateinit var myViewModel: MyViewModel
    private var currentPage = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        // Initialize RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        myAdapter = MyAdapter()
        recyclerView.adapter = myAdapter

        // Initialize ViewModel
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        // Observe the items LiveData from the ViewModel
        myViewModel.items.observe(viewLifecycleOwner, { items ->
            myAdapter.submitList(items)
        })

        // Add scroll listener to the RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1 && !myViewModel.isLoading && myViewModel.hasMorePages) {
                    // Load the next page of data
                    currentPage++
                    myViewModel.loadItems(currentPage)
                }
            }
        })

        return view
    }
}

///adapter
class MyAdapter : ListAdapter<Item, MyViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)

        fun bind(item: Item) {
            titleTextView.text = item.title
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}


     */
}