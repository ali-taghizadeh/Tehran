package ir.taghizadeh.tehran.activities.lists.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.models.Comments;

class CommentsListViewHolder extends RecyclerView.ViewHolder {

    private ShapedImageView image_item_comment_user_photo;
    private TextView text_item_comment_username;
    private TextView text_item_comment;

    CommentsListViewHolder(View itemView) {
        super(itemView);
        this.image_item_comment_user_photo = itemView.findViewById(R.id.image_item_comment_user_photo);
        this.text_item_comment_username = itemView.findViewById(R.id.text_item_comment_username);
        this.text_item_comment = itemView.findViewById(R.id.text_item_comment);
    }

    void configureWith(Comments comments, Glide mGlide) {
        text_item_comment_username.setText(comments.getmUsername());
        text_item_comment.setText(comments.getmComment());
        mGlide.loadImage(comments.getmUserPhotoUrl(), image_item_comment_user_photo);
    }

}
