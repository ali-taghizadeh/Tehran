package ir.taghizadeh.tehran.activities.lists.places;

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

/**
 * <h1>PlacesAdapter</h1>
 *
 * This class is a simple Adapter that passes injected {@link Glide} to its viewHolder to load images.
 * {@link OnPlaceItemClickListener} is used to handle item click events.
 * <b>Note:</b> setHasStableIds() true and override getItemId() with position to
 * make sure that your recyclerView does its best performance.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesListViewHolder> {

    public List<NewPlace> newPlaces;
    private Glide mGlide;
    private OnPlaceItemClickListener itemClickListener;

    public PlacesAdapter(List<NewPlace> newPlaces, OnPlaceItemClickListener onPlaceItemClickListener) {
        this.newPlaces = newPlaces;
        this.itemClickListener = onPlaceItemClickListener;
        DependencyRegistry.register.inject(this);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public PlacesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlacesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesListViewHolder holder, int index) {
            NewPlace newPlace = newPlaces.get(index);
            holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(newPlace, index));
            holder.configureWith(newPlace, mGlide);
    }

    @Override
    public int getItemCount() {
        return newPlaces.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void configureWith(Glide glidePresenter) {
        this.mGlide = glidePresenter;
    }
}
