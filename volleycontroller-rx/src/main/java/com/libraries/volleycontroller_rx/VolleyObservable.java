package com.libraries.volleycontroller_rx;

import com.android.volley.VolleyError;
import com.libraries.inlacou.volleycontroller.CustomResponse;
import com.libraries.inlacou.volleycontroller.InternetCall;
import com.libraries.inlacou.volleycontroller.VolleyController;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class VolleyObservable implements ObservableOnSubscribe<CustomResponse> {
	final InternetCall internetCall;
	final VolleyController.IOCallbacks callbacks;

	VolleyObservable(InternetCall internetCall, VolleyController.IOCallbacks callbacks) {
		this.internetCall = internetCall;
		this.callbacks = callbacks;
	}
	
	@Override
	public void subscribe(final ObservableEmitter<CustomResponse> subscriber) throws Exception {
		///MainThreadSubscription.verifyMainThread();
		
		VolleyController.getInstance().onCall(internetCall.addCallback(new VolleyController.IOCallbacks() {
			@Override
			public void onResponse(CustomResponse response, String code) {
				callbacks.onResponse(response, code);
				subscriber.onNext(response);
			}
			
			@Override
			public void onResponseError(VolleyError error, String code) {
				callbacks.onResponseError(error, code);
				subscriber.onError(error);
			}
		}));
		subscriber.setCancellable(new Cancellable() {
			@Override
			public void cancel() throws Exception {
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
		return Observable.fromIterable(internetCall);
	}
}