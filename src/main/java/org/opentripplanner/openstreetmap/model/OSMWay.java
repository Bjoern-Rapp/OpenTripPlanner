package org.opentripplanner.openstreetmap.model;

import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;

public class OSMWay extends OSMWithTags {

  private final TLongList nodes = new TLongArrayList();

  public void addNodeRef(OSMNodeRef nodeRef) {
    nodes.add(nodeRef.getRef());
  }

  public void addNodeRef(long nodeRef, int index) {
    nodes.insert(index, nodeRef);
  }

  public TLongList getNodeRefs() {
    return nodes;
  }

  public String toString() {
    return "osm way " + id;
  }

  /**
   * Returns true if way geometry is a closed loop
   */
  public boolean isClosed() {
    int size = nodes.size();

    if (size > 2) {
      long a = nodes.get(0);
      long b = nodes.get(size - 1);
      return a == b;
    }
    return false;
  }

  /**
   * Returns true if way is both boarding location and closed polygon
   */
  public boolean isBoardingArea() {
    return isBoardingLocation() && isClosed();
  }

  /**
   * Returns true if bicycle dismounts are forced.
   */
  public boolean isBicycleDismountForced() {
    String bicycle = getTag("bicycle");
    return isTag("cycleway", "dismount") || "dismount".equals(bicycle);
  }

  /**
   * Returns true if these are steps.
   */
  public boolean isSteps() {
    return "steps".equals(getTag("highway"));
  }

  /**
   * Is this way a roundabout?
   */
  public boolean isRoundabout() {
    return "roundabout".equals(getTag("junction"));
  }

  /**
   * Returns true if this is a one-way street for driving.
   */
  public boolean isOneWayForwardDriving() {
    return isTagTrue("oneway");
  }

  /**
   * Returns true if this way is one-way in the opposite direction of its definition.
   */
  public boolean isOneWayReverseDriving() {
    return isTag("oneway", "-1");
  }

  /**
   * Returns true if bikes can only go forward.
   */
  public boolean isOneWayForwardBicycle() {
    String oneWayBicycle = getTag("oneway:bicycle");
    return isTrue(oneWayBicycle) || isTagFalse("bicycle:backwards");
  }

  /**
   * Returns true if bikes can only go in the reverse direction.
   */
  public boolean isOneWayReverseBicycle() {
    String oneWayBicycle = getTag("oneway:bicycle");
    return "-1".equals(oneWayBicycle) || isTagFalse("bicycle:forward");
  }

  /**
   * Returns true if bikes must use sidepath in forward direction
   */
  public boolean isForwardDirectionSidepath() {
    return "use_sidepath".equals(getTag("bicycle:forward"));
  }

  /**
   * Returns true if bikes must use sidepath in reverse direction
   */
  public boolean isReverseDirectionSidepath() {
    return "use_sidepath".equals(getTag("bicycle:backward"));
  }

  /**
   * Some cycleways allow contraflow biking.
   */
  public boolean isOpposableCycleway() {
    // any cycleway which is opposite* allows contraflow biking
    String cycleway = getTag("cycleway");
    String cyclewayLeft = getTag("cycleway:left");
    String cyclewayRight = getTag("cycleway:right");

    return (
      (cycleway != null && cycleway.startsWith("opposite")) ||
      (cyclewayLeft != null && cyclewayLeft.startsWith("opposite")) ||
      (cyclewayRight != null && cyclewayRight.startsWith("opposite"))
    );
  }

  @Override
  public String getOpenStreetMapLink() {
    return String.format("https://www.openstreetmap.org/way/%d", getId());
  }
}
