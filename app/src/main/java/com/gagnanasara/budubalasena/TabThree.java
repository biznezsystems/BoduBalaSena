package com.gagnanasara.budubalasena;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabThree.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabThree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabThree extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    private ScrollView notificationScrollView;

    MainActivity mainActivity;

    //private OnFragmentInteractionListener mListener;

    public TabThree() {
        // Required empty public constructor
    }

    public TabThree(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabThree.
     */
    // TODO: Rename and change types and number of parameters
    public static TabThree newInstance(String param1, String param2) {
        TabThree fragment = new TabThree();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment

        notificationScrollView = (ScrollView)inflater.inflate(R.layout.fragment_tab_three, container, false);

        return notificationScrollView;
    }

    public void updateNotificationTable(){

        notificationScrollView.removeAllViews();

        int nCount = 0 ;
        if(PreferenceHelper.getValue(getString(R.string.notification_count)) != "") {
            nCount = Integer.parseInt(PreferenceHelper.getValue(getString(R.string.notification_count)));
        }

        Context context = getContext();

        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setShrinkAllColumns(true);

        //TableLayout notificationScrollView = (TableLayout)getActivity().getLayoutInflater().inflate(R.layout.fragment_tab_three,null,false);

        for (int i = nCount; i > 0; i--) {

            String date = PreferenceHelper.getValue(getString(R.string.pref_key_date)+i);
            String title = PreferenceHelper.getValue(getString(R.string.pref_key_title)+i);
            String message = PreferenceHelper.getValue(getString(R.string.pref_key_message)+i);

            TableRow tRow = new TableRow(context);
            if(i%2 != 0) {
                tRow.setBackgroundColor(Color.rgb(255,240,240));
            }

            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tRow.setLayoutParams(layoutParams);
            tRow.setPadding(50,50,50,50);

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textViewMessage = new TextView(context);
            textViewMessage.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
            //textViewMessage.setPadding(50,10,75,0);
            textViewMessage.setTextSize(14);
            textViewMessage.setText(message);
            linearLayout.addView(textViewMessage, 0);

            TextView textViewTitle = new TextView(context);
            textViewMessage.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
            textViewTitle.setTextColor(Color.parseColor("#A20008"));
            textViewTitle.setTextSize(16);
            textViewTitle.setText(title);
            linearLayout.addView(textViewTitle, 0);

            TextView textViewDate = new TextView(context);
            textViewDate.setTextSize(12);
            textViewDate.setText(date);
            linearLayout.addView(textViewDate, 0);

            tRow.addView(linearLayout);

            tableLayout.addView(tRow);

            System.out.println("xxxxxxxxxxxxxxxx aad row: "+i);
        }


        TableRow tRow = new TableRow(context);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tRow.setLayoutParams(layoutParams);
        tRow.setPadding(50,50,50,50);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textViewDummy = new TextView(context);
        textViewDummy.setTextSize(30);
        textViewDummy.setText("");
        linearLayout.addView(textViewDummy, 0);

        tRow.addView(linearLayout);

        tableLayout.addView(tRow);

        notificationScrollView.addView(tableLayout);

        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);
    }

    // Handling the received Intents for the "my-integer" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            int notificationCount = intent.getIntExtra("n-count",0);
            System.out.println("xyxyxyxyxyxyxyxyxyxyxyxyxy: "+notificationCount);

            updateNotificationTable();
        }
    };

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(getContext())
                .unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // This registers mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver,
                        new IntentFilter("notification-count"));

        updateNotificationTable();
    }

    /*

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
     */
}
