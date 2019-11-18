package danbroid.vtmdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_map.*
import org.oscim.android.MapView
import org.oscim.backend.CanvasAdapter
import org.oscim.layers.GroupLayer
import org.oscim.layers.tile.buildings.BuildingLayer
import org.oscim.layers.tile.vector.labeling.LabelLayer
import org.oscim.renderer.GLViewport
import org.oscim.scalebar.*
import org.oscim.theme.VtmThemes

class MapFragment : BaseMapFragment() {

  private lateinit var mapScaleBar: DefaultMapScaleBar
  private lateinit var mBuildingLayer: BuildingLayer
  private var mShadow = false

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ) = inflater.inflate(R.layout.fragment_map, container, false)


  override fun createLayers() {
    val groupLayer = GroupLayer(map)
    mBuildingLayer = BuildingLayer(map, baseLayer, false, mShadow)
    groupLayer.layers.add(mBuildingLayer)
    groupLayer.layers.add(LabelLayer(map, baseLayer))
    map.layers().add(groupLayer)

    mapScaleBar = DefaultMapScaleBar(map)
    mapScaleBar.setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH)
    mapScaleBar.setDistanceUnitAdapter(MetricUnitAdapter.INSTANCE)
    mapScaleBar.setSecondaryDistanceUnitAdapter(ImperialUnitAdapter.INSTANCE)
    mapScaleBar.setScaleBarPosition(MapScaleBar.ScaleBarPosition.BOTTOM_LEFT)

    val mapScaleBarLayer = MapScaleBarLayer(map, mapScaleBar)
    val renderer = mapScaleBarLayer.renderer
    renderer.setPosition(GLViewport.Position.BOTTOM_LEFT)
    renderer.setOffset(5 * CanvasAdapter.getScale(), 0f)
    map.layers().add(mapScaleBarLayer)

    map.setTheme(VtmThemes.DEFAULT)
  }


  override fun onDestroy() {

    mapScaleBar.destroy()

    super.onDestroy()
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(MapFragment::class.java)

