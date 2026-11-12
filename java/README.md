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
includes input parsing and solving; input file loading, solution discovery, and
printing happen outside the timer. The run ends with the total measured time across
the selected parts and days. These are cold, single-run timings. Use JMH for
measurements with warmup and separate JVM forks.

Inputs live only in `src/main/resources/dayXX/`, shared by the runner, benchmarks,
and tests. The default is `input1`. Select a bundled sample with `--resource`, or
use `--input` for an external UTF-8 file with a single day:

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
operation and an error estimate. Input is loaded once per trial; every invocation
gets a fresh input array and parses its own data, including mutable grids and
slabs. Input loading is excluded, as in the normal runner. JMH consumes the returned
answer. Benchmark sources and dependencies are isolated in the `jmh` source set.

Override the defaults for a quick benchmark smoke test:

```sh
./gradlew jmh -Pday=14 -Ppart=2 -Presource=test1 -Pforks=1 -Pwarmup=1 -Piterations=1 -Ptime=100ms
```

The application can also run without Gradle. `./gradlew installDist` creates
`build/install/java/bin/java`; use the same arguments, for example
`build/install/java/bin/java --day 14`. Its bundled inputs work from inside JARs.

Initialize another day with `./gradlew initDay -Pd=XX`, where `XX` has two digits
(for example, `01` or `24`). The template implements `harness.Solution`. The runner
automatically finds `net.ddns.spellbank.dayXX.DayXX` classes implementing this
interface; no task or registry entry is needed. Keep parsing in `solvePart1` and
`solvePart2` so each invocation starts fresh. The existing static algorithm methods
remain available to tests.

Test an individual day with `./gradlew test --tests '*TestDayXX'`.

Download an input with `./gradlew getInput -Pd=XX`. Save your Advent of Code session
cookie in `cookie.txt` in this directory first. Downloads and scaffolding write
only to the shared main resource directory.
