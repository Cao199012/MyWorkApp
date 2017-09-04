package com.caojian.myworkapp.friend;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.friend.adapter.FriendListAdapter;
import com.caojian.myworkapp.friend.dummy.FriendItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.caojian.myworkapp.friend.activitys.FriendMessageActivity.go2FriendMessageActivity;
import static com.caojian.myworkapp.friend.activitys.SearchByContactActivity.go2SearchByContactActivity;
import static com.caojian.myworkapp.friend.activitys.SearchByPhoneActivity.go2SearchByPhoneActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment implements FriendListAdapter.ItemClick {

    @BindView(R.id.recy_friend)
    RecyclerView mRecy_friend;

    private FriendListAdapter listAdapter;
    private List<FriendItem> mListData = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
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
        setHasOptionsMenu(true);//创建Menu必须设置
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_froend, container, false);
        unbinder = ButterKnife.bind(this,root);
        initRecy();
        return root;
    }

    private void initRecy() {
        listAdapter = new FriendListAdapter(mListData,this);
        mRecy_friend.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecy_friend.setAdapter(listAdapter);
    }

    //跳转好友请信息求页面
    @OnClick(R.id.new_friend)
    public void goToMessage()
    {

        go2FriendMessageActivity(getActivity());
    }

    @OnClick(R.id.friend_group)
    public void go2Group()
    {

    }

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.title_add,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.title_add)
        {
            PopUpMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void PopUpMenu() {
        PopupMenu popuMenu = new PopupMenu(getContext(),getActivity().findViewById(R.id.title_add));
        popuMenu.inflate(R.menu.friend_search);
        popuMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search_phone:
                        // TODO: 2017/9/3 跳转手机号搜索页面
                        go2SearchByPhoneActivity(getActivity());
                        break;
                    case R.id.search_commect:
                        // TODO: 2017/9/3 跳转联系人添加
                        go2SearchByContactActivity(getActivity());
                        break;
                }
                return false;
            }
        });
        popuMenu.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void itemSlect(FriendItem item) {
        // TODO: 2017/9/2 跳转到好友详情页面
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
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
