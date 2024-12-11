package se.askware.aoc2023.dec24;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final List<Trajectory> trajectories = parseTrajectories(input);
		System.out.println(trajectories);

		final List<Line> lines = trajectories.stream().map(line1 -> {
			final Point3D at = line1.at(100);
			final double m = (at.y - line1.base.y) / (double) (at.x - line1.base.x);
			final double b = line1.base.y - (m * line1.base.x);
			return new Line(BigDecimal.valueOf(m), BigDecimal.valueOf(b));
		}).toList();

		System.out.println(lines);

		BigDecimal min = BigDecimal.valueOf(200000000000000L);
		BigDecimal max = BigDecimal.valueOf(400000000000000L);
		//		BigDecimal min = BigDecimal.valueOf(7);
		//		BigDecimal max = BigDecimal.valueOf(27);

		int numIntersections = 0;
		for (int i = 0; i < lines.size(); i++) {
			Line l1 = lines.get(i);
			for (int j = i + 1; j < lines.size(); j++) {
				Line l2 = lines.get(j);
				try {
					BigDecimal x0 = (l1.b.subtract(l2.b)).divide(l2.m.subtract(l1.m), 5, RoundingMode.DOWN);
					BigDecimal y0 = ((l1.b.multiply(l2.m)).subtract(l2.b.multiply(l1.m)))
							.divide(l2.m.subtract(l1.m), 5, RoundingMode.DOWN);

					if (x0.compareTo(min) >= 0 && x0.compareTo(max) <= 0 &&
							y0.compareTo(min) >= 0 && y0.compareTo(max) <= 0) {
						final Trajectory trajectory = trajectories.get(i);
						final Trajectory trajectory2 = trajectories.get(j);
						//System.out.println(l1 + " " + l2);
						//System.out.println(trajectory + " " + trajectory2);
						if ((trajectory.velocity.x > 0 && x0.compareTo(BigDecimal.valueOf(trajectory.base.x)) < 0) ||
								(trajectory.velocity.x < 0 && x0.compareTo(BigDecimal.valueOf(trajectory.base.x)) > 0) ||
								(trajectory.velocity.y > 0 && y0.compareTo(BigDecimal.valueOf(trajectory.base.y)) < 0) ||
								(trajectory.velocity.y < 0 && y0.compareTo(BigDecimal.valueOf(trajectory.base.y)) > 0)) {
							System.out.println("in past (1)");
						} else if ((trajectory2.velocity.x > 0 && x0.compareTo(BigDecimal.valueOf(trajectory2.base.x)) < 0) ||
								(trajectory2.velocity.x < 0 && x0.compareTo(BigDecimal.valueOf(trajectory2.base.x)) > 0) ||
								(trajectory2.velocity.y > 0 && y0.compareTo(BigDecimal.valueOf(trajectory2.base.y)) < 0) ||
								(trajectory2.velocity.y < 0 && y0.compareTo(BigDecimal.valueOf(trajectory2.base.y)) > 0)) {
							System.out.println("in past (2)");
						} else {
							System.out.println(x0 + " " + y0);
							numIntersections++;
						}
					} else {
						System.out.println("outside");
					}

				} catch (Exception e) {
					System.out.println("parallell");
					//ignore
				}

			}
		}
		System.out.println(numIntersections);

	}

	record Line(BigDecimal m, BigDecimal b) {

		BigDecimal atqX(int x) {
			return m.multiply(BigDecimal.valueOf(x)).add(b);
		}

	}

	@Override
	public void solvePartTwo(List<String> input) {
		final List<Trajectory> trajectories = parseTrajectories(input);

		Set<Integer> possibleValuesX = getPossibleValues(trajectories, t -> t.velocity.x, t1 -> t1.base.x);
		Set<Integer> possibleValuesY = getPossibleValues(trajectories, t -> t.velocity.y, t1 -> t1.base.y);
		Set<Integer> possibleValuesZ = getPossibleValues(trajectories, t -> t.velocity.z, t1 -> t1.base.z);
		System.out.println(possibleValuesX);
		System.out.println(possibleValuesY);
		System.out.println(possibleValuesZ);

		int vx = possibleValuesX.iterator().next();
		int vy = possibleValuesY.iterator().next();
		int vz = possibleValuesZ.iterator().next();

		final Trajectory t1 = trajectories.get(0);
		final Trajectory t2 = trajectories.get(1);


		// simultaneous linear equation
		// H1:  x = A + a*t   y = B + b*t
		// H2:  x = C + c*u   y = D + d*u
		//
		//  t = [ d ( C - A ) - c ( D - B ) ] / ( a * d - b * c )
		//
		// Solve for origin of rock intercepting both hailstones in x,y:
		//     x = A + a*t - vx*t   y = B + b*t - vy*t
		//     x = C + c*u - vx*u   y = D + d*u - vy*u

		long A = t1.base.x, a = t1.velocity.x - vx;
		long B = t1.base.y, b = t1.velocity.y - vy;
		long C = t2.base.x, c = t2.velocity.x - vx;
		long D = t2.base.y, d = t2.velocity.y - vy;

		// Rock intercepts H1 at time t
		long t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c));

		// Calculate starting position of rock from intercept point
		long x = t1.base.x + t1.velocity.x * t - vx * t;
		long y = t1.base.y + t1.velocity.y * t - vy * t;
		long z = t1.base.z + t1.velocity.z * t - vz * t;

		System.out.println(x + " + " + y + " + " + z + " = " + (x+y+z));

	}

	private static List<Trajectory> parseTrajectories(List<String> input) {
		return input.stream().map(s -> {
			final String[] s1 = s.replace("@", ",").replace(" ", "").split(",");
			final Point3D point3D = new Point3D(Long.parseLong(s1[0]), Long.parseLong(s1[1]), Long.parseLong(s1[2]));
			final Point3D velocity = new Point3D(Long.parseLong(s1[3]), Long.parseLong(s1[4]), Long.parseLong(s1[5]));
			return new Trajectory(point3D, velocity);
		}).toList();
	}

	/**
	 * Two trajectories t1,t2 with common x-velocity can only be hit by a stone if the stone has a x-velocity vx that fulfills
	 *
	 * <code>(t1.x - t2.x) % (vx - t1.vx) == 0</code>
	 *
	 * Try finding a velocity that matches this for all examples of trajectories that has a common vx.
	 * Repeat same procedure to find vy and vz
	 */
	private static Set<Integer> getPossibleValues(List<Trajectory> trajectories, Function<Trajectory, Long> velocityExtract, Function<Trajectory, Long> positionExtract) {
		final Map<Long, List<Trajectory>> collect = trajectories.stream().collect(Collectors.groupingBy(velocityExtract));
		final List<List<Trajectory>> list = collect.values().stream().filter(l -> l.size() > 1).toList();

		final int velocity = 500;
		Set<Integer> possibleValues = new HashSet<>();
		for (int i = -velocity; i < velocity; i++) {
			possibleValues.add(i);
		}
		for (List<Trajectory> list1 : list) {
			final Trajectory trajectory1 = list1.get(0);
			final Trajectory trajectory2 = list1.get(1);

			long hailVelocity = velocityExtract.apply(trajectory1);
			final long distanceDifference = positionExtract.apply(trajectory1) - positionExtract.apply(trajectory2);
			for (int rockVelocity = -velocity; rockVelocity < velocity; rockVelocity++) {
				if ((rockVelocity - hailVelocity) == 0 || distanceDifference % (rockVelocity - hailVelocity) != 0) {
					possibleValues.remove(rockVelocity);
				}
			}
		}
		return possibleValues;
	}

	record Point3D(long x, long y, long z) {

	}

	record Trajectory(Point3D base, Point3D velocity) {
		Point3D at(long time) {
			final long x1 = base.x + (velocity.x * time);
			final long y1 = base.y + (velocity.y * time);
			final long z1 = base.z + (velocity.z * time);
			return new Point3D(x1, y1, z1);
		}

	}

}
