package com.distinctroutes.app.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.distinctroutes.app.domain.model.Route
import com.distinctroutes.app.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * UseCase to export route GPX data and create Intent choosers for navigation handoff and sharing.
 */
class ExportToGPXUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun createOsmAndIntent(route: Route): Intent {
        val gpxContent = route.rawGpx ?: generateSampleGpx(route)
        val gpxFile = File(context.cacheDir, "GPX/${Constants.GPX_EXPORT_PREFIX}${route.id}.gpx")
        gpxFile.parentFile?.mkdirs()
        gpxFile.writeText(gpxContent)

        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            gpxFile
        )

        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/gpx+xml")
            setPackage(Constants.OSMAND_NAVIGATOR_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun createNavigationShareIntent(route: Route): Intent {
        val gpxContent = route.rawGpx ?: generateSampleGpx(route)
        val gpxFile = File(context.cacheDir, "GPX/${Constants.GPX_EXPORT_PREFIX}${route.id}.gpx")
        gpxFile.parentFile?.mkdirs()
        gpxFile.writeText(gpxContent)

        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            gpxFile
        )

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/gpx+xml"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, route.name)
            putExtra(Intent.EXTRA_TEXT, "Distinct Routes GPX Route: ${route.name} (${"%.2f".format(route.distanceKm)} km)")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        return Intent.createChooser(sendIntent, "Export GPX / Navigate with...").apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun generateSampleGpx(route: Route): String {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <gpx version="1.1" creator="DistinctRoutes">
              <trk>
                <name>${route.name}</name>
                <trkseg>
                  <trkpt lat="-35.2809" lon="149.1300" />
                  <trkpt lat="-33.8688" lon="151.2093" />
                </trkseg>
              </trk>
            </gpx>
        """.trimIndent()
    }
}