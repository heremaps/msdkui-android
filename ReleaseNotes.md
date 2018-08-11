# Release Notes
## HERE Mobile SDK UI Kit Version 1.4.0 for iOS and Android
This document provides a summary of important changes for this version. Apart from newly resolved issues and added or improved functionality, this document also includes information about known issues remaining in this release as well as any existing limitations and workarounds.

### Highlights - What's New in Version 1.4.0?
- The HERE Mobile SDK UI Kit now supports scooter routing. Please, make sure to extend your license key if you want to include scooter routing in your app.
- Added support for guidance flows with a new UI component showing next maneuver instructions.
- Extended demo application to show production ready flows including guidance.
- Added example apps and a new user guide with in-depth explanations on how to use the new components.

### Functional and Behavioral Changes
- Due to several API changes in HERE Mobile SDK, the minimum supported HERE Mobile SDK for iOS and Android is increased to version 3.8.
- Please note, this version requires a map update (Map version: 84), therefore a re-installation of the demo app is recommended. Please delete the hidden folder `msdkui-data` on the root level of the internal storage to get rid of the old map data.
- The formely deprecated `ToolkitButton` is removed. Its usage in the demo app is replaced with the custom `IconButton` class, which is not part of the HERE Mobile SDK UI Kit itself.

### Resolved Issues
- Improved Turkish accessibility strings.

### Known Issues
- A few strings of the demo app are not yet localized and will appear in English only.
- A few strings of the UI Kit are not yet localized and will appear in English only.

### UI Kit UI Components Available in Version 1.4.0
- **Route Planner**: WaypointList, WaypointEntry, TransportModePanel, RouteOptionsPanel, RoutingOptionsPanel, RouteTypeOptionsPanel, TrafficOptionsPanel, TruckOptionsPanel, TunnelOptionsPanel, TravelTimePanel, TravelTimePicker
- **Route Summary**: RouteDescriptionList, RouteDescriptionItem
- **Route Maneuver**: ManeuverDescriptionList, ManeuverDescriptionItem
- **Guidance**: GuidanceManeuverPanel
- **Supporting Components**: ToolkitButton, MultipleChoiceOptionItem, NumericOptionItem, SingleChoiceOptionItem

## Supported platforms
Please see our [README](../../README.md#supported-platforms).
