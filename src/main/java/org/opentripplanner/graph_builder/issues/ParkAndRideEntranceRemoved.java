package org.opentripplanner.graph_builder.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.routing.vehicle_parking.VehicleParkingEntrance;

public record ParkAndRideEntranceRemoved(VehicleParkingEntrance vehicleParkingEntrance)
  implements DataImportIssue {
  private static String FMT =
    "Park and ride entrance '%s' is removed because it's StreetVertex ('%s') is removed in a previous step.";

  @Override
  public String getMessage() {
    return String.format(
      FMT,
      vehicleParkingEntrance.getEntranceId().toString(),
      vehicleParkingEntrance.getVertex().getDefaultName()
    );
  }
}
