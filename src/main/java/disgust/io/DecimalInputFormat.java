package disgust.io;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Pure pt-BR decimal display conversion; the stored value keeps a dot separator. */
final class DecimalInputFormat {
    private DecimalInputFormat() {}

    static String normalize(String value) {
        if (value == null || value.isBlank()) return "";
        String cleaned = value.replaceAll("[^0-9,.]", "");
        int comma = cleaned.indexOf(',');
        if (comma >= 0) {
            String integer = cleaned.substring(0, comma).replaceAll("[^0-9]", "");
            String fraction = cleaned.substring(comma + 1).replaceAll("[^0-9]", "");
            return (integer.isEmpty() ? "0" : integer.replaceFirst("^0+(?!$)", "")) + "." + fraction;
        }
        return cleaned.replaceAll("[^0-9]", "").replaceFirst("^0+(?!$)", "");
    }

    static String display(String value) {
        if (value == null || value.isBlank()) return "";
        int dot = value.indexOf('.');
        String integer = dot >= 0 ? value.substring(0, dot) : value;
        String fraction = dot >= 0 ? value.substring(dot + 1) : "";
        StringBuilder grouped = new StringBuilder();
        for (int i = 0; i < integer.length(); i++) {
            if (i > 0 && (integer.length() - i) % 3 == 0) grouped.append('.');
            grouped.append(integer.charAt(i));
        }
        return dot >= 0 ? grouped + "," + fraction : grouped.toString();
    }

    static String fixedTwoFromInput(String value) {
        return fixedScaleFromInput(value, 2);
    }

    static String fixedScaleFromInput(String value, int scale) {
        if (value == null) return "";
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return "";
        return new BigDecimal(digits).movePointLeft(scale).setScale(scale).toPlainString();
    }

    static String fixedTwoFromStored(String value) {
        return fixedScaleFromStored(value, 2);
    }

    static String fixedScaleFromStored(String value, int scale) {
        if (value == null || value.isBlank()) return "";
        return new BigDecimal(value.replace(',', '.')).setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

}
