package com.example.androidtema2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtema2.R;
import com.example.androidtema2.constants.Constants;
import com.example.androidtema2.interfaces.OnItemClickListener;
import com.example.androidtema2.models.Album;
import com.example.androidtema2.models.ElementType;
import com.example.androidtema2.models.Element;
import com.example.androidtema2.models.Image;
import com.example.androidtema2.models.Post;
import com.example.androidtema2.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Element> elementList;
    OnItemClickListener onItemClickListener;

    public MyAdapter(ArrayList<Element> elementList, OnItemClickListener onItemClickListener) {
        this.elementList = elementList;
        this.onItemClickListener = onItemClickListener;
    }

    public MyAdapter(ArrayList<Element> elementList) {
        this.elementList = elementList;
    }

    @Override
    public int getItemViewType(int position) {
        if (elementList.get(position).getElement() == ElementType.USER) {
            return 0;
        } else if (elementList.get(position).getElement() == ElementType.POST) {
            return 1;
        } else if (elementList.get(position).getElement() == ElementType.ALBUM) {
            return 2;
        } else if (elementList.get(position).getElement() == ElementType.IMAGE) {
            return 3;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        }
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view);
        }
        if (viewType == 2) {
            View view = inflater.inflate(R.layout.item_album, parent, false);
            return new AlbumViewHolder(view);
        }
        if (viewType == 3) {
            View view = inflater.inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            User artist = (User) elementList.get(position);
            ((UserViewHolder) holder).bind(artist);
        } else if (holder instanceof PostViewHolder) {
            Post post = (Post) elementList.get(position);
            ((PostViewHolder) holder).bind(post);
        } else if (holder instanceof AlbumViewHolder) {
            Album album = (Album) elementList.get(position);
            ((AlbumViewHolder) holder).bind(album);
        } else if (holder instanceof ImageViewHolder) {
            Image image = (Image) elementList.get(position);
            ((ImageViewHolder) holder).bind(image.getUrl());
        }
    }

    @Override
    public int getItemCount() {
        return this.elementList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView username;
        private final TextView email;
        private final ImageButton imageButton;
        private final View view;

        UserViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            username = view.findViewById(R.id.username);
            email = view.findViewById(R.id.email);
            imageButton = view.findViewById(R.id.imageView);
            this.view = view;
        }


        void bind(User user) {
            name.setText(user.getName());
            username.setText(user.getUsername());
            email.setText(user.getEmail());
            imageButton.setOnClickListener(
                    v -> {
                        if (onItemClickListener != null) {
                            onItemClickListener.onImageClick(user);
                            notifyItemChanged(getAdapterPosition());
                        }
                    });

            view.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(user);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView body;

        PostViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.post_title);
            body = view.findViewById(R.id.body);
        }

        void bind(Post post) {
            title.setText(post.getTitle());
            body.setText(post.getBody());
        }
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        private final TextView albumId;
        private final TextView body;
        private final View view;

        public AlbumViewHolder(@NonNull View view) {
            super(view);
            body = view.findViewById(R.id.album_body);
            albumId = view.findViewById(R.id.album_id);
            this.view = view;
        }

        public void bind(Album album) {
            body.setText(album.getBody());
            albumId.setText(String.valueOf(album.getId()));
            view.setOnClickListener(v -> onItemClickListener.onItemClick(album));
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ImageViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.image_body);
        }

        public void bind(String imageUrl) {
            Picasso picasso = Picasso.get();
            picasso.load(imageUrl).resize(Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT).into(imageView);
        }
    }
}