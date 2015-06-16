package kirimin.me.whoongithub.network

import android.content.Context

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

public object RequestQueueSingleton {

    private var queue: RequestQueue? = null

    public fun get(context: Context): RequestQueue {
        if (queue == null) {
            queue = Volley.newRequestQueue(context)
        }
        return queue!!
    }
}
