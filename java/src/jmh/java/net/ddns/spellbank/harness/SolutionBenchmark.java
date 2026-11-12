package net.ddns.spellbank.harness;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
public class SolutionBenchmark {
    @Param({"1"})
    public int day;

    @Param({"1", "2"})
    public int part;

    @Param({"input1"})
    public String resource;

    @Param({""})
    public String inputFile;

    private Solution solution;
    private String[] lines;

    @Setup(Level.Trial)
    public void load() {
        if (part != 1 && part != 2) {
            throw new IllegalArgumentException("Part must be 1 or 2.");
        }

        solution = Solutions.forDay(day);
        lines = Inputs.load(day, resource, inputFile.isEmpty() ? null : Path.of(inputFile));
    }

    @Benchmark
    public long solve() {
        return solution.solve(part, lines.clone());
    }
}
