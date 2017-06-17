package app.wt.noolis.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import app.wt.noolis.ActivityNoteEdit
import app.wt.noolis.R
import app.wt.noolis.adapter.ListAdapterNote
import app.wt.noolis.data.DatabaseManager
import app.wt.noolis.model.Note

class FragmentFavorites : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ListAdapterNote? = null
    private var view1: View? = null
    private var searchView: SearchView? = null
    private var lyt_not_found: LinearLayout? = null
    private var db: DatabaseManager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 = inflater!!.inflate(R.layout.fragment_favorites, null)

        //connect db
        db = DatabaseManager(activity)

        // activate fragment menu
        setHasOptionsMenu(true)

        recyclerView = view1!!.findViewById(R.id.recyclerView) as RecyclerView
        lyt_not_found = view1!!.findViewById(R.id.lyt_not_found) as LinearLayout

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        // specify an adapter (see also next example)
        displayData(db!!.allFavNote)
        return view1
    }

    override fun onResume() {
        displayData(db!!.allFavNote)
        super.onResume()
    }

    private fun displayData(items: List<Note>) {
        mAdapter = ListAdapterNote(activity, items)
        recyclerView!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener { view, model ->
            val intent = Intent(activity, ActivityNoteEdit::class.java)
            intent.putExtra(ActivityNoteEdit.EXTRA_OBJCT, model.toString())
            activity.startActivity(intent)
        }
        if (mAdapter!!.itemCount == 0) {
            lyt_not_found!!.visibility = View.VISIBLE
        } else {
            lyt_not_found!!.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_fragment_note, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView!!.isIconified = false
        searchView!!.queryHint = "Search note..."
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                try {
                    mAdapter!!.filter.filter(s)
                } catch (e: Exception) {
                }

                return true
            }
        })
        // Detect SearchView icon clicks
        searchView!!.setOnSearchClickListener { setItemsVisibility(menu, searchItem, false) }

        // Detect SearchView close
        searchView!!.setOnCloseListener {
            setItemsVisibility(menu, searchItem, true)
            false
        }
        searchView!!.onActionViewCollapsed()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean) {
        for (i in 0..menu.size() - 1) {
            val item = menu.getItem(i)
            if (item !== exception) item.isVisible = visible
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
