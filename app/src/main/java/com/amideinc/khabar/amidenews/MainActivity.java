package com.amideinc.khabar.amidenews;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Article> articleList;
    private static String url;
    String mytitle;
    String country ;
    String my_co_code;
    private String TAG = MainActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    private ProgressDialog pDialog;
    Map<String, String> country_code = new HashMap();

    ArrayList<HashMap<String, String>> newsarticleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        country_code.put("United Arab", "ae");
        country_code.put("Argentina", "ar");
        country_code.put("Austria", "at");
        country_code.put("Australia", "au");
        country_code.put("Belgium", "be");
        country_code.put("Bulgaria", "bg");
        country_code.put("Brazil", "br");
        country_code.put("Canada", "ca");
        country_code.put("Switzerland", "ch");
        country_code.put("China", "cn");
        country_code.put("Colombia", "co");
        country_code.put("Cuba", "cu");
        country_code.put("Germany", "de");
        country_code.put("Egypt", "eg");
        country_code.put("France", "fr");
        country_code.put("United Kingdom", "gb");
        country_code.put("Greece", "gr");
        country_code.put("Hungary", "hu");
        country_code.put("Indonesia", "id");
        country_code.put("Israel", "il");
        country_code.put("India", "in");
        country_code.put("Italy", "it");
        country_code.put("Japan", "jp");
        country_code.put("South Korea", "kr");
        country_code.put("Lithuania", "lt");
        country_code.put("Latvia", "lv");
        country_code.put("Morocco", "ma");
        country_code.put("Mexico", "mx");
        country_code.put("Malaysia", "my");
        country_code.put("Nigeria", "ng");
        country_code.put("Netherlands", "nl");
        country_code.put("Norway", "no");
        country_code.put("New Zealand", "nz");
        country_code.put("Philippines", "ph");
        country_code.put("Poland","pl");
        country_code.put("Portugal", "pt");
        country_code.put("Romania","ro");
        country_code.put("Serbia","rs");
        country_code.put("Russia", "ru");
        country_code.put("South Africa", "sa");
        country_code.put("Sweden", "se");
        country_code.put("Singapore", "sg");
        country_code.put("Slovenia", "si");
        country_code.put("USA", "us");



        newsarticleList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        country = sharedPreferences.getString("country","India");
        my_co_code = country_code.get(country);
        String now_url = getIntent().getExtras().getString("link");
        String title_bar = getIntent().getExtras().getString("title_bar");

        if(now_url == null ){
            url = "https://newsapi.org/v2/top-headlines?country="+my_co_code+"&apiKey=dbcb4e7f67444349a2377ab8d49e3e0d";
            mytitle = getString(R.string.app_name);
        }else {
            url  = "https://newsapi.org/v2/top-headlines?country="+my_co_code+now_url+"&apiKey=dbcb4e7f67444349a2377ab8d49e3e0d";
            mytitle = title_bar;
        }

        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        articleList = new ArrayList<>();
        adapter = new NewsAdapter(this, articleList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setCoverPhoto(mytitle);
        new GetContacts().execute();
    }
  public void setCoverPhoto(String cc){
        switch (cc){
            case "Entertainment":
                Glide.with(this).load(R.drawable.entertainment).into((ImageView) findViewById(R.id.backdrop));
                break;
            case "Health":
                Glide.with(this).load(R.drawable.health).into((ImageView) findViewById(R.id.backdrop));
                break;
            case "Science":
                Glide.with(this).load(R.drawable.science).into((ImageView) findViewById(R.id.backdrop));
                break;
            case "Sports":
                Glide.with(this).load(R.drawable.sports).into((ImageView) findViewById(R.id.backdrop));
                break;
            case "General":
                Glide.with(this).load(R.drawable.general).into((ImageView) findViewById(R.id.backdrop));
                break;
            case "Business":
                Glide.with(this).load(R.drawable.bussiness).into((ImageView) findViewById(R.id.backdrop));
                break;
            case "Technology":
                Glide.with(this).load(R.drawable.technology).into((ImageView) findViewById(R.id.backdrop));
                break;
            default:
                Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
                break;

        }
  }
    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(mytitle + " ( "+my_co_code.toUpperCase()+" ) ");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray articles = jsonObj.getJSONArray("articles");

                    // looping through All Contacts
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject c = articles.getJSONObject(i);

                        String tilte = c.getString("title");
                        String author = c.getString("author");
                        String description = c.getString("description");
                        String urlToImage = c.getString("urlToImage");
                        String url = c.getString("url");




                        // tmp hash map for single contact
                        HashMap<String, String> arts = new HashMap<>();

                        // adding each child node to HashMap key => value
                        arts.put("title", tilte);
                        arts.put("author", author );
                        arts.put("description", description);
                        arts.put("urlToImage", urlToImage);
                        arts.put("url", url);

                        // adding contact to contact list
                        newsarticleList.add(arts);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
               prepareNews();
        }

        private void prepareNews() {
            Iterator<HashMap<String, String>> li = newsarticleList.listIterator();
            while(li.hasNext()){
                HashMap<String, String> pp = li.next();
                String ui = pp.get("urlToImage");
                String uIm = "";
                if(!ui.equals(null)){
                    uIm = ui;
                }
                Article a = new Article(" ", "", "ok", pp.get("title"),pp.get("description"), pp.get("url"), uIm,"ok");
                articleList.add(a);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

