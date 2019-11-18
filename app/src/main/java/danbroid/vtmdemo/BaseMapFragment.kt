package danbroid.vtmdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_map.*
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.oscim.android.MapPreferences
import org.oscim.android.MapView
import org.oscim.core.MapPosition
import org.oscim.layers.TileGridLayer
import org.oscim.layers.tile.vector.VectorTileLayer
import org.oscim.map.Map
import org.oscim.theme.VtmThemes
import org.oscim.tiling.TileSource
import org.oscim.tiling.source.OkHttpEngine
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource
import java.io.File

/**
 * Base class for MapFragments
 */

abstract class BaseMapFragment : Fragment() {
  protected lateinit var baseLayer: VectorTileLayer
  protected lateinit var tileSource: TileSource

  protected val mapView: MapView by lazy {
    map_view
  }

  protected val map: Map
    inline get() = mapView.map()

  protected lateinit var prefs: MapPreferences

  protected var tileGridLayer: TileGridLayer? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setHasOptionsMenu(true)

    prefs = MapPreferences(this.javaClass.name, context)

    tileSource = OSciMap4TileSource.builder()
      .httpFactory(
        OkHttpEngine.OkHttpFactory(
          OkHttpClient.Builder()
            .cache(Cache(File(context!!.cacheDir, "mapcache"), 1024 * 1024 * 10))
            .addInterceptor(object : Interceptor {
              override fun intercept(chain: Interceptor.Chain) =
                chain.proceed(chain.request()).also {
                  if (it.cacheResponse() != null)
                    log.trace("CACHE RESPONSE")
                  else log.trace("NETWORK RESPONSE")
                }
            })
        )
      )
      .build()

    baseLayer = map.setBaseMap(tileSource)

    createLayers()
    /* set initial position on first run */
    val pos = MapPosition()
    map.getMapPosition(pos)
    if (pos.x == 0.5 && pos.y == 0.5)
      map.setMapPosition(-41.29, 174.78, Math.pow(2.0, 16.0))
  }

  protected abstract fun createLayers()

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.theme_menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      R.id.theme_default -> {
        map.setTheme(VtmThemes.DEFAULT)
        item.isChecked = true
        true
      }
      R.id.theme_newtron -> {
        map.setTheme(VtmThemes.NEWTRON)
        item.isChecked = true
        true
      }
      R.id.theme_osmagray -> {
        map.setTheme(VtmThemes.OSMAGRAY)
        item.isChecked = true
        true
      }
      R.id.theme_osmarender -> {
        map.setTheme(VtmThemes.OSMARENDER)
        item.isChecked = true
        true
      }
      R.id.theme_tubes -> {
        map.setTheme(VtmThemes.TRONRENDER)
        item.isChecked = true
        true
      }
      R.id.gridlayer -> {
        if (item.isChecked) {
          tileGridLayer?.also { map.layers().remove(it) }
        } else {
          tileGridLayer ?: TileGridLayer(map).also {
            tileGridLayer = it
            map.layers().add(it)
          }
        }
        item.isChecked = !item.isChecked
        map.updateMap(true)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  override fun onResume() {
    super.onResume()
    prefs.load(mapView.map())
    mapView.onResume()
  }

  override fun onPause() {
    prefs.save(mapView.map())
    mapView.onPause()
    super.onPause()
  }

  override fun onDestroy() {
    mapView.onDestroy()
    super.onDestroy()
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(BaseMapFragment::class.java)

