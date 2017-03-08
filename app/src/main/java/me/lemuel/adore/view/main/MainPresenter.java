package me.lemuel.adore.view.main;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import me.drakeet.multitype.Items;
import me.lemuel.adore.App;
import me.lemuel.adore.items.movie.Movie;
import me.lemuel.adore.items.movie.SubjectsBean;

/**
 * Created by lemuel on 2017/2/24.
 */

public class MainPresenter implements MainContract.Presenter {

    private final MainNowFragment mainView;

   /* @Inject
    @Named("MainView")
    public MainPresenter(MainContract.View mainView) {
        this.mainView = mainView;
    }*/

    @Inject
    public MainPresenter(MainNowFragment nowFragment) {
        this.mainView = nowFragment;
    }

    @Inject
    public void setupListeners() {
        mainView.setPresenter(this);

    }

    @Override
    public void onCreate() {

        App.getAppComponent().getDoubanService().getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mainView.showLoading();
                    }

                    @Override
                    public void onNext(Movie movie) {
                        mainView.hideLoding();
                        Realm realm = App.getAppComponent().getRealm();
                        realm.beginTransaction();
                        List<SubjectsBean> subjects = movie.getSubjects();
                        realm.delete(SubjectsBean.class);
                        Items items = new Items();
                        for (SubjectsBean subject : subjects) {
                            items.add(subject);
                            realm.copyToRealm(subject);
                        }
                        realm.commitTransaction();
                        mainView.loadData(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.hideLoding();
                        RealmResults<SubjectsBean> all = App.getAppComponent().getRealm()
                                .where(SubjectsBean.class)
                                .findAll();
                        mainView.loadCacheData(all);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}