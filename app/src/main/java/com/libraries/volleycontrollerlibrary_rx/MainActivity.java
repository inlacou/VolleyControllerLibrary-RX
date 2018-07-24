package com.libraries.volleycontrollerlibrary_rx;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libraries.inlacou.volleycontroller.CustomResponse;
import com.libraries.inlacou.volleycontroller.InternetCall;
import com.libraries.inlacou.volleycontroller.VolleyController;
import com.libraries.volleycontroller_rx.CustomResponseObs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private TextView textView;
	private static final String DEBUG_TAG = MainActivity.class.getName();
	private Disposable getEachSecond, postEachSecond;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		textView = findViewById(R.id.textView);

		ArrayList<InternetCall> internetCalls = new ArrayList<>();
		internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=0&limit=1").setCode("Code 1"));
		internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=0&limit=1").setCode("Code 2"));
		internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=0&limit=1").setCode("Code 3"));
		internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=0&limit=1").setCode("Code 4"));

		Observable.fromIterable(internetCalls)
				.subscribe(new Observer<InternetCall>() {
					@Override
					public void onComplete() {
						Log.d(DEBUG_TAG+".1"+".subscribe", "--------- onComplete");
					}

					@Override
					public void onError(Throwable e) {
						Log.e(DEBUG_TAG+".1"+".subscribe", e.getMessage());
						textView.setText(e.getMessage());
					}
					
					@Override
					public void onSubscribe(Disposable d) {
						
					}
					
					@Override
					public void onNext(InternetCall response) {
						Log.d(DEBUG_TAG+".1"+".subscribe", "Response: " + response.getCode());
						textView.setText(response.getCode());
					}
				});

		Observable.fromIterable(internetCalls)
				.map(new Function<InternetCall, InternetCall>() {
					@Override
					public InternetCall apply(@NonNull InternetCall item) {
						Log.d(DEBUG_TAG+".2"+".map", item.getCode());
						item.setCode(item.getCode()+" modified");
						return item;
					}
				})
				.subscribe(new Observer<InternetCall>() {
					@Override
					public void onComplete() {
						Log.d(DEBUG_TAG+".2"+".subscribe", "--------- onComplete");
					}

					@Override
					public void onError(Throwable e) {
						Log.e(DEBUG_TAG+".2"+".subscribe", e.getMessage());
						textView.setText(e.getMessage());
					}
					
					@Override
					public void onSubscribe(Disposable d) {
						
					}
					
					@Override
					public void onNext(InternetCall response) {
						Log.d(DEBUG_TAG+".2"+".subscribe", "Response: " + response.getCode());
						textView.setText(response.getCode());
					}
				});
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void doGet(){
		CustomResponseObs.Companion.create(new InternetCall()
				.setUrl("http://playground.byvapps.cm/api/search?offset=0&limit=100")
				.setMethod(InternetCall.Method.GET)
				.setCode("code_get_rx"))
				// default Scheduler is Computation
				.observeOn(AndroidSchedulers.mainThread())
				//Example map response
				.map(new Function<CustomResponse, String>() {
					@Override
					public String apply(CustomResponse item) {
						return item.getData();
					}
				})
				.subscribe(new Observer<String>() {
					@Override
					public void onComplete() {
						Log.d(DEBUG_TAG, "--------- onComplete");
					}

					@Override
					public void onError(Throwable e) {
						Log.e(DEBUG_TAG, e.getMessage());
						textView.setText(e.getMessage());
					}
					
					@Override
					public void onSubscribe(Disposable d) {
						
					}
					
					@Override
					public void onNext(String response) {
						Log.d(DEBUG_TAG, "Response: " + response);
						textView.setText(response);
					}
				});
	}

	public void doPost(){
		VolleyController.INSTANCE.onCall(new InternetCall()
				.setUrl("http://jsonplaceholder.typicode.com/posts")
				.setMethod(InternetCall.Method.POST)
				.putParam("title", "foo")
				.putParam("body", "bar")
				.putParam("userId", "5")
				.putParam("null", null)
				.putParam("notNull", "something")
				.setCode("code_create_posts")
				.addSuccessCallback(new Function2<CustomResponse, String, Unit>() {
					@Override
					public Unit invoke(CustomResponse response, String code) {
						Log.d(DEBUG_TAG, "Code: " + code + " | CustomResponse: " + response);
						textView.setText(response.getData());
						return null;
					}
				})
				.addErrorCallback(new Function2<VolleyError, String, Unit>() {
					@Override
					public Unit invoke(VolleyError error, String code) {
						Log.d(DEBUG_TAG, "Code: " + code + " | CustomResponse: " + error);
						textView.setText(VolleyController.INSTANCE.getMessage(error));
						return null;
					}
				}));
				
	}

	public void doPut(){
		HashMap<String, String> params = new HashMap<>();
		params.put("id", "1");
		params.put("title", "foo");
		params.put("null", null);
		params.put("notNull", "something");
		params.put("body", "bar");
		params.put("userId", "1");
		VolleyController.INSTANCE.onCall(new InternetCall()
				.setUrl("http://jsonplaceholder.typicode.com/posts/1")
				.setMethod(InternetCall.Method.PUT)
				.putParams(params)
				.setCode("code_modify_post")
				.addSuccessCallback(new Function2<CustomResponse, String, Unit>() {
					@Override
					public Unit invoke(CustomResponse response, String code) {
						Log.d(DEBUG_TAG, "Code: " + code + " | CustomResponse: " + response);
						textView.setText(response.getData());
						return null;
					}
				})
				.addErrorCallback(new Function2<VolleyError, String, Unit>() {
					@Override
					public Unit invoke(VolleyError error, String code) {
						Log.d(DEBUG_TAG, "Code: " + code + " | CustomResponse: " + error);
						textView.setText(VolleyController.INSTANCE.getMessage(error));
						return null;
					}
				}));
	}

	public void doDelete(){
		VolleyController.INSTANCE.onCall(new InternetCall()
				.setUrl("http://jsonplaceholder.typicode.com/posts/1")
				.setMethod(InternetCall.Method.DELETE)
				.setCode("code_delete_post")
				.addSuccessCallback(new Function2<CustomResponse, String, Unit>() {
					@Override
					public Unit invoke(CustomResponse response, String code) {
						Log.d(DEBUG_TAG, "Code: " + code + " | CustomResponse: " + response);
						textView.setText(response.getData());
						return null;
					}
				})
				.addErrorCallback(new Function2<VolleyError, String, Unit>() {
					@Override
					public Unit invoke(VolleyError error, String code) {
						Log.d(DEBUG_TAG, "Code: " + code + " | CustomResponse: " + error);
						textView.setText(VolleyController.INSTANCE.getMessage(error));
						return null;
					}
				}));
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_GET) {
			doGet();
		} else if (id == R.id.nav_POST) {
			doPost();
		} else if (id == R.id.nav_PUT) {
			doPut();
		} else if (id == R.id.nav_DELETE) {
			doDelete();
		} else if (id == R.id.nav_get_start) {
			Observable.interval(5, TimeUnit.SECONDS)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<Long>() {
						@Override
						public void onComplete() {
							Log.d(DEBUG_TAG, "--------- onComplete");
						}

						@Override
						public void onError(Throwable e) {
							Log.e(DEBUG_TAG, e.getMessage());
							textView.setText(e.getMessage());
						}
						
						@Override
						public void onSubscribe(Disposable d) {
							getEachSecond = d;
						}
						
						@Override
						public void onNext(Long response) {
							Log.d(DEBUG_TAG, "Response: " + response);
							doGet();
						}
					});
		} else if (id == R.id.nav_get_stop) {
			if(getEachSecond!=null && !getEachSecond.isDisposed()) getEachSecond.dispose();
		} else if (id == R.id.nav_post_start) {
			Observable.interval(10, TimeUnit.SECONDS)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<Long>() {
						@Override
						public void onComplete() {
							Log.d(DEBUG_TAG, "--------- onComplete");
						}

						@Override
						public void onError(Throwable e) {
							Log.e(DEBUG_TAG, e.getMessage());
							textView.setText(e.getMessage());
						}
						
						@Override
						public void onSubscribe(Disposable d) {
							postEachSecond = d;
						}
						
						@Override
						public void onNext(Long response) {
							Log.d(DEBUG_TAG, "Response: " + response);
							doPost();
						}
					});
		} else if (id == R.id.nav_post_stop) {
			if(postEachSecond!=null && !postEachSecond.isDisposed()) postEachSecond.dispose();
		} else if (id == R.id.nav_multiple_calls_at_once){
			Toast.makeText(this, "WARNING! Flatmap may cause unordered results", Toast.LENGTH_SHORT).show();
			ArrayList<InternetCall> internetCalls = new ArrayList<>();
			internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=0&limit=1").setCode("Code 1"));
			internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=1&limit=1").setCode("Code 2"));
			internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=2&limit=1").setCode("Code 3"));
			internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset=3&limit=1").setCode("Code 4"));
			textView.setText("");
			Observable.fromIterable(internetCalls)
					.flatMap(new Function<InternetCall, ObservableSource<CustomResponse>>() {
						@Override
						public Observable<CustomResponse> apply(InternetCall item) {
							Log.d(DEBUG_TAG+".flat"+".flatMap", item.getCode());
							return CustomResponseObs.Companion.create(item);
						}
					})
					.subscribe(new Observer<CustomResponse>() {
						@Override
						public void onComplete() {
							Log.e(DEBUG_TAG+".flat"+".subscribe", "--------- onComplete");
						}

						@Override
						public void onError(Throwable e) {
							Log.e(DEBUG_TAG+".flat"+".subscribe", e.getMessage());
							textView.setText(e.getMessage());
						}
						
						@Override
						public void onSubscribe(Disposable d) {
							
						}
						
						@Override
						public void onNext(CustomResponse response) {
							Log.d(DEBUG_TAG+".flat"+".subscribe", "Response: " + response.getData());
							textView.setText(textView.getText() + "\n\n" + response.getData());
						}
					});
		} else if (id == R.id.nav_multiple_calls_at_once_ordered){
			Toast.makeText(this, "ConcatMap should give ordered and consistent results", Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "But only gives the last one", Toast.LENGTH_LONG).show();
			ArrayList<InternetCall> internetCalls = new ArrayList<>();

			for (int i=0; i<10; i++) {
				internetCalls.add(new InternetCall().setUrl("http://playground.byvapps.com/api/search?offset="+i+"&limit=1").setCode("Code "+i));
			}

			textView.setText("");
			Observable.fromIterable(internetCalls)
					.switchMap(new Function<InternetCall, ObservableSource<CustomResponse>>() {
						@Override
						public Observable<CustomResponse> apply(InternetCall item) {
							Log.d(DEBUG_TAG+".concat"+".concatMap", item.getCode());
							return CustomResponseObs.Companion.create(item);
						}
					})
					.subscribe(new Observer<CustomResponse>() {
						@Override
						public void onComplete() {
							Log.e(DEBUG_TAG+".concat"+".subscribe", "--------- onComplete");
						}

						@Override
						public void onError(Throwable e) {
							Log.e(DEBUG_TAG+".concat"+".subscribe", e.getMessage());
							textView.setText(e.getMessage());
						}
						
						@Override
						public void onSubscribe(Disposable d) {
							
						}
						
						@Override
						public void onNext(CustomResponse response) {
							Log.d(DEBUG_TAG+".concat"+".subscribe", "Response: " + response.getData());
							textView.setText(textView.getText() + "\n\n" + response.getData());
						}
					});
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
