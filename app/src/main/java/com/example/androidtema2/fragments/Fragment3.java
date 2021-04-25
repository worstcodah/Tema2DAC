package com.example.androidtema2.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidtema2.R;
import com.example.androidtema2.adapters.MyAdapter;
import com.example.androidtema2.constants.Constants;
import com.example.androidtema2.models.Album;
import com.example.androidtema2.models.Element;
import com.example.androidtema2.models.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Fragment3 extends Fragment {
    private Album currentAlbum;
    private ArrayList<Element> elementList = new ArrayList<>();
    private MyAdapter myAdapter = null;

    public Fragment3() {
        // Required empty public constructor
    }

    public Fragment3(Element currentAlbum) {
        this.currentAlbum = (Album) currentAlbum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_3, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.image_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        getImages();
        myAdapter = new MyAdapter(this.elementList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(this.myAdapter);
        recyclerView.post(() -> myAdapter.notifyDataSetChanged());

        return view;
    }

    void getImages() {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        String url = Constants.BASE_URL + "/photos?" + Constants.ALBUM_ID + "=" + currentAlbum.getId();
        StringRequest getAlbumsRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        handlePhotosResponse(response);
                    } catch (JSONException exception) {
                        Log.e("images load error", exception.getMessage());
                    }
                },
                error -> Toast.makeText(getContext(), "PHOTO ERROR", Toast.LENGTH_SHORT).show()
        );
        queue.add(getAlbumsRequest);
    }

    void handlePhotosResponse(String response) throws JSONException {
        JSONArray photosJSONArray = new JSONArray(response);
        for (int index = 0; index < photosJSONArray.length(); ++index) {
            JSONObject userPostJSON = (JSONObject) photosJSONArray.get(index);
            int id = userPostJSON.getInt("id");
            String title = userPostJSON.getString("title");
            String url = userPostJSON.getString("url");
            String thumbnailUrl = userPostJSON.getString("thumbnailUrl");
            Image image = new Image(id, title, url, thumbnailUrl);
            this.elementList.add(image);
        }
        myAdapter.notifyDataSetChanged();
    }
}