package com.libraries.volleycontroller_rx;

import com.android.volley.VolleyError;
import com.libraries.inlacou.volleycontroller.CustomResponse;
import com.libraries.inlacou.volleycontroller.InternetCall;
import com.libraries.inlacou.volleycontroller.VolleyController;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;


public class CustomResponseObservable implements ObservableOnSubscribe<CustomResponse> {
	final InternetCall internetCall;

	CustomResponseObservable(InternetCall internetCall) {
		this.internetCall = internetCall;
	}

	@Override
	public void subscribe(final ObservableEmitter<CustomResponse> subscriber) {
		///MainThreadSubscription.verifyMainThread();

		VolleyController.getInstance().onCall(internetCall.addCallback(new VolleyController.IOCallbacks() {
			@Override
			public void onResponse(CustomResponse response, String code) {
				if(!subscriber.isDisposed())	subscriber.onNext(response);
				if(!subscriber.isDisposed())	subscriber.onComplete();
			}

			@Override
			public void onResponseError(VolleyError error, String code) {
				if(!subscriber.isDisposed())    subscriber.onError(error);
			}
		}));
		subscriber.setCancellable(new Cancellable() {
			@Override
			public void cancel() throws Exception {
				VolleyController.getInstance().cancelRequest(internetCall.getCancelTag());
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
		return Observable.fromIterable(internetCall);
	}
}