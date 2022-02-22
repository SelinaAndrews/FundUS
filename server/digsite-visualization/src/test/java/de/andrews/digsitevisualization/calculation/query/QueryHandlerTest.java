package de.andrews.digsitevisualization.calculation.query;

import de.andrews.digsitevisualization.SessionData;
import de.andrews.digsitevisualization.repository.Measurement;
import de.andrews.digsitevisualization.repository.MeasurementMapping;
import de.andrews.digsitevisualization.visualization.VisualizationFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static de.andrews.digsitevisualization.calculation.query.QueryHandler.*;


@SpringBootTest
class QueryHandlerTest {

    private static final String AND = "AND";
    private static final String OR = "OR";

    private final static SessionData sessionData = new SessionData();
    private final static VisualizationFormatter formatter = new VisualizationFormatter();

    @BeforeEach
    void test() {
        sessionData.setDigsite("digsite");
        sessionData.setYear("year");
        formatter.setSessionData(sessionData);
        Measurement ms1 = createTestMeasurement("TOPO","1","gruen");
        Measurement ms2 = createTestMeasurement("SURFACE","2","gruen");
        Measurement ms3 = createTestMeasurement("SURFACE","3","rot");
        Measurement ms4 = createTestMeasurement("TOPO","4","rot");
        Measurement ms5 = createTestMeasurement("SINGLE","5","rot");
        Measurement ms6 = createTestMeasurement("SINGLE","6","gruen");
        sessionData.setCurrentMeasurements(Arrays.asList(ms1, ms2, ms3, ms4, ms5, ms6));
    }

    @Test
    void additionalWhiteSpace() {
        String simple_query = "COL1 'val1' ";
        CustomQuery cQ1 = parseQuery(simple_query);
        Assertions.assertEquals("COL1", cQ1.getColumn());
        Assertions.assertEquals(1, cQ1.getValues().size());
        Assertions.assertEquals("'val1'", cQ1.getValues().get(0));
    }

    @Test
    void parsingOfAdditionalParts() {
        String simple_query = "COL1 'val1'";
        CustomQuery cQ1 = parseQuery(simple_query);
        Assertions.assertEquals("COL1", cQ1.getColumn());
        Assertions.assertEquals(1, cQ1.getValues().size());
        Assertions.assertEquals("'val1'", cQ1.getValues().get(0));

        String multi_param_query = "COL1 'val1' 'val2'";
        CustomQuery cQ2 = parseQuery(multi_param_query);
        Assertions.assertEquals("COL1", cQ2.getColumn());
        Assertions.assertEquals(2, cQ2.getValues().size());
        Assertions.assertEquals("'val1'", cQ2.getValues().get(0));
        Assertions.assertEquals("'val2'", cQ2.getValues().get(1));

        String multi_param_with_additional_part_query = "COL1 'val1' 'val2' AND COL2 'val3' 'val4'";
        CustomQuery cQ3 = parseQuery(multi_param_with_additional_part_query);
        Assertions.assertEquals("COL1", cQ3.getColumn());
        Assertions.assertEquals(2, cQ3.getValues().size());
        Assertions.assertEquals("'val1'", cQ3.getValues().get(0));
        Assertions.assertEquals("'val2'", cQ3.getValues().get(1));
        Assertions.assertEquals(1, cQ3.getAdditionalParts().size());
        CustomQueryPart cQ3_additional_part_1 = cQ3.getAdditionalParts().get(0);
        Assertions.assertEquals("COL2", cQ3_additional_part_1.getColumn());
        Assertions.assertEquals(2, cQ3_additional_part_1.getValues().size());
        Assertions.assertEquals("'val3'", cQ3_additional_part_1.getValues().get(0));
        Assertions.assertEquals("'val4'", cQ3_additional_part_1.getValues().get(1));

        String multi_param_with_additional_parts_query = "COL1 'val1' 'val2' OR COL2 'val3' 'val4' AND COL3 'val5' 'val6'";
        CustomQuery cQ4 = parseQuery(multi_param_with_additional_parts_query);
        Assertions.assertEquals("COL1", cQ4.getColumn());
        Assertions.assertEquals(2, cQ4.getValues().size());
        Assertions.assertEquals("'val1'", cQ4.getValues().get(0));
        Assertions.assertEquals("'val2'", cQ4.getValues().get(1));
        Assertions.assertEquals(2, cQ4.getAdditionalParts().size());
        CustomQueryPart cQ4_additional_part_1 = cQ4.getAdditionalParts().get(0);
        Assertions.assertEquals("COL2", cQ4_additional_part_1.getColumn());
        Assertions.assertEquals(2, cQ4_additional_part_1.getValues().size());
        Assertions.assertEquals("'val3'", cQ4_additional_part_1.getValues().get(0));
        Assertions.assertEquals("'val4'", cQ4_additional_part_1.getValues().get(1));
        CustomQueryPart cQ4_additional_part_2 = cQ4.getAdditionalParts().get(1);
        Assertions.assertEquals("COL3", cQ4_additional_part_2.getColumn());
        Assertions.assertEquals(2, cQ4_additional_part_2.getValues().size());
        Assertions.assertEquals("'val5'", cQ4_additional_part_2.getValues().get(0));
        Assertions.assertEquals("'val6'", cQ4_additional_part_2.getValues().get(1));

        String simple_query_with_additional_parts_query = "COL1 'val1' OR COL2 'val2' 'val3'";
        CustomQuery cQ5 = parseQuery(simple_query_with_additional_parts_query);
        Assertions.assertEquals("COL1", cQ5.getColumn());
        Assertions.assertEquals(1, cQ5.getValues().size());
        Assertions.assertEquals("'val1'", cQ5.getValues().get(0));
        Assertions.assertEquals(1, cQ5.getAdditionalParts().size());
        CustomQueryPart cQ5_additional_part_1 = cQ5.getAdditionalParts().get(0);
        Assertions.assertEquals("COL2", cQ5_additional_part_1.getColumn());
        Assertions.assertEquals(2, cQ5_additional_part_1.getValues().size());
        Assertions.assertEquals("'val2'", cQ5_additional_part_1.getValues().get(0));
        Assertions.assertEquals("'val3'", cQ5_additional_part_1.getValues().get(1));

    }

