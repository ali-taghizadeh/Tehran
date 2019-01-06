package ir.taghizadeh.tehran.activities.lists.comments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.models.Comments;

/**
 * <h1>CommentsAdapter</h1>
 *
 * This class is a simple Adapter that passes injected {@link Glide} to its viewHolder to load images.
 * No item click listener is implemented.
 * <b>Note:</b> setHasStableIds() true and override getItemId() with position to
 * make sure that your recyclerView does its best performance.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsListViewHolder> {

    public List<Comments> comments;
    private Glide mGlide;

    public CommentsAdapter(List<Comments> comments) {
        this.comments = comments;
        DependencyRegistry.register.inject(this);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public CommentsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsListViewHolder holder, int index) {
            Comments comments = this.comments.get(index);
            holder.configureWith(comments, mGlide);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void configureWith(Glide glidePresenter) {
        this.mGlide = glidePresenter;
    }
}
