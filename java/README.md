# adventofcode2023

Requires JDK 27. Run commands from this directory using the Gradle wrapper
(`gradlew.bat` on Windows). Build and run all tests with `./gradlew clean build`.

Custom benchmarking, profiling, day scaffolding, and input download tasks live in
the script plugin [`gradle/advent-of-code.gradle`](gradle/advent-of-code.gradle).
When copying this project to another year, update `aocYear` in
[`gradle.properties`](gradle.properties). Input downloads use that year; it can
also be overridden for a command with `-PaocYear=2024`.

All solutions use the same entry point. Run a day, one part, or all implemented days:

```sh
./gradlew run --args='--day 14'
./gradlew run --args='--day 14 --part 2'
./gradlew run --args='--day all'
./gradlew run --args='--list'
```

Each part prints its answer and one-shot elapsed time in milliseconds. The timer
includes input parsing and solving; input file loading, splitting into lines,
input array copying, solution discovery, and printing happen outside the timer. The run ends with the
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
operation and an error estimate. Input is loaded once per trial; for array input,
a reusable array is reset before each invocation, outside the measured method.
String input is immutable and reused directly. Each invocation
parses its own data, including mutable grids and slabs. Input loading and array
copying are excluded, as in the normal runner. JMH consumes the returned
answer. Benchmark sources and dependencies are isolated in the `jmh` source set.

Override the defaults for a quick benchmark smoke test:

```sh
./gradlew jmh -Pday=14 -Ppart=2 -Presource=test1 -Pforks=1 -Pwarmup=1 -Piterations=1 -Ptime=100ms
```

Add `-Pjfr` to record a run or benchmark with Java Flight Recorder (JFR). No extra
Java dependencies are needed; recording support is included in JDK 27. All
commands below run from this `java` directory.

Use the runner to inspect startup or a complete solution run. The usual day,
part, and input options still apply:

```sh
./gradlew run -Pjfr --args='--day 14 --part 2'
./gradlew run -Pjfr --args='--day 14 --part 2 --resource test1'
./gradlew run -Pjfr --args='--day all'
```

The runner uses the JDK's `profile` settings and saves the whole application run,
including startup and input loading, to `build/reports/jfr/run.jfr` on JVM exit.
Each recorded run replaces that file. Very short runs may contain few CPU samples;
use JMH for longer recordings of repeated solution invocations.

Use JMH to investigate a particular part after JVM warmup. For example, this
records five two-second measurement iterations after three two-second warmup
iterations, using one benchmark JVM fork:

```sh
./gradlew jmh -Pjfr -Pday=14 -Ppart=2 -Pforks=1 -Ptime=2s
```

Add `-Presource=test1` for a bundled sample or `-Pinput=/path/to/input.txt` for an
external input. Omit `-Ppart` to profile both parts separately. The existing
`-Pwarmup`, `-Piterations`, and `-Ptime` overrides also work with JFR; `-Ptime`
sets the duration of each warmup and measurement iteration.

JMH uses its built-in JFR profiler with `profile` settings, recording measurement
iterations after warmup in the benchmark JVM. It prints the recording paths under
`build/reports/jfr/<benchmark-and-parameters>/profile.jfr`. Select one fork to keep
a single recording: multiple forks with the same parameters overwrite that path,
so only the last fork's recording remains. JFR requires at least one fork
(`-Pforks=0` is rejected). Copy recordings elsewhere before rerunning the same
benchmark if you want to compare versions.

The JMH recording covers activity throughout the measurement phase, including
invocation setup and JMH machinery. It is broader than the timed `solve` method:
input array copying remains excluded from benchmark timings, but may appear in
the profile.

