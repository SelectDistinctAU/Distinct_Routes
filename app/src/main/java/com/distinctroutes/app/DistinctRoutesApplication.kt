package com.distinctroutes.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point for Hilt dependency injection initialization.
 */
@HiltAndroidApp
class DistinctRoutesApplication : Application()