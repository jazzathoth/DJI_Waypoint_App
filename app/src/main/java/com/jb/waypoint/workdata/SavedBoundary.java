package com.jb.waypoint.workdata;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class SavedBoundary {
    private static SavedBoundary savedBoundary = null;

    PolygonOptions boundary = new PolygonOptions();

    private SavedBoundary() {};

    public static SavedBoundary getInstance() {
        if (savedBoundary == null) {
            savedBoundary = new SavedBoundary();
        }
        return savedBoundary;
    }

    public PolygonOptions getBoundary() {
        return boundary;
    }

    public void setBoundary(ArrayList<LatLng> boundaryCoordinates) {
        this.boundary = boundary;
        int length = boundaryCoordinates.size();
        if(boundaryCoordinates.size() == 0 || boundaryCoordinates == null) {
            return;
        }
        for (int i = 0; i < length; i++){
            boundary.add(boundaryCoordinates.get(i));
        }
    }
}
