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


public class CommentsAdapter extends RecyclerView.Adapter<CommentsListViewHolder> {

    public List<Comments> comments;
    private Glide mGlide;

    public CommentsAdapter(List<Comments> comments) {
        this.comments = comments;
        DependencyRegistry.register.inject(this);
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

    public void configureWith(Glide glidePresenter) {
        this.mGlide = glidePresenter;
    }
}
