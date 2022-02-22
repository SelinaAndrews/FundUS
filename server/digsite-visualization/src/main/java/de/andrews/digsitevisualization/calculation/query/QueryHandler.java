package de.andrews.digsitevisualization.calculation.query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.andrews.digsitevisualization.SessionData;
import de.andrews.digsitevisualization.repository.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryHandler.class);
    private static final String AND = "AND";
    private static final String OR = "OR";


    public static String[] applyQuery(String query, List<Measurement> measurements) throws QueryParsingException {
        if (measurements.size() > 0) {
            CustomQuery queryObject = parseQuery(query);

            List<QueryTuple> parts = new ArrayList<>();
            parts.add(new QueryTuple(null, applySubQuery(queryObject.getColumn(), queryObject.getValues(), measurements)));
            for (CustomQueryPart cqp: queryObject.getAdditionalParts()) {
                parts.add(new QueryTuple(cqp.getOperator(), applySubQuery(cqp.getColumn(), cqp.getValues(), measurements)));
            }

            logger.info("Extraxted following query parts: " + parts);
            return applySetOperations(parts);
        } else {
            return new String[]{};
        }

    }

    private static List<String> applySubQuery(String column, List<String> values, List<Measurement> currentMeasurements) throws QueryParsingException {
        List<String> ids = new ArrayList<>();
        Field[] fields = Measurement.class.getDeclaredFields();
        List<String> fieldNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
        values = values.stream().map( val -> val.substring(1, val.length()-1)).collect(Collectors.toList());

        for (Measurement ms: currentMeasurements) {
            Map<String, String> unmappedFields = ms.getRemainingData();
            String fieldValue = null;
            if (unmappedFields.containsKey(column)) {
                fieldValue = unmappedFields.get(column);
            } else if (fieldNames.stream().anyMatch(f -> f.equals(column))) {
                try {
                    Method method = Measurement.class.getDeclaredMethod("get" + StringUtils.capitalize(column));
                    fieldValue = (String) method.invoke(ms);
                } catch (Exception e) {
                    throw new QueryParsingException(e.getMessage());
                }
            }

            String checkedField = checkFieldValue(ms, fieldValue, values);
            if (checkedField != null) {
                ids.add(checkedField);
            }
        }
        return ids;
    }

    public static String checkFieldValue(Measurement ms, String fieldValue, List<String> values) {
        String result = null;

        if (values.stream().anyMatch(val -> {
            if (val.startsWith("not:")) {
                String shortenedVal = val.substring(val.indexOf(":")+1);
                return !shortenedVal.equals(fieldValue);
            } else  {
                return val.equals(fieldValue);
            }
        })) {
            result = ms.getFindingNumber();
        }
        return result;
    }

    public static String[] applySetOperations(List<QueryTuple> parts) throws QueryParsingException {
        String[] result = parts.get(0).getIds().toArray(new String[0]);
        for (int i = 0; i < parts.size(); i++) {
            QueryTuple currentPart = parts.get(i);
            if (currentPart.getOperator() != null && i > 0) {
                result = setOperation(currentPart.getOperator(), parts.get(i - 1).getIds().toArray(new String[0]), currentPart.getIds().toArray(new String[0]));
            }
        }
        return result;
    }

    public static CustomQuery parseQuery(String query) {
        if (query.endsWith(" ")) {
            query = query.substring(0, query.length()-1);
        }
        List<String> splittedQuery = new ArrayList<>();
        List<String> finalParts = new ArrayList<>();
        int lastCut = 0;
        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == ' ' || i == query.length()-1) {
                if (i == query.length()-1) {
                    splittedQuery.add(query.substring(lastCut));
                } else {
                    splittedQuery.add(query.substring(lastCut, i));
                }
                lastCut = i+1;
            }
        }

        boolean inBrackets = false;
        StringBuilder formedString = new StringBuilder();
        for (String currentString : splittedQuery) {
            if (currentString.startsWith("'") && !currentString.endsWith("'")) {
                inBrackets = true;
                formedString.append(currentString);
            } else if (inBrackets) {
                formedString.append(" ").append(currentString);
                if (currentString.endsWith("'")) {
                    inBrackets = false;
                    finalParts.add(formedString.toString());
                    formedString = new StringBuilder();
                }
            } else {
                finalParts.add(currentString);
            }
        }

        List<String> values = new ArrayList<>();
        List<CustomQueryPart> additionalParts = new ArrayList<>();
        int endOfFirstPart = 0;

        String column = null;
        for (int i = 0; i < finalParts.size(); i++) {
            String currentString = finalParts.get(i);
            if (i == 0) {
                column = currentString;
            } else if (currentString.startsWith("'") && currentString.endsWith("'")) {
                values.add(currentString);
            } else {
                endOfFirstPart = i;
                break;
            }
        }

        if (finalParts.size() > endOfFirstPart + 2 && finalParts.size() > values.size() + 1) {
            int count = 0;
            String tempOperator = null;
            String tempColumn = null;
            List<String> tempValues = new ArrayList<>();

            for (int i = endOfFirstPart; i < finalParts.size(); i++) {
                String currentString = finalParts.get(i);
                if (count == 2 && !currentString.startsWith("'") && !currentString.endsWith("'")) {
                    CustomQueryPart tempCqp = new CustomQueryPart(tempOperator, tempColumn, tempValues);
                    additionalParts.add(tempCqp);
                    tempOperator = null;
                    tempColumn = null;
                    tempValues = new ArrayList<>();
                    count = 0;
                }

                if (count == 0) {
                    tempOperator = currentString;
                    count++;
                }
                else if (count == 1) {
                    tempColumn = currentString;
                    count++;
                }
                else if (count == 2 && currentString.startsWith("'") && currentString.endsWith("'")) {
                    tempValues.add(currentString);
                    if (i == finalParts.size() - 1) {
                        CustomQueryPart tempCqp = new CustomQueryPart(tempOperator, tempColumn, tempValues);
                        additionalParts.add(tempCqp);
                    }
                }
            }
        }


        CustomQuery cq = new CustomQuery(column, values, additionalParts);
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("created query: " + mapper.writeValueAsString(cq));
        } catch (JsonProcessingException e) {
            logger.info("could not log query: " + e.getMessage());
        }
        return cq;
    }

    public static String[] setOperation(String operator, String[] a, String[] b) throws QueryParsingException {
        String capsOperator = operator.toUpperCase();
        String[] result;
        switch(capsOperator) {
            case OR:
                result = Stream.concat(
                        Arrays.stream(a), Arrays.stream(b)).distinct().toArray(String[]::new);
                break;
            case AND:
                List<String> dublicates = new ArrayList<>();
                Arrays.stream(a).forEach(strA -> {
                    if (Arrays.asList(b).contains(strA)) {
                        dublicates.add(strA);
                    }
                });
                result = dublicates.stream().distinct().toArray(String[]::new);
                break;
            default:
                throw new QueryParsingException("Operator '" + capsOperator + "' not allowed.");
        }
    return result;
    }

    public static class CustomQuery {
        String column;
        List<String> values;
        List<CustomQueryPart> additionalParts;
        public CustomQuery(String column, List<String> values, List<CustomQueryPart> additionalParts) {
            this.column = column;
            this.values = values;
            this.additionalParts = additionalParts;
        }

        public String getColumn() {
            return column;
        }

        public List<String> getValues() {
            return values;
        }

        public List<CustomQueryPart> getAdditionalParts() {
            return additionalParts;
        }
    }

    public static class CustomQueryPart {
        String operator;
        String column;
        List<String> values;
        public CustomQueryPart(String operator, String column, List<String> values) {
            this.operator = operator;
            this.column = column;
            this.values = values;
        }

        public String getOperator() {
            return operator;
        }

        public String getColumn() {
            return column;
        }

        public List<String> getValues() {
            return values;
        }
    }

    public static class QueryTuple {
        String operator;
        List<String> ids;

        public QueryTuple(String operator, List<String> ids) {
            this.operator = operator;
            this.ids = ids;
        }

        public String getOperator() {
            return operator;
        }

        public List<String> getIds() {
            return ids;
        }

        @Override
        public String toString() {
            return operator + '[' + ids +
                    ']';
        }
    }
}
