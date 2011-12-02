package dynastymasra.GIS;

import com.nutiteq.BasicMapComponent;
import com.nutiteq.android.MapView;
import com.nutiteq.components.PlaceIcon;
import com.nutiteq.components.WgsPoint;
import com.nutiteq.controls.AndroidKeysHandler;
import com.nutiteq.maps.CloudMade;
import com.nutiteq.ui.ThreadDrivenPanning;
import com.nutiteq.utils.Utils;
import com.nutiteq.location.LocationMarker;
import com.nutiteq.location.LocationSource;
import com.nutiteq.location.NutiteqLocationMarker;
import com.nutiteq.location.providers.AndroidGPSProvider;
import com.nutiteq.kml.KmlUrlReader;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

public class dynastymasraGIS extends Activity {
    /** Called when the activity is first created. */
		private MapView mapView;
		private BasicMapComponent mapComponent;
		private ZoomControls zoomControls;
		private boolean onRetainCalled;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        	onRetainCalled = false;
        
        mapComponent = new BasicMapComponent("tutorial", "Nutiteq", "Android Mapper", 1, 1, new WgsPoint(110.367, -7.79058),10);
        mapComponent.setMap(new CloudMade("B37E70903EB540C1A07B3437AFA5DF2E", 64, 1));
        mapComponent.setPanningStrategy(new ThreadDrivenPanning());
        mapComponent.setControlKeysHandler(new AndroidKeysHandler());
        mapComponent.startMapping();
        mapComponent.addKmlService(new KmlUrlReader("http://www.panoramio.com/panoramio.kml?LANG=en_US.utf8&", true));
        mapView = new MapView(this, mapComponent);
        
        //Zoom Control
        zoomControls = new ZoomControls(this);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() { 
			public void onClick(final View v) {
				mapComponent.zoomIn();
			}
		});
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				mapComponent.zoomOut();
			}
		});
        
        //Map View
        final RelativeLayout relativeLayout = new RelativeLayout(this);
        setContentView(relativeLayout);
        final RelativeLayout.LayoutParams mapViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT); 
        relativeLayout.addView(mapView, mapViewLayoutParams);
        
        //zoom Control Screen
        final RelativeLayout.LayoutParams zoomControlsLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        zoomControlsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        zoomControlsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeLayout.addView(zoomControls, zoomControlsLayoutParams);
        
        //GPS Location
        final LocationSource locationSource = new AndroidGPSProvider((LocationManager) getSystemService(Context.LOCATION_SERVICE), 1000L); 
        final LocationMarker marker = new NutiteqLocationMarker(new PlaceIcon(Utils.createImage("/res/drawable/gps_marker.png"), 5, 17), 3000, true);
        locationSource.setLocationMarker(marker);
        mapComponent.setLocationSource(locationSource);
    }
    	@Override
    	public Object onRetainNonConfigurationInstance(){
    		onRetainCalled = true;
    		return mapComponent;
    	}
    	@Override
    	protected void onDestroy(){
    		super.onDestroy();
    		if(mapView != null){
    			mapView.clean();
    			mapView = null;
    		}
    		if(!onRetainCalled){
    			mapComponent.stopMapping();
    			mapComponent = null;
    		}
    	}
}