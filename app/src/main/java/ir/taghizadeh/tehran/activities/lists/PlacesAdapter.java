package ir.taghizadeh.tehran.activities.lists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    public PlacesAdapter(List<NewPlace> newPlaces) {
        this.newPlaces = newPlaces;
        DependencyRegistry.register.inject(this);
    }

    @NonNull
    @Override
    public PlacesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        view.getContext();
        return new PlacesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesListViewHolder holder, int index) {
            NewPlace newPlace = newPlaces.get(index);
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
