package com.distinctroutes.app.ui.route_planner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapViewContainer(
    startLat: Double,
    startLon: Double,
    endLat: Double,
    endLon: Double,
    modifier: Modifier = Modifier,
    viaWaypoints: List<Pair<Double, Double>> = emptyList(),
    routePoints: List<Pair<Double, Double>> = emptyList(),
    onStartPinMoved: (Double, Double) -> Unit = { _, _ -> },
    onEndPinMoved: (Double, Double) -> Unit = { _, _ -> },
    onViaPinMoved: (index: Int, Double, Double) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current

    val startIcon = remember(context) {
        createBbbikeStartPin(context)
    }

    val endIcon = remember(context) {
        createBbbikeEndPin(context)
    }

    val viaIcon = remember(context) {
        createBbbikeViaPin(context)
    }

    AndroidView(
        factory = { ctx ->
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
            Configuration.getInstance().userAgentValue = "DistinctRoutes/0.1.0 (Android)"

            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(13.0)
            }
        },
        onRelease = { map ->
            try {
                map.onPause()
            } catch (e: Exception) {
                // Non-blocking pause
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .semantics {
                contentDescription = "Interactive OpenStreetMap view showing Start, Destination, and optional Via Stop pins"
            },
        update = { map ->
            try {
                map.onResume()
            } catch (e: Exception) {
                // Ignore resume errors
            }

            map.overlays.clear()

            val startPoint = GeoPoint(startLat, startLon)
            val endPoint = GeoPoint(endLat, endLon)

            // Draw Route Polyline if points available
            if (routePoints.isNotEmpty()) {
                val geoPoints = routePoints.map { GeoPoint(it.first, it.second) }
                val polyline = Polyline().apply {
                    outlinePaint.color = Color.parseColor("#1565C0")
                    outlinePaint.strokeWidth = 14f
                    outlinePaint.strokeCap = Paint.Cap.ROUND
                    setPoints(geoPoints)
                }
                map.overlays.add(polyline)

                try {
                    val boundingBox = BoundingBox.fromGeoPoints(geoPoints)
                    map.zoomToBoundingBox(boundingBox, true, 80)
                } catch (e: Exception) {
                    map.controller.setCenter(startPoint)
                }
            } else {
                map.controller.setCenter(startPoint)
            }

            // Start Marker (Green Pin with Play Arrow ▶)
            val startMarker = Marker(map).apply {
                position = startPoint
                title = "Start Location (Play Arrow ▶)"
                snippet = "Lat: %.4f, Lon: %.4f".format(startLat, startLon)
                icon = startIcon
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                isDraggable = true
                setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                    override fun onMarkerDrag(marker: Marker) {}
                    override fun onMarkerDragStart(marker: Marker) {}
                    override fun onMarkerDragEnd(marker: Marker) {
                        onStartPinMoved(marker.position.latitude, marker.position.longitude)
                    }
                })
            }
            map.overlays.add(startMarker)

            // Multiple Via Markers (Yellow Pin with Dot ●)
            viaWaypoints.forEachIndexed { index, (vLat, vLon) ->
                val viaPoint = GeoPoint(vLat, vLon)
                val viaMarker = Marker(map).apply {
                    position = viaPoint
                    title = "Via Stop #${index + 1} (Yellow Dot ●)"
                    snippet = "Lat: %.4f, Lon: %.4f".format(vLat, vLon)
                    icon = viaIcon
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    isDraggable = true
                    setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                        override fun onMarkerDrag(marker: Marker) {}
                        override fun onMarkerDragStart(marker: Marker) {}
                        override fun onMarkerDragEnd(marker: Marker) {
                            onViaPinMoved(index, marker.position.latitude, marker.position.longitude)
                        }
                    })
                }
                map.overlays.add(viaMarker)
            }

            // End Marker (Red Pin with Stop Square ■)
            val endMarker = Marker(map).apply {
                position = endPoint
                title = "Destination Location (Stop Square ■)"
                snippet = "Lat: %.4f, Lon: %.4f".format(endLat, endLon)
                icon = endIcon
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                isDraggable = true
                setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                    override fun onMarkerDrag(marker: Marker) {}
                    override fun onMarkerDragStart(marker: Marker) {}
                    override fun onMarkerDragEnd(marker: Marker) {
                        onEndPinMoved(marker.position.latitude, marker.position.longitude)
                    }
                })
            }
            map.overlays.add(endMarker)

            // Map click listener for quick pin selection
            val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    p?.let {
                        onEndPinMoved(it.latitude, it.longitude)
                    }
                    return true
                }
                override fun longPressHelper(p: GeoPoint?): Boolean {
                    p?.let {
                        onStartPinMoved(it.latitude, it.longitude)
                    }
                    return true
                }
            })
            map.overlays.add(eventsOverlay)

            map.invalidate()
        }
    )
}