    @Test
    void multipleParameters() throws QueryParsingException {
        String multi_param_query = "BEST 'TOPO' 'SURFACE'"; // 1, 2, 3, 4
        String[] multi_param_query_result = QueryHandler.applyQuery(multi_param_query, sessionData.getCurrentMeasurements());
        String[] multi_param_query_goal = new String[]{"1", "2", "3", "4"};
        Assertions.assertEquals(multi_param_query_result.length, multi_param_query_goal.length);
        Assertions.assertTrue(checkValidity(multi_param_query_result, multi_param_query_goal));
        
        String multi_param_with_additional_parts_query = "BEST 'TOPO' 'SURFACE' AND BEMERK 'not:rot'"; // 1, 2
        String[] multi_param_with_additional_parts_query_result = QueryHandler.applyQuery(multi_param_with_additional_parts_query, sessionData.getCurrentMeasurements());
        String[] multi_param_with_additional_parts_query_goal = new String[]{"1", "2"};
        Assertions.assertEquals(multi_param_with_additional_parts_query_result.length, multi_param_with_additional_parts_query_goal.length);
        Assertions.assertTrue(checkValidity(multi_param_with_additional_parts_query_result, multi_param_with_additional_parts_query_goal));
    }

    @Test
    void subQueryOperator() throws QueryParsingException {
        String or_query = "BEST 'TOPO' OR BEMERK 'gruen'"; // 1, 2, 4, 6
        String[] or_query_result = QueryHandler.applyQuery(or_query, sessionData.getCurrentMeasurements());
        String[] or_query_goal = new String[]{"1", "2", "4", "6"};
        Assertions.assertEquals(or_query_result.length, or_query_goal.length);
        Assertions.assertTrue(checkValidity(or_query_result, or_query_goal));

        String or_query_one_negative = "BEST 'TOPO' or BEMERK 'not:gruen'"; // 1, 3, 4, 5
        String[] or_query_one_negative_result = QueryHandler.applyQuery(or_query_one_negative, sessionData.getCurrentMeasurements());
        String[] or_query_one_negative_goal = new String[]{"1", "3", "4", "5"};
        Assertions.assertEquals(or_query_one_negative_result.length, or_query_one_negative_goal.length);
        Assertions.assertTrue(checkValidity(or_query_one_negative_result, or_query_one_negative_goal));

        String or_query_all_negative = "BEST 'not:TOPO' or BEMERK 'not:gruen'"; // 2, 3, 4, 5, 6
        String[] or_query_all_negative_result = QueryHandler.applyQuery(or_query_all_negative, sessionData.getCurrentMeasurements());
        String[] or_query_all_negative_goal = new String[]{"2", "3", "4", "5", "6"};
        Assertions.assertEquals(or_query_all_negative_result.length, or_query_all_negative_goal.length);
        Assertions.assertTrue(checkValidity(or_query_all_negative_result, or_query_all_negative_goal));

        String and_query = "BEST 'TOPO' and BEMERK 'gruen'"; // 1
        String[] and_query_result = QueryHandler.applyQuery(and_query, sessionData.getCurrentMeasurements());
        String[] and_query_goal = new String[]{"1"};
        Assertions.assertEquals(and_query_result.length, and_query_goal.length);
        Assertions.assertTrue(checkValidity(and_query_result, and_query_goal));

        String and_query_one_negative = "BEST 'TOPO' and BEMERK 'not:gruen'"; // 4
        String[] and_query_one_negative_result = QueryHandler.applyQuery(and_query_one_negative, sessionData.getCurrentMeasurements());
        String[] and_query_one_negative_goal = new String[]{"4"};
        Assertions.assertEquals(and_query_one_negative_result.length, and_query_one_negative_goal.length);
        Assertions.assertTrue(checkValidity(and_query_one_negative_result, and_query_one_negative_goal));

        String and_query_all_negative = "BEST 'not:TOPO' and BEMERK 'not:gruen'"; // 3, 5
        String[] and_query_all_negative_result = QueryHandler.applyQuery(and_query_all_negative, sessionData.getCurrentMeasurements());
        String[] and_query_all_negative_goal = new String[]{"3", "5"};
        Assertions.assertEquals(and_query_all_negative_result.length, and_query_all_negative_goal.length);
        Assertions.assertTrue(checkValidity(and_query_all_negative_result, and_query_all_negative_goal));
    }

