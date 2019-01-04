package ir.taghizadeh.tehran.activities.lists.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.models.Comments;

class CommentsListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_item_comment_user_photo)
    ShapedImageView image_item_comment_user_photo;
    @BindView(R.id.text_item_comment_username)
    TextView text_item_comment_username;
    @BindView(R.id.text_item_comment)
    TextView text_item_comment;

    CommentsListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void configureWith(Comments comments, Glide mGlide) {
        text_item_comment_username.setText(comments.getUsername());
        text_item_comment.setText(comments.getComment());
        mGlide.loadImage(comments.getUserPhotoUrl(), image_item_comment_user_photo);
    }

}
