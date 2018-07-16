package com.example.apple.codequiz.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.codequiz.R;
import com.example.apple.codequiz.StartActivity;
import com.example.apple.codequiz.Tools.CategoryViewHolder;
import com.example.apple.codequiz.Tools.Pojo.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesFragment extends Fragment {
    private static final String ROOT_CATEGORY_REFERENCE = "Categories";
    private static final int COLUMNS_COUNT = 2;
    private static final int MIN_WITH = 380;
    private static final int MIN_IMAGE_HEIGHT = 35;
    private static final float MIN_TEXT_SIZE = 12.0f;

    FirebaseDatabase database;
    DatabaseReference categoriesReference;


    @BindView(R.id.categories_list)
    RecyclerView categoriesList;

    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;


    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View categoryFragmentLayout = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this,categoryFragmentLayout);

        return categoryFragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

        initDatabase();
        initRecycler();
    }

    private void initDatabase() {
        database = FirebaseDatabase.getInstance();
        categoriesReference = database.getReference(ROOT_CATEGORY_REFERENCE);
    }

    private void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),COLUMNS_COUNT);

        Configuration config = getContext().getResources().getConfiguration();
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class,
                R.layout.category_item,
                CategoryViewHolder.class,
                categoriesReference) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                if (config.screenWidthDp <= MIN_WITH){
                    viewHolder.categoryImage.setMaxHeight(MIN_IMAGE_HEIGHT);
                    viewHolder.categoryTitle.setTextSize(MIN_TEXT_SIZE);
                }
                Picasso.get().load(model.getImgUrl()).into(viewHolder.categoryImage);
                viewHolder.categoryTitle.setText(model.getTitle());

                viewHolder.itemView.setOnClickListener(view -> {
                    String categoryName = adapter.getItem(position).getTitle();

                    Intent intent = new Intent(getContext(), StartActivity.class);
                    intent.putExtra(StartActivity.CATEGORY_NAME_TAG,categoryName);

                    startActivity(intent);
                });
            }
        };


        categoriesList.setLayoutManager(layoutManager);
        categoriesList.setAdapter(adapter);
        categoriesList.setHasFixedSize(true);
    }
}
