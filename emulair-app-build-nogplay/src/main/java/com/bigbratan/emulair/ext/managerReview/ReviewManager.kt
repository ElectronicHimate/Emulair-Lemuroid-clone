package com.bigbratan.emulair.ext.managerReview

import android.app.Activity
import android.content.Context

// The real ReviewManager is implemented in the play version. This is basically stubbed.
class ReviewManager {
    suspend fun initialize(context: Context) {}

    suspend fun launchReviewFlow(activity: Activity, sessionTimeMillis: Long) {}
}
