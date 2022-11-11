package org.opentripplanner.graph_builder.module.osm.specifier;

import static org.opentripplanner.graph_builder.module.osm.specifier.Operation.MatchResult.EXACT;
import static org.opentripplanner.graph_builder.module.osm.specifier.Operation.MatchResult.NONE;
import static org.opentripplanner.graph_builder.module.osm.specifier.Operation.MatchResult.PARTIAL;

import javax.annotation.Nonnull;
import org.opentripplanner.openstreetmap.model.OSMWithTags;

public sealed interface Operation {
  @Nonnull
  static MatchResult getMatchResult(OSMWithTags way, String opKey, String opValue) {
    if (opValue.equals("*") && way.hasTag(opKey)) {
      return MatchResult.WILDCARD;
    } else if (way.matchesKeyValue(opKey, opValue)) {
      return EXACT;
    } else if (opValue.contains(":")) {
      // treat cases like cobblestone:flattened as cobblestone if a more-specific match
      // does not apply
      var splitValue = opValue.split(":", 2)[0];
      if (way.matchesKeyValue(opKey, splitValue)) {
        return MatchResult.PREFIX;
      } else {
        return NONE;
      }
    } else {
      return NONE;
    }
  }

  default boolean matches(OSMWithTags way) {
    return match(way) == EXACT;
  }

  boolean isWildcard();

  MatchResult match(OSMWithTags way);

  MatchResult matchLeft(OSMWithTags way);

  MatchResult matchRight(OSMWithTags way);

  enum MatchResult {
    EXACT,
    PREFIX,
    PARTIAL,
    WILDCARD,
    NONE;

    public MatchResult ifNone(MatchResult ifNone) {
      if (this == NONE) {
        return ifNone;
      } else {
        return this;
      }
    }

    public MatchResult ifExact(MatchResult ifExact) {
      if (this == EXACT) {
        return ifExact;
      } else {
        return this;
      }
    }
  }

  record Equals(String key, String value) implements Operation {
    @Override
    public boolean isWildcard() {
      return value.equals("*");
    }

    @Override
    public MatchResult match(OSMWithTags way) {
      return getMatchResult(way, key, value);
    }

    @Override
    public MatchResult matchLeft(OSMWithTags way) {
      var leftKey = key + ":left";
      return foo(way, leftKey);
    }

    @Override
    public MatchResult matchRight(OSMWithTags way) {
      var rightKey = key + ":right";
      return foo(way, rightKey);
    }

    private MatchResult foo(OSMWithTags way, String oneSideKey) {
      var oneSideResult = getMatchResult(way, oneSideKey, value);
      var mainRes = match(way).ifExact(PARTIAL);
      return oneSideResult.ifNone(mainRes);
    }
  }
}
