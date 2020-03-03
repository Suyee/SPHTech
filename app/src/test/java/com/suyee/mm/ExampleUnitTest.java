package com.suyee.mm;

import com.suyee.mm.service.ApiService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.json.JSONException;
import org.json.JSONObject;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(Parameterized.class)
public class ExampleUnitTest {

    @Parameters
    public static Iterable<String> responseCodes() {
        return Arrays.asList(new String[]{
                "500", "404", null
        });
    }

    private final String expectedResponseCode;
    private ApiService apiService;

    public ExampleUnitTest(String expectedResponseCode){
        this.expectedResponseCode = expectedResponseCode;
    }

    @Before
    public void init(){
        apiService = new ApiService();
    }

    /* For response code 200 ==>> response json will return without error code */
    @Test
    public void checkInternalServer_ResponseCode() throws JSONException {
        String result = apiService.requestData();
        String actualResponseCode = (new JSONObject(result)).getString("code");
        assertThat(actualResponseCode, is(equalTo(expectedResponseCode)));
    }
}