/**
 * BBBike Green Start Pin with Play Arrow (▶) symbol.
 */
private fun createBbbikeStartPin(context: Context): BitmapDrawable {
    val density = context.resources.displayMetrics.density
    val width = (42 * density).toInt()
    val height = (54 * density).toInt()
    val radius = 18 * density

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val greenColor = Color.parseColor("#2E7D32")

    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = greenColor
        style = Paint.Style.FILL
    }
    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3 * density
    }

    val centerX = width / 2f
    val centerY = radius + 3 * density

    // Circular Pin Head & Teardrop Tip
    canvas.drawCircle(centerX, centerY, radius, fillPaint)
    canvas.drawCircle(centerX, centerY, radius, strokePaint)

    val path = Path().apply {
        moveTo(centerX - radius * 0.5f, centerY + radius * 0.7f)
        lineTo(centerX + radius * 0.5f, centerY + radius * 0.7f)
        lineTo(centerX, height - 2 * density)
        close()
    }
    canvas.drawPath(path, fillPaint)

    // Inner White Circle
    val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, centerY, radius * 0.55f, whitePaint)

    // Green Play Arrow (▶)
    val playPath = Path().apply {
        val arrowSize = radius * 0.32f
        moveTo(centerX - arrowSize * 0.4f, centerY - arrowSize)
        lineTo(centerX + arrowSize * 0.7f, centerY)
        lineTo(centerX - arrowSize * 0.4f, centerY + arrowSize)
        close()
    }
    canvas.drawPath(playPath, fillPaint)

    return BitmapDrawable(context.resources, bitmap)
}

/**
 * BBBike Yellow Via Pin with Dot (●) symbol.
 */
private fun createBbbikeViaPin(context: Context): BitmapDrawable {
    val density = context.resources.displayMetrics.density
    val width = (42 * density).toInt()
    val height = (54 * density).toInt()
    val radius = 18 * density

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val yellowColor = Color.parseColor("#F57F17")

    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = yellowColor
        style = Paint.Style.FILL
    }
    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3 * density
    }

    val centerX = width / 2f
    val centerY = radius + 3 * density

    // Circular Pin Head & Teardrop Tip
    canvas.drawCircle(centerX, centerY, radius, fillPaint)
    canvas.drawCircle(centerX, centerY, radius, strokePaint)

    val path = Path().apply {
        moveTo(centerX - radius * 0.5f, centerY + radius * 0.7f)
        lineTo(centerX + radius * 0.5f, centerY + radius * 0.7f)
        lineTo(centerX, height - 2 * density)
        close()
    }
    canvas.drawPath(path, fillPaint)

    // Inner White Circle
    val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, centerY, radius * 0.55f, whitePaint)

    // Yellow Dot (●)
    canvas.drawCircle(centerX, centerY, radius * 0.32f, fillPaint)

    return BitmapDrawable(context.resources, bitmap)
}

/**
 * BBBike Red Destination Pin with Stop Square (■) symbol.
 */
private fun createBbbikeEndPin(context: Context): BitmapDrawable {
    val density = context.resources.displayMetrics.density
    val width = (42 * density).toInt()
    val height = (54 * density).toInt()
    val radius = 18 * density

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val redColor = Color.parseColor("#C62828")

    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = redColor
        style = Paint.Style.FILL
    }
    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3 * density
    }

    val centerX = width / 2f
    val centerY = radius + 3 * density

    // Circular Pin Head & Teardrop Tip
    canvas.drawCircle(centerX, centerY, radius, fillPaint)
    canvas.drawCircle(centerX, centerY, radius, strokePaint)

    val path = Path().apply {
        moveTo(centerX - radius * 0.5f, centerY + radius * 0.7f)
        lineTo(centerX + radius * 0.5f, centerY + radius * 0.7f)
        lineTo(centerX, height - 2 * density)
        close()
    }
    canvas.drawPath(path, fillPaint)

    // Inner White Circle
    val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, centerY, radius * 0.55f, whitePaint)

    // Red Stop Square (■)
    val squareSize = radius * 0.32f
    val squareRect = RectF(
        centerX - squareSize,
        centerY - squareSize,
        centerX + squareSize,
        centerY + squareSize
    )
    canvas.drawRoundRect(squareRect, 2 * density, 2 * density, fillPaint)

    return BitmapDrawable(context.resources, bitmap)
}