package org.where2pair.presentation;

import java.util.List;

import org.where2pair.Coordinates;

public class MapViewportState {

	public final MapZoomType zoom;
	public final List<Coordinates> coordinateBounds;

	public MapViewportState(MapZoomType zoom, List<Coordinates> coordinateBounds) {
		this.zoom = zoom;
		this.coordinateBounds = coordinateBounds;
	}
	
}