    private boolean checkValidity(String[] resultIds, String[] goalIds) {
        List<String> sortedResultIds = Arrays.stream(resultIds).map(str -> str.substring(str.lastIndexOf(".")+1)).sorted().collect(Collectors.toList());
        List<String> sortedGoalIds = Arrays.stream(goalIds).sorted().collect(Collectors.toList());
        boolean valid = true;
        for (int i = 0; i < resultIds.length; i++) {
            String str1 = sortedResultIds.get(i);
            String str2 = sortedGoalIds.get(i);
            if (!str1.equals(str2)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    @Test
    void negativeValueCheck() {
        Measurement ms1 = createTestMeasurement("TOPO","1","gruen");
        Measurement ms2 = createTestMeasurement("SURFACE","2","gruen");
        Measurement ms3 = createTestMeasurement("SURFACE","3","rot");
        Measurement ms4 = createTestMeasurement("TOPO","4","rot");
        Measurement ms5 = createTestMeasurement("DOUBLE","5","rot");
        Measurement ms6 = createTestMeasurement("SINGLE","6","gruen");
        List<Measurement> msList = Arrays.asList(ms1, ms2, ms3, ms4, ms5, ms6);
        List<String> result = new ArrayList<>();
        for (Measurement ms : msList) {
            result.add(QueryHandler.checkFieldValue(ms, ms.getBEST(), Arrays.asList("DOUBLE", "SINGLE", "TOPO")));
        }
        Assertions.assertEquals(Arrays.asList("digsiteyear.1.1", null, null, "digsiteyear.4.4", "digsiteyear.5.5", "digsiteyear.6.6"), result);
    }

    @Test
    void setOperations() throws QueryParsingException {
        String[] a = new String[]{"1", "2", "3"};
        String[] b = new String[]{"3", "4", "5"};
        Assertions.assertEquals(Arrays.toString(new String[]{"3"}), Arrays.toString(setOperation(AND, a, b)));
        Assertions.assertEquals(Arrays.toString(new String[]{"1", "2", "3", "4", "5"}), Arrays.toString(setOperation(OR, a, b)));
    }

    @Test
    void applySetOperation() throws QueryParsingException {
        List<String> list1 = Arrays.asList("1", "2", "3");
        QueryHandler.QueryTuple qt1 = new QueryHandler.QueryTuple(null, list1);
        List<String> list2 = Arrays.asList("3", "4", "5");
        QueryHandler.QueryTuple qt2 = new QueryHandler.QueryTuple(AND, list2);
        List<String> list3 = Arrays.asList("5", "6", "7");
        QueryHandler.QueryTuple qt3 = new QueryHandler.QueryTuple(OR, list3);
        Assertions.assertEquals(Arrays.toString(new String[]{"3"}), Arrays.toString(applySetOperations(Arrays.asList(qt1, qt2))));
        Assertions.assertEquals(Arrays.toString(new String[]{"3", "4", "5", "6", "7"}), Arrays.toString(applySetOperations(Arrays.asList(qt2, qt3))));
    }

    private static Measurement createTestMeasurement(String best, String id, String bemerk){
        HashMap<String, String> data = new HashMap<>();
        data.put("EF","true");
        data.put("BEST",best);
        data.put("UNIT",id);
        data.put("ID",id);
        data.put("BEMERK",bemerk);
        MeasurementMapping mapping = createTestMapping();
        Measurement ms = new Measurement(data, mapping);
        formatter.formatFindingNumber(ms, Measurement.Type.SINGLEFIND);
        return ms;
    }

    private static MeasurementMapping createTestMapping() {
        MeasurementMapping mapping = new MeasurementMapping();
        mapping.setX("X");
        mapping.setY("Y");
        mapping.setZ("Z");
        mapping.setFindingNumberParts(Arrays.asList("UNIT", "ID"));
        mapping.setGroupFindingNumberParts(Arrays.asList("UNIT", "ID", "SUFFIX"));
        mapping.setGH("GH");
        mapping.setAH("AH");
        mapping.setGF("GF");
        mapping.setDEF("DEF");
        mapping.setBEMERK("BEMERK");
        mapping.setBEARBEITET("BEARBEITET");
        mapping.setBestId(Arrays.asList("UNIT","ID"));
        mapping.setBEST("BEST");
        mapping.setTopoKeys(Arrays.asList("TOPO"));
        mapping.setEFColumn("EF");
        mapping.setEFKeys(Arrays.asList("TRUE","1","true","TRUE()","true()"));
        return mapping;
    }
}