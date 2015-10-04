package kirimin.me.whoongithub.network

import android.content.Context

import com.android.volley.RequestQueue

public object RequestQueueSingleton {

    private var queue: RequestQueue? = null

    public fun get(context: Context): RequestQueue {
        if (queue == null) {
            queue = com.android.volley.toolbox.Volley.newRequestQueue(context)
        }
        return queue!!
    }
}
