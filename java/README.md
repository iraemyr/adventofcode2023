# adventofcode2023

Requires JDK 27. Run commands from this directory using the Gradle wrapper
(`gradlew.bat` on Windows). Build and run all tests with `./gradlew clean build`.

All solutions use the same entry point. Run a day, one part, or all implemented days:

```sh
./gradlew run --args='--day 14'
./gradlew run --args='--day 14 --part 2'
./gradlew run --args='--day all'
./gradlew run --args='--list'
```

Each part prints its answer and one-shot elapsed time in milliseconds. The timer
includes input parsing and solving; input file loading, input array copying,
solution discovery, and printing happen outside the timer. The run ends with the
total measured time across the selected parts and days. These are cold, single-run timings. Use JMH for
measurements with warmup and separate JVM forks.

Solution classes live in `src/main/java/net/ddns/spellbank/dayXX/DayXX.java`.
Inputs are shared by the runner, benchmarks, and tests under
`src/main/resources/dayXX/`. Each day defaults to `input1` in its directory.
Samples use `test1`, `test2`, and so on. Select a bundled file within the day's
directory with `--resource`, or use `--input` for an external UTF-8 file:

```sh
./gradlew run --args='--day 14 --resource test1'
./gradlew run --args='--day 14 --input /path/to/input.txt'
```

Run JMH for a selected day (both parts by default), optionally selecting one part
and a bundled resource or external input:

```sh
./gradlew jmh -Pday=14
./gradlew jmh -Pday=14 -Ppart=2
./gradlew jmh -Pday=14 -Ppart=2 -Presource=test1
./gradlew jmh -Pday=14 -Ppart=2 -Pinput=/path/to/input.txt
```

JMH defaults to day 1, two forks, three one-second warmup iterations, and five
one-second measurement iterations per part. It reports average milliseconds per
operation and an error estimate. Input is loaded once per trial; a reusable array
is reset before each invocation, outside the measured method. Each invocation
parses its own data, including mutable grids and slabs. Input loading and array
copying are excluded, as in the normal runner. JMH consumes the returned
answer. Benchmark sources and dependencies are isolated in the `jmh` source set.

Override the defaults for a quick benchmark smoke test:

```sh
./gradlew jmh -Pday=14 -Ppart=2 -Presource=test1 -Pforks=1 -Pwarmup=1 -Piterations=1 -Ptime=100ms
```

The application can also run without Gradle. `./gradlew installDist` creates
`build/install/java/bin/java`; use the same arguments, for example
`build/install/java/bin/java --day 14`. Its bundled inputs work from inside JARs.

Initialize another day with `./gradlew initDay -Pd=24` (single-digit days are padded
automatically). This creates `net/ddns/spellbank/day24/Day24.java` and
`resources/day24/input1` under `src/main`, preserving existing files. The
template implements `harness.Solution`. The runner automatically finds
`net.ddns.spellbank.dayXX.DayXX` classes implementing this interface; no task or registry
entry is needed. Keep parsing in `solvePart1` and `solvePart2` so each invocation
starts fresh. The existing static algorithm methods remain available to tests.

Annotate each part with its verified answers. `input` defaults to `"input1"`;
repeat the same annotation to check samples or other inputs:

```java
@Override
@ExpectedAnswer(2913L)
@ExpectedAnswer(value = 8L, input = "test1")
public long solvePart1(String[] lines) {
    return part1(lines);
}
```

For `Day02`, these annotations check `day02/input1` against `2913` and
`day02/test1` against `8`. Add another annotation with `input = "test2"` for
another sample. An input is relative to the day's resource directory.

`RegressionTest` uses a JUnit test factory to discover every annotation and create
one named case per part and input, such as `Day02 part 1 [test1]`. Each case loads
fresh input, runs the part, and compares the result with the annotation. A wrong
answer or missing input fails the case. Parts without annotations have no cases
until their answers are known; zero is a valid expected answer. Samples can be
annotated before the real answer is known. No separate test file or answer table
is needed.

`./gradlew test` and `./gradlew build` run the generated regressions along with
the harness tests. Run just the annotated cases with
`./gradlew test --tests '*RegressionTest'`. Sample checks with extra parameters,
such as Day 11's expansion factor and Day 21's step counts, remain custom JUnit
tests; the annotation uses the same part behavior as the runner.

Download an input with `./gradlew getInput -Pd=XX`. Save your Advent of Code session
cookie in `cookie.txt` in this directory first. Downloads and scaffolding write
only to `src/main/resources/dayXX/input1`.
