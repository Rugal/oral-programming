package ml.rugal.googlespeech.gson;

import com.google.gson.annotations.SerializedName;

/**
 * This whole class is for GSON to reflect a data structure, so I deliberately
 * left all fields with public accessors.
 *
 * @author Rugal Bernstein
 */
public class SpeechResponseData
{

    public Result[] result;

    public int result_index;

    public SpeechResponseData()
    {
    }

    public static class Result
    {

        public Alternative[] alternative;

        @SerializedName("final")
        public boolean isfinal;

        public Result()
        {
        }

    }

    public static class Alternative
    {

        public String transcript;

        public double confidence;

        public Alternative()
        {
        }

    }

}
