package ru.tooloolooz.bumazhka.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(2)
@State(Scope.Thread)
public class VehicleRegionCodeBenchmark {

    private static final Pattern THREE_DIGIT_VEHICLE_CODE_PATTERN =
            Pattern.compile("^[1-9]\\d{2}$");

    private static final int THREE_DIGIT_CODE_LENGTH = 3;

    private static final Set<String> REGION_CODES = Set.of(
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "95", "21", "82", "22", "59", "81", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
            "36", "37", "38", "85", "39", "40", "42", "43", "44", "45", "46", "47", "48", "49", "50", "90", "51", "75",
            "80", "41", "23", "93", "24", "84", "88", "57", "58", "60", "61", "62", "63", "64", "65", "66", "96", "67",
            "68", "69", "70", "71", "72", "73", "74", "76", "52", "53", "54", "55", "56", "77", "97", "99", "78", "98",
            "92", "79", "83", "86", "87", "89", "94"
    );
    private List<String> validCodes;
    private List<String> invalidCodes;
    private List<String> mixedCodes;

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    private static boolean isValidManual(final String code) {
        final int length = code.length();
        if (length != THREE_DIGIT_CODE_LENGTH) {
            return false;
        }
        char firstChar = code.charAt(0);
        String regionCode = code.substring(1);
        return REGION_CODES.contains(regionCode) && '1' <= firstChar && firstChar <= '9';
    }

    public static boolean isValidPattern(final String code) {
        final int length = code.length();
        if (length != THREE_DIGIT_CODE_LENGTH) {
            return false;
        }
        String regionCode = code.substring(1);
        return THREE_DIGIT_VEHICLE_CODE_PATTERN.matcher(code).matches()
                && REGION_CODES.contains(regionCode);
    }

    @Setup
    public void setup() {
        validCodes = new ArrayList<>();
        for (String regionCode : REGION_CODES) {
            for (char firstDigit = '1'; firstDigit <= '9'; firstDigit++) {
                validCodes.add(firstDigit + regionCode);
            }
        }
        Collections.shuffle(validCodes);

        invalidCodes = List.of(
                "000", "0", "1234", "012", "0", "a12", "1a2", "12a", "100",
                "200", "300", "400", "500", "600", "700", "800", "900", "1010", "9999"
        );

        mixedCodes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                mixedCodes.add(validCodes.get(i % validCodes.size()));
            } else if (i % 3 == 1) {
                mixedCodes.add("1" + String.format("%02d", i % 100));
            } else {
                mixedCodes.add("0" + String.format("%02d", i % 100));
            }
        }
    }

    @Benchmark
    public void benchmarkManualVersion_validCodes(Blackhole bh) {
        for (String code : validCodes) {
            bh.consume(isValidManual(code));
        }
    }

    @Benchmark
    public void benchmarkPatternVersion_validCodes(Blackhole bh) {
        for (String code : validCodes) {
            bh.consume(isValidPattern(code));
        }
    }

    @Benchmark
    public void benchmarkManualVersion_invalidCodes(Blackhole bh) {
        for (String code : invalidCodes) {
            bh.consume(isValidManual(code));
        }
    }

    @Benchmark
    public void benchmarkPatternVersion_invalidCodes(Blackhole bh) {
        for (String code : invalidCodes) {
            bh.consume(isValidPattern(code));
        }
    }

    @Benchmark
    public void benchmarkManualVersion_mixedCodes(Blackhole bh) {
        for (String code : mixedCodes) {
            bh.consume(isValidManual(code));
        }
    }

    @Benchmark
    public void benchmarkPatternVersion_mixedCodes(Blackhole bh) {
        for (String code : mixedCodes) {
            bh.consume(isValidPattern(code));
        }
    }
}