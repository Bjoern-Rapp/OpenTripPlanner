package org.opentripplanner.raptor.rangeraptor.debug;

import java.util.List;
import org.opentripplanner.raptor.api.path.RaptorPath;
import org.opentripplanner.raptor.api.request.DebugRequest;
import org.opentripplanner.raptor.rangeraptor.internalapi.WorkerLifeCycle;

final class DebugHandlerPathAdapter extends AbstractDebugHandlerAdapter<RaptorPath<?>> {

  DebugHandlerPathAdapter(DebugRequest debug, WorkerLifeCycle lifeCycle) {
    super(debug, debug.pathFilteringListener(), lifeCycle);
  }

  @Override
  protected int stop(RaptorPath<?> path) {
    return path.egressLeg().fromStop();
  }

  @Override
  protected List<Integer> stopsVisited(RaptorPath<?> path) {
    return path.listStops();
  }
}
