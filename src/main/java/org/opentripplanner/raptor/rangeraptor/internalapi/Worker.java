package org.opentripplanner.raptor.rangeraptor.internalapi;

import java.util.Collection;
import org.opentripplanner.raptor.api.model.RaptorTripSchedule;
import org.opentripplanner.raptor.api.path.RaptorPath;
import org.opentripplanner.raptor.api.response.StopArrivals;

/**
 * The worker performs the travel search. There are multiple implementations, even some that do not
 * return paths.
 *
 * @param <T> The TripSchedule type defined by the user of the raptor API.
 */
public interface Worker<T extends RaptorTripSchedule> {
  /**
   * Perform the routing request.
   *
   */
  void route();

  /**
   * Return all paths found. An empty set is returned if no paths are found or the algorithm does
   * not collect paths.
   */
  Collection<RaptorPath<T>> paths();

  /**
   * Return best over-all-arrival-times, best transit-arrival-times, and lowest number of
   * transfers for all stops.
   */
  StopArrivals stopArrivals();
}
