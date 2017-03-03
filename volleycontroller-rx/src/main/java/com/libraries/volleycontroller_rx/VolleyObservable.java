package com.libraries.volleycontroller_rx;

import com.android.volley.VolleyError;
import com.libraries.inlacou.volleycontroller.CustomResponse;
import com.libraries.inlacou.volleycontroller.InternetCall;
import com.libraries.inlacou.volleycontroller.VolleyController;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

public class VolleyObservable implements Observable.OnSubscribe<CustomResponse> {
	final InternetCall internetCall;
	final VolleyController.IOCallbacks callbacks;

	VolleyObservable(InternetCall internetCall, VolleyController.IOCallbacks callbacks) {
		this.internetCall = internetCall;
		this.callbacks = callbacks;
	}

	@Override
	public void call(final Subscriber<? super CustomResponse> subscriber) {
		MainThreadSubscription.verifyMainThread();

		VolleyController.getInstance().onCall(internetCall.addCallback(new VolleyController.IOCallbacks() {
			@Override
			public void onResponse(CustomResponse response, String code) {
				callbacks.onResponse(response, code);
				if (!subscriber.isUnsubscribed()) {
					subscriber.onNext(response);
				}
			}

			@Override
			public void onResponseError(VolleyError error, String code) {
				callbacks.onResponseError(error, code);
				if (!subscriber.isUnsubscribed()) {
					subscriber.onError(error);
				}
			}
		}));

		subscriber.add(new MainThreadSubscription() {
			@Override
			protected void onUnsubscribe() {
				VolleyController.getInstance().cancelRequest(internetCall.getCancelTag());
			}
		});
	}

	public static Observable<CustomResponse> create(InternetCall internetCall, VolleyController.IOCallbacks callbacks) {
		if(internetCall==null){
			throw new NullPointerException();
		}
		return Observable.create(new VolleyObservable(internetCall, callbacks));
	}

	public static Observable<InternetCall> from(ArrayList<InternetCall> internetCall) {
		if(internetCall==null){
			throw new NullPointerException();
		}
		return Observable.from(internetCall);
	}
}