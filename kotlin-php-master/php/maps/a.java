package com.mortley.android.restaurantsaver;

public class NearbyRestaurantActivity extends ListActivity implements OnClickListener, LocationListener{
    private Button refreshButton, searchRestaurants; 
    ImageButton goToSearch;
    private double[] lastKnownLocation;
    private EditText locationEditText;
    private LocationManager locManager;
    private LocationListener locListener;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearbyrestaurants);
        refreshButton = (Button)findViewById(R.id.reloadButton);
        refreshButton.setOnClickListener(this);

        searchRestaurants = (Button)findViewById(R.id.searchButton);
        searchRestaurants.setOnClickListener(this);
        goToSearch = (ImageButton)findViewById(R.id.goLocationButton);
        goToSearch.setOnClickListener(this);
        locationEditText = (EditText)findViewById(R.id.addressTextBox);
        locationEditText.setVisibility(View.GONE);
        goToSearch.setVisibility(View.GONE);

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//??


        locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 100, this);

        //checks network connectivity
        boolean checkConnection = isNetworkAvailable();
        if(!checkConnection){
            Toast.makeText(getApplicationContext(), "Check your Network Connectivity", Toast.LENGTH_LONG).show();
        }

        if(checkConnection){
            //sets current location parameters for the user
            lastKnownLocation = RestaurantHelper.getLastKnownLocation(this);
            //Log.v("NearbyRestaurantActivity", "This"+this);

            RestaurantApplication application = (RestaurantApplication) this.getApplication();
            RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, R.layout.restaurantrow,  R.id.label,new ArrayList<RestaurantReference>());
            restaurantAdapter.setLastKnownLocation(lastKnownLocation);  


            //set a global variable for the RestaurantAdapter in the RestaurantApplication class.
            application.setRestaurantAdapter(restaurantAdapter);
            //Set the adapter first and then update it when the RestaurantHttpAsyncTask makes a web service call.
            setListAdapter(restaurantAdapter);
            //Make a webservice call in a different thread passing Keyword for URL as a string array.
            RestaurantHttpAsyncTask m_progressTask;
            String[] keywords = {"", "american", "asian", "italian","mexican"};
            //String[] keywords = {"indian"};
            m_progressTask = new RestaurantHttpAsyncTask(NearbyRestaurantActivity.this, keywords);
            m_progressTask.setRestaurantAdapter(restaurantAdapter);
            m_progressTask.execute();
        }
    }

    @Override
    public void onClick(View v) {   
        //Refresh button helps to refresh the restaurant list on location change. Again it makes a call to the webservice using Async Task
        if(v.getId() == refreshButton.getId() ){


            try {
                gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // don't start listeners if no provider is enabled
            if (!gps_enabled && !network_enabled) {
                Toast.makeText(getApplicationContext(), "Sorry, Location is not determined. Please enable your Network Providers.", Toast.LENGTH_LONG).show();


            }

            //check network connectivity before refresh
            boolean checkConnection = isNetworkAvailable();
            if(!checkConnection){
                Toast.makeText(getApplicationContext(), "Check your Network Connectivity", Toast.LENGTH_LONG).show();
            }
            if(checkConnection){

                RestaurantApplication application = (RestaurantApplication) this.getApplication();
                RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, R.layout.restaurantrow,  R.id.label, new ArrayList<RestaurantReference>());
                restaurantAdapter.setLastKnownLocation(lastKnownLocation);  
                //set a global variable for the RestaurantAdapter in the RestaurantApplication class.
                application.setRestaurantAdapter(restaurantAdapter);
                //Set the adapter first and then update it when the RestaurantHttpAsyncTask makes a web service call.
                setListAdapter(restaurantAdapter);
                //Make a webservice call in a different thread passing Keyword for URL as a string array.
                RestaurantHttpAsyncTask m_progressTask, m_progressTask1;
                String[] keywords = {"", "american", "asian", "italian","mexican", "chinese", "indian"};
                //String[] keywords = {"Chinese"};
                m_progressTask = new RestaurantHttpAsyncTask(NearbyRestaurantActivity.this, keywords);
                m_progressTask.setRestaurantAdapter(restaurantAdapter);
                m_progressTask.execute();
            }
        }

        if(v.getId() == goToSearch.getId() ){

            Activity child = this;
            while(child.getParent() != null){
                child = child.getParent();
            }
            TabGroup1Activity parent = (TabGroup1Activity)getParent();


            InputMethodManager imm = (InputMethodManager)getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(locationEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            //changes ** restaurantAdapter to RestaurantAdapter1 to test & application to application1  
            RestaurantApplication application1 = (RestaurantApplication) this.getApplication();
            RestaurantAdapter restaurantAdapter1 = new RestaurantAdapter(this, R.layout.restaurantrow,  R.id.label, new ArrayList<RestaurantReference>());
            restaurantAdapter1.setLastKnownLocation(lastKnownLocation);  
            //set a global variable for the RestaurantAdapter in the RestaurantApplication class.
            application1.setRestaurantAdapter(restaurantAdapter1);
            //Set the adapter first and then update it when the RestaurantHttpAsyncTask makes a web service call.
            setListAdapter(restaurantAdapter1);
            //Make a webservice call in a different thread passing Keyword for URL as a string array.
            RestaurantHttpAsyncTaskTextSearch m_progressTask, m_progressTask1;
            String keywords = locationEditText.getText().toString();
            if(keywords.equals("")){
                keywords = "Pizza in Palo Alto";
            }
            keywords = keywords.replaceAll(" ", "%20");
            keywords = keywords.replaceAll(",", "%20");
            m_progressTask = new RestaurantHttpAsyncTaskTextSearch (NearbyRestaurantActivity.this, keywords);
            m_progressTask.setRestaurantAdapter(restaurantAdapter1);
            m_progressTask.execute();

            locationEditText.setVisibility(View.GONE);
            goToSearch.setVisibility(View.GONE);
        }
        if(v.getId() == searchRestaurants.getId() ){
            if(goToSearch.isShown() == true){
                goToSearch.setVisibility(View.GONE);
                locationEditText.setVisibility(View.GONE);
            }
            else if(goToSearch.isShown() == false){
                //check network connectivity before refresh
                boolean checkConnection = isNetworkAvailable();
                if(!checkConnection){
                    Toast.makeText(getApplicationContext(), "Check your Network Connectivity", Toast.LENGTH_LONG).show();
                }
                if(checkConnection){
                    goToSearch.setVisibility(View.VISIBLE);
                    locationEditText.setVisibility(View.VISIBLE);

                }
            }
        }

    }
    //Method to check network connectivity
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
        = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            //Log.d("network", "Network available:true");
            return true;
        } else {
            //Log.d("network", "Network available:false");
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 100, this); 
        //Log.v("NearbyRestaurantActivity", "In OnResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locManager.removeUpdates(this); 
        //Log.v("NearbyRestaurantActivity", "In onPause()");

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}






public class RestaurantHelper {

public static double[] getLastKnownLocation(Activity activity){
    double lat = 0.0;
    double lon = 0.0;
    LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);    
    Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  

    if(location == null){
        lat = 0.0;
        lon = 0.0;
    }
    else{
        //Log.v("Latitude", Double.toString(location.getLatitude()));
        //Log.v("Longitude", Double.toString(location.getLongitude()));

        lat = location.getLatitude();
        lon = location.getLongitude();
    }
    return new double[]{lat,lon};
}
}