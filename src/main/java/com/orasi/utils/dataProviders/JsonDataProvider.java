package com.orasi.utils.dataProviders;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.orasi.api.restServices.exceptions.RestException;
import com.orasi.exception.automation.DataProviderInputFileException;
import com.orasi.exception.automation.DataProviderInputFileNotFound;
import com.orasi.utils.TestReporter;
import com.orasi.utils.io.FileLoader;

public class JsonDataProvider {

    /**
     * The json read in must follow a specific format
     * <br/>
     * {
     * <br/>
     * &nbsp; "testData": [
     * <br/>
     * &nbsp;&nbsp; {
     * <br/>
     * &nbsp;&nbsp;&nbsp; "iterationName": "Scenario 1",
     * <br/>
     * &nbsp;&nbsp;&nbsp; "data":[
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; {
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 1 name",
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 1 value"
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; },
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; {
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 2 name",
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 2 value"
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; }
     * <br/>
     * &nbsp;&nbsp;&nbsp; ]
     * <br/>
     * &nbsp;&nbsp; },
     * <br/>
     * &nbsp;&nbsp; {
     * <br/>
     * &nbsp;&nbsp;&nbsp; "iterationName": "Scenario 2",
     * <br/>
     * &nbsp;&nbsp;&nbsp; "data":[
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; {
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 1 name",
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 1 value"
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; },
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; {
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 2 name",
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 2 value"
     * <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp; }
     * <br/>
     * &nbsp;&nbsp;&nbsp; ]
     * <br/>
     * &nbsp;&nbsp; }
     * 
     * @param filePath
     *            filepath starting from src/main/resources
     * @return Object[][] for the dataprovider
     */
    public static Object[][] getData(String filePath) {
        TestReporter.logTrace("Entering JsonDataProvider#getData");

        TestReporter.logTrace("Attempt to load json file");
        String json = null;
        JSONArray testData = null;

        try {
            json = FileLoader.loadFileFromProjectAsString(filePath);
        } catch (FileNotFoundException fnfe) {
            throw new DataProviderInputFileNotFound("Failed to locate json file in path [ " + filePath + " ]");
        } catch (IOException ioe) {
            throw new RestException("Failed to read json file", ioe);
        }

        TestReporter.logTrace("Json file loaded, attempt to parse");
        try {
            testData = new JSONObject(json).getJSONArray("testData");
        } catch (JSONException e) {
            throw new DataProviderInputFileException("First JSON object was not [ testData ]");
        }

        TestReporter.logTrace("Determing dataprovider array rows");
        int rows = testData.length();
        TestReporter.logTrace("Rows will be [ " + rows + " ]");

        TestReporter.logTrace("Parsing data parameters");
        JSONArray data;
        try {
            data = (JSONArray) testData.getJSONObject(0).get("data");
        } catch (JSONException e) {
            throw new DataProviderInputFileException("Inner data JSON object was not found");
        }

        TestReporter.logTrace("Determing dataprovider array columns");
        int columns = data.length() + 1;
        TestReporter.logTrace("Columns will be [ " + columns + " ]");

        Object[][] dataArray = new String[rows][columns];
        TestReporter.logTrace("Transferring data to Array");

        int rowNum,
                colNum,
                iterationColumnLength;

        String iterationName = null,
                parameterName = null,
                parameterValue = null;

        for (rowNum = 0; rowNum < rows; rowNum++) {
            try {
                iterationName = testData.getJSONObject(rowNum).get("iterationName").toString();
            } catch (JSONException e2) {
                throw new DataProviderInputFileException("TestData iteration [ " + (rowNum + 1) + " ] is missing it's [ iterationName ] object");
            }

            TestReporter.logTrace("Storing data parameters for iteration name [ " + iterationName + " ]");
            dataArray[rowNum][0] = iterationName;

            for (colNum = 1; colNum < columns; colNum++) {
                try {
                    parameterName = ((JSONArray) testData.getJSONObject(rowNum).get("data")).getJSONObject(colNum - 1).get("name").toString();
                    TestReporter.logTrace("Storing data parameter for iteration name [ " + iterationName + " ] and data parameter name [ " + parameterName + " ]");
                } catch (JSONException e2) {
                    parameterName = null;
                }

                try {
                    iterationColumnLength = ((JSONArray) testData.getJSONObject(0).get("data")).length();
                    if (columns != iterationColumnLength + 1) {
                        throw new DataProviderInputFileException("Number of parameters for iterationName [ " + iterationName + " ] was [ " + iterationColumnLength + " ] instead of [ " + (columns - 1) + " ] as expected");
                    }
                    parameterValue = ((JSONArray) testData.getJSONObject(rowNum).get("data")).getJSONObject(colNum - 1).get("value").toString();
                    dataArray[rowNum][colNum] = parameterValue;
                    TestReporter.logTrace("Storing data parameter for iteration name [ " + iterationName + " ] and data parameter name [ " + parameterName + " ] with value [ " + parameterValue + " ]");
                } catch (JSONException e) {
                    if (null == parameterName) {
                        throw new DataProviderInputFileException("Failed to find [ value ] in data iteration name [ " + iterationName + " ]");
                    } else {
                        throw new DataProviderInputFileException("Failed to find [ value ] for parameter [ " + parameterName + " ] in data iteration name [ " + iterationName + " ]");
                    }
                }
            }

        }

        TestReporter.logTrace("Exitting JsonDataProvider#getData");
        return dataArray;
    }

}
