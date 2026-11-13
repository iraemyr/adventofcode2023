package net.ddns.spellbank.harness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class RegressionTest {
    @TestFactory
    List<DynamicTest> expectedAnswers() {
        var tests = new ArrayList<DynamicTest>();
        for (int day : Solutions.days()) {
            var solution = Solutions.forDay(day);
            for (int part = 1; part <= 2; part++) {
                var method = Solutions.partMethod(solution, part);
                for (var expected : method.getAnnotationsByType(ExpectedAnswer.class)) {
                    int selectedPart = part;
                    String name = String.format(Locale.ROOT, "Day%02d part %d [%s]", day, part, expected.input());
                    tests.add(dynamicTest(name, () -> {
                        var lines = Inputs.load(day, expected.input(), null);

                        String actual = solution.solve(selectedPart, lines);

                        assertNotNull(actual, "Solution returned no answer");
                        assertEquals(expected.value(), actual);
                    }));
                }
            }
        }

        return tests;
    }
}
