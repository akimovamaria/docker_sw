import com.sun.net.httpserver.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

/**
 * The class starts webservice on address http://localhost:8080. Webservice is intended
 * for converting xml to json. To convert, pass xml to address http://localhost:8080/convert.
 * Service will respond json if everything was ok or "BAD REQUEST" if there were errors.
 */
public class Service {

    /**
     * Method starts {@link HttpServer} on port 8080 to handle /convert http post requests.
     * @param args command line args
     */
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/convert", new Converter());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class handles http post request for converting xml to json.
     */
    private static class Converter implements HttpHandler {
        /**
         * Handle http post request with xml string in request data.
         * Respond 200 OK and json string in response data if {@link #json(String)} is ok.
         * Respond 400 BAD REQUEST and "BAD REQUEST" if request data caused {@link JSONException} in
         * {@link #json(String)}.
         * @param exchange http request + response attributes
         */
        @Override
        public void handle(HttpExchange exchange) {
            if ("POST".equals(exchange.getRequestMethod())) {
                String response;
                int code;

                try {
                    InputStream is = exchange.getRequestBody();
                    String request = IOUtils.toString(is, StandardCharsets.UTF_8);
                    response = json(request);
                    code = 200;
                } catch (Exception e) {
                    response = "BAD REQUEST";
                    code = 400;
                }

                try (OutputStream os = exchange.getResponseBody()) {
                    exchange.sendResponseHeaders(code, response.length());
                    os.write(response.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            exchange.close();
        }

        /**
         * Convert xml string to json string.
         * @param xml xml formatted string
         * @return json formatted string if input format is correct xml
         * @throws JSONException if format is incorrect xml
         */
        public static String json(String xml) throws JSONException {
            JSONObject json = XML.toJSONObject(xml);
            return json.toString(4);
        }
    }
}
