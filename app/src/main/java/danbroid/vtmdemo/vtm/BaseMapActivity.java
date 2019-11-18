/*
 * Copyright 2013 Hannes Janetzek
 * Copyright 2016-2018 devemux86
 *
 * This file is part of the OpenScienceMap project (http://www.opensciencemap.org).
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package danbroid.vtmdemo.vtm;

import android.os.Bundle;

import org.oscim.android.cache.TileCache;
import org.oscim.core.MapPosition;
import org.oscim.layers.TileGridLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseMapActivity extends MapActivity {
    static final Logger log = LoggerFactory.getLogger(BaseMapActivity.class);

    static final boolean USE_CACHE = false;

    VectorTileLayer mBaseLayer;
    TileSource mTileSource;
    TileGridLayer mGridLayer;

    private TileCache mCache;

    public BaseMapActivity(int contentView) {
        super(contentView);
    }

    public BaseMapActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTileSource = OSciMap4TileSource.builder()
                .httpFactory(new OkHttpEngine.OkHttpFactory())
                .build();

        if (USE_CACHE) {
            mCache = new TileCache(this, null, "tile.db");
            mCache.setCacheSize(512 * (1 << 10));
            mTileSource.setCache(mCache);
        }
        mBaseLayer = mMap.setBaseMap(mTileSource);

        /* set initial position on first run */
        MapPosition pos = new MapPosition();
        mMap.getMapPosition(pos);
        if (pos.x == 0.5 && pos.y == 0.5)
            mMap.setMapPosition(53.08, 8.83, Math.pow(2, 16));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCache != null)
            mCache.dispose();
    }


}
