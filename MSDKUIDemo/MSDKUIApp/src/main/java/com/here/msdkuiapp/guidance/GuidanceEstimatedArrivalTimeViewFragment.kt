package com.here.msdkuiapp.guidance

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewData
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewPresenter
import com.here.msdkui.guidance.GuidanceEstimatedArrivalView
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewListener
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for GuidanceEstimatedArrivalView.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceEstimatedArrivalViewFragment : Fragment(), GuidanceEstimatedArrivalViewListener {

    internal var viewPresenter : GuidanceEstimatedArrivalViewPresenter? = null;

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceEstimatedArrivalViewFragment()
    }

    /**
     * Creates view.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return GuidanceEstimatedArrivalView(activity).apply {
            id = R.id.guidanceEstimatedArrivalViewId
            unitSystem = Util.getLocaleUnit()
        }
    }

    /**
     * Creates Presenter for this [GuidanceEstimatedArrivalViewFragment].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewPresenter == null) {
            viewPresenter = GuidanceEstimatedArrivalViewPresenter(SingletonHelper.navigationManager).apply {
                addListener(this@GuidanceEstimatedArrivalViewFragment)
                resume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewPresenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        viewPresenter?.resume()
    }

    override fun onDataChanged(viewData: GuidanceEstimatedArrivalViewData?) {
        (view as? GuidanceEstimatedArrivalView)?.estimatedArrivalData = viewData
    }
}