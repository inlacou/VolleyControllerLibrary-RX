package com.libraries.volleycontroller_rx;

import com.android.volley.VolleyError;
import com.libraries.inlacou.volleycontroller.CustomResponse;
import com.libraries.inlacou.volleycontroller.InternetCall;
import com.libraries.inlacou.volleycontroller.VolleyController;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

public class CustomResponseObservable implements Observable.OnSubscribe<CustomResponse> {
	final InternetCall internetCall;

	CustomResponseObservable(InternetCall internetCall) {
		this.internetCall = internetCall;
	}

	@Override
	public void call(final Subscriber<? super CustomResponse> subscriber) {
		MainThreadSubscription.verifyMainThread();

		VolleyController.getInstance().onCall(internetCall.addCallback(new VolleyController.IOCallbacks() {
			@Override
			public void onResponse(CustomResponse response, String code) {
				if (!subscriber.isUnsubscribed()) {
					subscriber.onNext(response);
				}
			}

			@Override
			public void onResponseError(VolleyError error, String code) {
				if (!subscriber.isUnsubscribed()) {
					subscriber.onError(error);
				}
			}
		}));

		subscriber.add(new MainThreadSubscription() {
			@Override
			protected void onUnsubscribe() {
				VolleyController.getInstance().cancelRequest(internetCall);
			}
		});
	}

	public static Observable<CustomResponse> create(InternetCall internetCall) {
		if(internetCall==null){
			throw new NullPointerException();
		}
		return Observable.create(new CustomResponseObservable(internetCall));
	}

	public static Observable<InternetCall> from(ArrayList<InternetCall> internetCall) {
		if(internetCall==null){
			throw new NullPointerException();
		}
		return Observable.from(internetCall);
	}
}