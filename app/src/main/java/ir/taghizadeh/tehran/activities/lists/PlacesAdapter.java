package ir.taghizadeh.tehran.activities.lists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.models.NewPlace;


public class PlacesAdapter extends RecyclerView.Adapter<PlacesListViewHolder> {

    public List<NewPlace> newPlaces;
    private Glide mGlide;
    OnPlaceItemClickListener itemClickListener;

    public PlacesAdapter(List<NewPlace> newPlaces, OnPlaceItemClickListener onPlaceItemClickListener) {
        this.newPlaces = newPlaces;
        this.itemClickListener = onPlaceItemClickListener;
        DependencyRegistry.register.inject(this);
    }

    @NonNull
    @Override
    public PlacesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        final PlacesListViewHolder viewHolder = new PlacesListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesListViewHolder holder, int index) {
            NewPlace newPlace = newPlaces.get(index);
            holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(newPlace));
            holder.configureWith(newPlace, mGlide);
    }

    @Override
    public int getItemCount() {
        return newPlaces.size();
    }

    public void configureWith(Glide glidePresenter) {
        this.mGlide = glidePresenter;
    }
}