For interactive analysis, install
[JDK Mission Control](https://www.oracle.com/java/technologies/jdk-mission-control.html)
separately from the JDK and open the recording with **File → Open File**. Start
with these views:

- **Method Profiling:** sort by sample count and expand stack traces to find
  expensive code under `net.ddns.spellbank`. Sample percentages estimate where
  execution time is spent; they are not exact method timings or invocation counts.
- **Memory:** inspect allocated classes and allocation sites for temporary object
  creation, then follow their stack traces back to the solution.
- **Garbage Collections:** check pause duration and frequency to see whether GC
  contributes to execution cost.
- **Automated Analysis Results:** review potential problems and inspect their
  supporting events before deciding what to optimize.

See the [Mission Control analysis guide](https://docs.oracle.com/en/java/java-components/jdk-mission-control/9/user-guide/using-jdk-flight-recorder.html)
for navigation and filtering details.

For terminal analysis, use the `jfr` tool from your JDK's `bin` directory. Set
`recording` to the runner output or to a JMH `profile.jfr` path printed by the
benchmark; quote paths that contain spaces:

```sh
recording=build/reports/jfr/run.jfr

jfr summary "$recording"
jfr view hot-methods "$recording"
jfr view allocation-by-site "$recording"
jfr view gc-pauses "$recording"
jfr print --events jdk.ExecutionSample "$recording"
```

`summary` shows recording duration and event counts. `hot-methods` highlights
frequently sampled methods, `allocation-by-site` groups sampled allocations by
their source, and `gc-pauses` lists garbage collection pauses. `print` shows the
individual CPU samples and their stack traces. If a view is empty, check the
event counts and collect a longer JMH recording; a short recording with no
samples does not establish that the solution has no hotspots or allocations.

If `jfr` is not on `PATH`, use `"$JAVA_HOME/bin/jfr"` with `JAVA_HOME` pointing to
your JDK 27 installation. On macOS you can locate it with `java_home`, for example:

```sh
JFR="$(/usr/libexec/java_home -v 27)/bin/jfr"
"$JFR" view hot-methods "$recording"
```

Recording adds profiling overhead, so collect baseline timings without `-Pjfr`.
After changing a solution, rerun the same benchmark with the same day, part, input,
and iteration settings to check whether timings improve.
Recordings are ignored by Git; `./gradlew clean` removes those under `build`.

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
@ExpectedAnswer("2913")
@ExpectedAnswer(value = "8", input = "test1")
public long solvePart1(String[] lines) {
    return part1(lines);
}
```

For `Day02`, these annotations check `day02/input1` against `2913` and
`day02/test1` against `8`. Add another annotation with `input = "test2"` for
another sample. An input is relative to the day's resource directory.

Expected answers are strings, so numeric and textual results use the same
annotation. For each part, implement exactly one of the numeric or string methods:

```java
@Override
@ExpectedAnswer("HELLO")
public String solvePart1Str(String[] lines) {
    return decodeMessage(lines);
}
```

Numeric parts implement `long solvePart1(...)` or `long solvePart2(...)`.
String-valued parts implement `String solvePart1Str(...)` or
`String solvePart2Str(...)`, leaving the numeric variant out entirely. By default,
the string method converts the numeric result with `Long.toString`. Numeric
defaults throw if called directly on a string-only part. The harness validates
that each part overrides exactly one variant and rejects both or neither.
Place expectations on the method you implement.

Parts can also accept a single `String` instead of `String[]`. Choose the input
type independently for each part, and implement exactly one of the four
combinations of input and answer types. For example:

```java
@Override
@ExpectedAnswer(value = "1320", input = "test1")
public long solvePart1(String input) {
    return hashSequence(input.stripTrailing());
}
```

String input contains the complete UTF-8 file, including blank lines, original
line endings, and any trailing newline. Trim or split it inside the solution as
needed. Array input keeps the existing behavior: one string per line, without
line terminators. Textual answers can use `String solvePart1Str(String input)`
or `String solvePart2Str(String input)` as well. Place `@ExpectedAnswer` on the
chosen overload; discovery, the runner, regression tests, and JMH use its input
type automatically. Day 15 demonstrates string input. Scaffolding continues to
default to arrays, which suit most days.

The runner and JMH select the implemented variant before timing. Numeric answers
are measured as primitive `long`; conversion for display happens after timing.
JMH consumes numeric results directly, without converting them to strings.

`RegressionTest` uses a JUnit test factory to discover every annotation and create
one named case per part and input, such as `Day02 part 1 [test1]`. Each case loads
fresh input, runs the part through its string entry point, and compares it with the
annotation exactly, including whitespace and case. A null result, wrong
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
