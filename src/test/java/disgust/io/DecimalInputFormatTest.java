package disgust.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecimalInputFormatTest {
    @Test void groupsThousandsWithoutChangingTheStoredDecimal() {
        assertEquals("1.234,50", DecimalInputFormat.display("1234.50"));
        assertEquals("1234.50", DecimalInputFormat.normalize("1.234,50"));
        assertEquals("1.234", DecimalInputFormat.display(DecimalInputFormat.normalize("1234")));
        assertEquals("12345", DecimalInputFormat.normalize("1.2345"));
    }

    @Test void ignoresLettersAndExtraSeparators() {
        assertEquals("12.345,67", DecimalInputFormat.display(DecimalInputFormat.normalize("R$ 12.345,6x7")));
        assertEquals("0.", DecimalInputFormat.normalize(","));
    }

    @Test void shiftsDigitsForFixedTwoFields() {
        assertEquals("3,33", DecimalInputFormat.display(DecimalInputFormat.fixedTwoFromInput("333")));
        assertEquals("33,33", DecimalInputFormat.display(DecimalInputFormat.fixedTwoFromInput("3,333")));
        assertEquals("3.25", DecimalInputFormat.fixedTwoFromStored("3.25"));
    }

    @Test void preservesThreeDecimalPlacesForMeasurements() {
        assertEquals("2,130", DecimalInputFormat.display(DecimalInputFormat.fixedScaleFromInput("2130", 3)));
        assertEquals("2.130", DecimalInputFormat.fixedScaleFromStored("2.130", 3));
    }
}
