package danbroid.vtmdemo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_map.*
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.oscim.android.MapPreferences
import org.oscim.android.MapView
import org.oscim.core.MapPosition
import org.oscim.layers.tile.vector.VectorTileLayer
import org.oscim.map.Map
import org.oscim.tiling.TileSource
import org.oscim.tiling.source.OkHttpEngine
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource
import java.io.File

abstract class BaseMapFragment : Fragment() {
  protected lateinit var baseLayer: VectorTileLayer
  protected lateinit var tileSource: TileSource

  protected val mapView: MapView by lazy {
    map_view
  }

  protected val map: Map
    inline get() = mapView.map()

  protected lateinit var prefs: MapPreferences


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
                              log.debug("CACHE RESPONSE")
                            else log.warn("NETWORK RESPONSE")
                          }
                    })
            )
        )
        .build()

    baseLayer = map.setBaseMap(tileSource)

    /* set initial position on first run */
    val pos = MapPosition()
    map.getMapPosition(pos)
    if (pos.x == 0.5 && pos.y == 0.5)
      map.setMapPosition(53.08, 8.83, Math.pow(2.0, 16.0))
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

