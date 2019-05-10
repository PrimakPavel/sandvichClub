package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String LIST_ELEMENTS_SEPARATOR = ", ";
    private static final int DEFAULT_POSITION = -1;
    @BindView(R.id.also_known_tv)
    TextView mAlsoKnownAsTv;
    @BindView(R.id.also_known_label_tv)
    TextView mAlsoKnownAsLabelTv;

    @BindView(R.id.ingredients_tv)
    TextView mIngredientsTv;
    @BindView(R.id.detail_ingredients_label_tv)
    TextView mIngredientsLabelTv;

    @BindView(R.id.origin_tv)
    TextView mPlaceOfOriginTv;
    @BindView(R.id.detail_place_of_origin_label_tv)
    TextView mPlaceOfOriginLabelTv;

    @BindView(R.id.description_tv)
    TextView mDescriptionTv;
    @BindView(R.id.detail_description_label_tv)
    TextView mDescriptionLabelTv;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.image_load_error_iv)
    ImageView mImageLoadErrorIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        ImageView posterIv = findViewById(R.id.image_iv);

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
