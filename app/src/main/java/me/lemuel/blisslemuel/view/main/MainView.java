package me.lemuel.blisslemuel.view.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.realm.RealmResults;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.lemuel.blisslemuel.R;
import me.lemuel.blisslemuel.items.movie.MovieViewProvider;
import me.lemuel.blisslemuel.items.movie.SubjectsBean;

/**
 * Created by lemuel on 2017/2/24.
 */
public class MainView extends Fragment
        implements MainContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final int SPAN_COUNT = 2;
    private MainContract.Presenter mMainPresenter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private MultiTypeAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.view_main, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        listAdapter = new MultiTypeAdapter();
        listAdapter.register(SubjectsBean.class, new MovieViewProvider());
        recyclerView.setAdapter(listAdapter);
        mMainPresenter.onCreate();
    }

    public static MainView newInstance() {
        return new MainView();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoding() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void loadData(Items items) {
        listAdapter.setItems(items);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadCacheData(RealmResults<SubjectsBean> results) {
        Items items = new Items();
        for (SubjectsBean subject : results) {
            items.add(subject);
        }
        listAdapter.setItems(items);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mMainPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }
}
