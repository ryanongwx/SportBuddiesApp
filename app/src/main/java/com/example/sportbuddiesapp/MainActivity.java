package com.example.sportbuddiesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

// Required for the animator
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.TypeEvaluator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;



import com.google.gson.JsonElement;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.geojson.Point;
import com.ramotion.circlemenu.CircleMenuView;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;



public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {

    private MapView mapView;
    private CardView cardLayout;
    private RecyclerView recyclerView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";
    private static final String CALLOUT_IMAGE_ID = "CALLOUT_IMAGE_ID";
    private static final String MARKER_LAYER_ID = "MARKER_LAYER_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";
    private GeoJsonSource source;
    private LatLng point1;
    private Feature featureAtMapClickPoint;
    private AnimatorSet animatorSet;
    private static final long CAMERA_ANIMATION_TIME = 1950;
    private FirebaseAuth firebaseAuth;
    private Button bookingsBtn;
    private TextView ratingCount;
    private Integer count = 0;
    private Integer starrating = 0;
    private RatingBar ratings;
    private Integer avgrating;
    Button reviewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        // Create button for the popup cardview in which pressing the button will call the function to bring to the next activity
        Button sportLocationBtn = (Button) findViewById(R.id.sportlocationbtn);
        sportLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = cardLayout.findViewById(R.id.cardtopleft);
                String titleText = title.getText().toString();
                openSportLocationActivity(titleText);

            }
        });

        // Create button and set the listener for the click of the button to go to the reviews page
        // This reviews page only allows one review per person per location
        reviewBtn = findViewById(R.id.reviewBtn);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = cardLayout.findViewById(R.id.cardtopleft);
                String titleText = title.getText().toString();
                openReviewActivity(titleText);
            }
        });


        // Set the reference of the ratings indicator for the displaying of the review of each location
        ratingCount = findViewById(R.id.ratingcount);


        mapView = (MapView) findViewById(R.id.mapView);
        cardLayout = findViewById(R.id.single_location_cardview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        // Obtain the instance of Firebaseauth
        firebaseAuth = FirebaseAuth.getInstance();


        // Creating the circle menu
        final CircleMenuView circlemenu = findViewById(R.id.circle_menu);
        circlemenu.setEventListener(new CircleMenuView.EventListener(){
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationEnd(view, buttonIndex);
                if(buttonIndex == 0) {
                    //TODO Open Chat activity
                    openChatActivity();
                }
                else if(buttonIndex == 1) {
                    openBookingsActivity();
                }
                else if(buttonIndex == 2) {
                    //TODO Open Profile activity
                }
            }
        });
    }

    // This will take care of creating the menu bar within the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // This handles the onclick events of the items in the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            // Note: If switch case is used, have to break at the end of every case
            case R.id.logoutMenu:
                firebaseAuth.signOut();
                finish();
                openLoginActivity();
                break;
            case R.id.home:
                openMainActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    // Creating the functions to go to all the other activities in my app
    private void openLoginActivity() {
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);

    }

    private void openBookingsActivity() {
        Intent intent = new Intent(this, Bookings_Activity.class);
        startActivity(intent);

    }

    private void openChatActivity() {
        Intent intent = new Intent(this, Buddies_Network_Activity.class);
        startActivity(intent);

    }

    private void openReviewActivity(String s) {
        Intent intent = new Intent(this, Review_Activity.class);
        intent.putExtra("location", s);
        startActivity(intent);

    }

    private void openSportLocationActivity(String s) {
        Intent intent = new Intent(this, Sport_Location_Activity.class);
        intent.putExtra("title", s);
        startActivity(intent);

    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        this.mapboxMap = mapboxMap;

        // Set my built map to the mapbox map style using the url
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ryanongwx/ckvz8ryhd7los15qsroznkuq9"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        setUpData();
                        enableLocationComponent(style);
                        mapboxMap.addOnMapClickListener(MainActivity.this);

                    }
                });



    }


    /**
     * Sets up all of the sources and layers needed for this example
     */

    // Setup the style for the marking of the clicked location

    public void setUpData() {
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                setupSource(style);
                setUpClickLocationIconImage(style);
                setUpClickLocationMarkerLayer(style);
                setUpInfoWindowLayer(style);
            });
        }
    }

    /**
     * Adds the GeoJSON source to the map
     */

    // I am clueless as to what is to be passed into the source id and where they get the id from
    // I think is just the naming convention of giving the source an id

    // Sets up the GeoJSON Object

    private void setupSource(@NonNull Style loadedStyle) {
        source = new GeoJsonSource(GEOJSON_SOURCE_ID);
        loadedStyle.addSource(source);
    }

    /**
     * Adds the marker image to the map for use as a SymbolLayer icon
     */

    // This determines what icon is shown when the user clicks on the map

    private void setUpClickLocationIconImage(@NonNull Style loadedStyle) {
        loadedStyle.addImage(MARKER_IMAGE_ID, BitmapFactory.decodeResource(
                this.getResources(), R.drawable.red_marker));
                // I must create the drawable of the red marker for the marking of the spot i clicked
    }

    /**
     * Needed to show the Feature properties info window.
     * This sets the GeoJSON of the source ton the point in which the user clicks on
     */

    // Process the information at the location in which was clicked

    private void refreshSource(Feature featureAtClickPoint) {
        if (source != null) {
            source.setGeoJson(featureAtClickPoint);
            // featureAtClickPoint is a Feature type in which to set
        }
    }

    /**
     * Adds a SymbolLayer to the map to show the click location marker icon.
     */

    // Adding of another layer on the map specially for the popup marker to be placed

    private void setUpClickLocationMarkerLayer(@NonNull Style loadedStyle) {
        loadedStyle.addLayer(new SymbolLayer(MARKER_LAYER_ID, GEOJSON_SOURCE_ID)
                .withProperties(
                        iconImage(MARKER_IMAGE_ID),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconOffset(new Float[] {0f, -8f})
                ));
    }

    /**
     * Adds a SymbolLayer to the map to show the Feature properties info window.
     */

    // Adding of another layer on the map specially for the popup info window to be seen

    private void setUpInfoWindowLayer(@NonNull Style loadedStyle) {
        loadedStyle.addLayer(new SymbolLayer(CALLOUT_LAYER_ID, GEOJSON_SOURCE_ID)
                .withProperties(
                        // show image with id title based on the value of the name feature property
                        iconImage(CALLOUT_IMAGE_ID),

                        // set anchor of icon to bottom-left
                        iconAnchor(ICON_ANCHOR_BOTTOM),

                        // prevent the feature property window icon from being visible even
                        // if it collides with other previously drawn symbols
                        iconAllowOverlap(false),

                        // prevent other symbols from being visible even if they collide with the feature property window icon
                        iconIgnorePlacement(false),

                        // offset the info window to be above the marker
                        iconOffset(new Float[] {-2f, -28f})
                ));
    }

    /**
     * This method handles click events for SymbolLayer symbols.
     *
     * @param screenPoint the point on screen clicked
     */
    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        // queryRenderedFeatures(screenPoint) Queries the map for rendered features and an array of rendered features
        if (!features.isEmpty()) {
            // Get the first feature within the list if one exist
            Feature feature = features.get(0);
            StringBuilder stringBuilder = new StringBuilder();
            // This StringBuilder is a mutable variable which i can set to a string
            // In this case, I set it to true when there is a property which matches a unique property only found in my sport locations

            StringBuilder show = new StringBuilder();
            StringBuilder locationType = new StringBuilder();
            StringBuilder openingHours = new StringBuilder();
            StringBuilder name = new StringBuilder();
            StringBuilder rating = new StringBuilder();

            // Ensure the feature has properties defined
            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    stringBuilder.append(String.format("%s : %s", entry.getKey(), entry.getValue()));
                    stringBuilder.append(System.getProperty("line.separator"));
                    // The condition below adds True to the StringBuilder if the Feature has the property "Name"
                    // The "Name" property key can only be found in the locations marked by my custom dataset sport locations
                    if (String.format("%s", entry.getKey()).equals("Name")) {
                        show.append("True");
                        name.append(entry.getValue().toString().replace("\"",""));
                    }

                    if (String.format("%s", entry.getKey()).equals("Sport")) {
                        locationType.append(entry.getValue().toString().replace("\"",""));
                    }
                    if (String.format("%s", entry.getKey()).equals("Opening Hours")) {
                        openingHours.append(entry.getValue().toString().replace("\"",""));
                    }

                    // Below is defining the cardview at the top
                    TextView topLeft = cardLayout.findViewById(R.id.cardtopleft);
                    topLeft.setText(name.toString().replace("\"",""));


                    TextView topRight = cardLayout.findViewById(R.id.cardtopright);
                    topRight.setText(locationType.toString());

                    ratings = cardLayout.findViewById(R.id.cardratings);

                    // Checking the database when a point on th emap is clicked in order to find the average ratings collected from the database at the certain point
                    Query databaseReference = FirebaseDatabase.getInstance("https://sportbuddiesapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Review");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                starrating = 0;
                                count = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    // Filter the booking list by all the bookings made by the user in this user session
                                    // So it displays only the booking made by this user and not anu other user
                                    if(snapshot.child("location").getValue().toString().equals(name.toString()))
                                    {
                                        starrating += Integer.parseInt(snapshot.child("rating").getValue().toString());
                                        count += 1;
                                    }
                                }
                                if(count!=0)
                                {
                                    avgrating = starrating/count;
                                }else{
                                    avgrating = 0;
                                }
                                ratingCount.setText(count + " Reviews");
                                ratings.setRating(avgrating);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                // The condition below compares the StringBuilder to a string "True"
                // If it matches, it generates the popup icon and bubblelayout on that location and also set the cardview at the top to be visible
                // Also the animate to camera selection pans the camera to make the pointer jump to the centre of the screen
                if (show.toString().equals("True")){

                    /* TODO Access the database to check the average rating for each sport location */
                    /* TODO Access the database to check the total number of ratings for each sport location */


                    cardLayout.setVisibility(View.VISIBLE);
                    new GenerateViewIconTask(MainActivity.this).execute(FeatureCollection.fromFeature(feature));
                    animateCameraToSelection(featureAtMapClickPoint);
                }
                else {
                    Toast.makeText(this, getString(R.string.query_feature_no_properties_found), Toast.LENGTH_SHORT).show();
                    deselectAll();
                    // Create a message to print a string when the feature is not a location in the sportlocations dataset
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.query_feature_no_properties_found), Toast.LENGTH_SHORT).show();
            deselectAll();
            // Create a message to print a string when the feature is not a location in the sportlocations dataset
        }
        return true;

    }

    // Deselect is to remove the top cardview view when another location is clicked
    private void deselectAll() {
        cardLayout.setVisibility(View.GONE);
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        // mapboxMap.getProjection().toScreenLocation(point) converts the point in LatLng on the map to a screenpoint which is the position on the phone screen
        point1 = point;
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    /**
     * Invoked when the bitmap has been generated from a view.
     */
    public void setImageGenResults(HashMap<String, Bitmap> imageMap) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                // Adds images to be used in the map's style.
                // The bitmap of the view of the pop up will be added to the map
                style.addImages(imageMap);
            });
        }
    }

    /**
     * AsyncTask to generate Bitmap from Views to be used as iconImage in a SymbolLayer.
     * <p>
     * Call be optionally be called to update the underlying data source after execution.
     * </p>
     * <p>
     * Generating Views on background thread since we are not going to be adding them to the view hierarchy.
     * </p>
     */

    // An asynchronous task is defined by a computation that runs on a background thread and whose result is published on the UI thread.
    private class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {
        // A weak reference is used such that when weakReference.get() is called
        // it will return the type QueryFeatureActivity rather than the object itself
        private final WeakReference<MainActivity> activityRef;

        private Button sportLocationBtn;
        //This defines a GeoJson Feature object which represents a spatially bound thing. Every Feature object is a GeoJson object
        // no matter where it occurs in a GeoJson text. A Feature object will always have a "TYPE" member with the value "Feature".
        // https://docs.mapbox.com/android/java/api/libjava-geojson/5.8.0/com/mapbox/geojson/Feature.html

        GenerateViewIconTask(MainActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @SuppressWarnings("WrongThread")
        @Override
        // Output is a Hashmap type with String and Bitmap values
        // Override this method to perform a computation on a background thread. The specified parameters are the
        // parameters passed to execute(Params...) by the caller of this task. This will normally run on a background thread.

//        doInBackground, invoked on the background thread immediately after onPreExecute() finishes executing. This step is used to perform
//        background computation that can take a long time. The parameters of the asynchronous task are passed to this step. The result of the
//        computation must be returned by this step and will be passed back to the last step. This step can also use publishProgress to publish
//        one or more units of progress. These values are published on the UI thread, in the onProgressUpdate step.
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            MainActivity activity = activityRef.get();
            // A HashMap however, store items in "key/value" pairs, and you can access them by an index of another type (e.g. a String).
            HashMap<String, Bitmap> imagesMap = new HashMap<>();
            if (activity != null) {
                // https://developer.android.com/reference/android/view/LayoutInflater#summary
                LayoutInflater inflater = LayoutInflater.from(activity);

                if (params[0].features() != null) {
                    // Getting the features a the point clicked by user on the map
                    featureAtMapClickPoint = params[0].features().get(0);
                    // WE CAN POSSIBLY USE THE addProperty(key, value) TO ADD PROPERTIES WE WANT TO BE DISPLAYED
                    // https://docs.mapbox.com/android/java/api/libjava-geojson/5.8.0/com/mapbox/geojson/Feature.html#properties--

                    // StringBuilder is a class for a mutable sequence of characters.
                    StringBuilder stringBuilder = new StringBuilder();
                    StringBuilder titleBuilder = new StringBuilder();

                    StringBuilder address = new StringBuilder();
                    StringBuilder openinghrs = new StringBuilder();
                    StringBuilder players = new StringBuilder();

                    BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(
                            R.layout.activity_query_feature_window_symbol_layer, null);



                    if (featureAtMapClickPoint.properties() != null) {
                        // Feature.properties() returns a JSONObject which holds this feature's current properties
                        // Map.Entry is an interface. https://docs.oracle.com/javase/8/docs/api/java/util/Map.Entry.html
                        // The entrySet() method of Properties class is used to get the Set view of this Properties mappings.
                        // It returns a Set view of the mappings contained in this map.
                        for (Map.Entry<String, JsonElement> entry : featureAtMapClickPoint.properties().entrySet()) {

                            // entry.getKey() will get K in Map<K,V> which is the property of the location clicked such as sizerank
                            // entry.getValue() will get V in Map<K,V> which is the property of the location clicked such as 13
                            // So this will be displayed as Sizerank - 13 in the popup window

                            // In this example, I used title Builder to name the title of the location to be the type of location

                            if (String.format("%s", entry.getKey()).equals("Name")) {
                                titleBuilder.append(String.format("%s", entry.getValue()));
                                titleBuilder.deleteCharAt(0);
                                titleBuilder.deleteCharAt(titleBuilder.length()-1);
                            }

                            if (String.format("%s", entry.getKey()).equals("Address")) {
                                String property = entry.getKey().toString().replace("\"", "");
                                String value = entry.getValue().toString().replace("\"", "");
                                address.append(String.format("%s - %s", property, value));
                                address.append(System.getProperty("line.separator"));
                            }

                            if (String.format("%s", entry.getKey()).equals("Opening Hours")) {
                                String property = entry.getKey().toString().replace("\"", "");
                                String value = entry.getValue().toString().replace("\"", "").replace("[", "").replace("]", "").replace(",", " to ");
                                openinghrs.append(String.format("%s - %s", property, value));
                                openinghrs.append(System.getProperty("line.separator"));
                            }

                            if (String.format("%s", entry.getKey()).equals("Players")) {
                                String property = entry.getKey().toString().replace("\"", "");
                                String value = entry.getValue().toString().replace("\"", "").replace("[", "").replace("]", "").replace(",", " to ");
                                players.append(String.format("%s - %s", property, value));
                                players.append(System.getProperty("line.separator"));
                            }
                            // The above 2 lines defined how the pop up showing the properties at one point would look like
                        }

                        stringBuilder.append(address);
                        stringBuilder.append(openinghrs);
                        stringBuilder.append(players);
                        // This sets the properties and links to the textview id in the layout
                        TextView propertiesListTextView = bubbleLayout.findViewById(R.id.info_window_feature_properties_list);
                        propertiesListTextView.setText(stringBuilder.toString());

                        // This sets the title and links to the textview id in the layout
                        TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
                        titleTextView.setText(titleBuilder.toString());

                        // set the clicking listener of the review button to be after the location variable is retrieved such taht it can be passed as an intent
                        reviewBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, Review_Activity.class);
                                intent.putExtra("location", titleBuilder.toString());
                                startActivity(intent);
                            }
                        });

                        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        bubbleLayout.measure(measureSpec, measureSpec);

                        float measuredWidth = bubbleLayout.getMeasuredWidth();

                        bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);


                        Bitmap bitmap = MainActivity.SymbolGenerator.generate(bubbleLayout);
                        imagesMap.put(CALLOUT_IMAGE_ID, bitmap);
                    }
                }
            }

            return imagesMap;
        }

        @Override
        // onPostExecute, invoked on the UI thread after the background computation finishes.
        // The result of the background computation is passed to this step as a parameter.
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            MainActivity activity = activityRef.get();
            if (activity != null && bitmapHashMap != null) {
                // Invoked when the bitmap has been generated from a view.
                activity.setImageGenResults(bitmapHashMap);
                activity.refreshSource(featureAtMapClickPoint);
                // Get the camera to pan to the location in which was clicked by the user

            }
        }

    }

    /**
     * Helper class to evaluate LatLng objects with a ValueAnimator
     */
    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }

    // From here until animate camera selection is the animator for the camera to pan to the location clicked

    private LatLng convertToLatLng(Feature feature) {
        Point symbolPoint = (Point) feature.geometry();
        return new LatLng(symbolPoint.latitude(), symbolPoint.longitude());
    }



    private Animator createLatLngAnimator(LatLng currentPosition, LatLng targetPosition) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), currentPosition, targetPosition);
        latLngAnimator.setDuration(CAMERA_ANIMATION_TIME);
        latLngAnimator.setInterpolator(new FastOutSlowInInterpolator());
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLng((LatLng) animation.getAnimatedValue()));
            }
        });
        return latLngAnimator;
    }

    // I edited the animation function to be removed of zoom, bearing and tilt due to the fact that my map point does not have all those properties
    /**
     * Animate camera to a feature.
     *
     * @param feature the feature to animate to
     */
    private void animateCameraToSelection(Feature feature) {
        CameraPosition cameraPosition = mapboxMap.getCameraPosition();

        if (animatorSet != null) {
            animatorSet.cancel();
        }

        animatorSet = new AnimatorSet();
        point1.setLatitude(point1.getLatitude()+0.004);
//        point1.setLongitude(point1.getLongitude()-0.0009);
        animatorSet.playTogether(
                createLatLngAnimator(cameraPosition.target, point1)
        );
        animatorSet.start();
    }


    /**
     * Utility class to generate Bitmaps for Symbol.
     */
    private static class SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        static Bitmap generate(@NonNull View view) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            // Here it is making the bubblelayout view into a bitmap so the the view can be displayed
            // within the main view
            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }



    // This enableLocationComponent() is for the generating of the pulsating user location on the device location

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Get an instance of the component
            LocationComponentOptions locationComponentOptions =
                    LocationComponentOptions.builder(this)
                            .pulseEnabled(true)
                            .pulseColor(Color.BLUE)
                            .pulseAlpha(.4f)
                            .pulseInterpolator(new BounceInterpolator())
                            .build();

            LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(this, loadedMapStyle)
                    .locationComponentOptions(locationComponentOptions)
                    .build();

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }


//    @Override
//    @SuppressWarnings( {"MissingPermission"})
//    protected void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }



}