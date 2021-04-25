package com.example.androidtema2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidtema2.R;
import com.example.androidtema2.adapters.MyAdapter;
import com.example.androidtema2.constants.Constants;
import com.example.androidtema2.interfaces.ActivityFragmentCommunication;
import com.example.androidtema2.interfaces.OnItemClickListener;
import com.example.androidtema2.models.Album;
import com.example.androidtema2.models.Element;
import com.example.androidtema2.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class Fragment2 extends Fragment implements OnItemClickListener {
    private ActivityFragmentCommunication activityFragmentCommunication;
    private User currentUser;
    private ArrayList<Element> elementList = new ArrayList<>();
    private MyAdapter myAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;

    public Fragment2() {
        // Required empty public constructor
    }

    public Fragment2(Element currentUser) {
        this.currentUser = (User) currentUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ActivityFragmentCommunication) {
            activityFragmentCommunication = (ActivityFragmentCommunication) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_2, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.album_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_rv_fragment2);
        myAdapter = new MyAdapter(this.elementList, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.myAdapter);
        getAlbums();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            elementList.clear();
            myAdapter.notifyDataSetChanged();
            getAlbums();
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    void getAlbums() {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(view.getContext()));
        String url = Constants.BASE_URL + "/albums?" + Constants.USER_ID + "=" + currentUser.getId();

        StringRequest getAlbumsRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        handleAlbumResponse(response);
                    } catch (JSONException exception) {
                        Log.e("albums load error", exception.getMessage());
                    }
                },
                error -> Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show()
        );
        queue.add(getAlbumsRequest);
    }

    void handleAlbumResponse(String response) throws JSONException {
        JSONArray albumJSONArray = new JSONArray(response);
        for (int index = 0; index < albumJSONArray.length(); ++index) {
            JSONObject userPostJSON = (JSONObject) albumJSONArray.get(index);
            int userId = userPostJSON.getInt("userId");
            if (userId == currentUser.getId()) {
                int id = userPostJSON.getInt("id");
                String title = userPostJSON.getString("title");
                Album album = new Album(id, title);
                this.elementList.add(album);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Element element) {
        activityFragmentCommunication.openFragment3(element);
    }

    @Override
    public void onImageClick(User user) {
        //Empty
    }
}