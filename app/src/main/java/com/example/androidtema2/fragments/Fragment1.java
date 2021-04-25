package com.example.androidtema2.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.androidtema2.R;
import com.example.androidtema2.volley.VolleyConfigSingleton;
import com.example.androidtema2.adapters.MyAdapter;
import com.example.androidtema2.constants.Constants;
import com.example.androidtema2.interfaces.ActivityFragmentCommunication;
import com.example.androidtema2.interfaces.OnItemClickListener;
import com.example.androidtema2.models.Element;
import com.example.androidtema2.models.Post;
import com.example.androidtema2.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Stack;


public class Fragment1 extends Fragment implements OnItemClickListener {
    private ActivityFragmentCommunication activityFragmentCommunication;
    private View view;
    private ArrayList<Element> elementList = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();
    private MyAdapter myAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.artist_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = view.findViewById(R.id.swipe_rv_fragment1);
        getUserDetails();
        myAdapter = new MyAdapter(elementList, this);
        recyclerView.setAdapter(myAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            elementList.clear();
            myAdapter.notifyDataSetChanged();
            getUserDetails();
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ActivityFragmentCommunication) {
            activityFragmentCommunication = (ActivityFragmentCommunication) context;
        }
    }

    void getUserDetails() {
        VolleyConfigSingleton volleyConfigSingleton = VolleyConfigSingleton.getInstance(view.getContext());
        RequestQueue queue = volleyConfigSingleton.getRequestQueue();
        String url = Constants.BASE_URL + "/users";
        StringRequest getPostsRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        handleUsersResponse(response);
                    } catch (JSONException exception) {
                        Log.e("users load error", exception.getMessage());
                    }
                }, error -> Toast.makeText(
                view.getContext(),
                "ERROR: get users failed with error: " + error.getMessage(),
                Toast.LENGTH_SHORT
        ).show()

        );
        queue.add(getPostsRequest);
    }

    void getPosts(User user) {
        VolleyConfigSingleton volleyConfigSingleton = VolleyConfigSingleton.getInstance(view.getContext());
        RequestQueue queue = volleyConfigSingleton.getRequestQueue();
        String url = Constants.BASE_URL + "/posts?" + Constants.USER_ID + "=" + user.getId();
        StringRequest getPostsRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        handlePostsResponse(response);
                    } catch (JSONException exception) {
                        Log.e("posts load error", exception.getMessage());
                    }
                }, error -> Toast.makeText(
                getContext(),
                "ERROR: get posts failed with error: " + error.getMessage(),
                Toast.LENGTH_SHORT
        ).show()
        );
        queue.add(getPostsRequest);
    }

    void handlePostsResponse(String response) throws JSONException {
        JSONArray postsJSONArray = new JSONArray(response);
        for (int index = 0; index < postsJSONArray.length(); ++index) {
            JSONObject userPostJSON = (JSONObject) postsJSONArray.get(index);
            int id = userPostJSON.getInt("id");
            int userId = userPostJSON.getInt("userId");
            String title = userPostJSON.getString("title");
            String body = userPostJSON.getString("body");
            Post newPost = new Post(userId, id, title, body);
            //Fara dubluri (override equals si hashcode)
            if (!posts.contains(newPost)) {
                posts.add(newPost);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    void handleUsersResponse(String response) throws JSONException {
        JSONArray postsJSONArray = new JSONArray(response);
        for (int index = 0; index < postsJSONArray.length(); ++index) {
            JSONObject userPostJSON = (JSONObject) postsJSONArray.get(index);
            int id = userPostJSON.getInt("id");
            String name = userPostJSON.getString("name");
            String username = userPostJSON.getString("username");
            String email = userPostJSON.getString("email");
            elementList.add(new User(id, name, username, email));
        }
        myAdapter.notifyDataSetChanged();
    }

    private void deletePosts(User user) {
        Stack<Element> postsToDelete = new Stack<>();

        for (Element element : elementList) {
            if (element instanceof Post && ((Post) element).getUserId() == user.getId()) {
                postsToDelete.add(element);
            }
        }
        while (!postsToDelete.empty()) {
            elementList.remove(postsToDelete.pop());
        }
        myAdapter.notifyDataSetChanged();
    }

    private void reorderElements() {
        //Previne suprapunerea posturilor.
        ArrayList<Element> reorderedElements = new ArrayList<>();
        for (Element element : elementList) {
            if (element instanceof User) {
                reorderedElements.add(element);
                if (((User) element).hasExpandedPosts()) {
                    for (Post post : posts) {
                        if (((User) element).getId() == post.getUserId()) {
                            reorderedElements.add(post);
                        }
                    }
                }
            }
        }
        elementList.clear();
        elementList.addAll(reorderedElements);
    }

    @Override
    public void onItemClick(Element user) {
        activityFragmentCommunication.openFragment2(user);
    }

    @Override
    public void onImageClick(User user) {
        getPosts(user);
        if (!user.hasExpandedPosts()) {
            user.setExpandedPosts(true);
            reorderElements();
            myAdapter.notifyDataSetChanged();
        } else {
            user.setExpandedPosts(false);
            deletePosts(user);
        }
    }
}