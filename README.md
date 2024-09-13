# TPL Maps SDK Sample App

Welcome to the **TPL Maps SDK Sample App**, a demonstration project built using Kotlin that showcases various features of the TPL Maps Android SDK. This app provides a comprehensive guide for developers to integrate and utilize TPL Maps in their Android applications. The sample includes features such as map rendering, gestures, routing, location search, and more.

## Features

This sample app demonstrates the following core features of TPL Maps:

### 1. **Maps** 
- Display the map view and manage map lifecycle events such as create, start, resume, pause, stop, and destroy.
- Sample code initializes a `MapView`, sets zoom and tilt parameters, and enables basic map controls.

```kotlin
mapController.uiSettings.showZoomControls(false)
mapController.setMyLocationEnabled(true, MapController.MyLocationArg.ZOOM_LOCATION_ON_FIRST_FIX)
```

### 2. **Map Features**
- Toggle night mode, building visibility, points of interest (POIs), live traffic, and more.
- Control map behavior using checkboxes for various settings, with immediate map updates.

```kotlin
cbNightMode.setOnCheckedChangeListener(this)
cbBuildings.setOnCheckedChangeListener(this)
cbTraffic.setOnCheckedChangeListener(this)
```

### 3. **UI Controls**
- Add built-in controls like compass, zoom buttons, and location tracking buttons.
- Allow users to easily interact with the map using these UI elements.

Here is an overview of the flow for adding UI controls (Compass, Zoom Controls, My Location, etc.) to the map using TPLMaps SDK. You can include this in your GitHub README file:

---

```kotlin
{
            R.id.cb_compass ->                 // Show compass
                if (mMapController != null) mMapController!!.uiSettings.showCompass(isChecked)

            R.id.cb_zoom_controls ->                 // Show zoom controls
                if (mMapController != null) mMapController!!.uiSettings.showZoomControls(isChecked)

            R.id.cb_my_location_updates -> try {
                if (mMapController != null) {
                    // Enable/Disable My Location
                    mMapController!!.setMyLocationEnabled(
                        isChecked,
                        MapController.MyLocationArg.ZOOM_LOCATION_ON_FIRST_FIX
                    )
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }

            R.id.cb_my_location_button -> try {
                // Show My Location Button
                if (mMapController != null) mMapController!!.uiSettings.showMyLocationButton(
                    isChecked
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
```




### 4. **Camera Controls**
- Manipulate the map camera by zooming to specific bounds or focusing on a location.
- Useful for guiding users to specific areas of interest or fitting content within the viewport.

```kotlin
  // Camera Change (ChangeStart, Change, ChangeEnd) listeners
        mapController.setOnCameraChangeStartedListener {
            Log.i(
                TAG,
                "Camera Change Started"
            )
        }
        mapController.setOnCameraChangeListener {
            Log.i(
                TAG,
                "Camera Changing"
            )
        }
        mapController.setOnCameraChangeEndListener { cameraPosition: CameraPosition ->
            Log.i(
                TAG,
                "Camera Change End: $cameraPosition"
            )
        }
```

### 5. **Map Gestures**
- Support a wide range of map gestures such as zooming, panning, rotating, and more.
- Detect clicks on the map, points of interest, or long presses.

