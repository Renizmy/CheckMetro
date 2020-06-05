package com.example.checkmetro.ui.fragments

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkmetro.R
import com.example.checkmetro.adapter.FavoritesFragmentAdapter
import com.example.checkmetro.data.LinkLineStationDao
import com.example.checkmetro.data.StationFavoritesDao
import com.example.checkmetro.model.StationFavorites
import com.example.checkmetro.model.StationsLine
import com.example.checkmetro.utils.dao
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlin.math.roundToInt


class FavoritesFragment : Fragment() {

    private  lateinit var stationFavoritesDao: StationFavoritesDao
    private lateinit var linkLineStationDao: LinkLineStationDao
    private lateinit var stations:MutableList<StationsLine>
       // private var linksLineIc: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.line_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_map -> {
                  val target=FavoritesFragmentDirections.actionNavigationFavoritesToMapFragment()
                     Navigation.findNavController(requireView()).navigate(target)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        val recyclerView:RecyclerView=root.findViewById(R.id.favorites_recyclerview)
        recyclerView.layoutManager= LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL,false)
        stationFavoritesDao=dao().getFavoritesStationDao()
        linkLineStationDao=dao().getLinkLineStationDao()

        var linksLineIc: MutableList<String> = arrayListOf()
        runBlocking {
           val favs=stationFavoritesDao.getStationFavorites()
            stations = arrayListOf()

            favs.map{
                val linksline=linkLineStationDao.getAllLinesFromStation(it.slug)
                linksLineIc= arrayListOf()
                linksline.map { it2 ->
                    linksLineIc.add(it2.codeLine)
                }
                val station=StationsLine(it.name,it.slug,it.lineCode,linksLineIc)
                 stations.add(station)
            }
               recyclerView.adapter=FavoritesFragmentAdapter(stations.toList())

            val myCallback = object: ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onChildDraw(c: Canvas,recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,dX: Float,dY: Float,actionState: Int,isCurrentlyActive: Boolean) {
                    val BinIc = ContextCompat.getDrawable(requireContext(),R.drawable.ic_delete_sweep_black_24dp)

                    c.clipRect(0f, viewHolder.itemView.top.toFloat(),
                        dX, viewHolder.itemView.bottom.toFloat())

                   val width=viewHolder.itemView.right+viewHolder.itemView.left

                    if(dX < width/3)
                        c.drawColor(Color.GRAY)
                    else
                        c.drawColor(Color.RED)

                    val textMargin = resources.getDimension(R.dimen.text_margin)
                        .roundToInt()
                    BinIc!!.bounds = Rect(
                        textMargin,
                        viewHolder.itemView.top + textMargin,
                        textMargin + BinIc.intrinsicWidth,
                        viewHolder.itemView.top + BinIc.intrinsicHeight
                                + textMargin
                    )
                    BinIc.draw(c)
                    super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos=viewHolder.adapterPosition
                    val station= stations[pos]
                    stations.removeAt(pos)
                    val scope = CoroutineScope(Dispatchers.Default+ SupervisorJob())

                    recyclerView.adapter=FavoritesFragmentAdapter(stations.toList())
                    recyclerView.adapter?.notifyDataSetChanged()
                    val snackbar = Snackbar.make(requireView(),
                        station.name + " supprimée des favoris",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") {
                        stations.add(pos,station)
                        recyclerView.adapter=FavoritesFragmentAdapter(stations.toList())
                        recyclerView.adapter?.notifyDataSetChanged()
                 }
                    snackbar.show()
                        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            override fun onShown(transientBottomBar: Snackbar?) {
                                super.onShown(transientBottomBar)
                            }
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                if(event==Snackbar.Callback.DISMISS_EVENT_TIMEOUT)
                                scope.launch{
                                    stationFavoritesDao.deleteStationFavoritesWithSlug(station.slug)
                                }
                                super.onDismissed(transientBottomBar, event)
                            }
                        })

                }
            }
            val myHelper=ItemTouchHelper(myCallback)
            myHelper.attachToRecyclerView(recyclerView)

        }


        return root
    }


}
