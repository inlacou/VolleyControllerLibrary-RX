package com.libraries.volleycontrollerlibrary_rx;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libraries.inlacou.volleycontroller.CustomResponse;
import com.libraries.inlacou.volleycontroller.InternetCall;
import com.libraries.inlacou.volleycontroller.VolleyController;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by inlacou on 14/11/16.
 */
public class ApplicationController extends Application {

	private static final String DEBUG_TAG = ApplicationController.class.getName();
	private static ApplicationController ourInstance = new ApplicationController();
	
	public static ApplicationController getInstance() {
		return ourInstance;
	}
	
	public ApplicationController() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		VolleyController.INSTANCE.init(this, BuildConfig.DEBUG, new VolleyController.LogicCallbacks() {
			
			@Override
			public void setTokens(JSONObject jsonObject) {
			
			}
			
			@Override
			public void onRefreshTokenInvalid(VolleyError volleyError, String s) {
			
			}
			
			@Override
			public void onRefreshTokenExpired(VolleyError volleyError, String s) {
			
			}
			
			@NotNull
			@Override
			public InternetCall doRefreshToken(List<? extends Function2<? super CustomResponse, ? super String, Unit>> list, List<? extends Function2<? super VolleyError, ? super String, Unit>> list1) {
				return null;
			}
			
			@Nullable
			@Override
			public String getRefreshTokenInvalidMessage() {
				return null;
			}
			
			@Nullable
			@Override
			public String getRefreshTokenExpiredMessage() {
				return null;
			}
			
			@NotNull
			@Override
			public String getRefreshToken() {
				return null;
			}
			
			@NotNull
			@Override
			public String getCharset() {
				return VolleyController.CharSetNames.UTF_8.toString();
			}
			
			@Nullable
			@Override
			public String getAuthTokenExpiredMessage() {
				return null;
			}
			
			@NotNull
			@Override
			public String getAuthToken() {
				return null;
			}
		});
		VolleyController.INSTANCE.addInterceptor(new InternetCall.Interceptor() {
			@Override
			public void intercept(InternetCall internetCall) {
				internetCall
						.putHeader("deviceId", "5")
						.putParam("alwaysParam", "Hey! :D")
						.addSuccessCallback(new Function2<CustomResponse, String, Unit>() {
							@Override
							public Unit invoke(CustomResponse response, String code) {
								try {
									Toast.makeText(ApplicationController.this, response.getData().substring(0, 20) + "...", Toast.LENGTH_SHORT).show();
								}catch (IndexOutOfBoundsException ioobe){
									Toast.makeText(ApplicationController.this, response.getData().substring(0, response.getData().length()), Toast.LENGTH_SHORT).show();
								}
								return null;
							}
						})
						.addErrorCallback(new Function2<VolleyError, String, Unit>() {
							@Override
							public Unit invoke(VolleyError error, String code) {
								Log.d(DEBUG_TAG, "Code: " + code + " | VolleyError: " + error);
								return null;
							}
						});
			}
		});
	}
}