```kotlin
mMapView.setOnMapClickListener {
            when (listenerType) {
            ListenerType.CLICK_SINGLE -> if (mMapController != null) {
                mMapController!!.setOnMapClickListener(if (register) MapController.OnMapClickListener { lngLat: LngLat ->
                    binding.tvListener.text = getString(R.string.click_map)
                    val text = roundDecimalsUpTo(lngLat.latitude, 4)
                        .toString() + ", " + roundDecimalsUpTo(lngLat.longitude, 4)
                    binding.tvValue.text = text
                } else null)
            }

            ListenerType.CLICK_DOUBLE -> if (mMapController != null) {
                mMapController!!.setOnMapDoubleClickListener(if (register) DoubleTapResponder { x: Float, y: Float ->
                    val lngLat = mMapController!!.screenPositionToLngLat(
                        PointF(x, y)
                    )
                    binding.tvListener.text = getString(R.string.click_double_map)
                    val text = roundDecimalsUpTo(lngLat.latitude, 4)
                        .toString() + ", " + roundDecimalsUpTo(lngLat.longitude, 4)
                    binding.tvValue.text = text
                    false
                } else null)
            }

            ListenerType.CLICK_LONG -> if (mMapController != null) {
                mMapController!!.setOnMapLongClickListener(if (register) LongPressResponder { x: Float, y: Float ->
                    val lngLat = mMapController!!.screenPositionToLngLat(
                        PointF(x, y)
                    )
                    binding.tvListener.text = getString(R.string.click_long_map)
                    val text = roundDecimalsUpTo(lngLat.latitude, 4)
                        .toString() + ", " + roundDecimalsUpTo(lngLat.longitude, 4)
                    binding.tvValue.text = text
                } else null)
            }

            ListenerType.PAN -> if (mMapController != null) {
                mMapController!!.setOnMapPanListener(if (register) object : PanResponder {
                    override fun onPan(
                        startX: Float,
                        startY: Float,
                        endX: Float,
                        endY: Float
                    ): Boolean {
                        binding.tvListener.text = getString(R.string.gesture_pan_map)
                        binding.tvValue.text = ""
                        return false
                    }

                    override fun onFling(
                        posX: Float,
                        posY: Float,
                        velocityX: Float,
                        velocityY: Float
                    ): Boolean {
                        binding.tvListener.text = getString(R.string.gesture_fling_map)
                        binding.tvValue.text = ""
                        return false
                    }
                } else null)
            }

            ListenerType.ROTATE -> if (mMapController != null) {
                mMapController!!.setOnMapRotateListener(if (register) RotateResponder { x: Float, y: Float, rotation: Float ->
                    binding.tvListener.text = getString(R.string.gesture_rotate_map)
                    val valueRotation = "Rotation: " + roundDecimalsUpTo(rotation.toDouble(), 2)
                    binding.tvValue.text = valueRotation
                    false
                } else null)
            }

            ListenerType.SCALE -> {}
            ListenerType.SHOVE -> {}
            ListenerType.POI -> if (mMapController != null) {
                mMapController!!.setOnPoiClickListener(if (register) MapController.OnPoiClickListener { place: PointOfInterest ->
                    val lngLat = mMapController!!.screenPositionToLngLat(
                        PointF(
                            place.lngLat.longitude.toFloat(),
                            place.lngLat.latitude.toFloat()
                        )
                    )
                    binding.tvListener.text = getString(R.string.click_poi)
                    val text = (place.name + ", " + roundDecimalsUpTo(lngLat.latitude, 4)
                            + ", " + roundDecimalsUpTo(lngLat.longitude, 4))
                    binding.tvValue.text = text
                } else null)
            }

            null -> TODO()
        }

}
```

### 6. **Search Location by Latitude/Longitude**
- Search for a location using geographic coordinates (latitude and longitude).
- Easily locate places programmatically.

```kotlin
mapController.searchByLatLng(latitude, longitude)
```

### 7. **Routing**
- Calculate and display routes between two locations using starting and ending latitude and longitude.
- Perfect for guiding users through directions or route planning.

```kotlin
mapController.calculateRoute(startLatLng, endLatLng)
```

### 8. **Info Windows**
- Display an info window when a marker is tapped, allowing users to view more details about locations.
- This is particularly useful for providing additional information when interacting with map markers.

```kotlin
mapController.showInfoWindow(marker)
```

### 9. **Map Styles**
- Customize the appearance of the map by applying different styles, such as night mode or traffic view.
- Tailor the visual experience of the map to suit your app's needs.

```kotlin
mapController.setMapMode(MapMode.NIGHT)
```

### 10. **Locate Me Demo**
- Enable user location tracking with zoom controls and an interactive "Locate Me" button.
- This feature lets users easily find their current position on the map and explore nearby locations.

```kotlin
mapController.setMyLocationEnabled(true)
```

## Screenshots

Below are screenshots corresponding to each feature:

1. **Maps** - Where the map is shown.
   ![Map View Screenshot](path_to_screenshot)

2. **Map Features** - Night Mode, Buildings, POIs, Live Traffic, etc.
   ![Map Features Screenshot](path_to_screenshot)

3. **UI Controls** - Compass, Zoom Controls, My Location, etc.
   ![UI Controls Screenshot](path_to_screenshot)

4. **Camera Controls** - Zooming the camera to specific bounds.
   ![Camera Controls Screenshot](path_to_screenshot)

5. **Map Gestures** - Various map gestures such as double-tap zoom, pan, rotate.
   ![Map Gestures Screenshot](path_to_screenshot)

6. **Search Location by Lat/Lng** - Locate a position using coordinates.
   ![Search Location Screenshot](path_to_screenshot)

7. **Routing** - Route calculation and display.
   ![Routing Screenshot](path_to_screenshot)

8. **Info Window** - Info window displayed on marker tap.
   ![Info Window Screenshot](path_to_screenshot)

9. **Map Style** - Apply different map styles like Night Mode.
   ![Map Style Screenshot](path_to_screenshot)

10. **Locate Me Demo** - Current location tracking with additional features.
    ![Locate Me Screenshot](path_to_screenshot)

## Getting Started

### Prerequisites

- Android Studio
- Kotlin 1.4+
- TPL Maps SDK

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Daniyal-AK/TPLMapsAndroidSdkSamples-Kotlin.git
   ```

2. Open the project in Android Studio.

3. Sync the project with Gradle.

4. Add your TPL Maps SDK key in the `AndroidManifest.xml` file:
   ```xml
        <meta-data
            android:name="@string/metadata_name_api_key"
            android:value=""/>
   ```

5. Run the app on an emulator or physical device.


