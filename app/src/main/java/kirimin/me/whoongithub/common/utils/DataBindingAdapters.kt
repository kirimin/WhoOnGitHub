package kirimin.me.whoongithub.common.utils

import android.databinding.BindingAdapter
import android.widget.ImageView

import com.squareup.picasso.Picasso

object DataBindingAdapters {

    @BindingAdapter("src")
    @JvmStatic
    fun setImageViewResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, imageUrl: String?) {
        imageUrl ?: return
        Picasso.with(view.context).load(imageUrl).fit().into(view)
    }
}
