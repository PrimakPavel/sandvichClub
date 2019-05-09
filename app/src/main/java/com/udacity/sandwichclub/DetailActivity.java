package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String LIST_ELEMENTS_SEPARATOR = ", ";
    private static final int DEFAULT_POSITION = -1;
    private TextView mAlsoKnownAsTv;
    private TextView mAlsoKnownAsLabelTv;
    private TextView mIngredientsTv;
    private TextView mIngredientsLabelTv;
    private TextView mPlaceOfOriginTv;
    private TextView mPlaceOfOriginLabelTv;
    private TextView mDescriptionTv;
    private TextView mDescriptionLabelTv;

    private ProgressBar mProgressBar;
    private ImageView mImageLoadErrorIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterIv = findViewById(R.id.image_iv);

        findViewsById();

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.get()
                .load(sandwich.getImage())
                .into(posterIv, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        mImageLoadErrorIv.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

        setTitle(sandwich.getMainName());

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void findViewsById() {
        mAlsoKnownAsTv = findViewById(R.id.also_known_tv);
        mAlsoKnownAsLabelTv = findViewById(R.id.also_known_label_tv);

        mIngredientsTv = findViewById(R.id.ingredients_tv);
        mIngredientsLabelTv = findViewById(R.id.detail_ingredients_label_tv);

        mPlaceOfOriginTv = findViewById(R.id.origin_tv);
        mPlaceOfOriginLabelTv = findViewById(R.id.detail_place_of_origin_label_tv);

        mDescriptionTv = findViewById(R.id.description_tv);
        mDescriptionLabelTv = findViewById(R.id.detail_description_label_tv);

        mProgressBar = findViewById(R.id.progress_bar);
        mImageLoadErrorIv = findViewById(R.id.image_load_error_iv);

    }

    private void populateUI(Sandwich sandwich) {
        //"Also known as" ROW
        if (!sandwich.getAlsoKnownAs().isEmpty()) {
            mAlsoKnownAsTv.setVisibility(View.VISIBLE);
            mAlsoKnownAsLabelTv.setVisibility(View.VISIBLE);
            for (int i = 0; i < sandwich.getAlsoKnownAs().size(); i++) {
                mAlsoKnownAsTv.append(sandwich.getAlsoKnownAs().get(i));
                if (i < sandwich.getAlsoKnownAs().size() - 1) {
                    mAlsoKnownAsTv.append(LIST_ELEMENTS_SEPARATOR);
                }
            }
        }
        //"Ingredients" ROW
        if (!sandwich.getIngredients().isEmpty()) {
            mIngredientsTv.setVisibility(View.VISIBLE);
            mIngredientsLabelTv.setVisibility(View.VISIBLE);
            for (int i = 0; i < sandwich.getIngredients().size(); i++) {
                mIngredientsTv.append(sandwich.getIngredients().get(i));
                if (i < sandwich.getIngredients().size() - 1) {
                    mIngredientsTv.append(LIST_ELEMENTS_SEPARATOR);
                }
            }
        }
        //"Place of origin" ROW
        if (!sandwich.getPlaceOfOrigin().isEmpty()) {
            mPlaceOfOriginTv.setVisibility(View.VISIBLE);
            mPlaceOfOriginLabelTv.setVisibility(View.VISIBLE);
            mPlaceOfOriginTv.setText(sandwich.getPlaceOfOrigin());
        }
        //"Description" ROW
        if (!sandwich.getDescription().isEmpty()) {
            mDescriptionTv.setVisibility(View.VISIBLE);
            mDescriptionLabelTv.setVisibility(View.VISIBLE);
            mDescriptionTv.setText(sandwich.getDescription());
        }

    }
}
