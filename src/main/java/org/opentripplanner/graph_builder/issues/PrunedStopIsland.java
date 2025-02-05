package org.opentripplanner.graph_builder.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.graph_builder.issue.api.OsmUrlGenerator;
import org.opentripplanner.street.model.vertex.Vertex;

public record PrunedStopIsland(
  Vertex vertex,
  int streetSize,
  int stopSize,
  int nothru,
  int restricted,
  int removed,
  String stopLabels
)
  implements DataImportIssue {
  private static String FMT =
    "Unlinked stops from pruned walk subgraph %s of %d street vertices and %d stops %s. Edge changes: %d to nothru, %d to no walking, %d erased";

  private static String HTMLFMT =
    "Unlinked stops from pruned walk <a href='http://www.openstreetmap.org/node/%s'>subgraph %s</a> of %d street vertices and %d stops %s. Edge changes: %d to nothru, %d to no walking, %d erased";

  @Override
  public String getMessage() {
    return String.format(
      FMT,
      vertex.getLabel(),
      this.streetSize,
      this.stopSize,
      this.stopLabels,
      this.nothru,
      this.restricted,
      this.removed
    );
  }

  @Override
  public String getHTMLMessage() {
    String label = vertex.getLabel();
    if (label.startsWith("osm:")) {
      String osmNodeId = label.split(":")[2];
      return String.format(
        HTMLFMT,
        osmNodeId,
        osmNodeId,
        this.streetSize,
        this.stopSize,
        this.stopLabels,
        this.nothru,
        this.restricted,
        this.removed
      );
    } else {
      return this.getMessage();
    }
  }

  @Override
  public int getPriority() {
    return streetSize + stopSize;
  }

  @Override
  public Vertex getReferencedVertex() {
    return vertex;
  }
}
