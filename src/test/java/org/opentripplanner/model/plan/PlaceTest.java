package org.opentripplanner.model.plan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.opentripplanner.model.FlexStopLocation;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.vertextype.SimpleVertex;
import org.opentripplanner.test.support.VariableSource;
import org.opentripplanner.transit.model._data.TransitModelForTest;
import org.opentripplanner.transit.model.basic.I18NString;
import org.opentripplanner.transit.model.basic.NonLocalizedString;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.site.Stop;

public class PlaceTest {

  @Test
  public void sameLocationBasedOnInstance() {
    Place aPlace = Place.normal(60.0, 10.0, new NonLocalizedString("A Place"));
    assertTrue(aPlace.sameLocation(aPlace), "same instance");
  }

  @Test
  public void sameLocationBasedOnCoordinates() {
    Place aPlace = Place.normal(60.0, 10.0, new NonLocalizedString("A Place"));
    Place samePlace = Place.normal(
      60.000000000001,
      10.0000000000001,
      new NonLocalizedString("Same Place")
    );
    Place otherPlace = Place.normal(65.0, 14.0, new NonLocalizedString("Other Place"));

    assertTrue(aPlace.sameLocation(samePlace), "same place");
    assertTrue(samePlace.sameLocation(aPlace), "same place(symmetric)");
    assertFalse(aPlace.sameLocation(otherPlace), "other place");
    assertFalse(otherPlace.sameLocation(aPlace), "other place(symmetric)");
  }

  @Test
  public void sameLocationBasedOnStopId() {
    var s1 = TransitModelForTest.stop("1").withCoordinate(1.0, 1.0).build();
    var s2 = TransitModelForTest.stop("2").withCoordinate(1.0, 2.0).build();

    Place aPlace = place(s1);
    Place samePlace = place(s1);
    Place otherPlace = place(s2);

    assertTrue(aPlace.sameLocation(samePlace), "same place");
    assertTrue(samePlace.sameLocation(aPlace), "same place(symmetric)");
    assertFalse(aPlace.sameLocation(otherPlace), "other place");
    assertFalse(otherPlace.sameLocation(aPlace), "other place(symmetric)");
  }

  static Stream<Arguments> flexStopCases = Stream.of(
    Arguments.of(null, "an intersection name"),
    Arguments.of(new NonLocalizedString("1:stop_id"), "an intersection name (part of 1:stop_id)"),
    Arguments.of(
      new NonLocalizedString("Flex Zone 123"),
      "an intersection name (part of Flex Zone 123)"
    )
  );

  @ParameterizedTest(name = "Flex stop name of {0} should lead to a place name of {1}")
  @VariableSource("flexStopCases")
  public void flexStop(I18NString stopName, String expectedPlaceName) {
    var stop = new FlexStopLocation(new FeedScopedId("1", "stop_id"), stopName);

    var vertex = new SimpleVertex(new Graph(), "corner", 1, 1) {
      @Override
      public I18NString getIntersectionName() {
        return new NonLocalizedString("an intersection name");
      }
    };

    var place = Place.forFlexStop(stop, vertex);

    assertEquals(expectedPlaceName, place.name.toString());
  }

  @Test
  public void acceptsNullCoordinates() {
    var p = Place.normal(null, null, new NonLocalizedString("Test"));
    assertNull(p.coordinate);
  }

  private static Place place(Stop stop) {
    return Place.forStop(stop);
  }
}
