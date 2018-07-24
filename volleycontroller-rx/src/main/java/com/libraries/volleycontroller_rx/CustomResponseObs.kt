package com.libraries.volleycontroller_rx

import com.libraries.inlacou.volleycontroller.CustomResponse
import com.libraries.inlacou.volleycontroller.InternetCall
import com.libraries.inlacou.volleycontroller.VolleyController
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.*

class CustomResponseObs internal constructor(private val internetCall: InternetCall) : ObservableOnSubscribe<CustomResponse> {

	override fun subscribe(subscriber: ObservableEmitter<CustomResponse>) {
		///MainThreadSubscription.verifyMainThread();

		VolleyController.onCall(internetCall
				.addSuccessCallback { item, code ->
					if (!subscriber.isDisposed) subscriber.onNext(item)
					if (!subscriber.isDisposed) subscriber.onComplete()
				}
				.addErrorCallback { error, code ->
					if (!subscriber.isDisposed) subscriber.onError(error)
				})
		subscriber.setCancellable { VolleyController.cancelRequest(internetCall.cancelTag) }
	}

	companion object {

		fun create(internetCall: InternetCall?): Observable<CustomResponse> {
			if (internetCall == null) {
				throw NullPointerException()
			}
			return Observable.create(CustomResponseObs(internetCall))
		}

		fun from(internetCall: ArrayList<InternetCall>?): Observable<InternetCall> {
			if (internetCall == null) {
				throw NullPointerException()
			}
			return Observable.fromIterable(internetCall)
		}
	}
}