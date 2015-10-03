package apidez.com.android_mvvm_sample.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

/**
 * Created by nongdenchet on 10/2/15.
 */
public final class TextViewSubscribeUnInit implements Observable.OnSubscribe<CharSequence> {

    private final TextView view;

    public TextViewSubscribeUnInit(TextView view) {
        this.view = view;
    }

    @Override
    public void call(final Subscriber<? super CharSequence> subscriber) {
        checkUiThread();

        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        view.addTextChangedListener(watcher);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                view.removeTextChangedListener(watcher);
            }
        });
    }
}