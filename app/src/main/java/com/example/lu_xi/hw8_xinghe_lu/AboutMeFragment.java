package com.example.lu_xi.hw8_xinghe_lu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AboutMeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private onBackToFrontPageListener mListener;

    public AboutMeFragment() {
        // Required empty public constructor
    }

    public static AboutMeFragment newInstance() {
        AboutMeFragment fragment = new AboutMeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_about_me, container, false);

        return v;
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onBackToFrontPageListener) {
            mListener = (onBackToFrontPageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onBackToFrontPageListener");
        }
    }

    public interface onBackToFrontPageListener {
        // TODO: Update argument type and name
        void onBackToFrontPageListener();
    }*/
}
