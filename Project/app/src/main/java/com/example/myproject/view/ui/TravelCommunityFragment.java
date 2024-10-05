package com.example.myproject.view.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TravelCommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TravelCommunityFragment extends Fragment {

    /**
     * Naming of variable.
     */
    private static final String ARG_PARAM1 = "param1";
    /**
     * Naming of variable.
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * Naming of variable.
     */
    private String mParam1;
    /**
     * Naming of variable.
     */
    private String mParam2;

    /**
     * Fragment class for travel community.
     */
    public TravelCommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TravelCommunityFragment.
     */
    public static TravelCommunityFragment newInstance(final String param1,
                                                      final String param2) {
        TravelCommunityFragment fragment = new TravelCommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method runs on create.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Returns a View for NavBar.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null,
     *                          this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel_community,
                container, false);
    }
}
