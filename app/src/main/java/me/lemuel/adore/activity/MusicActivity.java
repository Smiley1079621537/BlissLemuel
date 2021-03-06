package me.lemuel.adore.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.lemuel.adore.R;
import me.lemuel.adore.base.BaseActivity;
import me.lemuel.adore.base.MusicExtras;
import me.lemuel.adore.bean.music.Music;
import me.lemuel.adore.bean.music.OnlineMusic;
import me.lemuel.adore.bean.music.OnlineMusicList;
import me.lemuel.adore.bean.music.SongListInfo;
import me.lemuel.adore.mvp.music.MusicContract;
import me.lemuel.adore.mvp.music.MusicPresenter;
import me.lemuel.adore.provider.BillboardViewProvider;
import me.lemuel.adore.provider.OnlineMusicViewProvider;
import me.lemuel.adore.service.PlayMusicService;


public class MusicActivity extends BaseActivity implements MusicContract.View {

    @BindView(R.id.lv_online_music_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_header_bg)
    SimpleDraweeView mIvHeaderBg;
    @BindView(R.id.iv_cover)
    SimpleDraweeView mIvCover;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_update_date)
    TextView mTvUpdateDate;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private boolean headerAdded = false;
    private MultiTypeAdapter mAdapter;
    private OnlineMusicViewProvider mMusicViewProvider;
    private MusicPresenter mMusicPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_online_music;
    }

    @Override
    protected void initView() {
        mMusicViewProvider = new OnlineMusicViewProvider();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Immanuel");
        }
        mToolbarLayout.setTitleEnabled(false);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(OnlineMusicList.Billboard.class, new BillboardViewProvider());
        mAdapter.register(OnlineMusic.class, mMusicViewProvider);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initData() {
        SongListInfo songListInfo = getIntent().getParcelableExtra(MusicExtras.MUSIC_LIST_TYPE);
        mMusicPresenter = new MusicPresenter(this);
        mMusicPresenter.requestMusic(songListInfo, 0);
    }

    @Override
    protected void initEvent() {
        mMusicViewProvider.setOnMusicClick(new OnlineMusicViewProvider.OnMusicClick() {
            @Override
            public void onItemMusicClick(OnlineMusic onlineMusic) {
                mMusicPresenter.doPlay(onlineMusic);
            }
        });
    }

    @Override
    public void doPlayMusic(Music music) {
        startService(new Intent(MusicActivity.this, PlayMusicService.class).putExtra("music", music));
        mToolbar.setTitle(music.getSonginfo().getTitle());
    }

    @Override
    public void responseMusicList(OnlineMusicList onlineMusicList) {
        Items items = new Items();
        if (!headerAdded) {
            OnlineMusicList.Billboard billboard = onlineMusicList.getBillboard();
            mIvCover.setImageURI(Uri.parse(billboard.getPic_s640()));
            mIvHeaderBg.setImageURI(Uri.parse(billboard.getPic_s640()));
            mTvTitle.setText(billboard.getName());
            mTvUpdateDate.setText(billboard.getUpdate_date());
            mTvComment.setText(billboard.getComment());
            blurHeaderView(mIvHeaderBg, billboard);
            headerAdded = true;
        }
        items.addAll(onlineMusicList.getSong_list());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }
}
