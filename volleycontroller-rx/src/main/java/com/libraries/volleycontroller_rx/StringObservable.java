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

public class StringObservable implements ObservableOnSubscribe<String> {
	final InternetCall internetCall;
	
	StringObservable(InternetCall internetCall) {
		this.internetCall = internetCall;
	}
	
	@Override
	public void subscribe(final ObservableEmitter<String> subscriber) throws Exception {
		///MainThreadSubscription.verifyMainThread();
		
		VolleyController.getInstance().onCall(internetCall.addCallback(new VolleyController.IOCallbacks() {
			@Override
			public void onResponse(CustomResponse response, String code) {
				subscriber.onNext(response.getData());
			}
			
			@Override
			public void onResponseError(VolleyError error, String code) {
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
	
	public static Observable<String> create(InternetCall internetCall) {
		if(internetCall==null){
			throw new NullPointerException();
		}
		return Observable.create(new StringObservable(internetCall));
	}
	
	public static Observable<InternetCall> from(ArrayList<InternetCall> internetCall) {
		if(internetCall==null){
			throw new NullPointerException();
		}
		return Observable.fromIterable(internetCall);
	}
}