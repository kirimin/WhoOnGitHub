package kirimin.me.whoongithub.network.apis

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest

import org.json.JSONObject

import java.util.concurrent.ExecutionException

import kirimin.me.whoongithub.network.ApiRequestException
import rx.Observable
import rx.Subscriber

class ApiAccessor {
    companion object {

        fun request(requestQueue: RequestQueue, url: String): Observable<JSONObject> {
            return Observable.create<JSONObject>{ subscriber ->
                val future = RequestFuture.newFuture<JSONObject>()
                requestQueue.add<JSONObject>(JsonObjectRequest(url, null, future, future))
                try {
                    subscriber.onNext(future.get())
                    subscriber.onCompleted()
                } catch (e: InterruptedException) {
                    subscriber.onError(ApiRequestException(e.getMessage()))
                } catch (e: ExecutionException) {
                    subscriber.onError(ApiRequestException(e.getMessage()))
                }
            }
        }

        fun stringRequest(requestQueue: RequestQueue, url: String): Observable<String> {
            return Observable.create<String> { subscriber ->
                val future = RequestFuture.newFuture<String>()
                requestQueue.add<String>(StringRequest(url, future, future))
                try {
                    subscriber.onNext(future.get())
                    subscriber.onCompleted()
                } catch (e: InterruptedException) {
                    subscriber.onError(ApiRequestException(e.getMessage()))
                } catch (e: ExecutionException) {
                    subscriber.onError(ApiRequestException(e.getMessage()))
                }
            }
        }
    }
}